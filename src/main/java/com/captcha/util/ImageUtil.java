package com.captcha.util;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 图像工具类 <br>
 * 版权: Copyright (c) 2011-2015<br>
 * 公司: 北京活力天汇<br>
 * 
 * @author: 童凡<br>
 * @date: 2015年11月27日<br>
 */
public class ImageUtil {
	private static Logger logger = LoggerFactory.getLogger(ImageUtil.class);

	/**
	 * 克隆方法
	 * 
	 * @param bufferedImage
	 *            源图像
	 * @return 新图像
	 */
	public static BufferedImage clone(BufferedImage bufferedImage) {
		BufferedImage bufferedImageNew = new BufferedImage(bufferedImage.getWidth(), bufferedImage.getHeight(), bufferedImage.getType());
		bufferedImageNew.setData(bufferedImage.getData());
		return bufferedImageNew;
	}

	/**
	 * 读取图片
	 * 
	 * @param file
	 * @return
	 */
	public static BufferedImage read(File file) {
		try {
			BufferedImage bufferedImage = ImageIO.read(file);
			return bufferedImage;
		} catch (IOException e) {
			logger.error("ERROR", e);
		}
		return null;
	}

	/**
	 * 读取图片
	 * 
	 * @param file
	 * @return
	 */
	public static BufferedImage read(byte[] data) {
		try {
			BufferedImage bufferedImage = ImageIO.read(new ByteArrayInputStream(data));
			return bufferedImage;
		} catch (IOException e) {
			logger.error("ERROR", e);
		}
		return null;
	}

	/**
	 * 读取图片
	 * 
	 * @param file
	 * @return
	 */
	public static BufferedImage read(String filename) {
		return read(new File(filename));
	}

	/**
	 * 图片写入文件
	 * 
	 * @param bufferedImage
	 * @param format
	 * @param file
	 */
	public static void write(BufferedImage bufferedImage, String format, File file) {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		try {
			ImageIO.write(bufferedImage, format, outputStream);
			byte[] data = outputStream.toByteArray();
			FileUtils.writeByteArrayToFile(file, data);
		} catch (IOException e) {
			logger.error("ERROR", e);
		}

	}

	/**
	 * 图片写入文件
	 * 
	 * @param bufferedImage
	 * @param format
	 * @param file
	 */
	public static void write(BufferedImage bufferedImage, String format, String filename) {
		write(bufferedImage, format, new File(filename));
	}

	/**
	 * 以png格式写入图片文件
	 * 
	 * @param bufferedImage
	 * @param format
	 * @param file
	 */
	public static void write(BufferedImage bufferedImage, File file) {
		write(bufferedImage, "PNG", file);
	}

	/**
	 * 以png格式写入图片文件
	 * 
	 * @param bufferedImage
	 * @param format
	 * @param file
	 */
	public static void write(BufferedImage bufferedImage, String filename) {
		write(bufferedImage, new File(filename));
	}

	/**
	 * 水平投影,水平方向的投影就是每行的非白色像素值的个数
	 * 
	 * @param bufferedImage
	 * @param isNegative
	 *            是否是负片
	 * @return
	 */
	public static List<Integer> getHorizontalProjection(BufferedImage bufferedImage, boolean isNegative) {
		List<Integer> list = new ArrayList<Integer>();
		Integer width = bufferedImage.getWidth();
		Integer height = bufferedImage.getHeight();
		for (int y = 0; y < height; y++) {
			Integer count = 0;
			for (int x = 0; x < width; x++) {
				// 进行修正,透明色算作白色
				int rgb = bufferedImage.getRGB(x, y);
				Color color = new Color(rgb, true);
				if (color.getAlpha() == 0) {
					color = Color.WHITE;
				}

				if (isNegative) {
					if (color.getRGB() != Color.BLACK.getRGB()) {
						count++;
					}
				} else {
					if (color.getRGB() != Color.WHITE.getRGB()) {
						count++;
					}
				}
			}
			list.add(count);
		}
		return list;
	}

	public static List<Integer> getHorizontalProjection(BufferedImage bufferedImage) {
		return getHorizontalProjection(bufferedImage, false);
	}

	/**
	 * 垂直投影,垂直方向的投影就是每列的非白色像素值的个数
	 * 
	 * @param bufferedImage
	 * @param isNegative
	 *            是否是负片
	 * @return
	 */
	public static List<Integer> getVerticaProjection(BufferedImage bufferedImage, boolean isNegative) {
		List<Integer> list = new ArrayList<Integer>();
		Integer width = bufferedImage.getWidth();
		Integer height = bufferedImage.getHeight();
		for (int x = 0; x < width; x++) {
			Integer count = 0;
			for (int y = 0; y < height; y++) {
				// 进行修正,透明色算作白色
				int rgb = bufferedImage.getRGB(x, y);
				Color color = new Color(rgb, true);
				if (color.getAlpha() == 0) {
					color = Color.WHITE;
				}

				if (isNegative) {
					if (color.getRGB() != Color.BLACK.getRGB()) {
						count++;
					}
				} else {
					if (color.getRGB() != Color.WHITE.getRGB()) {
						count++;
					}
				}

			}
			list.add(count);
		}
		return list;
	}

	public static List<Integer> getVerticaProjection(BufferedImage bufferedImage) {
		return getVerticaProjection(bufferedImage, false);
	}

	/**
	 * 切分图片<br>
	 * 
	 * @param srcBufferedImage
	 * @return
	 */
	public static List<BufferedImage> splitImage(BufferedImage srcBufferedImage) {
		List<Integer> projectionList = getVerticaProjection(srcBufferedImage);
		Map<Integer, Integer> splitMap = new TreeMap<Integer, Integer>();
		int start = -1;
		int end = -1;
		for (int i = 0; i < projectionList.size(); i++) {
			Integer value = projectionList.get(i);
			if (value == 0 && start < 0) {
				continue;
			}
			if (start < 0) {
				start = i;
			}
			if (start >= 0 && value == 0) {
				end = i;
				splitMap.put(start, end);
				start = -1;
			}
		}

		logger.info("split image map:{}", splitMap);

		List<BufferedImage> imageList = new ArrayList<BufferedImage>();

		// TODO: 找出粘连,进行再次的坐标拆分
		for (Entry<Integer, Integer> entry : splitMap.entrySet()) {
			Integer tmpStart = entry.getKey();
			Integer tmpEnd = entry.getValue();
			Integer tmpWidth = tmpEnd - tmpStart;
			Integer tmpHeight = srcBufferedImage.getHeight();
			BufferedImage image = new BufferedImage(tmpWidth, tmpHeight, BufferedImage.TYPE_INT_ARGB);
			for (int x = 0; x < tmpEnd - tmpStart; x++) {
				for (int y = 0; y < tmpHeight; y++) {
					image.setRGB(x, y, srcBufferedImage.getRGB(x + tmpStart, y));
				}
			}
			imageList.add(image);
		}

		return imageList;

	}

	/**
	 * 旋转图片为指定角度
	 * 
	 * @param srcBufferedImage
	 *            目标图像
	 * @param degree
	 *            旋转角度
	 * @return
	 */
	public static BufferedImage rotateImage(final BufferedImage srcBufferedImage, final double degree) {
		int width = srcBufferedImage.getWidth();
		int height = srcBufferedImage.getHeight();
		int type = srcBufferedImage.getColorModel().getTransparency();
		BufferedImage image = new BufferedImage(width, height, type);
		Graphics2D graphics2d = image.createGraphics();
		graphics2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
		graphics2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_DEFAULT);
		graphics2d.rotate(Math.toRadians(degree), width / 2, height / 2);
		graphics2d.drawImage(srcBufferedImage, 0, 0, null);
		graphics2d.dispose();
		return image;
	}

	/**
	 * 倾斜校正<br>
	 * 思路主要是尝试将图像进行旋转,得到水平投影和垂直投影的长度<br>
	 * 判断长度之和在减少则认为图像是在向正常方向旋转,达到一个极限的角度值,投影长度不会再减少了,则旋转到该角度<br>
	 * 
	 * @param srcBufferedImage
	 * @return
	 */
	public static BufferedImage tiltCorrection(BufferedImage srcBufferedImage) {
		BufferedImage bufferedImage = ImageUtil.clone(srcBufferedImage);

		int minProjection = 100;
		List<Integer> minProjectionList = new ArrayList<Integer>();

		for (int i = -30; i < 31; i++) {
			BufferedImage tmpBufferedImage = rotateImage(bufferedImage, i);
			Integer horizontalProjectionLength = getProjectionLength(getHorizontalProjection(tmpBufferedImage, true));
			Integer verticaProjectionLength = getProjectionLength(getVerticaProjection(tmpBufferedImage, true));
			// Integer projectionLength = (int) (Math.pow(horizontalProjectionLength, 2) + Math.pow(verticaProjectionLength, 2));
			Integer projectionLength = verticaProjectionLength;
			logger.debug("i:{},minProjection:{},projectionLength:{},minProjectionList:{}", i, minProjection, projectionLength, minProjectionList);
			if (projectionLength < minProjection) {
				minProjectionList.clear();
				minProjection = projectionLength;
				minProjectionList.add(i);
			} else if (projectionLength == minProjection) {
				minProjectionList.add(i);
			}
			write(tmpBufferedImage, "target/mu_temp/temp/" + i + ".jpg");

		}
		int curDegree = 0;
		if (minProjectionList.size() > 0) {
			curDegree = minProjectionList.get(minProjectionList.size() / 2);
		}
		bufferedImage = rotateImage(bufferedImage, curDegree);

		return bufferedImage;
	}

	/**
	 * 获得投影的长度
	 * 
	 * @param list
	 * @return
	 */
	public static Integer getProjectionLength(List<Integer> list) {
		List<Integer> tmpList = new ArrayList<Integer>();
		tmpList.addAll(list);
		if (tmpList.size() == 0) {
			return 0;
		}
		while (tmpList.get(0) == 0) {
			tmpList.remove(0);
			if (tmpList.size() == 0) {
				return 0;
			}
		}
		while (tmpList.get(tmpList.size() - 1) == 0) {
			tmpList.remove(tmpList.size() - 1);
		}
		return tmpList.size();
	}

	/**
	 * 获得投影开始的下标
	 * 
	 * @param list
	 * @return
	 */
	public static Integer getProjectionStart(List<Integer> list) {
		List<Integer> tmpList = new ArrayList<Integer>();
		tmpList.addAll(list);
		for (int i = 0; i < tmpList.size(); i++) {
			if (tmpList.get(i) != 0) {
				return i;
			}
		}
		return 0;
	}

	/**
	 * 获取图像的负片,只针对于二值化之后的图片有效
	 * 
	 * @param srcBufferedImage
	 */
	public static BufferedImage getNegativeImage(BufferedImage srcBufferedImage) {
		BufferedImage bufferedImage = ImageUtil.clone(srcBufferedImage);
		int width = srcBufferedImage.getWidth();
		int height = srcBufferedImage.getHeight();
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				int rgb = srcBufferedImage.getRGB(x, y);
				if (rgb == Color.BLACK.getRGB()) {
					bufferedImage.setRGB(x, y, Color.WHITE.getRGB());
				}
				if (rgb == Color.WHITE.getRGB()) {
					bufferedImage.setRGB(x, y, Color.BLACK.getRGB());
				}

			}
		}
		return bufferedImage;
	}

	/**
	 * 扩展画布
	 * 
	 * @param srcBufferedImage
	 * @return
	 */
	public static BufferedImage expandCanvas(BufferedImage srcBufferedImage, Integer targetWidth, Integer targetHeight) {
		BufferedImage bufferedImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB);

		Graphics graphics = bufferedImage.getGraphics();
		graphics.setColor(Color.BLACK);
		graphics.fillRect(0, 0, targetWidth, targetHeight);
		graphics.dispose();

		Integer width = srcBufferedImage.getWidth();
		Integer height = srcBufferedImage.getHeight();
		Integer offsetX = (targetWidth - width) / 2;
		Integer offsetY = (targetHeight - height) / 2;
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				logger.debug("{},{}", x + offsetX, y + offsetY);
				bufferedImage.setRGB(x + offsetX, y + offsetY, srcBufferedImage.getRGB(x, y));
			}
		}
		return bufferedImage;
	}

	/**
	 * 收缩画布
	 * 
	 * @param srcBufferedImage
	 * @return
	 */
	public static BufferedImage trimCanvas(BufferedImage srcBufferedImage) {
		List<Integer> verticaProjection = ImageUtil.getVerticaProjection(srcBufferedImage);
		List<Integer> horizontalProjection = ImageUtil.getHorizontalProjection(srcBufferedImage);

		Integer verticaProjectionLength = ImageUtil.getProjectionLength(verticaProjection);
		Integer horizontalProjectionLength = ImageUtil.getProjectionLength(horizontalProjection);
		Integer verticaProjectionStart = ImageUtil.getProjectionStart(verticaProjection);
		Integer horizontalProjectionStart = ImageUtil.getProjectionStart(horizontalProjection);

		Integer width = verticaProjectionLength;
		Integer height = horizontalProjectionLength;
		Integer offsetX = verticaProjectionStart;
		Integer offsetY = horizontalProjectionStart;

		if (width == 0 || height == 0) {
			return null;
		}

		BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				bufferedImage.setRGB(x, y, srcBufferedImage.getRGB(x + offsetX, y + offsetY));
			}
		}
		return bufferedImage;
	}

	/**
	 * 没有宽高限制
	 * 
	 * @return
	 */
	public static List<Point> getNeighborList8NotLmit(int x, int y) {
		List<Point> neighborList = new ArrayList<Point>();
		for (int tempY = y - 1; tempY < y + 2; tempY++) {
			// 出界
			if (tempY < 0) {
				continue;
			}
			for (int tempX = x - 1; tempX < x + 2; tempX++) {
				// 出界
				if (tempX < 0) {
					continue;
				}
				if (tempX == x && tempY == y) {
					continue;
				}
				neighborList.add(new Point(tempX, tempY));
			}
		}
		return neighborList;
	}

	/**
	 * 找出附近的点,步长2
	 * 
	 * @return
	 */
	public static List<Point> getNeighborList8Step2NotLmit(int x, int y) {
		List<Point> neighborList = new ArrayList<Point>();
		for (int tempY = y - 2; tempY < y + 3; tempY++) {
			// 出界
			if (tempY < 0) {
				continue;
			}
			for (int tempX = x - 2; tempX < x + 3; tempX++) {
				// 出界
				if (tempX < 0) {
					continue;
				}
				if (tempX == x && tempY == y) {
					continue;
				}
				neighborList.add(new Point(tempX, tempY));
			}
		}
		return neighborList;
	}

	/**
	 * 得到所有的相邻点,8方向
	 * 
	 * @return
	 */
	public static List<Point> getNeighborList(int x, int y, int width, int height) {
		List<Point> neighborList = new ArrayList<Point>();
		for (int tempY = y - 1; tempY < y + 2; tempY++) {
			// 出界
			if (tempY < 0 || tempY > height - 1) {
				continue;
			}
			for (int tempX = x - 1; tempX < x + 2; tempX++) {
				// 出界
				if (tempX < 0 || tempX > width - 1) {
					continue;
				}
				if (tempX == x && tempY == y) {
					continue;
				}
				neighborList.add(new Point(tempX, tempY));
			}
		}
		return neighborList;
	}

	/**
	 * 得到所有的相邻点,4方向
	 * 
	 * @return
	 */
	public static List<Point> getNeighborList4(int x, int y, int width, int height) {
		List<Point> neighborList = new ArrayList<Point>();
		for (int tempY = y - 1; tempY < y + 2; tempY++) {
			// 出界
			if (tempY < 0 || tempY > height - 1) {
				continue;
			}
			for (int tempX = x - 1; tempX < x + 2; tempX++) {
				// 出界
				if (tempX < 0 || tempX > width - 1) {
					continue;
				}

				if (tempX != x && tempY != y) {
					continue;
				}

				if (tempX == x && tempY == y) {
					continue;
				}
				neighborList.add(new Point(tempX, tempY));
			}
		}
		return neighborList;
	}

}
