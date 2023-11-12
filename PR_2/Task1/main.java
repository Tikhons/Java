package Task1;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Task1 {
    public static void main(String[] args) throws IOException {
        // Определение пути к файлу
        String path = "src/Task1/file.txt";
        Path filePath = Paths.get(path);

        // Создание файла по указанному пути
        Files.createFile(filePath);

        // Задание строки для записи в файл
        String str = "Hello World!";

        // Преобразование строки в массив байтов
        byte[] fileSize = str.getBytes();

        // Запись массива байтов в файл
        Path writtenFilePath = Files.write(filePath, fileSize);

        // Вывод содержимого файла на экран
        System.out.println("File content:\n" + new String(Files.readAllBytes(writtenFilePath)));
    }
}

