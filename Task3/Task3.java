package Task3;

// Класс, представляющий файл
public class File {
    String name;
    String type;
    Integer size;

    // Конструктор с параметрами
    public File(String name, String type, Integer size) {
        this.name = name;
        this.type = type;
        this.size = size;
    }

    // Геттер для имени файла
    public String getName() {
        return name;
    }
    
    // Сеттер для имени файла
    public void setName(String name) {
        this.name = name;
    }

    // Геттер для типа файла
    public String getType() {
        return type;
    }

    // Сеттер для типа файла
    public void setType(String type) {
        this.type = type;
    }

    // Геттер для размера файла
    public Integer getSize() {
        return size;
    }

    // Сеттер для размера файла
    public void setSize(Integer size) {
        this.size = size;
    }
}

package Task3;

import Task3.File;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

public class FileFactory implements Runnable {
    FileQueue fileQueue;
    static Queue<String> nameList = new LinkedList<String>();
    String[] typeList={"XML", "JSON", "XLS"};
    
    // Конструктор с параметром
    FileFactory(FileQueue fileQueue){
        this.fileQueue=fileQueue;
        addAllElements();
    }

    // Метод для заполнения списка имен файлов
    static public void addAllElements(){
        for (int i = 0; i < 6; i++) {
            nameList.add("name" + i);
        }
    }

    // Метод, который выполняется при запуске потока
    public void run() {
        Callable task = () -> {
            Integer time = (int) (Math.random() * 900 + 100);
            TimeUnit.MILLISECONDS.sleep(time);
            String type = typeList[(int) (Math.random() * 2)];
            Integer size = (int) (Math.random() * 900 + 100);
            File file = new File(nameList.poll(), type, size);
            System.out.println("FileFactory: " +
                    file.name + "(Тип: "
                    + file.type +
                    ", размер: "
                    + file.size
                    + " байт)"
                    + " создан за "
                    + time
                    + " миллисекунд");
            return file;
        };
        for (int i = 0; i < 6; i++) {
            FutureTask<File> file = new FutureTask<>(task);
            new Thread(file).start();
            try{
                fileQueue.put(file.get());
            } catch(Exception e){

            }
        }
    }
}

package Task3;

import java.util.concurrent.TimeUnit;

public class FileHandler implements Runnable {
    FileQueue fileQueue;
    
    // Конструктор с параметром
    FileHandler(FileQueue fileQueue){
        this.fileQueue=fileQueue;
    }

    // Метод, который выполняется при запуске потока
    public void run() {
        for (int i = 0; i < 6; i++) {
            File file = fileQueue.get();
            try{
                Integer time = file.size*7;
                TimeUnit.MILLISECONDS.sleep(time);
                System.out.println("FileHandler: Файл "
                        + file.name
                        + " обработан за "
                        + time + " миллисекунд");
                fileQueue.cut();
            }
            catch (Exception e){

            }
        }
    }
}

package Task3;

import Task3.File;
import java.util.LinkedList;
import java.util.Queue;

public class FileQueue {
    Queue<File> queue = new LinkedList<>();

    // Метод для извлечения файла из очереди
    public synchronized File get() {
        while (queue.size()<1) {
            try{
                wait();
            }
            catch (InterruptedException e) {
            }
        }
        notify();
        return queue.peek();
    }
// Метод для удаления файла из очереди
    public synchronized File cut() { //delete head of queue
        while (queue.size()<1) {
            try{
                wait();
            }
            catch (InterruptedException e) {
            }
        }
        notify();
        System.out.println("FileQueue: Файл " + queue.peek().name + " удален из очереди");

        return queue.poll();
    }

    // Метод для добавления файла в очередь
    public synchronized void put(File file) {
        while (queue.size()>=5) {
            try {
                wait();
            }
            catch (InterruptedException e) {
            }
        }
