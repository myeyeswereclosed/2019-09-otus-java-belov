package ru.otus.hw02;

import java.util.*;

public class DIYArrayList<T> implements List<T> {
    // initial elements container size
    private static final int INITIAL_SIZE = 10;

    // default factor to increase elements container size
    private static final float INCREASE_FACTOR = 1.5f;

    // container for elements
    private Object[] elementsContainer;

    // number of elements in container
    private int actualSize = 0;

    public DIYArrayList() {
        elementsContainer = new Object[INITIAL_SIZE];
    }

    public DIYArrayList(int size) {
        if (size > 0) {
            elementsContainer = new Object[size];
        } else if (size == 0) {
            elementsContainer = new Object[INITIAL_SIZE];
        } else {
            throw new IllegalArgumentException("List size is invalid " + size);
        }
    }

    @Override
    public int size() {
        return actualSize;
    }

    @Override
    public boolean isEmpty() {
        return actualSize == 0;
    }

    @Override
    public boolean contains(Object o) {
        for (int i = 0; i < actualSize; i++) {
            if (Objects.equals(elementsContainer[i], o)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public Object[] toArray() {
        return actualElements();
    }

    @Override
    public boolean add(T t) {
        if (actualSize == elementsContainer.length) {
            increaseContainerSizeDefault();
        }

        elementsContainer[actualSize++] = t;

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
        int collectionSize = c.size();

        if (actualSize + collectionSize >= elementsContainer.length) {
            increaseContainerSize(collectionSize);
        }

        System.arraycopy(c.toArray(), 0, elementsContainer, actualSize, collectionSize);
        actualSize += collectionSize;

        return true;
    }

    @Override
    public T get(int index) {
        Objects.checkIndex(index, actualSize);

        return (T) elementsContainer[index];
    }

    @Override
    public T set(int index, T element) {
        Objects.checkIndex(index, actualSize);

        T old = (T) elementsContainer[index];

        elementsContainer[index] = element;

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
            return currentIndex != actualSize;
        }

        @Override
        public T next() throws NoSuchElementException {
            if (currentIndex >= actualSize) {
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

            if (previousIndex >= actualSize) {
                throw new ConcurrentModificationException();
            }

            if (!hasPrevious()) {
                throw new NoSuchElementException();
            }

            currentIndex--;

            return get(previousIndex);
        }

        @Override
        public int nextIndex() {
            return currentIndex;
        }

        @Override
        public int previousIndex() {
            return currentIndex - 1;
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

    private void increaseContainerSizeDefault() {
        elementsContainer = Arrays.copyOf(elementsContainer, increasedContainerSize(elementsContainer.length));
    }

    private void increaseContainerSize(int newElementsNumber) {
        if (newElementsNumber > increasedContainerSize(elementsContainer.length)) {
            elementsContainer =
                Arrays.copyOf(
                    elementsContainer,
                    increasedContainerSize(elementsContainer.length + newElementsNumber)
                );
        } else {
            increaseContainerSizeDefault();
        }
    }

    private int increasedContainerSize(int containerSize) {
        return (int)(containerSize * INCREASE_FACTOR);
    }

    private Object[] actualElements() {
        return Arrays.copyOf(elementsContainer, actualSize);
    }
}
