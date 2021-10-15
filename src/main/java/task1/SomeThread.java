package task1;

public class SomeThread extends Thread {
    private String description;
    private int delay;

    public SomeThread(String name, String description, int delay) {
        setName(name);
        setPriority(2);
        this.description = description;
        this.delay = delay;
    }

    @Override
    public void run() {
        while (!isInterrupted()) {
            System.out.printf("Поток \"%s\" %s\n", getName(), description);
            try {
                sleep(delay);
            } catch (InterruptedException e) {
                System.out.printf("\"%s\" остановлен\n", getName());
                interrupt();
            }
        }
    }
}
