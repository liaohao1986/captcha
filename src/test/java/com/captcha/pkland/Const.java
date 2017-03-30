package com.captcha.pkland;

/**
 *
 * @author Administrator
 * @version $Id: Const.java, v 0.1 2017年3月30日 下午9:38:16 Administrator Exp $
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
