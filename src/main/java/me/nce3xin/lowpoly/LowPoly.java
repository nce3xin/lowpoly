package me.nce3xin.lowpoly;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by nce3xin on 2017/10/23.
 */

public class LowPoly {
    public static void generate(InputStream inputStream
            , OutputStream outputStream
            ,Configure config) throws IOException {
        int pointCount=config.pointCount;
        int accuracy=config.accuracy;
        boolean fill=config.fill;

        if (inputStream == null || outputStream == null) {
            return;
        }
        BufferedImage image = ImageIO.read(inputStream);
        int width = image.getWidth();
        int height = image.getHeight();

        List<Point> collectors = new ArrayList<>();
        List<Point> particles = new ArrayList<>();

        Sobel.sobel(image, (magnitude, x, y) -> {
            if (magnitude > 40) {
                collectors.add(new Point(x, y));
            }
        });

        for (int i = 0; i < pointCount; ++i) {
            particles.add(new Point((int) (Math.random() * width), (int) (Math.random() * height)));
        }

        int len = collectors.size() / accuracy;
        for (int i = 0; i < len; ++i) {
            int random = (int) (Math.random() * collectors.size());
            particles.add(collectors.get(random));
            collectors.remove(random);
        }

        particles.add(new Point(0, 0));
        particles.add(new Point(0, height));
        particles.add(new Point(width, 0));
        particles.add(new Point(width, height));

        List<Integer> triangles = Delaunay.triangulate(particles);

        BufferedImage out = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics g = out.getGraphics();

        double x1, x2, x3, y1, y2, y3, cx, cy;

        for (int i = 0; i < triangles.size(); i += 3) {
            x1 = particles.get(triangles.get(i)).x;
            x2 = particles.get(triangles.get(i + 1)).x;
            x3 = particles.get(triangles.get(i + 2)).x;
            y1 = particles.get(triangles.get(i)).y;
            y2 = particles.get(triangles.get(i + 1)).y;
            y3 = particles.get(triangles.get(i + 2)).y;

            cx = (x1 + x2 + x3) / 3.0;
            cy = (y1 + y2 + y3) / 3.0;

            Color color = new Color(image.getRGB((int) cx, (int) cy));
            g.setColor(color);

            if (fill) {
                g.fillPolygon(new int[]{(int) x1, (int) x2, (int) x3}, new int[]{(int) y1, (int) y2, (int) y3}, 3);
            } else {
                g.drawPolygon(new int[]{(int) x1, (int) x2, (int) x3}, new int[]{(int) y1, (int) y2, (int) y3}, 3);
            }
        }
        ImageIO.write(out, "png", outputStream);
    }
}
