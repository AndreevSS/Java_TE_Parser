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


    static void writeSplitter( Path[] logFiles, String newFileName, String splitter) throws IOException {

        String regex = "^(.+?) (.+?) ((.+?) (.+?)\\t(.*)|(INFO :)\\s(.*))";
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);

        String line;
        String result = "";

        BufferedWriter writer = Files.newBufferedWriter(Paths.get(newFileName));

        for (Path file : logFiles) {
            BufferedReader reader = Files.newBufferedReader(file);

            while ((line = reader.readLine()) != null) {
                Matcher matcher = pattern.matcher(line);
                int grps = matcher.groupCount();

                if (matcher.find()) {
                    if (matcher.group(4) != null)
                    {
                        result = matcher.group(1) + splitter + matcher.group(2) + splitter + matcher.group(4)
                                + splitter + matcher.group(5) + splitter + matcher.group(6);

                      //  writer.write(matcher.group(1) + splitter + matcher.group(2) + splitter + matcher.group(4)
                      //          + splitter + matcher.group(5) + splitter + matcher.group(6) + "\n");
                    }
                    else {
                  //      result = matcher.group(1) + splitter + matcher.group(2) + splitter + matcher.group(7)
                  //              + splitter + matcher.group(8);

                        writer.write(matcher.group(1) + splitter + matcher.group(2) + splitter + matcher.group(7)
                                + splitter + matcher.group(8) + "\n");
                    }
                    writer.write(result + "\n");
                    //System.out.println("result = " + result);
                }
                ;
            }
        }
        writer.close();
        //      System.out.println("dd");
    }

    public static void main(String[] args) throws IOException {
        // write your code here
        String splitter = args[0];
        String logFilePath = args[1];
        String newFileName = args[2];

        long startTime = System.nanoTime();

        Path[] files = findFiles(logFilePath);

        long endTime = System.nanoTime();
        long duration = (endTime - startTime) / 1000000 ;

        System.out.println("findFiles duration = " + duration);
        System.out.println("files count = " + files.length);

        startTime = System.nanoTime();

        writeSplitter(files, newFileName, splitter);

        endTime = System.nanoTime();
        duration = (endTime - startTime) / 1000000 ;
        System.out.println("writeSplitter duration = " + duration);

    }
}
