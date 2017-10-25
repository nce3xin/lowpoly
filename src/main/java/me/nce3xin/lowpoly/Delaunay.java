package me.nce3xin.lowpoly;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by nce3xin on 2017/10/24.
 */
class Triangle {
    public Point one;
    public Point two;
    public Point three;
    public Triangle(Point one, Point two, Point three) {
        this.one = one;
        this.two = two;
        this.three = three;
    }
}

class Circumcircle {
    public int i, j, k;
    public double x, y, r;

    public Circumcircle(int i, int j, int k, double x, double y, double r) {
        this.i = i;
        this.j = j;
        this.k = k;
        this.x = x;
        this.y = y;
        this.r = r;
    }
}

public class Delaunay {
    private static final double EPSILON = 1.0f / 1048576.0f;

    public static ArrayList<Integer> triangulate(List<Point> points) {
        int n = points.size();
        if (n < 3) return new ArrayList<>();

        List<Integer> indices = new ArrayList<>();
        for (int i = 0; i < n; ++i) {
            indices.add(i);
        }

        Collections.sort(indices, new Comparator<Integer>() {
            @Override
            public int compare(Integer a, Integer b) {
                return (points.get(b).x - points.get(a).x);
            }
        });

        Triangle st = Delaunay.superTriangle(points);

        points.add(st.one);
        points.add(st.two);
        points.add(st.three);

        ArrayList<Circumcircle> open = new ArrayList<>();
        open.add(circumcircle(points, n, n + 1, n + 2));

        ArrayList<Circumcircle> closed = new ArrayList<>();

        ArrayList<Integer> edges = new ArrayList<>();

        for (int i = indices.size() - 1; i >= 0; i--) {

            int c = indices.get(i);

            for (int j = open.size() - 1; j >= 0; j--) {

                Circumcircle cj = open.get(j);
                Point vj = points.get(c);

                double dx = vj.x - cj.x;

                if (dx > 0 && dx * dx > cj.r) {
                    closed.add(cj);
                    open.remove(j);
                    continue;
                }

                double dy = vj.y - cj.y;

                if (dx * dx + dy * dy - cj.r > EPSILON) {
                    continue;
                }

                edges.add(cj.i);
                edges.add(cj.j);
                edges.add(cj.j);
                edges.add(cj.k);
                edges.add(cj.k);
                edges.add(cj.i);

                open.remove(j);
            }

            dedup(edges);

            for (int j = edges.size(); j > 0; ) {
                int b = edges.get(--j);
                int a = edges.get(--j);
                open.add(circumcircle(points, a, b, c));
            }

            edges.clear();

        }

        for (int i = open.size() - 1; i >= 0; i--) {
            closed.add(open.get(i));
        }

        open.clear();

        ArrayList<Integer> out = new ArrayList<>();

        for (int i = closed.size() - 1; i >= 0; i--) {
            Circumcircle ci = closed.get(i);
            if (ci.i < n && ci.j < n && ci.k < n) {
                out.add((int) ci.i);
                out.add((int) ci.j);
                out.add((int) ci.k);
            }
        }
        return out;
    }

    private static void dedup(ArrayList<Integer> edges) {
        int a, b, m, n;
        for (int j = edges.size(); j > 0; ) {
            while (j > edges.size()) {
                j--;
            }
            if (j <= 0) {
                break;
            }
            b = edges.get(--j);
            a = edges.get(--j);

            for (int i = j; i > 0; ) {
                n = edges.get(--i);
                m = edges.get(--i);

                if ((a == m && b == n) || (a == n && b == m)) {
                    if (j + 1 < edges.size())
                        edges.remove(j + 1);
                    edges.remove(j);
                    if (i + 1 < edges.size())
                        edges.remove(i + 1);
                    edges.remove(i);
                    break;
                }
            }
        }
    }

    private static Triangle superTriangle(List<Point> points) {
        int xmin = Integer.MAX_VALUE;
        int ymin = Integer.MAX_VALUE;
        int xmax = Integer.MIN_VALUE;
        int ymax = Integer.MIN_VALUE;

        for (Point p : points) {
            if (p.x < xmin) xmin = p.x;
            if (p.y < ymin) ymin = p.y;
            if (p.x > xmax) xmax = p.x;
            if (p.y > ymax) ymax = p.y;
        }

        int dx = xmax - xmin;
        int dy = ymax - ymin;

        int dmax = Math.max(dx, dy);
        double xmid = xmin + dx * 0.5;
        double ymid = ymin + dy * 0.5;

        Triangle t = new Triangle(new Point((int) (xmid - 20 * dmax), (int) (ymid - dmax))
                , new Point((int) xmid, (int) (ymid + 20 * dmax))
                , new Point((int) (xmid + 20 * dmax), (int) (ymid - dmax)));
        return t;
    }

    private static Circumcircle circumcircle(List<Point> points, int i, int j, int k) {
        int x1 = points.get(i).x;
        int y1 = points.get(i).y;
        int x2 = points.get(j).x;
        int y2 = points.get(j).y;
        int x3 = points.get(k).x;
        int y3 = points.get(k).y;

        int fabsy1y2 = Math.abs(y1 - y2);
        int fabsy2y3 = Math.abs(y2 - y3);

        float xc, yc, m1, m2, mx1, mx2, my1, my2, dx, dy;

        if (fabsy1y2 <= 0) {
            m2 = -((float) (x3 - x2) / (y3 - y2));
            mx2 = (x2 + x3) / 2f;
            my2 = (y2 + y3) / 2f;
            xc = (x2 + x1) / 2f;
            yc = m2 * (xc - mx2) + my2;
        } else if (fabsy2y3 <= 0) {
            m1 = -((float) (x2 - x1) / (y2 - y1));
            mx1 = (x1 + x2) / 2f;
            my1 = (y1 + y2) / 2f;
            xc = (x3 + x2) / 2f;
            yc = m1 * (xc - mx1) + my1;
        } else {
            m1 = -((float) (x2 - x1) / (y2 - y1));
            m2 = -((float) (x3 - x2) / (y3 - y2));
            mx1 = (x1 + x2) / 2f;
            mx2 = (x2 + x3) / 2f;
            my1 = (y1 + y2) / 2f;
            my2 = (y2 + y3) / 2f;
            xc = (m1 * mx1 - m2 * mx2 + my2 - my1) / (m1 - m2);
            yc = (fabsy1y2 > fabsy2y3) ?
                    m1 * (xc - mx1) + my1 :
                    m2 * (xc - mx2) + my2;
        }

        dx = x2 - xc;
        dy = y2 - yc;

        return new Circumcircle(i, j, k, xc, yc, dx * dx + dy * dy);
    }
}

