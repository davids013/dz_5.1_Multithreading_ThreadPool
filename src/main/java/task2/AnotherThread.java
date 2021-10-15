package task2;

import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class AnotherThread<T> extends Thread {
    private final SomeCallable call;
    private final int repeats;
    private final int delay;
    private final Map<Callable<Integer>, Integer> resultMap;


    public AnotherThread(SomeCallable call, Map<Callable<Integer>, Integer> resultMap, int delay) {
        setPriority(Thread.NORM_PRIORITY);
        this.call = call;
        setName(call.getName());
        this.repeats = call.getRepeats();
        this.resultMap = resultMap;
        this.delay = delay;
    }

    @Override
    public void run() {
        for (int i = 0; i < repeats; i++) {
            if (!isInterrupted()) {
                FutureTask<Integer> futureTask = new FutureTask<>(call);
                try {
                    sleep(delay - Task2_Main.MIN_DELAY);
                    System.out.printf("Поток \"%s\" ", getName());
                    futureTask.run();
                    int calls = futureTask.get();
                    resultMap.put(call, calls);
//                    System.out.printf("Поток \"%s\" %s: %d\n", getName(), call.getDescription(), calls);
                    sleep(delay);
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                    System.out.printf("\"%s\" остановлен\n", getName());
                    interrupt();
                }
            }
        }
    }
}
