package com.captcha.pkland;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;

import com.captcha.recognizer.Recognizer;
import com.captcha.refactor.MonoColorImageProcessor;
import com.captcha.util.ImageUtil;

/**
 *
 * @author Administrator
 * @version $Id: ImageProcessTestIgnore.java, v 0.1 2017年3月30日 下午9:34:17
 *          Administrator Exp $
 */
public class ImageProcessTestIgnore {
    private static Logger logger = LoggerFactory.getLogger(ImageProcessTestIgnore.class);

    Map<String, BufferedImage> trainImageMap = new HashMap<String, BufferedImage>();

    @Before
    public void setup() throws Exception {
        File dir = new ClassPathResource("train").getFile();
        File[] files = dir.listFiles();
        for (File file : files) {
            trainImageMap.put(file.getName().charAt(0) + "", ImageIO.read(file));
        }
    }

    @Test
    public void singleTest() throws Exception {
        File file = FileUtils.getFile(Const.baseDir + "/batch1/0.png");
        test(file);
    }

    @Test
    public void batchTest() throws Exception {
        File dir = FileUtils.getFile(Const.baseDir + "/batch1");
        Collection<File> fileList = FileUtils.listFiles(dir, new String[] { "png" }, false);
        for (File file : fileList) {
            test(file);
        }
    }

    public void test(File file) throws Exception {
        BufferedImage bufferedImage = ImageIO.read(file);
        MonoColorImageProcessor monoColorImageProcessor = new MonoColorImageProcessor(100);
        bufferedImage = monoColorImageProcessor.process(bufferedImage).get(0);

        List<BufferedImage> splitImageList = new ArrayList<BufferedImage>();
        splitImageList.add(bufferedImage.getSubimage(10, 6, 8, 10));
        splitImageList.add(bufferedImage.getSubimage(19, 6, 8, 10));
        splitImageList.add(bufferedImage.getSubimage(28, 6, 8, 10));
        splitImageList.add(bufferedImage.getSubimage(37, 6, 8, 10));
        for (int i = 0; i < splitImageList.size(); i++) {
            BufferedImage tempBufferedImage = splitImageList.get(i);
            if (bufferedImage == null) {
                continue;
            }
            ImageUtil.write(tempBufferedImage, Const.baseDir + "/batch1_split/"
                                               + file.getName().split("\\.")[0] + "_" + i + ".png");
        }
        Recognizer recognizer = new Recognizer();
        String captcha = recognizer.recognize(splitImageList, trainImageMap);
        if (captcha == null || captcha.length() != 4) {
            ImageUtil.write(bufferedImage, new File(Const.baseDir + "/fail/"
                                                    + file.getName().split("\\.")[0] + "_"
                                                    + captcha + ".png"));
        }
        logger.debug("captcha:{}", captcha);

        ImageUtil.write(bufferedImage, new File(Const.baseDir + "/batch1-captcha/"
                                                + file.getName().split("\\.")[0] + "_" + captcha
                                                + ".png"));
    }

}
