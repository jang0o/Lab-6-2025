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

                // создаем объект логарифмической функции со случайным основанием от 1 до 10
                double base = 1 + random.nextDouble() * 9;
                Log logFunction = new Log(base);
                task.setFunction(logFunction);

                // левая граница области интегрирования (от 0 до 100)
                double leftBorder = random.nextDouble() * 100;
                task.setLeftBorder(leftBorder);

                // правая граница области интегрирования (от 100 до 200)
                double rightBorder = 100 + random.nextDouble() * 100;
                task.setRightBorder(rightBorder);

                // шаг дискретизации (от 0 до 1)
                double step = random.nextDouble();
                task.setStep(step);

                System.out.printf("Source %.4f %.4f %.4f\n",
                        task.getLeftBorder(), task.getRightBorder(), task.getStep());

            } catch (Exception e) {
                return;
            }
        }
    }
}