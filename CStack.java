package stacks;

import components.FineCursorArray;

public class CStack<T extends Comparable<T>> {

	FineCursorArray list = new FineCursorArray(); // the size is read from the number of sections
	int top;

	public CStack() {
		list.initializer();
	}

	public int creatList() {
		return list.createList();
	}

	public boolean isEmpty(int l) {
		if (list.isEmpty(l) == true) {
			return true;
		}
		return false;
	}

	public void clear(int l) {
		if (!isEmpty(l)) {
			
		}
	}

	public void push(T data, int l) {
		list.insertFirst(data, l);
	}

	public T pop(int l) {
		if (!isEmpty(l)) {
			T toDel = (T) list.firstElement(l);
			list.deleteFirst(l);
			return toDel;
		}
		return null;
	}

	public T peek(int l) {
		if (!isEmpty(l)) {
			T first = (T) list.firstElement(l);
			return first;
		}
		return null;
	}

}
