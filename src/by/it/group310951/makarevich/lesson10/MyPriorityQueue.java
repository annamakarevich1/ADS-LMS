package by.it.group310951.makarevich.lesson10;

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Queue;

public  class MyPriorityQueue<E extends Comparable<E>> implements Queue<E> {

    private E[] куча;
    private int размер;
    private static final int ДЕФОЛТНАЯ_ВМЕСТИМОСТЬ = 16;

    @SuppressWarnings("unchecked")
    public MyPriorityQueue() {
        куча = (E[]) new Comparable[ДЕФОЛТНАЯ_ВМЕСТИМОСТЬ];
        размер = 0;
    }

    @SuppressWarnings("unchecked")
    public MyPriorityQueue(int initialCapacity) {
        if (initialCapacity <= 0) {
            throw new IllegalArgumentException("Начальная вместимость должна быть положительной");
        }
        куча = (E[]) new Comparable[initialCapacity];
        размер = 0;
    }

    @SuppressWarnings("unchecked")
    private void ensureCapacity(int минВместимость) {
        if (минВместимость > куча.length) {
            int новаяВместимость = Math.max(минВместимость, куча.length * 2);
            E[] новаяКуча = (E[]) new Comparable[новаяВместимость];
            System.arraycopy(куча, 0, новаяКуча, 0, размер);
            куча = новаяКуча;
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < размер; i++) {
            sb.append(куча[i]);
            if (i < размер - 1) {
                sb.append(", ");
            }
        }
        sb.append("]");
        return sb.toString();
    }

    @Override
    public int size() {
        return размер;
    }

    @Override
    public void clear() {
        for (int i = 0; i < размер; i++) {
            куча[i] = null;
        }
        размер = 0;
    }

    @Override
    public boolean add(E элемент) {
        if (элемент == null) {
            throw new NullPointerException("Нельзя добавлять null элемент");
        }
        ensureCapacity(размер + 1);
        куча[размер] = элемент;
        всплытьВверх(размер);
        размер++;
        return true;
    }

    @Override
    public E remove() {
        if (isEmpty()) {
            throw new NoSuchElementException("Очередь пуста");
        }
        E корень = куча[0];
        куча[0] = куча[размер - 1];
        куча[размер - 1] = null;
        размер--;
        просочитьсяВниз(0);
        return корень;
    }

    @Override
    public boolean contains(Object o) {
        if (o == null) {
            return false;
        }
        for (int i = 0; i < размер; i++) {
            if (o.equals(куча[i])) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean offer(E элемент) {
        if (элемент == null) {
            return false; // Offer обычно возвращает false, если элемент не может быть добавлен
        }
        ensureCapacity(размер + 1);
        куча[размер] = элемент;
        всплытьВверх(размер);
        размер++;
        return true;
    }


    @Override
    public E poll() {
        if (isEmpty()) {
            return null;
        }
        E корень = куча[0];
        куча[0] = куча[размер - 1];
        куча[размер - 1] = null;
        размер--;
        просочитьсяВниз(0);
        return корень;
    }

    @Override
    public E peek() {
        if (isEmpty()) {
            return null;
        }
        return куча[0];
    }

    @Override
    public E element() {
        if (isEmpty()) {
            throw new NoSuchElementException("Очередь пуста");
        }
        return куча[0];
    }

    @Override
    public boolean isEmpty() {
        return размер == 0;
    }

    private void всплытьВверх(int индекс) {
        while (индекс > 0) {
            int индексРодителя = (индекс - 1) / 2;
            if (куча[индекс].compareTo(куча[индексРодителя]) < 0) {
                поменятьМестами(индекс, индексРодителя);
                индекс = индексРодителя;
            } else {
                break;
            }
        }
    }

    private void просочитьсяВниз(int индекс) {
        while (true) {
            int индексЛевогоРебенка = 2 * индекс + 1;
            int индексПравогоРебенка = 2 * индекс + 2;
            int наименьший = индекс;

            if (индексЛевогоРебенка < размер && куча[индексЛевогоРебенка].compareTo(куча[наименьший]) < 0) {
                наименьший = индексЛевогоРебенка;
            }

            if (индексПравогоРебенка < размер && куча[индексПравогоРебенка].compareTo(куча[наименьший]) < 0) {
                наименьший = индексПравогоРебенка;
            }

            if (наименьший != индекс) {
                поменятьМестами(индекс, наименьший);
                индекс = наименьший;
            } else {
                break;
            }
        }
    }

    private void поменятьМестами(int i, int j) {
        E temp = куча[i];
        куча[i] = куча[j];
        куча[j] = temp;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object item : c) {
            if (!contains(item)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean modified = false;
        for (E item : c) {
            if (add(item)) {
                modified = true;
            }
        }
        return modified;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean modified = false;
        for (Object item : c) {
            while (contains(item)) {
                if (remove(item)) {
                    modified = true;
                } else {
                    break;
                }
            }
        }
        return modified;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean modified = false;
        @SuppressWarnings("unchecked")
        E[] tempHeap = (E[]) new Comparable[куча.length]; // временный массив
        int tempSize = 0;

        for (int i = 0; i < размер; i++) {
            if (c.contains(куча[i])) {
                tempHeap[tempSize++] = куча[i];
            } else {
                modified = true;
            }
        }

        // Перестраиваем кучу
        clear(); // Очищаем оригинальную кучу
        куча = tempHeap;
        размер = tempSize; // Устанавливаем новый размер
        for (int i = 0; i < размер; i++) {
            всплытьВверх(i);
        }
        return modified;
    }


    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            private int currentIndex = 0;
            private final E[] snapshot = куча.clone();
            private int snapshotSize = размер;

            @Override
            public boolean hasNext() {
                return currentIndex < snapshotSize;
            }

            @Override
            public E next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                return snapshot[currentIndex++];
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException("Удаление не поддерживается");
            }
        };
    }

    @Override
    public Object[] toArray() {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        throw new UnsupportedOperationException();
    }

    public static void main(String[] args) {
        MyPriorityQueue<Integer> queue = new MyPriorityQueue<>();
        queue.add(5);
        queue.add(1);
        queue.add(3);
        queue.add(2);

        System.out.println("Очередь: " + queue);
        System.out.println("Размер: " + queue.size());
        System.out.println("Peek: " + queue.peek());
        System.out.println("Poll: " + queue.poll());
        System.out.println("Очередь после poll: " + queue);
        System.out.println("Содержит 3: " + queue.contains(3));
        System.out.println("Пуста: " + queue.isEmpty());

        queue.clear();
        System.out.println("Очередь после clear: " + queue);
        System.out.println("Пуста: " + queue.isEmpty());

        MyPriorityQueue<Integer> queue2 = new MyPriorityQueue<>(5);
        queue2.add(5);
        queue2.add(1);
        queue2.add(3);
        queue2.add(2);
        System.out.println("Очередь2: " + queue2);
        System.out.println("Итерация по очереди:");
        for (Integer element : queue2) {
            System.out.println(element);
        }
    }

    @Override
    public boolean remove(Object o) {
        if (o == null) {
            return false;
        }

        for (int i = 0; i < размер; i++) {
            if (o.equals(куча[i])) {
                куча[i] = куча[размер - 1];
                куча[размер - 1] = null;
                размер--;
                просочитьсяВниз(i);
                return true;
            }
        }
        return false;
    }
}