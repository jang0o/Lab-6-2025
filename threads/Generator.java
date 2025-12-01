package threads;

import functions.basic.Log;
import java.util.Random;
import java.util.concurrent.Semaphore;

public class Generator extends Thread {
    private Task task;
    private Semaphore semaphore;

    public Generator(Task task, Semaphore semaphore) {
        this.task = task;
        this.semaphore = semaphore;
    }

    public void run() {
        Random random = new Random();

        for (int i = 0; i < task.getTasksCount(); i++) {
            try {
                if (isInterrupted()) {
                    return;
                }


                double base = 1 + random.nextDouble() * 9;
                Log logFunction = new Log(base);
                double leftBorder = random.nextDouble() * 100;

                double rightBorder;
                do {
                    rightBorder = 100 + random.nextDouble() * 100;
                } while (rightBorder <= leftBorder);

                double step = random.nextDouble();

                // используем семафор для синхронизации записи
                semaphore.acquire();

                task.setFunction(logFunction);
                task.setLeftBorder(leftBorder);
                task.setRightBorder(rightBorder);
                task.setStep(step);

                System.out.printf("Source %.4f %.4f %.4f\n",
                        leftBorder, rightBorder, step);

                semaphore.release();

            } catch (InterruptedException e) {
                return;
            }
        }
    }
}