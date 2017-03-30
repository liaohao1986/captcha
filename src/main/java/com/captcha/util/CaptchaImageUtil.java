package com.captcha.util;

import java.awt.Color;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

/**
 * 验证码图像工具类 <br>
 * 版权：Copyright (c) 2011-2015<br>
 * 公司：北京活力天汇<br>
 * 作者：童凡<br>
 * 版本：1.0<br>
 * 创建日期：2015年8月17日<br>
 */
public class CaptchaImageUtil {
	/**
	 * 判断像素颜色是不是黑色
	 * 
	 * @param colorInt
	 * @return
	 */
	public static boolean isBlack(int colorInt) {
		Color color = new Color(colorInt);
		return isBlack(color);
	}

	/**
	 * 判断像素颜色是不是黑色
	 * 
	 * @param color
	 * @return
	 */
	public static boolean isBlack(Color color) {
		if (color.getRed() + color.getGreen() + color.getBlue() <= 100) {
			return true;
		}
		return false;
	}

	/**
	 * 判断像素颜色是不是黑色
	 * 
	 * @param colorInt
	 * @return
	 */
	public static boolean isBlackLike(int colorInt) {
		Color color = new Color(colorInt);
		return isBlackLike(color);
	}

	/**
	 * 判断像素颜色是不是黑色
	 * 
	 * @param color
	 * @return
	 */
	public static boolean isBlackLike(Color color) {
		if (color.getRed() + color.getGreen() + color.getBlue() <= 127 + 127 + 127) {
			return true;
		}
		return false;
	}

	/**
	 * 判断像素颜色是不是白色
	 * 
	 * @param colorInt
	 * @return
	 */
	public static boolean isWhite(int colorInt) {
		Color color = new Color(colorInt);
		return isWhite(color);
	}

	/**
	 * 判断像素颜色是不是白色
	 * 
	 * @param color
	 * @return
	 */
	public static boolean isWhite(Color color) {
		if (color.getRed() + color.getGreen() + color.getBlue() > 100) {
			return true;
		}
		return false;
	}
	
	/**
	 * 判断像素颜色是不是白色
	 * 
	 * @param color
	 * @return
	 */
	public static boolean isWhite(Color color, int whiteThreshold) {
		if (color.getRed() + color.getGreen() + color.getBlue() > 600) {
			return true;
		}
		return false;
	}
	
	/**
	 * 判断像素颜色是不是指定白色
	 * 
	 * @param colorInt
	 * @return
	 */
	public static boolean isWhite(int colorInt,int whiteThreshold) {
		Color color = new Color(colorInt);
		return isWhite(color,whiteThreshold);
	}

	/**
	 * 判断像素颜色是不是白色
	 * 
	 * @param colorInt
	 * @return
	 */
	public static boolean isWhiteLike(int colorInt) {
		Color color = new Color(colorInt);
		return isWhiteLike(color);
	}

	/**
	 * 判断像素颜色是不是像白色
	 * 
	 * @param color
	 * @return
	 */
	public static boolean isWhiteLike(Color color) {
		if (color.getRed() + color.getGreen() + color.getBlue() > 127 + 127 + 127) {
			return true;
		}
		return false;
	}

	/**
	 * 移除细线,对于和数字重合的位置,不移除<br>
	 * 初步只将像素点旁边的点<=2是黑色的移除
	 * 
	 * @param bufferedImage
	 * @return
	 */
	public static BufferedImage removeThin(BufferedImage bufferedImage) {
		int width = bufferedImage.getWidth();
		int height = bufferedImage.getHeight();
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				// 如果是白色点,忽略处理
				if (isWhite(bufferedImage.getRGB(x, y))) {
					continue;
				}
				int neighborBlackCount = 0;
				List<Point> neighborList = getNeighborList(x, y, width, height);
				for (Point point : neighborList) {
					if (isBlack(bufferedImage.getRGB(point.x, point.y))) {
						neighborBlackCount++;
					}
				}
				if (neighborBlackCount <= 2) {
					bufferedImage.setRGB(x, y, Color.WHITE.getRGB());
				}
			}
		}

		return bufferedImage;
	}

	/**
	 * 图像二值化
	 * 
	 * @param bufferedImage
	 * @return
	 */
	public static BufferedImage monoColor(BufferedImage bufferedImage) {
		int width = bufferedImage.getWidth();
		int height = bufferedImage.getHeight();
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				if (isWhite(bufferedImage.getRGB(x, y))) {
					bufferedImage.setRGB(x, y, Color.WHITE.getRGB());
				} else {
					bufferedImage.setRGB(x, y, Color.BLACK.getRGB());
				}

			}
		}

		return bufferedImage;
	}
	
	
	/**
	 * 图像二值化
	 * 
	 * @param bufferedImage
	 * @param whiteThreshold
	 * @return
	 */
	public static BufferedImage monoColor(BufferedImage bufferedImage, int whiteThreshold){
		int width = bufferedImage.getWidth();
		int height = bufferedImage.getHeight();
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				if (isWhite(bufferedImage.getRGB(x, y),whiteThreshold)) {
					bufferedImage.setRGB(x, y, Color.WHITE.getRGB());
				} else {
					bufferedImage.setRGB(x, y, Color.BLACK.getRGB());
				}
			}
		}
		return bufferedImage;
	}

	/**
	 * 得到所有的相邻点,
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
	 * 删除边框
	 * 
	 * @param bufferedImage
	 * @return
	 */
	public static BufferedImage clearBorder(BufferedImage bufferedImage) {
		int width = bufferedImage.getWidth();
		int height = bufferedImage.getHeight();
		for (int x = 0; x < width; x++) {
			bufferedImage.setRGB(x, 0, Color.WHITE.getRGB());
			bufferedImage.setRGB(x, height - 1, Color.WHITE.getRGB());
		}

		for (int y = 0; y < height; y++) {
			bufferedImage.setRGB(0, y, Color.WHITE.getRGB());
			bufferedImage.setRGB(width - 1, y, Color.WHITE.getRGB());
		}
		return bufferedImage;
	}
}
