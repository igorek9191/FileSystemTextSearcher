package main.java;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

/**
 * Created by IVlasyuk on 20.01.2018.
 */
public class MyFileVisitor extends SimpleFileVisitor<Path> {

    String searchText = null;

    public MyFileVisitor(String searchText){
        this.searchText = searchText;
    }

    @Override
    public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException{

        Path path = dir.getFileName();
        String s = path.toString();
        if(s.contains(searchText)){
            //System.out.println("Searched text has been found in "+dir.toAbsolutePath());
            FileTextSearcher.result.add(dir.toAbsolutePath().toString());
            return FileVisitResult.CONTINUE;
        }
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {

        if(file.getFileName().toString().contains(searchText)){
            //System.out.println("Searched text has been found in "+file.toAbsolutePath());
            FileTextSearcher.result.add(file.toAbsolutePath().toString());
            return FileVisitResult.CONTINUE;
        }
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException{
        //System.err.println("Can't access directory "+file.toString());
        return FileVisitResult.SKIP_SUBTREE;
    }



}
