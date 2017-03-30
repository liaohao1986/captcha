/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.captcha.wakabayes;

import java.io.File;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.core.io.ClassPathResource;

/**
 *
 * @author sorrge
 */
public class Test {
    static final String alphabet = "abcdefghijklmnopqrstuvwxyz";

    public static void main(String[] args) throws IOException {
        WakabaSimpleRecognition recognizer;
        try {
            recognizer = new WakabaSimpleRecognition();
        } catch (IOException ex) {
            System.err.println("Error reading network: " + ex);
            return;
        }

        Pattern answerPattern = Pattern.compile("^([a-z]+).*\\.gif");

        File captchaFolder = new ClassPathResource("examples/hard").getFile();
        int tested = 0, correct = 0;
        long start = System.nanoTime();
        for (final File fileEntry : captchaFolder.listFiles()) {
            if (!fileEntry.getName().endsWith(".gif"))
                continue;

            String fileName = fileEntry.getName();
            Matcher m = answerPattern.matcher(fileName);
            m.find();
            String answer = m.group(1);

            String detected;
            try {
                detected = recognizer.Recognize(captchaFolder.getPath() + "/" + fileName);
            } catch (IOException ex) {
                System.err.println("Error reading image: " + ex);
                continue;
            } catch (Exception ex) {
                System.err.println("Error reading image: " + ex);
                continue;
            }

            if (answer.equals(detected))
                ++correct;
            else
                System.out.println("Wrong [" + detected + "]" + ": " + fileName);
            System.out.println("detected: " + detected);
            ++tested;
        }
        long elapsed = System.nanoTime() - start;

        System.out.println("Tested: " + tested + ", correct: " + correct + " (" + (float) correct / tested * 100 + "%)");
        float elapsedSeconds = elapsed / 1e9f;
        System.out.println("Time: " + elapsedSeconds + "s., per captcha: " + elapsedSeconds / tested + "s.");
    }

}
