package threads;

import functions.Functions;
import java.util.concurrent.Semaphore;

public class Integrator extends Thread {
    private Task task;
    private Semaphore semaphore;

    public Integrator(Task task, Semaphore semaphore) {
        this.task = task;
        this.semaphore = semaphore;
    }

    public void run() {
        for (int i = 0; i < task.getTasksCount(); i++) {
            try {
                if (isInterrupted()) {
                    return;
                }

                // используем семафор для синхронизации чтения
                semaphore.acquire();

                // дополнительная проверка границ
                double left = task.getLeftBorder();
                double right = task.getRightBorder();
                double step = task.getStep();

                if (left >= right) {
                    System.out.printf("Ошибка: некорректные границы %.4f >= %.4f\n", left, right);
                    semaphore.release();
                    continue;
                }

                double result = Functions.integrate(task.getFunction(),
                        left, right, step);

                System.out.printf("Result %.4f %.4f %.4f %.4f\n",
                        left, right, step, result);

                semaphore.release();

            } catch (InterruptedException e) {
                return;
            }
        }
    }
}