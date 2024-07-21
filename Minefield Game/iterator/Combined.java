package iterator;

import java.util.Iterator;

// // This generic class implements Iterable<E> interface class by two arrays.
public class Combined<E> implements Iterable<E> {
	private Iterator<E> first, second;

	public Combined(Iterable<E> first, Iterable<E> second) {
		this.first = first.iterator();
		this.second = second.iterator();
	}

	@Override
	public Iterator<E> iterator() {
		return new CombinedIter();
	}

	private class CombinedIter implements Iterator<E> {
		private boolean isReturned = true;

		@Override
		public boolean hasNext() {
			return first.hasNext() || second.hasNext();
		}

		@Override
		public E next() {
			if (isReturned) {
				if (first.hasNext()) {
					if (second.hasNext())
						isReturned = false;
					return first.next();
				}
			} else {
				if (second.hasNext()) {
					if (first.hasNext())
						isReturned = true;
					return second.next();
				}
			}
			return null;
		}
	}
}
