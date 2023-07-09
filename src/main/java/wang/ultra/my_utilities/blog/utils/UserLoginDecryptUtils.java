package wang.ultra.my_utilities.blog.utils;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class UserLoginDecryptUtils {
    public static Map<String, String> decryptUserInfoToMap(String userInfo) {

        if (userInfo == null || userInfo.equals("")) {
            return null;
        }
        String loginInfoString = decryptEncryptString(userInfo);
        if (loginInfoString == null || loginInfoString.equals("")) {
            return null;
        }

        String[] loginInfoStringArr = loginInfoString.split("UltraWangAdministrator");

        Map<String, String> userInfoMap = new HashMap<>();
        userInfoMap.put("username", loginInfoStringArr[1]);
        userInfoMap.put("password", loginInfoStringArr[2]);

        return userInfoMap;
    }

    private static String decryptEncryptString(String encryptString) {
        if (encryptString == null) {
            System.out.println("encryptString is null");
            return null;
        }

        String hexText;
        try {
            // 不要MD5混淆
            hexText = encryptString.substring(32);
//            System.out.println("encryptStr = " + hexText);
            // 解密
            hexText = hexToString(hexText);
//            System.out.println("decryptText = " + hexText);
        } catch (Exception e) {
            System.out.println("blablablabla = " + e.getMessage());
            return null;
        }
        return hexText;
    }

    private static String hexToString(String hexText) {
        byte[] b = new byte[hexText.length() / 2];
        for (int i = 0; i < hexText.length(); i += 2) {
            b[(i / 2)] = Integer.valueOf(hexText.substring(i, i + 2), 16).byteValue();
        }
        return new String(b, StandardCharsets.UTF_8);
    }
}
