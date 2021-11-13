package com.kp.kcache.aop.support;

/**
 * description: ClassUtil <br>
 * date: 2021/11/3 10:05 下午 <br>
 * author: kangpei <br>
 * version: 1.0 <br>
 */
public class ClassUtil {

    public static String getShortClassName(String className) {
        if (className == null) {
            return null;
        }
        String[] ss = className.split("\\.");
        StringBuilder sb = new StringBuilder(className.length());
        for (int i = 0; i < ss.length; i++) {
            String s = ss[i];
            if (i != ss.length - 1) {
                sb.append(s.charAt(0)).append('.');
            } else {
                sb.append(s);
            }
        }
        return sb.toString();
    }

}
