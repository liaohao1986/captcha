package com.captcha;

/**
 * �ĳɶ�̬���� <br>
 * ��Ȩ: Copyright (c) 2011-2016<br>
 * ��˾: �����������<br>
 * 
 * @author: ͯ��<br>
 * @date: 2016��11��25��<br>
 */
public class Const {
    public static String type;
    public static String baseDir;

    static {
        String className = new Object() {
            public String getClassName() {
                String clazzName = this.getClass().getName();
                return clazzName.substring(0, clazzName.lastIndexOf('$'));
            }
        }.getClassName();
        String tmpType = className.replaceAll("com\\.captcha\\.|\\.Const", "");
        type = tmpType;
        baseDir = "target/" + type.replaceAll("\\.", "/");
    }

}
