package Task3;

public class File {
    String name;
    String type;
    Integer size;

    public File(String name, String type, Integer size) {
        this.name = name;
        this.type = type;
        this.size = size;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getSize() {
        return size;
    }

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
    FileFactory(FileQueue fileQueue){
        this.fileQueue=fileQueue;
        addAllElements();
    }

    static public void addAllElements(){
        for (int i = 0; i < 6; i++) {
            nameList.add("name" + i);
        }
    }

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
    FileHandler(FileQueue fileQueue){
        this.fileQueue=fileQueue;
    }

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
    public synchronized void put(File file) {
        while (queue.size()>=5) {
            try {
                wait();
            }
            catch (InterruptedException e) {
            }
        }
        System.out.println("FileQueue: Файл "
                + file.name +
                " добавлен в очередь");
        notify();
        queue.add(file);
    }
}

package Task3;

public class Program {
    public static void main(String[] args) {
        FileQueue fileQueue = new FileQueue();
        FileFactory fileFactory = new FileFactory(fileQueue);
        FileHandler fileHandler = new FileHandler(fileQueue);
        new Thread(fileFactory).start();
        new Thread(fileHandler).start();
    }
}
