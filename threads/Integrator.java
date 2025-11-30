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

                // вычисляем значение интеграла для параметров из объекта задания
                double result = Functions.integrate(task.getFunction(),
                        task.getLeftBorder(), task.getRightBorder(), task.getStep());

                System.out.printf("Result %.4f %.4f %.4f %.4f\n",
                        task.getLeftBorder(), task.getRightBorder(), task.getStep(), result);

            } catch (Exception e) {
                return;
            }
        }
    }
}