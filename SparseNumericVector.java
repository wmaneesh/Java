
package A1Q1;

import java.util.*;

import javax.xml.soap.Node;

/**
 * Represents a sparse numeric vector. Elements are comprised of a (long)
 * location index an a (double) value. The vector is maintained in increasing
 * order of location index, which facilitates numeric operations like inner
 * products (projections). Note that location indices can be any integer from 1
 * to Long.MAX_VALUE. The representation is based upon a singly-linked list. The
 * following methods are supported: iterator, getSize, getFirst, add, remove,
 * and dot, which takes the dot product of the with a second vector passed as a
 * parameter.
 * 
 * @author jameselder
 */
public class SparseNumericVector implements Iterable {

	protected SparseNumericNode head = null;
	protected SparseNumericNode tail = null;
	protected long size;

	/**
	 * Iterator
	 */
	@Override
	public Iterator<SparseNumericElement> iterator() { // iterator
		return new SparseNumericIterator(this);
	}

	/**
	 * @return number of non-zero elements in vector
	 */
	public long getSize() {
		return size;
	}

	/**
	 * @return the first node in the list.
	 */
	public SparseNumericNode getFirst() {
		return head;
	}

	/**
	 * Add the element to the vector. It is inserted to maintain the vector in
	 * increasing order of index. If the element has zero value, or if an
	 * element with the same index already exists, an
	 * UnsupportedOperationException is thrown.
	 * 
	 * @param e
	 *            element to add
	 */
	public void add(SparseNumericElement e) throws UnsupportedOperationException {
		SparseNumericNode temp = new SparseNumericNode(null, null);
		SparseNumericNode node = new SparseNumericNode(e, null);

		temp = this.head;

		if (this.head == null) {
			this.head = node;
			this.tail = node;

		} else if (this.head == this.tail && e.getIndex() < head.getElement().getIndex()) {
			node.setNext(this.head);
			this.head = node;
			size++;

		} else if (this.head == this.tail && e.getIndex() > head.getElement().getIndex()) {
			node.setNext(this.tail);
			size++;

		} else if (e.getIndex() == temp.getElement().getIndex() || e.getValue() == 0) {
			throw new UnsupportedOperationException();

		} else {

			while (temp.getNext() != null) {

				if (e.getIndex() < temp.getNext().getElement().getIndex()) {
					node.setNext(temp.getNext());
					temp.setNext(node);
					size++;
					break;
				}
				if (node.getElement().getIndex() > this.tail.getElement().getIndex()) {
					this.tail = node;
				}

				temp = temp.getNext();
			}
		}
	}

	/**
	 * If an element with the specified index exists, it is removed and the
	 * method returns true. If not, it returns false.
	 *
	 * @param index
	 *            of element to remove
	 * @return true if removed, false if does not exist
	 */
	public boolean remove(Long index) {
		SparseNumericNode temp = new SparseNumericNode(null, null);
		SparseNumericNode tempNext = new SparseNumericNode(null, null);
		SparseNumericNode tempOld = new SparseNumericNode(null, null);

		temp = this.head;
		tempOld = this.head;
		tempNext = this.head.getNext();

		
		while (temp != null) {

			if (this.head.getElement().getIndex() == index) {
				this.head = tempNext;
			}
			if (this.tail.getElement().getIndex() == index){
				this.tail = tempOld;
			}
			if (temp.getElement().getIndex() == index) {
				tempOld.setNext(tempNext);
				temp.setNext(null);
				size--;
				return true;
			}

			tempOld = temp;
			temp = temp.getNext();
			tempNext = tempNext.getNext();
		}

		return false;
	}

	/**
	 * Returns the inner product of the vector with a second vector passed as a
	 * parameter. The vectors are assumed to reside in the same space. Runs in
	 * O(m+n) time, where m and n are the number of non-zero elements in each
	 * vector.
	 * 
	 * @param Y
	 *            Second vector with which to take inner product
	 * @return result of inner product
	 */

	public double dot(SparseNumericVector Y) {

		SparseNumericNode tempX = new SparseNumericNode(null, null);
		SparseNumericNode tempY = new SparseNumericNode(null, null);
		double dotProduct = 0.0;

		tempX = this.head;
		tempY = Y.head;

		while (tempX != null && tempY != null) {

			/*
			 * if (tempX.getElement().getIndex() <
			 * tempY.getElement().getIndex()) { tempX = this.head.getNext();
			 * 
			 * if (tempX.getElement().getIndex() ==
			 * tempY.getElement().getIndex()) { dotProduct = dotProduct +
			 * (tempX.getElement().getIndex() * tempY.getElement().getIndex());
			 * tempX = this.head.getNext();
			 * 
			 * } else if (tempX.getElement().getIndex() >
			 * tempY.getElement().getIndex()) { tempY = Y.head.getNext();
			 * 
			 * } }
			 * 
			 * if (tempX.getElement().getIndex() ==
			 * tempY.getElement().getIndex()) { dotProduct = dotProduct +
			 * (tempX.getElement().getIndex() * tempY.getElement().getIndex());
			 * tempX = this.head.getNext(); tempY = Y.head.getNext(); }
			 * 
			 * if (tempX.getElement().getIndex() >
			 * tempY.getElement().getIndex()) { tempY = Y.head.getNext();
			 * 
			 * if (tempX.getElement().getIndex() ==
			 * tempY.getElement().getIndex()) { dotProduct = dotProduct +
			 * (tempX.getElement().getIndex() * tempY.getElement().getIndex());
			 * tempY = Y.head.getNext();
			 * 
			 * } else if (tempX.getElement().getIndex() <
			 * tempY.getElement().getIndex()) { tempX = this.head; } } tempX =
			 * this.head.getNext(); tempY = Y.head.getNext();
			 */

			if (tempX.getElement().getIndex() > tempY.getElement().getIndex()) {
				tempY = tempY.getNext();
			} else if (tempX.getElement().getIndex() == tempY.getElement().getIndex()) {
				dotProduct = dotProduct + (tempX.getElement().getValue() * tempY.getElement().getValue());
				tempY = tempY.getNext();
				tempX = tempX.getNext();
			}

			else if (tempX.getElement().getIndex() < tempY.getElement().getIndex()) {
				tempX = tempX.getNext();
			}
		}
		return dotProduct;
	}

	/**
	 * returns string representation of sparse vector
	 */

	@Override
	public String toString() {
		String sparseVectorString = "";
		Iterator<SparseNumericElement> it = iterator();
		SparseNumericElement x;
		while (it.hasNext()) {
			x = it.next();
			sparseVectorString += "(index " + x.getIndex() + ", value " + x.getValue() + ")\n";
		}
		return sparseVectorString;
	}
}
