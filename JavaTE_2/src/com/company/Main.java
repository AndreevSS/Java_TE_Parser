package com.company;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class Main {

    public static Path[] findFiles(String logFilePath) throws IOException {
        Path dir = Paths.get(logFilePath);

        Stream<Path> stream =
                Files.find(dir, 1,
                        (path, basicFileAttributes) -> {
                            File file = path.toFile();
                            return !file.isDirectory() &&
                                    file.getName().endsWith(".log");
                        });


        Path[] path = stream.toArray(Path[]::new);

        stream.close();

        return path;
        }


    static void writeRegex(Path[] logFiles, String newFileName, String regex) throws IOException {

        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);

        String line;
        String result = "";

        BufferedWriter writer = Files.newBufferedWriter(Paths.get(newFileName));

        for (Path file : logFiles) {
            BufferedReader reader = Files.newBufferedReader(file);

            while ((line = reader.readLine()) != null) {
                Matcher matcher = pattern.matcher(line);
                while (matcher.find()) {
                    result = line.substring(matcher.start(), matcher.end());
//                System.out.println(result);
                    // result = result + "\n" + line.substring(matcher.start(), matcher.end());
                    writer.write(result + "\n");
                }
                ;
            }
        }
        writer.close();
  //      System.out.println("dd");
    }

    public static void main(String[] args) throws IOException {
        // write your code here
        String regex = args[0];
        String logFilePath = args[1];
        String newFileName = args[2];

        long startTime = System.nanoTime();

        Path[] files = findFiles(logFilePath);

        long endTime = System.nanoTime();
        long duration = (endTime - startTime) / 1000000 ;

        System.out.println("findFiles duration = " + duration);
        System.out.println("files count = " + files.length);


         startTime = System.nanoTime();

         writeRegex(files, newFileName, regex);


        endTime = System.nanoTime();
        duration = (endTime - startTime) / 1000000 ;

        System.out.println("writeRegex duration = " + duration);

    }

}
