package main.java;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by IVlasyuk on 20.01.2018.
 */
public class FileTextSearcher {

    public static List<String> result = new ArrayList<>();

    private static ExecutorService pool = Executors.newFixedThreadPool(4);

    private static String searchingText;

    public static void main(String[] args) throws IOException, ExecutionException, InterruptedException {

        searchingText = args[0];

        //для каждого рутового диска...
        for(File rootDir :  File.listRoots()){

            //получаем массив названий того, что содержится в корневом диске
            String[] filesAndDirectoriesInRoot = rootDir.list();

            if(filesAndDirectoriesInRoot != null) {

                //запускаем задачу в пул, в которую передаём i-ое название содержимого корневого диска
                for (int i = 0; i < filesAndDirectoriesInRoot.length; i++) {

                    pool.submit(new MyRunnable(rootDir.getAbsolutePath() + filesAndDirectoriesInRoot[i]));
                }

            } else System.err.println("Disc "+rootDir+" has no content");
        }

        pool.shutdown();
        pool.awaitTermination(1, TimeUnit.HOURS);
        System.out.println("---RESULT---");
        for(String s : result) System.out.println(s);
    }

    public static class MyRunnable implements Runnable {

        Path pathToDirectory;
        //При создании экземпляра устанавливаем значение Path целевого каталога
        public MyRunnable(String pathToDirectory){
            Path path = Paths.get(pathToDirectory).toAbsolutePath();
            this.pathToDirectory = path;
        }

        @Override
        public void run() {
            try {
                Files.walkFileTree(pathToDirectory, new MyFileVisitor(searchingText));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}

        /*DirectoryStream<Path> entries = Files.newDirectoryStream(Paths.get("C:\\"));
        for(Path path : entries){
            System.out.println(path.getFileName());
        }*/

