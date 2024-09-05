package util;
import java.util.*;

public class SortedList<E> extends ArrayList<E> {
    private Comparator<E> comparator;

    public SortedList(Comparator<E> comparator) {
        this.comparator = comparator;
    }

    public SortedList(Collection<? extends E> c, Comparator<E> comparator) {
        this(comparator);
        this.addAll(c);
    }

    @Override
    public boolean add(E e) {
        int index = searchForPosition(e, 0, this.size());
        super.add(index, e);
        return (index != -1);
    }

    @Override
    public void add(int index, E element) {
        this.add(element);     
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean allAdded = true;
        for (E element : c) {
            allAdded &= this.add(element);
        }
        return allAdded;
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        return this.addAll(c);
    }

    private int searchForPosition(E element, int mindex, int maxdex) {
        while (mindex < maxdex) {
            int middex = mindex + (maxdex - mindex) / 2;

            if (comparator.compare(element, get(middex)) < 0) {
                maxdex = middex;
            } else {
                mindex = middex + 1;
            }
        }
        return mindex;
    }
}
