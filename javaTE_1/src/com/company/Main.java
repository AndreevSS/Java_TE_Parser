package com.company;


import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;


public class Main {

    static int countLinesNew(String filename) throws IOException {
        InputStream is = new BufferedInputStream(new FileInputStream(filename));
        try {
            byte[] c = new byte[1024];

            int readChars = is.read(c);
            if (readChars == -1) {
                // bail out if nothing to read
                return 0;
            }

            // make it easy for the optimizer to tune this loop
            int count = 0;
            while (readChars == 1024) {
                for (int i=0; i<1024;) {
                    if (c[i++] == '\n') {
                        ++count;
                    }
                }
                readChars = is.read(c);
            }

            // count remaining characters
            while (readChars != -1) {
                System.out.println(readChars);
                for (int i=0; i<readChars; ++i) {
                    if (c[i] == '\n') {
                        ++count;
                    }
                }
                readChars = is.read(c);
            }

            return count == 0 ? 1 : count;
        } finally {
            is.close();
        }
    }

    static void WriteToFile(String logFileName, String prefix ,long totalLinesCount, int outpitFilesCount) throws IOException
    {
        long linesPerFile = totalLinesCount / 9;
//        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(logFileName),"windows-1251"));
        BufferedReader reader = Files.newBufferedReader(Paths.get(logFileName), Charset.forName("windows-1251"));

        for (int k = 0; k < outpitFilesCount; k++)
        {
            BufferedWriter writer = Files.newBufferedWriter(Paths.get(prefix + k + ".log"));
            for (long i = k*linesPerFile; i < Math.min( (k+1) * linesPerFile, totalLinesCount); i++)
            {
                writer.write(reader.readLine() + "\n");
            }
            writer.close();
        }

    }

    public static void main(String[] args) throws IOException {
	// write your code here
        String logFileName = args[0];
        String prefix = args[1];


        long startTime = System.nanoTime();
        long totalLinesCount = countLinesNew(logFileName);
        System.out.println("totalLinesCount = " + totalLinesCount);
        long endTime = System.nanoTime();
        long duration = (endTime - startTime) / 1000000 ;
        System.out.println("countLines duration = " + duration);

        startTime = System.nanoTime();
        WriteToFile(logFileName, prefix ,totalLinesCount, 10);
        endTime = System.nanoTime();
        duration = (endTime - startTime) / 1000000 ;
        System.out.println("WriteToFile duration = " + duration);

    }
}
