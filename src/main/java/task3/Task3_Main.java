package task3;

import java.util.*;
import java.util.concurrent.ForkJoinPool;

public class Task3_Main {
    private static final int ARRAY_SIZE = 15_000_000;
    private static final int MIN_VALUE = 0;
    private static final int MAX_VALUE = 10_000;

    public static void main(String[] args) {
        long sum = 0L;
        int mean = 0;
        long time = 0L;
        Map<String, Double> timeMap = new TreeMap<>(Comparator.reverseOrder());
        System.out.println("\u001b[33m" + "\n\n\tЗадача 3*. Калькулятор массива\n");

        int[] array = new int[ARRAY_SIZE];
        time = System.nanoTime();
        arrayFill(array);
        time = System.nanoTime() - time;
        System.out.printf("Заполнение массива: %d микросекунд\n", time / 1000);

        System.out.println("\tОднопоточный расчёт:");
        time = System.nanoTime();
        sum = arraySum(array);
        mean = arrayMean(array, sum);
        time = System.nanoTime() - time;
        printResult(sum, mean, time);
        timeMap.put("Однопоточно", (double) time);

        System.out.println("\tМногопоточный расчёт:");
        System.out.println("\tForkJoinPool из лекции. Потоков " + Runtime.getRuntime().availableProcessors());
        time = System.nanoTime();
        sum = multiSum(array);
        mean = arrayMean(array, sum);
        time = System.nanoTime() - time;
        System.gc();
        printResult(sum, mean, time);
        timeMap.put("Многопоточно (Юрий) x" + Runtime.getRuntime().availableProcessors(), (double) time);

        for (int threads = Runtime.getRuntime().availableProcessors() + 2; threads > 0; threads--) {
            System.gc();
            System.out.println("\tForkJoinPool из сети (Хабр). Потоков " + threads);
            time = System.nanoTime();
            sum = multiSumFromWeb(array, threads);
            mean = arrayMean(array, sum);
            time = System.nanoTime() - time;
            printResult(sum, mean, time);
            timeMap.put("Многопоточно (Хабр) x" + threads, (double) time);
        }

        setRelativeMapValues(timeMap);
        System.out.println("Результаты замера времени (относительные):");
        int maxKeyLength = 0;
        for (String key : timeMap.keySet()) {
            if (key.length() > maxKeyLength) maxKeyLength = key.length();
        }
        for (String key : timeMap.keySet()) {
            System.out.printf("\t%" + maxKeyLength + "s \t%d %%\n", key, Math.round(timeMap.get(key) * 100));
        }
    }

    private static void arrayFill(int[] array) {
        Random random = new Random();
        for (int i = 0; i < array.length; i++)
            array[i] = random.nextInt(MIN_VALUE, MAX_VALUE + 1);
    }

    private static long arraySum(int[] array) {
        long sum = 0;
        for (int x : array) sum += x;
        return sum;
    }

    private static int arrayMean(int[] array, long sum) {
        int mean = 0;
        mean = (int) (sum / array.length);
        return mean;
    }

    private static long multiSum(int[] array) {
        return new RecursiveSum(array, 0, array.length).compute();
    }

    private static long multiSumFromWeb(int[] array, int threads) {
        ValueSumCounter rec = new ValueSumCounter(array);
        return new ForkJoinPool(threads).invoke(rec);
    }

    private static void setRelativeMapValues(Map<String, Double> map) {
        double max = Collections.min(map.values());
        map.replaceAll((k, v) -> (int) (map.get(k) / max * 1000) / 1000.0);
    }

    private static void printResult(long sum, int mean, long nanoTime) {
        System.out.println("Сумма элементов массива: " + sum);
        System.out.println("Среднее значение элементов: " + mean);
        System.out.printf("Время выполнения: %d микросекунд\n", nanoTime / 1000);
    }
}
