package wang.ultra.my_utilities.common.utils;

public class StringUtils {

    /**
     * 判断是否是汉字, 建议传一个字符进来
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
}
