package task2;

import java.util.*;
import java.util.concurrent.*;

public class Task2_Main {
    static final int MIN_DELAY = 2000;
    static final int MAX_DELAY = 3000;
    private static final int MAX_CALLS = 4;
    private static final int SLEEP_TIME = MAX_DELAY * (MAX_CALLS - 1) + (MAX_DELAY - MIN_DELAY) + 100;

    public static void main(String[] args) {
        System.out.println("\u001b[36m" + "\n\n\tЗадача 2. Межпоточный диалог со счетчиком\n");

        final List<SomeCallable> tasks = Arrays.asList(
                new SomeCallable("Водонос", "носит воду", new Random().nextInt(1, MAX_CALLS)),
                new SomeCallable("Кофеман", "пьёт кофе", new Random().nextInt(1, MAX_CALLS)),
                new SomeCallable("Писатель", "пишет книгу", new Random().nextInt(1, MAX_CALLS)),
                new SomeCallable("Жнец", "собирает урожай", new Random().nextInt(1, MAX_CALLS)));

        System.out.println("\tИсходные данные:");
        for (SomeCallable task : tasks) {
            System.out.println("Задача \"" + task.getName() + "\", вызовов " + task.getRepeats());
        }

        final Map<Callable<Integer>, Integer> resultMap = new HashMap<>();
        final List<Thread> threads = new ArrayList<>();
        for (int i = 0; i < tasks.size(); i++) {
            threads.add(new AnotherThread(tasks.get(i), resultMap, new Random().nextInt(MIN_DELAY, MAX_DELAY)));
        }

        System.out.println("\n\tПотоковые вызовы:");
        for (Thread t : threads) {
            t.start();
        }

        try {
            Thread.sleep(SLEEP_TIME);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("\tКоличество вызовов:");
        for (Callable<Integer> item : resultMap.keySet()) {
            System.out.println("\"" + ((SomeCallable) item).getName() + "\" = " + resultMap.get(item));
        }

        System.out.println("\n\tВызовы из пула:");
        final ExecutorService es = Executors.newFixedThreadPool(2);
        List<Future<Integer>> list = null;
        try {
            list = es.invokeAll(Arrays.asList(tasks.get(2), tasks.get(3)));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("\tЗначения в \"будущем\": ");
        for (int i = 0; i < list.size(); i++) {
            try {
                System.out.println("Задача " + (i + 1) + ": " + list.get(i).get(300, TimeUnit.MILLISECONDS));
            } catch (InterruptedException | ExecutionException | TimeoutException e) {
                e.printStackTrace();
            }
        }

        System.out.println("\n\tВызов всех задач из пула:");
        int temp = Integer.MIN_VALUE;
        try {
            temp = es.invokeAny(tasks);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        System.out.println("\nОтвет от самой быстрой задачи: " + temp);

        es.shutdown();
    }
}
