package org.rola.metaclerk.utils;

import org.junit.Test;

import java.util.NoSuchElementException;

import static org.junit.Assert.*;

public class ArrayIteratorTest {

    @Test (expected = UnsupportedOperationException.class)
    public void normalIterator_remove_throwsUnsupportedOperationException() throws Exception {
        ArrayIterator<String> iter = new ArrayIterator<>(new String[]{"Vienas", "Du"});
        iter.remove();
    }

    @Test(expected = NoSuchElementException.class)
    public void iteratorReadUntilEnd_peekNext_throwsNoSuchElementException() throws Exception {
        ArrayIterator<String> iter = new ArrayIterator<>(new String[]{"Vienas"});
        iter.next();
        assertFalse(iter.hasNext());
        iter.peekNext();
    }
}