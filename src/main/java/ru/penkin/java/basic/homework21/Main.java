package ru.penkin.java.basic.homework21;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

public class Main {         // в гитхаб vetka_1_dz21

    static class PrintTask implements Runnable {
        private final char letter;
        private final int times;
        private final Semaphore currentSemaphore;
        private final Semaphore nextSemaphore;

        public PrintTask(char letter, int times, Semaphore currentSemaphore, Semaphore nextSemaphore) {
            this.letter = letter;
            this.times = times;
            this.currentSemaphore = currentSemaphore;
            this.nextSemaphore = nextSemaphore;
        }

        @Override
        public void run() {
            for (int i = 0; i < times; i++) {
                try {
                    currentSemaphore.acquire();
                    System.out.print(letter);
                    System.out.flush();
                    nextSemaphore.release();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

    public static void main(String[] args) {
        ExecutorService executor = Executors.newFixedThreadPool(3);

        Semaphore semaphoreA = new Semaphore(1);
        Semaphore semaphoreB = new Semaphore(0);
        Semaphore semaphoreC = new Semaphore(0);

        executor.submit(new PrintTask('A', 5, semaphoreA, semaphoreB));
        executor.submit(new PrintTask('B', 5, semaphoreB, semaphoreC));
        executor.submit(new PrintTask('C', 5, semaphoreC, semaphoreA));

        executor.shutdown();
    }
}