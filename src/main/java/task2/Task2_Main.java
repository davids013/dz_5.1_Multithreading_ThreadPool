package task2;

import java.util.concurrent.*;

public class Task2_Main {
    public static void main(String[] args) {
        int temp = 0;
        ExecutorService es = Executors.newFixedThreadPool(2);
        Callable<Integer> c1 = new SomeCallable("носит воду");
        Callable<Integer> c2 = new SomeCallable("стирает одежду");
        Callable<Integer> c3 = new SomeCallable("пишет книгу");

        Future<Integer> f1 = es.submit(c1);
        Future<Integer> f2 = es.submit(c2);
        Future<Integer> f3 = es.submit(c3);

        for (int i = 0; i < 5; i++) {
            try {
                es.submit(c1).get();
                es.submit(c2).get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            try {
                temp = f2.get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
        try {
            es.awaitTermination(1000, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(temp);
        System.out.println(f1.isDone());
        System.out.println(f2.isDone());
        System.out.println(f3.isDone());

        es.shutdown();
    }
}
