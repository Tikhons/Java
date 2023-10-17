package Task1;

import java.util.concurrent.TimeUnit;

public class FirstMethod {
    public static void main(String[] args) throws InterruptedException {
        long usedBytes = Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory();

        int[] array;
        array = new int[10000];

        for (int i = 0; i < array.length; i++) {
            array[i] = i;
        }

        long start = System.currentTimeMillis();
        int max = array[0];
        for(int i = 0; i < array.length; i++){
            if (i > max)
                max = i;
            TimeUnit.MILLISECONDS.sleep(1);
        }
        long finish = System.currentTimeMillis();
        long time = finish - start;
        System.out.println("Задействовано " + usedBytes + " байт\n"
                + "Время выполнения: " + time + " милисекунды\n" + "Максимальный элемент массива: " + max);
    }
}
package Task1;

import java.util.Arrays;
import java.util.Date;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;
import java.util.concurrent.TimeUnit;

public class ForkJoinMethod {

    public static void main(String[] args) throws Exception {
        long usedBytes = Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory();
        int[] array = getInitArray(10000);

        ValueMaxCounter counter = new ValueMaxCounter(array);

        System.out.println(new Date());
        long start = System.currentTimeMillis();
        ForkJoinPool forkJoinPool = new ForkJoinPool();
        System.out.println(new Date());
        System.out.println("Максимальный элемент массива: " + forkJoinPool.invoke(counter));
        long finish = System.currentTimeMillis();
        long time = finish - start;
        System.out.println(new Date());

        System.out.println("Задействовано " + usedBytes + " байт\n"
                + "Время выполнения: " + time + " милисекунды\n");
    }

    //заполнение массива
    public static int[] getInitArray(int capacity) {
        int[] array = new int[capacity];
        for (int i = 0; i < capacity; i++) {
            array[i] = i;
        }
        return array;
    }
}
package Task1;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class FutureThreadMethod {
    public static void main(String[] args) throws InterruptedException {
        int[] array;
        array = new int[10000];

        for (int i = 0; i < array.length; i++) {
            array[i] = i;
        }
        findMinInSeveralThreads(array, 8);
    }
    public static Integer findMinInSeveralThreads(int[] arr, int numOfThreads)
            throws InterruptedException {
                List<MyThread> threads = new ArrayList<>();
                CountDownLatch latch = new CountDownLatch(numOfThreads);
                long start = System.currentTimeMillis();
                for (int i = 0; i < numOfThreads; i++) {
                    MyThread thread = new MyThread(Arrays.copyOfRange(arr, i * 1000 / numOfThreads, (i + 1) * 1000 / numOfThreads), latch);
                    threads.add(thread);
                    thread.start();
        }
        latch.await();
        int result = threads.stream().max(Comparator.comparing(MyThread::getMax)).get().max;
        long end = System.currentTimeMillis();
        long usedBytes = Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory();
        System.out.println("Задействовано " + usedBytes
                + " байт\n" + "Время выполнения: " + (end - start) + " милисекунды\n"
                + "Максимальный элемент массива: " + result);
        return result;
    }

}


