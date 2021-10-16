package task3;

import java.util.concurrent.RecursiveTask;

public class RecursiveSum extends RecursiveTask<Long> {
    private final int[] array;
    private final int start;
    private final int end;

    public RecursiveSum(int[] array, int start, int end) {
        this.array = array;
        this.start = start;
        this.end = end;
    }

    @Override
    protected Long compute() {
//        System.out.println(Thread.currentThread().getName());
        final int diff = end - start;
        return switch (diff) {
            case 0 -> 0L;
            case 1 -> (long) array[start];
            case 2 -> (long) array[start] + array[start + 1];
            default -> forkTasksAndGetResult();
        };
    }

    private long forkTasksAndGetResult() {
        final int middle = (end - start) / 2 + start;
        RecursiveSum leftTask = new RecursiveSum(array, start, middle);
        RecursiveSum rightTask = new RecursiveSum(array, middle, end);
        invokeAll(leftTask, rightTask);
        return leftTask.join() + rightTask.join();
    }
}
