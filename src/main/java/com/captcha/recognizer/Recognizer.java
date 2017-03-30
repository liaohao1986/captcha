package com.captcha.recognizer;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.imageio.ImageIO;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;

import com.captcha.util.CaptchaImageUtil;

/**
 * 图片识别器 <br>
 * 分为两种：一是固定位置；二是不固定位置 版权: Copyright (c) 2011-2016<br>
 * 公司: 北京活力天汇<br>
 * 
 * @author: 童凡<br>
 * @date: Mar 29, 2016<br>
 */
public class Recognizer {
    private static Logger logger = LoggerFactory.getLogger(Recognizer.class);

    /** 字符列表,针对所有的情况,第一层是位置,第二层是字符,第三层是图片列表 */
    private List<Map<String, List<List<Point>>>> characterList;
    private Map<String, BufferedImage> trainImageMap = new HashMap<String, BufferedImage>();

    public void init(String path) throws IOException {
        if (trainImageMap.size() > 0) {
            return;
        }
        File dir = new ClassPathResource("puke888").getFile();
        File[] files = dir.listFiles();
        for (File file : files) {
            trainImageMap.put(file.getName().charAt(0) + "", ImageIO.read(file));
        }
    }

    /**
     * 识别图片列表
     * 
     * @param imageList
     * @return
     * @throws IOException
     */
    public String recognize(List<BufferedImage> imageList, Map<String, BufferedImage> trainImageMap)
                                                                                                    throws IOException {
        StringBuilder builder = new StringBuilder();
        // 通过列表判断是位置模式还是切割模式
        for (BufferedImage bufferedImage : imageList) {
            if (bufferedImage == null) {
                continue;
            }
            builder.append(recognizeCharacter(bufferedImage, trainImageMap));
        }
        return builder.toString();
    }

    /**
     * 识别字符
     * 
     * @param bufferedImage
     *            需要识别的图片
     * @param characterMap
     *            字符点阵映射
     * @return
     */
    public String recognizeCharacter(BufferedImage bufferedImage,
                                     Map<String, BufferedImage> trainImageMap) {
        int width = bufferedImage.getWidth();
        int height = bufferedImage.getHeight();
        int min = width * height;
        String result = "";
        for (Entry<String, BufferedImage> entry : trainImageMap.entrySet()) {
            int count = 0;
            Label1: for (int x = 0; x < width; ++x) {
                for (int y = 0; y < height; ++y) {
                    if (CaptchaImageUtil.isWhite(bufferedImage.getRGB(x, y)) != CaptchaImageUtil
                        .isWhite(entry.getValue().getRGB(x, y))) {
                        count++;
                        if (count >= min)
                            break Label1;
                    }
                }
            }
            if (count < min) {
                min = count;
                result = StringUtils.defaultString(entry.getKey());
            }
        }
        return result;
    }

}
