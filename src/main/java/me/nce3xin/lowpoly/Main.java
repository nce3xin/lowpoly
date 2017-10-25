package me.nce3xin.lowpoly;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by nce3xin on 2017/10/23.
 */
public class Main {
    public static void main(String[] args) throws IOException {
        if (args.length <= 0) {
            return;
        }
        String in = args[0];
        String out = args[1];
        File inFile = new File(in);
        if (inFile.exists()) {
            File outFile = new File(out);
            FileInputStream inputStream = new FileInputStream(inFile);
            FileOutputStream outputStream = new FileOutputStream(outFile);

            Configure config = Parser.parseParam("config/config.param");

            long startTime = System.currentTimeMillis();

            LowPoly.generate(inputStream, outputStream, config);

            long endTime = System.currentTimeMillis();
            System.out.println("target image has been saved to: " + outFile.getAbsolutePath());
            System.out.println("runtime: " + (endTime - startTime) + " ms");
        } else {
            System.err.println(inFile.getAbsolutePath() + " file doesn't exist");
        }
    }
}
