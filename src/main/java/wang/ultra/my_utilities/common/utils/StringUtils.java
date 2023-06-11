package wang.ultra.my_utilities.common.utils;

import java.util.UUID;

public class StringUtils {

    /**
     * 判断是否是汉字, 是汉字就传回去, 不是就是null
     * @param string
     * @return
     */
    public static String isChinese(String string) {
        int n = 0;
        for (int i = 0; i < string.length(); i++) {
            n = (int) string.charAt(i);
            if (!(19968 <= n && n < 40869)) {
                return null;
            }
        }
        return string;
    }

    public static String getMyUUID() {
        return String.valueOf(UUID.randomUUID()).replaceAll("-", "");
    }
}
