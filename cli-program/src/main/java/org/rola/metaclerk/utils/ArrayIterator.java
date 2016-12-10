package org.rola.metaclerk.utils;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class ArrayIterator<T> implements Iterator<T> {
	private final T[] array;
	private int pos = 0;

	public ArrayIterator(T anArray[]) {
		array = anArray;
	}

	public boolean hasNext() {
		return pos < array.length;
	}

	public T next() throws NoSuchElementException {
		T result = peekNext();
		pos++;
		return result;
	}

	public void remove() {
		throw new UnsupportedOperationException();
	}

	public T current() {
		return array[pos-1];
	}

    public T peekNext() {
		if (hasNext())
			return array[pos];
		else
			throw new NoSuchElementException();
    }
}