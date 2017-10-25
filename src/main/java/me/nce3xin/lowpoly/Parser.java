package me.nce3xin.lowpoly;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by nce3xin on 2017/10/25.
 */


public class Parser {
    public static Configure parseParam(String filePath) {
        Configure config = new Configure();
        BufferedReader in = null;
        try {
            in = new BufferedReader(new FileReader(filePath));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        String s;
        try {
            while ((s = in.readLine()) != null) {
                String[] tokens = s.split("=");
                if (tokens[0].equals("pointCount")) {
                    config.pointCount = Integer.parseInt(tokens[1]);
                } else if (tokens[0].equals("accuracy")) {
                    config.accuracy = Integer.parseInt(tokens[1]);
                } else if (tokens[0].equals("fill")) {
                    config.fill = Boolean.parseBoolean(tokens[1]);
                }
            }
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return config;
    }
}
