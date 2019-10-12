package ru.otus.hw02;

import java.util.*;

public class DIYArrayList<T> implements List<T> {
    private Object[] elements = new Object[]{};

    @Override
    public int size() {
        return elements.length;
    }

    @Override
    public boolean isEmpty() {
        return elements.length == 0;
    }

    @Override
    public boolean contains(Object o) {
        for (Object element : elements) {
            if (Objects.equals(element, o)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public Object[] toArray() {
        return elements;
    }

    @Override
    public boolean add(T t) {
        int currentElementsNumber = elements.length;

        increaseElementsSize(1);

        elements[currentElementsNumber] = t;

        return true;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object search : c) {
            if (!contains(search)) {
                return false;
            }
        }

        return true;
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        Object[] currentElements = elements;

        int collectionSize = c.size();
        increaseElementsSize(collectionSize);

        System.arraycopy(c.toArray(), 0, elements, currentElements.length, collectionSize);

        return true;
    }

    @Override
    public T get(int index) {
        Objects.checkIndex(index, elements.length);

        return (T)elements[index];
    }

    @Override
    public T set(int index, T element) {
        Objects.checkIndex(index, elements.length);

        T old = (T)elements[index];

        elements[index] = element;

        return old;
    }

    @Override
    public Iterator<T> iterator() {
        return new DiyIterator();
    }

    @Override
    public ListIterator<T> listIterator() {
        return new DiyListIterator(0);
    }

    @Override
    public ListIterator<T> listIterator(int index) {
        return new DiyListIterator(index);
    }

    // iterators
    private class DiyIterator implements Iterator<T> {
        int currentIndex = 0;
        int lastReturned = -1;

        @Override
        public boolean hasNext() {
            return currentIndex != elements.length;
        }

        @Override
        public T next() throws NoSuchElementException {
            if (currentIndex >= elements.length) {
                throw new NoSuchElementException();
            }

            T result = get(currentIndex);

            lastReturned = currentIndex;
            currentIndex++;

            return result;
        }
    }

    private class DiyListIterator extends DiyIterator implements ListIterator<T> {
        DiyListIterator(int index) {
            super();
            currentIndex = index;
        }

        @Override
        public boolean hasPrevious() {
            return currentIndex > 0;
        }

        @Override
        public T previous() throws NoSuchElementException {
            int previousIndex = previousIndex();

            lastReturned = previousIndex;

            if (previousIndex >= elements.length) {
                throw new ConcurrentModificationException();
            }

            if (!hasPrevious()) {
                throw new NoSuchElementException();
            }

            return get(previousIndex);
        }

        @Override
        public int nextIndex() {
            return currentIndex;
        }

        @Override
        public int previousIndex() {
            return --currentIndex;
        }

        @Override
        public void set(T t) {
            if (lastReturned < 0) {
                throw  new IllegalStateException();
            }

            try {
                DIYArrayList.this.set(lastReturned, t);
            } catch (IndexOutOfBoundsException ex) {
                throw new ConcurrentModificationException();
            }
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }

        @Override
        public void add(T t) {
            throw new UnsupportedOperationException();
        }
    }

    @Override
    public boolean addAll(int index, Collection<? extends T> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T1> T1[] toArray(T1[] a) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean remove(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void add(int index, T element) {
        throw new UnsupportedOperationException();
    }

    @Override
    public T remove(int index) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int indexOf(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int lastIndexOf(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<T> subList(int fromIndex, int toIndex) {
        throw new UnsupportedOperationException();
    }

    private void increaseElementsSize(int newElementsNumber) {
        Object[] increasedElementsArray = new Object[elements.length + newElementsNumber];

        System.arraycopy(elements, 0, increasedElementsArray, 0, elements.length);

        elements = increasedElementsArray;
    }
}
