import java.util.Scanner;
import java.util.concurrent.*;
public class Task2 {
    public static void main(String []args) throws Exception {
        Calculator squareCalculator = new Calculator();
        Scanner in = new Scanner(System.in);
        System.out.print("Вычислить квадрат для числа ");
        int x = in.nextInt();
        Future future = squareCalculator.calculate(x);
        int x1 = in.nextInt();
        Future future1 = squareCalculator.calculate(x1);
        System.out.println("Квадрат числа: " + future.get());
        System.out.println("Квадрат числа: " + future1.get());
    }
}

class Calculator {
    private ExecutorService executor = Executors.newFixedThreadPool(2);
    public Future calculate(Integer input) {
        return executor.submit(() -> {
            int time = (int) ((Math.random() * 4000) + 1000);
            System.out.println("Время задержки: " + time + " миллисекунд");
            Thread.sleep(time);
            return input * input;
        });
    }
}
