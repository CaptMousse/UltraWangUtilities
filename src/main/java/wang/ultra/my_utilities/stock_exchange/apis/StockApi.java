package wang.ultra.my_utilities.stock_exchange.apis;

import org.apache.commons.lang3.StringUtils;
import wang.ultra.my_utilities.common.utils.JsonConverter;
import wang.ultra.my_utilities.stock_exchange.utils.UrlConnectionUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 使用StockApi查询股票数据
 * 但一个IP一天只能调三次就有点坑
 */
public class StockApi {

    private final String stockId;
    private final String date;

    public StockApi(String stockId, String date) {
        this.stockId = stockId;
        this.date = date.substring(0, 4) + "-" + date.substring(4, 6) + "-" + date.substring(6, 8);
        ;
    }

    /**
     * 获取MACD
     *
     * @return
     */
    public Map<String, String> getMACD() {
        String macdUrl = "https://stockapi.com.cn/v1/quota/macd?code=" + stockId + "&date=" + date;

        String responseMACD = null;
        try {
            responseMACD = UrlConnectionUtils.getConnection(macdUrl, "GET", "UTF-8");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Map<String, Object> responseMap = null;
        try {
            responseMap = JsonConverter.JsonStringToMapObject(responseMACD);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        String responseCode = String.valueOf(responseMap.get("code"));
        String responseMsg = String.valueOf(responseMap.get("msg"));
        Object responseData = responseMap.get("data");
        if ("20000".equals(responseCode) && "success".equals(responseMsg) && responseData instanceof Map) {
            Map<String, String> macdData = new LinkedHashMap<>();
            macdData.put("stockId", ((Map<String, String>) responseData).get("api_code"));
            macdData.put("date", (((Map<String, List<String>>) responseData).get("date")).get(0).replaceAll("-", ""));
            macdData.put("dif", String.valueOf((((Map<String, List<Float>>) responseData).get("dif")).get(0)));
            macdData.put("dea", String.valueOf((((Map<String, List<Float>>) responseData).get("dea")).get(0)));
            macdData.put("macd", String.valueOf((((Map<String, List<Float>>) responseData).get("macd")).get(0)));
            return macdData;
        }

        return null;
    }

    /**
     * 获取均线
     *
     * @param maArr 均线[5,10,20,30]
     * @return
     */
    public Map<String, String> getMA(String[] maArr) {
        String maStr = StringUtils.join(maArr, ",");
        String maUrl = "https://stockapi.com.cn/v1/quota/ma?code=" + stockId + "&date=" + date + "&ma=" + maStr;

        String responseMA = null;
        try {
            responseMA = UrlConnectionUtils.getConnection(maUrl, "GET", "UTF-8");
            System.out.println("responseMA = " + responseMA);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Map<String, Object> responseMap = null;
        try {
            responseMap = JsonConverter.JsonStringToMapObject(responseMA);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        String responseCode = String.valueOf(responseMap.get("code"));
        String responseMsg = String.valueOf(responseMap.get("msg"));
        Object responseData = responseMap.get("data");
        if ("20000".equals(responseCode) && "success".equals(responseMsg) && responseData instanceof Map) {
            Map<String, String> macdData = new LinkedHashMap<>();
            macdData.put("stockId", ((Map<String, String>) responseData).get("api_code"));
            macdData.put("date", (((Map<String, String>) responseData).get("date")).replaceAll("-", ""));

            for (String ma : maArr) {
                ma = "ma" + ma;
                macdData.put(ma, ((Map<String, String>) responseData).get(ma));
            }

            return macdData;
        }

        return null;
    }

    /**
     * 获取是否是工作日
     * @return  true/false
     */
    private boolean getTradingDay() {
        String url = "https://stockapi.com.cn/v1/base/tradeDate?tradeDate=" + date;

        String responseTradingDay;
        try {
            responseTradingDay = UrlConnectionUtils.getConnection(url, "GET", "UTF-8");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Map<String, Object> responseMap = null;
        try {
            responseMap = JsonConverter.JsonStringToMapObject(responseTradingDay);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        System.out.println("responseMap = " + responseMap);

        String responseCode = String.valueOf(responseMap.get("code"));
        String responseMsg = String.valueOf(responseMap.get("msg"));
        Object responseData = responseMap.get("data");
        if ("20000".equals(responseCode) && "success".equals(responseMsg) && responseData instanceof Map) {
            String result = ((Map<String, String>) responseData).get("data");
            System.out.println("result = " + result);
            return "1".equals(result);
        }
        return false;
    }



    public static void main(String[] args) {
        StockApi stockApi = new StockApi("600919", "20230913");
        Map<String, String> getMA = stockApi.getMA(new String[]{"5", "10", "20", "30"});
        System.out.println(getMA);
    }
}
