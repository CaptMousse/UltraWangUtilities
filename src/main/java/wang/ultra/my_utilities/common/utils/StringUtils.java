package wang.ultra.my_utilities.common.utils;

import java.util.UUID;

public class StringUtils {

    /**
     * 判断是否是汉字, 是汉字就传回去, 不是就是null
     *
     * @param string
     * @return
     */
    public static String isChinese(String string) {
        int n;
        for (int i = 0; i < string.length(); i++) {
            n = string.charAt(i);
            if (!(19968 <= n && n < 40869)) {
                return null;
            }
        }
        return string;
    }

    public static String getMyUUID() {
        return String.valueOf(UUID.randomUUID()).replaceAll("-", "");
    }

    /**
     *
     * @param fileName
     * @return  只返回后缀名, 要自己加"."
     */
    public static String getFileType(String fileName) {
        return fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
    }

    /**
     * 是否为空
     * @param cs
     * @return
     */
    public static boolean isEmpty(CharSequence cs) {
        return cs == null || cs.length() == 0;
    }
}
