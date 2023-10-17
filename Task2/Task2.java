import java.util.Scanner;
import java.util.concurrent.*;

public class Task2 {
    public static void main(String []args) throws Exception {
        // Создаем экземпляр калькулятора для вычисления квадрата числа
        Calculator squareCalculator = new Calculator();
        
        Scanner in = new Scanner(System.in);
        
        // Запрашиваем у пользователя ввод числа
        System.out.print("Вычислить квадрат для числа ");
        int x = in.nextInt();
        
        // Запускаем вычисление квадрата в отдельном потоке
        Future future = squareCalculator.calculate(x);
        
        // Запрашиваем у пользователя ввод еще одного числа
        int x1 = in.nextInt();
        
        // Запускаем вычисление квадрата для второго числа в отдельном потоке
        Future future1 = squareCalculator.calculate(x1);
        
        // Ожидаем завершения вычислений и выводим результаты
        System.out.println("Квадрат числа: " + future.get());
        System.out.println("Квадрат числа: " + future1.get());
    }
}

class Calculator {
    // Создаем пул потоков для выполнения вычислений
    private ExecutorService executor = Executors.newFixedThreadPool(2);
    
    // Метод для асинхронного вычисления квадрата числа
    public Future calculate(Integer input) {
        return executor.submit(() -> {
            // Генерируем случайное время задержки
            int time = (int) ((Math.random() * 4000) + 1000);
            System.out.println("Время задержки: " + time + " миллисекунд");
            
            // Задерживаем поток на указанное время
            Thread.sleep(time);
            
            // Возвращаем результат вычисления квадрата числа
            return input * input;
        });
    }
}
