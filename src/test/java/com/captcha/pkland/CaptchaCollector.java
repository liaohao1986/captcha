package com.captcha.pkland;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.huoli.httptask.DirectRequestSender;
import com.huoli.httptask.Request;
import com.huoli.httptask.Response;

/**
 *
 * @author Administrator
 * @version $Id: CaptchaCollector.java, v 0.1 2017年3月30日 下午10:57:11 Administrator Exp $
 */
public class CaptchaCollector {
    private static Logger logger = LoggerFactory.getLogger(CaptchaCollector.class);

    @Test
    public void test1() throws Exception {
        DirectRequestSender requestSender = new DirectRequestSender();

        for (int i = 0; i < 20; i++) {
            logger.debug("{}", i);
            byte[] imageData = null;
            {
                Request request = new Request();
                request.setProperty("url", "http://www.pkland.net/img.php");
                request.setProperty("method", "get");

                request
                    .setHeader(
                        "User-Agent",
                        "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_10_5) AppleWebKit/601.2.7 (KHTML, like Gecko) Version/9.0.1 Safari/601.2.7");
                Response response = requestSender.sendRequest(request);
                imageData = response.getContent();
                String fileName = StringUtils.leftPad(String.valueOf(i), 4, "0") + ".png";
                FileUtils.writeByteArrayToFile(new File(Const.baseDir + "/batch1/" + fileName),
                    imageData);
            }

        }
    }
}
