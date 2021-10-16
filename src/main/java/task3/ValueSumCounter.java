package task3;

import java.util.Arrays;
import java.util.concurrent.RecursiveTask;

public class ValueSumCounter extends RecursiveTask<Long> {
    private int[] array;

    public ValueSumCounter(int[] array) {
        this.array = array;
    }

    @Override
    protected Long compute() {
        if (array.length <= 2) {
//            System.out.println(Thread.currentThread().getName());
            return (long) Arrays.stream(array).sum();
        }
        ValueSumCounter left =
                new ValueSumCounter(Arrays.copyOfRange(array, 0, array.length/2));
        ValueSumCounter right =
                new ValueSumCounter(Arrays.copyOfRange(array, array.length/2, array.length));
        left.fork();
        right.fork();
        return left.join() + right.join();
    }
}
