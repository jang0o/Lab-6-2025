import functions.*;
import functions.basic.*;
import functions.meta.*;
import java.io.*;
import threads.*;
import java.util.Random;
import java.util.concurrent.Semaphore;

public class Main {
    public static void main(String[] args) {
        // создаем экспоненту
        Exp expFunction = new Exp();

        // теоретическое значение интеграла exp(x) от 0 до 1 = e - 1
        double theoreticalValue = Math.E - 1;
        System.out.println("теоретическое значение интеграла: " + theoreticalValue);

        // подбираем шаг дискретизации для точности до 7 знака
        double step = 0.1;

        while (true) {
            double currentIntegral = Functions.integrate(expFunction, 0, 1, step);
            double difference = Math.abs(currentIntegral - theoreticalValue);
            System.out.println("шаг: " + step + ", интеграл: " + currentIntegral + ", разница: " + difference);

            if (difference < 1e-7) {
                System.out.println("достигнута требуемая точность при шаге: " + step);
                break;
            }
            step /= 2.0;
        }

    System.out.println("\n****** Тест nonThread ******\n");
    // тестирование nonThread
    nonThread();

    System.out.println("\n****** Тест simpleThreads ******\n");
    // тестирование simpleThreads
    simpleThreads();

    System.out.println("\n****** Тест complicatedThreads ******\n");
    // тестирование complicatedThreads
    complicatedThreads();
    }

    // метод, реализующий последовательную версию программы
    public static void nonThread() {
        // создаем объект класса Task
        Task task = new Task();

        // устанавливаем количество выполняемых заданий
        task.setTasksCount(100);

        // создаем генератор случайных чисел
        Random random = new Random();

        // выполняем задания в цикле
        for (int i = 0; i < task.getTasksCount(); i++) {
            // создаем объект логарифмической ф-ции со случайным основанием от 1 до 10
            double base = 1 + random.nextDouble() * 9; // от 1 до 10
            Log logFunction = new Log(base);
            task.setFunction(logFunction);

            // левая граница области интегрирования
            double leftBorder = random.nextDouble() * 100;
            task.setLeftBorder(leftBorder);

            // правая граница области интегрирования
            double rightBorder = 100 + random.nextDouble() * 100;
            task.setRightBorder(rightBorder);

            // шаг дискретизации
            double step = random.nextDouble();
            task.setStep(step);

            System.out.printf("Source %.4f %.4f %.4f\n",
                    task.getLeftBorder(), task.getRightBorder(), task.getStep());

            // вычисляем значение интеграла для параметров из объекта задания
            double result = Functions.integrate(task.getFunction(),
                    task.getLeftBorder(), task.getRightBorder(), task.getStep());

            System.out.printf("Result %.4f %.4f %.4f %.4f\n",
                    task.getLeftBorder(), task.getRightBorder(), task.getStep(), result);
        }
    }

    // метод с многопоточной версией программы
    public static void simpleThreads() {
        // создаем объект задания
        Task task = new Task();

        // устанавливаем количество выполняемых заданий
        task.setTasksCount(100);

        // создаем потоки
        Thread generatorThread = new Thread(new SimpleGenerator(task));
        Thread integratorThread = new Thread(new SimpleIntegrator(task));

        // запускаем потоки
        generatorThread.start();
        integratorThread.start();

        // ждем завершения потоков
        try {
            generatorThread.join();
            integratorThread.join();
        } catch (InterruptedException e) {
            System.out.println("потоки были прерваны");
        }
    }

    // метод с усложненной многопоточной версией программы
    public static void complicatedThreads() {
        // создаем объект задания
        Task task = new Task();

        // устанавливаем количество выполняемых заданий (минимум 100)
        task.setTasksCount(100);

        // создаем два семафора для синхронизации
        Semaphore generatorSemaphore = new Semaphore(1);  // генератор начинает первым
        Semaphore integratorSemaphore = new Semaphore(0); // интегратор ждет

        // создаем потоки
        Generator generator = new Generator(task, generatorSemaphore, integratorSemaphore);
        Integrator integrator = new Integrator(task, generatorSemaphore, integratorSemaphore);

        // запускаем потоки
        generator.start();
        integrator.start();

        // ждем 50 миллисекунд
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
        }

        // прерываем потоки
        generator.interrupt();
        integrator.interrupt();

        // ждем завершения потоков
        try {
            generator.join();
            integrator.join();
        } catch (InterruptedException e) {
        }
    }
}