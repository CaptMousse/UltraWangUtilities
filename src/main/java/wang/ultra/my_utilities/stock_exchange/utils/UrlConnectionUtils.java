package wang.ultra.my_utilities.stock_exchange.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class UrlConnectionUtils {
    /**
     * @param urlString     URL
     * @param requestMethod 请求方法(GET, POST)
     * @param charsets      字符集(UTF-8, GB2312)
     * @return
     */
    public static String getConnection(String urlString, String requestMethod, String charsets) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
        httpURLConnection.setRequestMethod(requestMethod);
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
        return responseStr.toString();
    }
}
