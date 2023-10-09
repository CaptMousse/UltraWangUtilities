package wang.ultra.my_utilities.stock_exchange;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;

public class StockTest {

    public static void main(String[] args) {

        StockTest test = new StockTest();
        Map<String, String> map = test.stockMain("sh600919");
        System.out.println(map);

    }

    public Map<String, String> stockMain(String stockId) {
        String urlString = "https://qt.gtimg.cn/q=" + stockId;
        URL url;
        StringBuilder stringBuilder = new StringBuilder();
        String key = "交易所\n" +
                "股票名字\n" +
                "股票代码\n" +
                "当前价格\n" +
                "昨收\n" +
                "开盘\n" +
                "成交量\n" +
                "外盘\n" +
                "内盘\n" +
                "买1\n" +
                "买1量\n" +
                "买2\n" +
                "买2量\n" +
                "买3\n" +
                "买3量\n" +
                "买4\n" +
                "买4量\n" +
                "买5\n" +
                "买5量\n" +
                "卖1\n" +
                "卖1量\n" +
                "卖2\n" +
                "卖2量\n" +
                "卖3\n" +
                "卖3量\n" +
                "卖4\n" +
                "卖4量\n" +
                "卖5\n" +
                "卖5量\n" +
                "-\n" +
                "请求时间\n" +
                "涨跌\n" +
                "涨跌%\n" +
                "最高\n" +
                "最低\n" +
                "最新价/成交量(手)/成交额\n" +
                "成交量\n" +
                "成交额\n" +
                "换手率\n" +
                "TTM市盈率\n" +
                "-\n" +
                "均价\n" +
                "动态市盈率\n" +
                "静态市盈率\n" +
                "-\n" +
                "-\n" +
                "-\n" +
                "成交额";

        try {
            url = new URL(urlString);

            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.connect();
            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "GB2312"));
            String strRead;
            while ((strRead = bufferedReader.readLine()) != null) {
                stringBuilder.append(strRead);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        if (stringBuilder.isEmpty()) {
            return null;
        }


        String result = stringBuilder.toString();
        System.out.println(result);
        result = result.substring(0, result.lastIndexOf("\""));
        result = result.substring(result.lastIndexOf("\"") + 1);
        String[] resultArr = result.split("~");
        String[] keyArr = key.split("\n");

        Map<String, String> stockMap = new LinkedHashMap<>();
        for (int i = 0; i < keyArr.length; i++) {
            stockMap.put(keyArr[i], resultArr[i]);
        }
        return stockMap;
    }
}
