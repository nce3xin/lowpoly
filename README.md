## Low Poly风格图片生成器
低多边形（Low Poly）是一种设计风格，是把多色元素，用三角形分割，每个小三角形的颜色，取自原多色元素的相应位置。
## 效果图
![](img/firefox_lowpoly.png)
## 用法
```java
Configure config = Parser.parseParam("config/config.param");
LowPoly.generate(inputStream, outputStream, config);
```
## 参数
在文件config/config.param中定义了3个参数，解释如下：
- pointCount：随机采样点数
- accuracy：精确度
- fill：是否填充颜色，为false时只绘制线条
## 原理介绍
1. 提取图片的边缘信息，常用的边缘检测算法有Sobel算法等。如果只提取边缘信息，在后面绘制三角形时，可能会出现非常尖锐的三角形。为了降低出现这种情况的可能性，需要加入若干全图随机采样点。
2. 使用Delaunay三角剖分算法将选中的点集组成若干三角形。Delaunay三角剖分有最大化最小角，“最接近于规则化的“的三角网和唯一性（任意四点不能共圆）两个特点。
3. 最后是上色。用每个三角形重心处的颜色作为整个三角形的颜色。重心位置的计算非常简单，就是顶点坐标的算术平均数。