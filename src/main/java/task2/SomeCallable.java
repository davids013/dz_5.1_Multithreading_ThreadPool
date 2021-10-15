package task2;

import java.util.concurrent.Callable;

public class SomeCallable implements Callable<Integer> {
    private final String name;
    private final int repeats;
    private final String description;
    private int calls;

    public SomeCallable(String name, String description, int repeats) {
        this.name = name;
        this.description = description;
        this.repeats = repeats;
        calls = 0;
    }

    public String getName() { return name; }

    public String getDescription() { return description; }

    public int getCalls() { return calls; }

    public int getRepeats() { return repeats; }

    @Override
    public Integer call() {
//        for (int i = 0; i < 1; i++) {
//            calls++;
//            System.out.println(description + ": " + calls);
//        }
        calls++;
        System.out.println(description + ": " + calls);
        return calls;
    }
}
