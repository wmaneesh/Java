package A1Q3;

import java.util.*;

/**
 * Specializes the stack data structure for comparable elements, and provides a
 * method for determining the maximum element on the stack in O(1) time.
 * 
 * @author jameselder
 */
public class MaxStack<E extends Comparable<E>> extends Stack<E> {

	private Stack<E> maxStack;
	private Stack<E> altStack;

	public MaxStack() {
		maxStack = new Stack<>();
		altStack = new Stack<>();
	}

	/* must run in O(1) time */
	public E push(E element) {

		if (altStack.isEmpty()) {
			maxStack.push(element);
			altStack.push(element);
		}
		if (element.compareTo(maxStack.peek()) > 0) {
			altStack.push(element);
			maxStack.push(element);
		} else {
			altStack.push(element);
			maxStack.push(maxStack.peek());
		}

		return element; 
	}

	/* @exception EmptyStackException if this stack is empty. */
	/* must run in O(1) time */
	public synchronized E pop() {

		if (altStack.isEmpty()) {
			throw new EmptyStackException();
		} else {
			altStack.pop();
			return maxStack.pop();
		}
	}

	/* Returns the maximum value currenctly on the stack. */
	/* @exception EmptyStackException if this stack is empty. */
	/* must run in O(1) time */
	public synchronized E max() {

		if (maxStack.isEmpty()) {
			throw new EmptyStackException();
		} else {
			return maxStack.peek();
		}

	}
}
