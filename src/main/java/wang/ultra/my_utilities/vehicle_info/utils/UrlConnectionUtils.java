package wang.ultra.my_utilities.vehicle_info.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class UrlConnectionUtils {

    /**
     * 带着header的Http请求
     * @param urlString     URL
     * @param requestMethod 请求方法(GET, POST)
     * @param headerMap     表头
     * @param charsets      字符集(UTF-8, GB2312)
     * @return
     */
    public static String getHeaderConnection(String urlString, String requestMethod, Map<String, String> headerMap, String charsets) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
        httpURLConnection.setRequestMethod(requestMethod);

        // header
        if (!headerMap.isEmpty()) {
            for (String key : headerMap.keySet()) {
                httpURLConnection.setRequestProperty(key, headerMap.get(key));
            }
        }

        httpURLConnection.setConnectTimeout(10000);
        httpURLConnection.setReadTimeout(10000);
        httpURLConnection.connect();
        InputStream inputStream = httpURLConnection.getInputStream();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, charsets));
        StringBuilder responseStr = new StringBuilder();
        String strRead;
        while ((strRead = bufferedReader.readLine()) != null) {
            responseStr.append(strRead);
        }
        String returnStr = responseStr.toString().replaceAll("'", "");

//        System.out.println("returnStr = " + returnStr);
        return returnStr;
    }

    /**
     * Http请求
     * @param urlString     URL
     * @param requestMethod 请求方法(GET, POST)
     * @param charsets      字符集(UTF-8, GB2312)
     * @return
     */
    public static String getHeaderConnection(String urlString, String requestMethod, String charsets) throws IOException {
        Map<String, String> headerMap = new HashMap<>();
        return getHeaderConnection(urlString, requestMethod, headerMap, charsets);
    }
}
