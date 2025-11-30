package threads;

import functions.basic.Log;
import java.util.Random;

public class SimpleGenerator implements Runnable {
    private Task task;

    public SimpleGenerator(Task task) {
        this.task = task;
    }

    public void run() {
        Random random = new Random();

        for (int i = 0; i < task.getTasksCount(); i++) {
            synchronized (task) {
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
            }

            // пауза чтобы интегрирующий поток забрал данные
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                System.out.println("генерирующий поток был прерван");
                return;
            }
        }
    }
}