package com.captcha.refactor;

import java.awt.image.BufferedImage;
import java.util.List;

/**
 * 图像处理器接口 <br>
 * 不要修改来源的图像,采用新的图像<br>
 * 版权：Copyright (c) 2011-2017<br>
 * 公司：北京活力天汇<br>
 * 版本：1.0<br>
 * 作者：廖浩<br>
 * 创建日期：2017年3月30日<br>
 */
public interface ImageProcessor {
    /**
     * 处理图像
     * 
     * @param bufferedImage
     *            来源图像
     * @return 处理之后的图像
     */
    public List<BufferedImage> process(BufferedImage srcBufferedImage);
}
