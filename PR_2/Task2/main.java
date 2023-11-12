package Task2;

import java.io.*;

public class FirstMethod {
    private static void copyFileUsingStream(File source, File dest) throws IOException {
        InputStream is = null;
        OutputStream os = null;
        try {
            is = new FileInputStream(source);
            os = new FileOutputStream(dest);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = is.read(buffer)) > 0) {
                os.write(buffer, 0, length);
            }
        } finally {
            is.close();
            os.close();
        }
    }

    public static void main(String[] args) throws IOException {
        long usedBytes = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        String path = "src/Task2/100MB.txt";
        File f = new File(path);
        String path1 = "src/Task2/100MBcopy1.txt";
        File f1 = new File(path1);
        long start = System.nanoTime();
        copyFileUsingStream(f, f1);
        System.out.println("Время копирования файла = " + (System.nanoTime() - start) + "\nИспользовано памяти: " + usedBytes);
    }
}
package Task2;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

public class SecondMethod {
    public static void copyFile(File sourceFile, File destFile) throws IOException {
        if(!destFile.exists()) {
            destFile.createNewFile();
        }

        FileChannel source = null;
        FileChannel destination = null;

        try {
            source = new FileInputStream(sourceFile).getChannel();
            destination = new FileOutputStream(destFile).getChannel();
            destination.transferFrom(source, 0, source.size());
        }
        finally {
            if(source != null) {
                source.close();
            }
            if(destination != null) {
                destination.close();
            }
        }
    }

    public static void main(String[] args) throws IOException {
        long usedBytes = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        String path = "src/Task2/100MB.txt";
        File f = new File(path);

        String path1 = "src/Task2/100MBcopy2.txt";
        File f1 = new File(path1);
        long start = System.nanoTime();
        copyFile(f, f1);
        System.out.println("Время копирования файла = " + (System.nanoTime() - start) + "\nИспользовано памяти: " + usedBytes);
    }
}
package Task2;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

public class ThirdMethod {
    private static void copyFileUsingApacheCommonsIO(File source, File dest) throws IOException {
        FileUtils.copyFile(source, dest);
    }

    public static void main(String[] args) throws IOException {
        long usedBytes = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        String path = "src/Task2/100MB.txt";
        File f = new File(path);
        String path1 = "src/Task2/100MBcopy3.txt";
        File f1 = new File(path1);
        long start = System.nanoTime();
        copyFileUsingApacheCommonsIO(f, f1);
        System.out.println("Время копирования файла = " + (System.nanoTime() - start) + "\nИспользовано памяти: " + usedBytes);
    }
}
package Task2;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class FourthMethod {
    private static void copyFileUsingJava7Files(File source, File dest) throws IOException {
        Files.copy(source.toPath(), dest.toPath());
    }

    public static void main(String[] args) throws IOException {
        long usedBytes = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        String path = "src/Task2/100MB.txt";
        File f = new File(path);
        String path1 = "src/Task2/100MBcopy4.txt";
        File f1 = new File(path1);
        long start = System.nanoTime();
        copyFileUsingJava7Files(f, f1);
        System.out.println("Время копирования файла = " + (System.nanoTime() - start) + "\nИспользовано памяти: " + usedBytes);
    }
}

