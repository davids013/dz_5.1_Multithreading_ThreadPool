package task2;

import java.util.concurrent.Callable;

public class SomeCallable implements Callable<Integer> {
    private static int calls;
    private String description;

    public SomeCallable(String description) {
        this.description = description;
    }

    @Override
    public Integer call() {
        System.out.println(description);
        calls++;
        return calls;
    }
}
