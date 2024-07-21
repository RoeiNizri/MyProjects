package iterator;

import java.util.Iterator;
import java.util.NoSuchElementException;

// This class implements Iterable<Integer> interface class by two arrays.
// Arrays of int, in each call to the next it returns the next number.
public class TwoArrays implements Iterable<Integer> {
	private int[] a1, a2;

	public TwoArrays(int[] a1, int[] a2) {
		this.a1 = a1;
		this.a2 = a2;
	}

	@Override
	public Iterator<Integer> iterator() {
		return new TwoArraysIter();
	}

	private class TwoArraysIter implements Iterator<Integer> {
		private int cnt1, cnt2;
		private boolean isReturned = true;

		@Override
		public boolean hasNext() {
			return cnt1 < a1.length || cnt2 < a2.length;
		}

		@Override
		public Integer next() {
			if (!hasNext())
				throw new NoSuchElementException();
			if (cnt1 < a1.length && cnt2 < a2.length) {
				if (isReturned) {
					isReturned = false;
					return a1[cnt1++];
				} else {
					isReturned = true;
					return a2[cnt2++];
				}
				
			}

			// The rest integers
			if (cnt1 < a1.length)
				return a1[cnt1++];
			else if (cnt2 < a2.length)
				return a2[cnt2++];
			return -1;
		}

	}
}
