import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
public class Four {

    public static void main(String[] args) {
        new Four().Run();
    }

    public void Run() {
        String reservePath = ".reserve";
        String path = "File_Folder";
        Reserve(path, reservePath);
        RunTask("File_Folder", reservePath);
    }

    void Reserve(String path, String reservePath) {
        CreateDirectory(".reserve");
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                DeleteDirectory(reservePath);
            }
        });
        CopyFolder(path, reservePath);
    }

    void CopyFolder(String path, String reservePath) {
        File[] files = new File(path).listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                CopyFolder(file.getAbsolutePath(), reservePath);
            } else {
                File reserveFile = new File(reservePath + "/" + file.getName() + ".bak");
                try {
                    reserveFile.createNewFile();
                    FileInputStream fis = new FileInputStream(file);
                    FileOutputStream fos = new FileOutputStream(reserveFile);
                    byte[] buffer = new byte[1024];
                    int length;
                    while ((length = fis.read(buffer)) > 0) {
                        fos.write(buffer, 0, length);
                    }
                    fis.close();
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    public void RunTask(String path, String reservePath) {
        try {
            // Create a WatchService instance
            WatchService watcher = FileSystems.getDefault().newWatchService();
            // Create a Path instance
            Path dir = Paths.get(path);
            // Register the path with the WatchService
            dir.register(watcher, StandardWatchEventKinds.ENTRY_CREATE,
                    StandardWatchEventKinds.ENTRY_DELETE,
                    StandardWatchEventKinds.ENTRY_MODIFY);

            while (true) {
                WatchKey key = watcher.take();
                for (WatchEvent<?> event : key.pollEvents()) {
                    WatchEvent.Kind<?> kind = event.kind();
                    // The filename is the context of the event.
                    WatchEvent<Path> ev = (WatchEvent<Path>) event;
                    Path filename = ev.context();
                    System.out.println(kind.name() + ": " + filename);
                    // If file is created, create bak file
                    if (kind == StandardWatchEventKinds.ENTRY_CREATE) {
                        CreateBak(Paths.get(path, filename.toString()),
                                Paths.get(reservePath, filename.toString()));
                    }
                    // If the modification event is triggered print diff
                    if (kind == StandardWatchEventKinds.ENTRY_MODIFY) {
                        Diff(Paths.get(path, filename.toString()),
                                Paths.get(reservePath, filename.toString() + 
".bak"));
                        CreateBak(Paths.get(path, filename.toString()),
                                Paths.get(reservePath, filename.toString()));
                    }
                    // If the deletion event is triggered, print the file name
                    if (kind == StandardWatchEventKinds.ENTRY_DELETE) {
                        OnDelete(Paths.get(reservePath, filename.toString() + ".bak"));
                        DeleteBakFile(Paths.get(reservePath, filename.toString() + ".bak"));
                    }
                }
                boolean valid = key.reset();
                if (!valid) {
                    break;
                }
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    // Compare the two files and print added or deleted lines with line numbers
    Boolean Diff(Path path, Path reservePath) {
        Boolean isDiff = false;
        try {
            FileInputStream fis = new FileInputStream(path.toFile());
            FileInputStream fisReserve = new FileInputStream(reservePath.toFile());
            byte[] buffer = new byte[1024];
            byte[] bufferReserve = new byte[1024];
            int length;
            int lengthReserve;
            int line = 1;
            while ((length = fis.read(buffer)) > 0 && (lengthReserve = fisReserve.read(bufferReserve)) > 0) {
                String str = new String(buffer, 0, length);
                String strReserve = new String(bufferReserve, 0, lengthReserve);
                String[] lines = str.split("\\r?\\n");
                String[] linesReserve = strReserve.split("\\r?\\n");
                int i = 0;
                int j = 0;
                while (i < lines.length && j < linesReserve.length) {
                    if (!lines[i].equals(linesReserve[j])) {
                        System.out.println("Line " + line + " added: " + lines[i]);
                        j++;
                    } else {
                        i++;
                        j++;
                    }
                    line++;
                }
                while (i < lines.length) {
                    isDiff = true;
                    System.out.println("Line " + line + " added: " + lines[i]);
                    i++;
                    line++;
                }
                while (j < linesReserve.length) {
                    isDiff = true;
                    System.out.println("Line " + line + " deleted: " + linesReserve[j]);
                    j++;
                    line++;
                }
            }
            fis.close();
            fisReserve.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return isDiff;
    }

    public void CreateBak(Path path, Path reservePath) {
        try {

            java.nio.file.Files.copy(path, reservePath.resolveSibling(
                    reservePath.getFileName() + ".bak"), java.nio.file.StandardCopyOption.REPLACE_EXISTING);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void OnDelete(Path path) {
        System.out.println("Checksum: " + CalculateSum(path.toString().toString()));
    }

    public void DeleteBakFile(Path path) {
        try {
            java.nio.file.Files.delete(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int CalculateSum(String filename) {
        try {
            File file = new File(filename);
            FileInputStream fis = new FileInputStream(file);
            byte[] arr = new byte[(int) file.length()];
            fis.read(arr);
            ByteBuffer bb = ByteBuffer.wrap(arr);
            int sum = 0;
            while (bb.hasRemaining()) {
                sum += bb.getShort();
            }
            fis.close();
            return sum;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
    public void CreateDirectory(String name) {
        File file = new File(name);
        if (!file.exists()) {
            if (file.mkdir()) {
                System.out.println("Start monitoring...");
            } else {
                System.out.println("Failed to create reserve folder for monitoring.");
            }
        }
    }

    public void DeleteDirectory(String name) {
        CleanDirectory(name);
        File file = new File(name);
        if (file.exists()) {
            if (file.delete()) {
                System.out.println("Stop monitoring...");
            } else {
                System.out.println("Failed to delete reserve folder for monitoring.");
            }
        }
    }

        File file = new File(name);
        if (file.exists()) {
            File[] files = file.listFiles();
            for (File f : files) {
                if (f.isFile()) {
                    f.delete();
                }
            }
        }
    }
}
