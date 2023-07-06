package wang.ultra.my_utilities.common.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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
     * 获取MD5
     * 
     * @param str
     * @return
     * @throws NoSuchAlgorithmException
     */
    public static String makeMD5(String str) {
        if (str == null) {
            return null;
        }
        MessageDigest messageDigest = null;
        try {
            messageDigest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        messageDigest.update(str.getBytes());
        byte[] b = messageDigest.digest();

        char[] digit = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
        char[] c = new char[2];
        StringBuilder stringBuilder = new StringBuilder();
        for (byte value : b) {
            c[0] = digit[(value >>> 4 & 0xF)];
            c[1] = digit[(value & 0xF)];
            stringBuilder.append(c);
        }
        return stringBuilder.toString();
    }

    // MD5备用方法
    private static String bytesHEX(byte[] b) {
        StringBuilder stringBuilder = new StringBuilder();
        for (byte value : b) {
            stringBuilder.append(byteHEX(value));
        }
        return stringBuilder.toString();
    }
    private static String byteHEX(byte b) {
        char[] digit = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
        char[] c = new char[2];
        c[0] = digit[(b >>> 4 & 0xF)];
        c[1] = digit[(b & 0xF)];
        return new String(c);
    }

    /**
     *
     * @param fileName
     * @return 只返回后缀名, 要自己加"."
     */
    public static String getFileType(String fileName) {
        return fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
    }

    /**
     * 是否为空
     * 
     * @param obj
     * @return
     */
    public static boolean isEmpty(Object obj) {
        return obj == null || obj.toString().length() == 0;
    }
}
