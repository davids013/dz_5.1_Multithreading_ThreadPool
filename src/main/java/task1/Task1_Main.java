package task1;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

public class Task1_Main {
    public static void main(String[] args) {
        final int DELAY = 15_000;
        System.out.println("\n\tЗадача 1. Межпоточный диалог\n");

        Thread timer = new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                System.out.println(LocalTime.now().truncatedTo(ChronoUnit.SECONDS));
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        ThreadGroup tg = new ThreadGroup("group");
        Thread t1 = new Thread(tg, new SomeThread("Пешеход", "переходит улицу", 2000));
        Thread t2 = new Thread(tg, new SomeThread("Грибник", "собирает грибы", 3000));
        Thread t3 = new Thread(tg, new SomeThread("Спортсмен", "тренируется", 3000));
        Thread t4 = new Thread(tg, new SomeThread("Кулинар", "варит суп", 2000));

        timer.setPriority(Thread.MAX_PRIORITY);
        timer.setDaemon(true);

        timer.start();
        t1.start();
        t2.start();
        t3.start();
        t4.start();

        try {
            Thread.sleep(DELAY);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        tg.interrupt();
    }
}
