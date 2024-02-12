package application;

import stacks.CStack;

public class AlgExpressions {

	CStack cursorSpace = new CStack<>();

	public String infixToPostfix(String infix) {
		String postExpression = "";
		int stack1 = cursorSpace.creatList();
		String[] in = infix.split(" ");

		for (int i = 0; i < in.length; i++) {
			String value = in[i];

			if (cursorSpace.isEmpty(stack1) && (value.equals("+") || value.equals("-") || value.equals("/")
					|| value.equals("*") || value.equals("^") || value.equals(")") || value.equals("("))) {
				cursorSpace.push(value, stack1);
			}
			// if number
			else if (!value.equals("+") && !value.equals("-") && !value.equals("/") && !value.equals("*")
					&& !value.equals("^") && !value.equals(")") && !value.equals("(")) {
				postExpression += value + " ";

				// if operator
			} else {
				switch (value) {
				case "+":
				case "-":
					while (!cursorSpace.isEmpty(stack1)
							&& (cursorSpace.peek(stack1).equals("+") || cursorSpace.peek(stack1).equals("-")
									|| cursorSpace.peek(stack1).equals("*") || cursorSpace.peek(stack1).equals("/"))) {
						String toPost = (String) cursorSpace.pop(stack1);
						postExpression += toPost + " ";
					}
					cursorSpace.push(value, stack1);
					break;
				case "*":
				case "/":
					while (!cursorSpace.isEmpty(stack1)
							&& (cursorSpace.peek(stack1).equals("*") || cursorSpace.peek(stack1).equals("/"))) {
						String toPost = (String) cursorSpace.pop(stack1);
						postExpression += toPost + " ";
					}
					cursorSpace.push(value, stack1);
					break;
				case "(":
				case "^":
					cursorSpace.push(value, stack1);
					break;
				case ")":
					while (!cursorSpace.isEmpty(stack1) && !cursorSpace.peek(stack1).equals("(")) {
						String toPost = (String) cursorSpace.pop(stack1);
						postExpression += toPost + " ";
					}
					cursorSpace.pop(stack1); // Pop the opening parenthesis
					break;
				}
			}
		}

		while (!cursorSpace.isEmpty(stack1)) {
			Comparable toPost = cursorSpace.pop(stack1);
			postExpression += toPost + " ";
		}

		return postExpression;
	}

	public double postEvaluation(String postfix) {
		int stack2 = cursorSpace.creatList();
		double finalResult;

		for (int i = 0; i < postfix.length(); i++) {
			char c = postfix.charAt(i);

			if (Character.isDigit(c) || c == '.') {
				StringBuilder numBuilder = new StringBuilder();
				// Keep appending digits and '.' to form the complete number
				while (i < postfix.length() && (Character.isDigit(postfix.charAt(i)) || postfix.charAt(i) == '.')) {
					numBuilder.append(postfix.charAt(i));
					i++;
				}
				i--; // Move the index back by one as it was incremented in the loop

				// Convert the complete number string to double and push onto the stack
				cursorSpace.push(Double.parseDouble(numBuilder.toString()), stack2);

			} else {

				switch (c) {
				case '+':
					if (!cursorSpace.isEmpty(stack2)) {

						double left = (double) cursorSpace.pop(stack2);
						double right = (double) cursorSpace.pop(stack2);
						double result = left + right;
						cursorSpace.push(result, stack2);

					}
					break;
				case '-':
					if (!cursorSpace.isEmpty(stack2)) {
						double left = (double) cursorSpace.pop(stack2);
						double right = (double) cursorSpace.pop(stack2);
						double result = right - left;
						cursorSpace.push(result, stack2);
					}
					break;
				case '*':
					if (!cursorSpace.isEmpty(stack2)) {
						double left = (double) cursorSpace.pop(stack2);
						double right = (double) cursorSpace.pop(stack2);
						double result = left * right;
						cursorSpace.push(result, stack2);
					}
					break;
				case '/':
					if (!cursorSpace.isEmpty(stack2)) {
						double left = (double) cursorSpace.pop(stack2);
						double right = (double) cursorSpace.pop(stack2);
						double result = right / left;
						cursorSpace.push(result, stack2);
					}
					break;
				}
			}

		}

		finalResult = (double) cursorSpace.pop(stack2);

		return finalResult;
	}

	public String postfixToPre(String post) {
		String prefix = "";
		int stack3 = cursorSpace.creatList();
		for (int i = 0; i < post.length(); i++) {
			char c = post.charAt(i);

			if (!Character.isWhitespace(c)) {
				if (c != '+' && c != '-' && c != '*' && c != '/' && c != '(' && c != ')') {
					StringBuilder numBuilder = new StringBuilder();
					// Keep appending digits and '.' to form the complete number
					while (i < post.length() && (Character.isDigit(post.charAt(i)) || post.charAt(i) == '.')) {
						numBuilder.append(post.charAt(i));
						i++;
					}
					i--;
					String number = numBuilder.toString();
					cursorSpace.push(number, stack3);
				} else {
					switch (c) {
					case '+':
						if (!cursorSpace.isEmpty(stack3)) {
							String left = (String) cursorSpace.pop(stack3);
							String right = (String) cursorSpace.pop(stack3);
							String result = "";
							result = result.concat(String.valueOf(c)) + " ";
							result = result.concat(right) + " ";
							result = result.concat(left);
							cursorSpace.push(result, stack3);
						}
						break;
					case '-':
						if (!cursorSpace.isEmpty(stack3)) {
							String left = (String) cursorSpace.pop(stack3);
							String right = (String) cursorSpace.pop(stack3);
							String result = String.valueOf(c) + " " + right + " " + left;
							cursorSpace.push(result, stack3);
						}
						break;
					case '*':
						if (!cursorSpace.isEmpty(stack3)) {
							String left = (String) cursorSpace.pop(stack3);
							String right = (String) cursorSpace.pop(stack3);
							String result = String.valueOf(c) + " " + right + " " + left;
							cursorSpace.push(result, stack3);
						}
						break;
					case '/':
						if (!cursorSpace.isEmpty(stack3)) {
							String left = (String) cursorSpace.pop(stack3);
							String right = (String) cursorSpace.pop(stack3);
							String result = String.valueOf(c) + " " + right + " " + left;
							cursorSpace.push(result, stack3);
						}
						break;
					}
				}
			}

		}

		prefix = (String) cursorSpace.pop(stack3); // at the end one string mush exsit in the stack
		return prefix;
	}

	public static String reverseString(String initial) {
		StringBuilder rev = new StringBuilder();
		StringBuilder numBuilder = new StringBuilder();

		for (int i = initial.length() - 1; i >= 0; i--) {
			char c = initial.charAt(i);

			if (Character.isDigit(c) || c == '.') {
				// Keep track of digits and decimal points in their original order
				numBuilder.insert(0, c);
			} else {
				// If a non-digit character is encountered, append the reversed digits and then
				// the character
				if (numBuilder.length() > 0) {
					rev.append(numBuilder.reverse());
					numBuilder.setLength(0); // Clear the StringBuilder for the next set of digits
				}
				rev.append(c);
			}
		}

		// Append any remaining reversed digits at the end
		if (numBuilder.length() > 0) {
			rev.append(numBuilder.reverse());
		}

		return rev.toString();
	}

	public double prefixEvaluation(String pre) {
		String preR = reverseString(pre);

		int stack4 = cursorSpace.creatList();
		double finalResult;

		for (int i = 0; i < preR.length(); i++) {
			char c = preR.charAt(i);

			if (Character.isDigit(c) || c == '.') {
				StringBuilder numBuilder = new StringBuilder();
				// Keep appending digits and '.' to form the complete number
				while (i < preR.length() && (Character.isDigit(preR.charAt(i)) || preR.charAt(i) == '.')) {
					numBuilder.append(preR.charAt(i));
					i++;
				}
				i--; // Move the index back by one as it was incremented in the loop

				// Convert the complete number string to double and push onto the stack
				cursorSpace.push(Double.parseDouble(numBuilder.toString()), stack4);
			} else {

				switch (c) {
				case '+':
					if (!cursorSpace.isEmpty(stack4)) {
						double left = (double) cursorSpace.pop(stack4);
						double right = (double) cursorSpace.pop(stack4);
						double result = left + right;
						cursorSpace.push(result, stack4);
					}
					break;
				case '-':
					if (!cursorSpace.isEmpty(stack4)) {
						double left = (double) cursorSpace.pop(stack4);
						double right = (double) cursorSpace.pop(stack4);
						double result = left - right;
						cursorSpace.push(result, stack4);
					}
					break;
				case '*':
					if (!cursorSpace.isEmpty(stack4)) {
						double left = (double) cursorSpace.pop(stack4);
						double right = (double) cursorSpace.pop(stack4);
						double result = left * right;
						cursorSpace.push(result, stack4);
					}
					break;
				case '/':
					if (!cursorSpace.isEmpty(stack4)) {
						double left = (double) cursorSpace.pop(stack4);
						double right = (double) cursorSpace.pop(stack4);
						double result = left / right;
						cursorSpace.push(result, stack4);
					}
					break;
				}
			}

		}

		finalResult = (double) cursorSpace.pop(stack4);

		return finalResult;
	}

}
