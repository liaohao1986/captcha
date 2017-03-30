package com.captcha;

/**
 * 改成动态生成 <br>
 * 版权: Copyright (c) 2011-2016<br>
 * 公司: 北京活力天汇<br>
 * 
 * @author: 童凡<br>
 * @date: 2016年11月25日<br>
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
