package me.nce3xin.lowpoly;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by nce3xin on 2017/10/23.
 */
public class Sobel {
    private static final int[][] kernelX = {{-1, 0, 1}, {-2, 0, 2}, {-1, 0, 1}};
    private static final int[][] kernelY = {{-1, -2, -1}, {0, 0, 0}, {1, 2, 1}};

    public static void sobel(BufferedImage image, SobelCallback callback) {
        int w = image.getWidth();
        int h = image.getHeight();
        for (int y = 0; y < h; ++y) {
            for (int x = 0; x < w; ++x) {
                int pixelX = kernelX[0][0] * getGrayValue(image, x - 1, y - 1)
                        + kernelX[0][1] * getGrayValue(image, x, y - 1)
                        + kernelX[0][2] * getGrayValue(image, x + 1, y - 1)
                        + kernelX[1][0] * getGrayValue(image, x - 1, y)
                        + kernelX[1][1] * getGrayValue(image, x, y)
                        + kernelX[1][2] * getGrayValue(image, x + 1, y)
                        + kernelX[2][0] * getGrayValue(image, x - 1, y + 1)
                        + kernelX[2][1] * getGrayValue(image, x, y + 1)
                        + kernelX[2][2] * getGrayValue(image, x + 1, y + 1);
                int pixelY = kernelY[0][0] * getGrayValue(image, x - 1, y - 1)
                        + kernelY[0][1] * getGrayValue(image, x, y - 1)
                        + kernelY[0][2] * getGrayValue(image, x + 1, y - 1)
                        + kernelY[1][0] * getGrayValue(image, x - 1, y)
                        + kernelY[1][1] * getGrayValue(image, x, y)
                        + kernelY[1][2] * getGrayValue(image, x + 1, y)
                        + kernelY[2][0] * getGrayValue(image, x - 1, y + 1)
                        + kernelY[2][1] * getGrayValue(image, x, y + 1)
                        + kernelY[2][2] * getGrayValue(image, x + 1, y + 1);
                int magnitude = (int) Math.sqrt((pixelX * pixelX) + (pixelY * pixelY));
                callback.call(magnitude, x, y);
            }
        }
    }

    private static int getGrayValue(BufferedImage image, int x, int y) {
        if (x < 0 || y < 0 || x >= image.getWidth() || y >= image.getHeight()) {
            return 0;
        }
        Color color = new Color(image.getRGB(x, y));
        int grayValue = (color.getRed() + color.getGreen() + color.getBlue()) / 3;
        return grayValue;
    }

    public interface SobelCallback {
        void call(int magnitude, int x, int y);
    }
}
