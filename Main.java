package application;

import java.awt.Cursor;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import javax.swing.JFileChooser;

import javafx.application.Application;
import javafx.stage.Stage;
import stacks.CStack;
import stacks.LStack;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;


public class Main extends Application {
	private TextArea equationsDisplay;
	public CStack cursorSpace = new CStack<>();
	
	@Override
	public void start(Stage primaryStage) {
		try {

			Button bt1 = new Button("Load");
			Label lb1 = new Label("File:");
			TextField tf = new TextField();
			Button next = new Button("Next");
			Button prev = new Button("prev");
			
			int nextSection = cursorSpace.creatList();
			int prevSection = cursorSpace.creatList();
		
			bt1.setOnAction(e -> {
				JFileChooser filechooser = new JFileChooser();
				filechooser.showSaveDialog(null);
				File equationsFile = filechooser.getSelectedFile();

				String delimiters = "";
				Scanner scan;
				try {
					scan = new Scanner(equationsFile);
					while (scan.hasNext()) {
						String line = scan.next();

						for (int i = 0; i < line.length(); i++) {
							char c = line.charAt(i);
							if (c == '{' || c == '}' || c == '(' || c == ')' || c == '[' || c == ']') {
								delimiters += c;
							}
						}
					}

					delimiters = delimiters.trim(); // Corrected line
					System.out.println(delimiters);
					// print to the concole if the file is valid or not
					System.out.println(fileValidity(delimiters)+ "File is valid");

				} catch (FileNotFoundException e1) {
					e1.printStackTrace();
				}

				if (equationsFile != null) {
					String fileName = equationsFile.getName();
					tf.setText(fileName);
				}
				try {
					if (fileValidity(delimiters)==true) {
						Scanner scan2 = new Scanner(equationsFile);
						while (scan2.hasNextLine()) {
							String outerLine = scan2.nextLine().trim();
							String sectionLine = "";
							if (outerLine.contains("<section>")) {
								String sectionContent = "";
								String sectionContentEvaluated = "";
								while (!sectionLine.contains("<section>")) {
									sectionLine = scan2.nextLine();
									sectionContent += sectionLine + "\n"; // Use += for string concatenation
								}
								sectionContentEvaluated = evaluation(sectionContent);
								cursorSpace.push(sectionContentEvaluated, nextSection);
							}
						}

					}
					else {
						System.out.println("File is not valid");
					}

				} catch (FileNotFoundException e1) {
					e1.printStackTrace();
				}

			});

			equationsDisplay = new TextArea();

			next.setOnAction(e -> {
				if (!cursorSpace.isEmpty(nextSection)) {
					String str = (String) cursorSpace.pop(nextSection);
					cursorSpace.push(str, prevSection);
					equationsDisplay.clear();
					equationsDisplay.appendText(str);
				}
			});
			prev.setOnAction(e -> {
				if (!cursorSpace.isEmpty(prevSection)) {
					String str = (String) cursorSpace.pop(prevSection);
					cursorSpace.push(str, nextSection);
					equationsDisplay.clear();
					equationsDisplay.appendText(str);
				}

			});

			HBox pane1 = new HBox();
			pane1.getChildren().addAll(next, prev, equationsDisplay);
			HBox pane2 = new HBox();
			pane2.getChildren().addAll(lb1, tf, bt1);
			BorderPane general = new BorderPane();
			general.setBottom(pane1);
			general.setCenter(pane2);

			Scene scene = new Scene(general);
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public boolean fileValidity(String D) {
		int open = cursorSpace.creatList();

		for (int i = 0; i < D.length(); i++) {
			char c = D.charAt(i);
			if (c == '{' || c == '[' || c == '(' || c == '<') {
				cursorSpace.push(c, open);
			} else if (c == '}' || c == ']' || c == ')' || c == '>') {
				if (cursorSpace.isEmpty(open)) {
					return false;
				}
				char poppedChar = (char) cursorSpace.pop(open);
				if (!matching(poppedChar, c)) {
					return false;
				}
			}
		}

		return cursorSpace.isEmpty(open);
	}

	public boolean matching(char open, char close) {
		return ((open == '(' && close == ')') || (open == '[' && close == ']') || (open == '{' && close == '}')
				|| (open == '<' && close == '>'));
	}

	public static String evaluation(String str) {
		AlgExpressions exp = new AlgExpressions();

		String postEquations = "";
		String infixEquations = "";
		String evaluatedSection = "";
		
		if (str.contains("<postfix>")) {
			int start = str.indexOf("<postfix>");
			int end = str.indexOf("</postfix>");
			if (start != -1 && end != -1) {
				postEquations = str.substring(start + "<postfix>".length(), end);
			}
		}
		
		Scanner scan = new Scanner(postEquations);
		while (scan.hasNextLine()) {
			String line = scan.nextLine();
			if (line.contains("<equation>")) {
				int start = line.indexOf("<equation>");
				int end = line.indexOf("<\\equation>");
				if (start != -1 && end != -1) {
					String equation = line.substring(start + "<equation>".length(), end);
					evaluatedSection += "\n" + "POSTFIX TO PREFIX EVALUATION" + "\n" + equation + "    " + "===>" + " "
							+ exp.postfixToPre(equation) + "  " + "===>" + "  "
							+ exp.prefixEvaluation(exp.postfixToPre(equation));
				}
			}
		}
		
		if (str.contains("<infix>")) {
			int start = str.indexOf("<infix>");
			int end = str.indexOf("</infix>");
			if (start != -1 && end != -1) {
				infixEquations = str.substring(start + "<infix>".length(), end);
			}
		}
		Scanner scan2 = new Scanner(infixEquations);
		while (scan2.hasNextLine()) {
			String line = scan2.nextLine();
			if (line.contains("<equation>")) {
				int start = line.indexOf("<equation>");
				int end = line.indexOf("</equation>");
				if (start != -1 && end != -1) {
					String equation = line.substring(start + "<equation>".length(), end);
					evaluatedSection += "\n" + "INFIX TO POSTFIX SECTION" + "\n" + equation + "   " + "===>" + " "
							+ exp.infixToPostfix(equation) + "  " + "===>" + " "
							+ exp.postEvaluation(exp.infixToPostfix(equation));
				}
			}
		}

		
		return evaluatedSection;
	}

	public static void main(String[] args) {
		launch(args);
	}

}
