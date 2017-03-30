package com.captcha.refactor;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.List;

import com.captcha.util.ImageUtil;

/**
 * 图像二值化处理器,同时可以支持黑色,白色判断 <br>
 *
 * @author Administrator
 * @version $Id: MonoColorImageProcessor.java, v 0.1 2017年3月30日 下午9:30:20 Administrator Exp $
 */
public class MonoColorImageProcessor implements ImageProcessor {

    /** 黑色的限制 */
    private Integer blackLimit = 100;
    /** 白色的限制 */
    private Integer whiteLimit = 600;

    public MonoColorImageProcessor() {
        super();
    }

    public MonoColorImageProcessor(Integer whiteLimit) {
        super();
        this.whiteLimit = whiteLimit;
    }

    /**
     * 处理图像,将不是白色的点设置成黑色
     * 
     * @param bufferedImage
     *            来源图像
     * @return 处理之后的图像
     */
    @Override
    public List<BufferedImage> process(BufferedImage bufferedImage) {
        BufferedImage bufferedImageNew = ImageUtil.clone(bufferedImage);
        int width = bufferedImageNew.getWidth();
        int height = bufferedImageNew.getHeight();
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (isWhite(bufferedImageNew.getRGB(x, y))) {
                    bufferedImageNew.setRGB(x, y, Color.WHITE.getRGB());
                } else {
                    bufferedImageNew.setRGB(x, y, Color.BLACK.getRGB());
                }
            }
        }
        return Arrays.asList(bufferedImageNew);
    }

    /**
     * 判断像素颜色是不是黑色
     * 
     * @param colorInt
     * @return
     */
    public boolean isBlack(int colorInt) {
        Color color = new Color(colorInt);
        return isBlack(color);
    }

    /**
     * 判断像素颜色是不是黑色
     * 
     * @param color
     * @return
     */
    public boolean isBlack(Color color) {
        if (color.getRed() + color.getGreen() + color.getBlue() <= blackLimit) {
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
    public boolean isWhite(int colorInt) {
        Color color = new Color(colorInt);
        return isWhite(color);
    }

    /**
     * 判断像素颜色是不是白色
     * 
     * @param color
     * @return
     */
    public boolean isWhite(Color color) {
        if (color.getRed() + color.getGreen() + color.getBlue() > whiteLimit) {
            return true;
        }
        return false;
    }

    public Integer getBlackLimit() {
        return blackLimit;
    }

    public void setBlackLimit(Integer blackLimit) {
        this.blackLimit = blackLimit;
    }

    public Integer getWhiteLimit() {
        return whiteLimit;
    }

    public void setWhiteLimit(Integer whiteLimit) {
        this.whiteLimit = whiteLimit;
    }

}
