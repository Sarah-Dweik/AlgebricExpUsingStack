package components;

public class FineCursorArray<T extends Comparable<T>> {

	CNode<T> cursorarray[] = new CNode[200];

	public int initializer() {
		for (int i = 0; i < cursorarray.length - 1; i++) {
			CNode node = new CNode(null);
			node.setNext(i + 1);
			cursorarray[i] = node;
		}
		CNode lastNode = new CNode(null);
		lastNode.setNext(0);
		cursorarray[cursorarray.length - 1] = lastNode;
		return 0;
	}

	public int malloc() {
		int p = cursorarray[0].getNext();
		cursorarray[0].setNext(cursorarray[p].getNext());
		return p; // this int is always in the range of the array size because it was the next of
					// the freelist
	}

	// since we add a completly new empty Node to the array, why we do not adjust
	// the length?
	// should not we check if p is in the bound of the cursorarray?
	// what happenes to the Node that the Node at P might have been pointing at ?
	// free method, empties the cell by adding new Node so that the reference to the
	// old one is deleted
	public void free(int p) {
		CNode node = new CNode(null);
		node.setNext(cursorarray[0].getNext());
		cursorarray[p] = node;
		cursorarray[0].setNext(p);
	}

	// no Node at that position
	public boolean isNull(int l) {
		if (cursorarray[l] == null) {
			return true;
		}
		return false;
	}

	public boolean isEmpty(int l) {
		if (cursorarray[l].getNext() == 0) {
			return true;
		}
		return false;
	}

	public boolean isLast(int p) {
		if (cursorarray[p].getNext() == 0) {
			return true;
		}
		return false;
	}

	// so far they are only tools, no addition of data occured

	// should not we update the size of the array since we are adding new Node
	// instead of using any existing empty Node?
	public int createList() {
		int l = malloc();
		if (l == 0) {
			System.out.println("no space !");
		} else {
			CNode node = new CNode("-");
			node.setNext(0);
			cursorarray[l] = node;
		}
		return l;
	}

	// I am still not sure why we create a whole new Node? even though we create new
	// node we replace in place of an existing
	// empty node in the initialized cursor array, so its size is not affected?
	public void insertFirst(T data, int l) {
		if (isNull(l) == true) {
			System.out.println("list is not created...");
		}
		int p = malloc();
		if (p != 0) {// list is not full
			CNode node = new CNode(data);
			cursorarray[p] = node;
			node.setNext(cursorarray[l].getNext());
			cursorarray[l].setNext(p);
		} else {
			System.out.println("List is full!");
		}
	}

	public T firstElement(int l) {
		if (!isNull(l) && !isEmpty(l)) {
			int p = cursorarray[l].getNext();
			T first = (T) cursorarray[p].getData();
			return first;
		}
		return null;
	}

	public void deleteFirst(int l) {
		if (!isNull(l) && !isEmpty(l)) {
			int p = cursorarray[l].getNext();
			cursorarray[l].setNext(cursorarray[p].getNext());
			free(p);
		}
	}

	public void traverse(int l) { // given a certian list
		if (isNull(l) == false) {
			while (isLast(l) == false) {
				l = cursorarray[l].getNext(); // skip the first item because it is the dummy head
				System.out.println(cursorarray[l] + "\n");
			}
		} else {
			System.out.println("list is not created");
		}
	}

	public void traverseRec(int l) {
		if (isLast(l) == false) {
			return;
		}
		l = cursorarray[l].getNext();
		System.out.println(cursorarray[l] + "...");
		traverseRec(l);
	}

	public int find(T data, int l) {
		if (isNull(l) == false) {
			while (isLast(l) == false) {
				if (cursorarray[l].getData().equals(data)) {
					return l;
				}
				l = cursorarray[l].getNext();
			}
		} else {
			System.out.println("list is not created");
		}
		return -1; // data is not found
	}

	public int findPrev(T data, int l) {
		while (isNull(l) == false && isEmpty(l) == false) {
			if (cursorarray[cursorarray[l].getNext()].getData().equals(data)) {
				return l;
			}
			l = cursorarray[l].getNext();
		}

		return -1;
	}

	// there is no difference between delete and free because they both complete
	// each other
	// but there you delete by index, while here you have more constraids before
	// deleting by index
	public void delete(T data, int l) {
		int p = findPrev(data, l);
		if (p != -1) {
			int c = cursorarray[p].getNext();// the one that we want to delete
			cursorarray[p].setNext(cursorarray[c].getNext());
			free(c);
		} else {
			System.out.println("data can not be deleted"); // when it is at the first position
		}
	}

	public int length(int l) {
		int len = 0;
		if (isNull(l) == false) {
			while (!isNull(l) && !isEmpty(l)) {
				l = cursorarray[l].getNext();
				len++;
			}
		} else {
			System.out.println("list is not created");
		}

		return len;

	}

	// 306332233630 is Pal ebbe is pal
	public boolean isPal(int l) {
		int len = length(l);
		while (isLast(l) == false && isEmpty(l) == false) {
			for (int i = len; i > 0; i--) {
				if (!cursorarray[l].getData().equals(cursorarray[i])) {
					return false;
				}
			}
			l = cursorarray[l].getNext();
		}

		return true;
	}

}
