package wang.ultra.my_utilities.stock_exchange.apis;

import wang.ultra.my_utilities.common.utils.JsonConverter;
import wang.ultra.my_utilities.stock_exchange.utils.UrlConnectionUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

/**
 * 这个接口没有名字, 只有个网页上面写着 "善猎者,必善狩，交易纪律"
 * 接口每分钟只限十次访问
 * 如果被限流就会写 "调用达到上限， 每一分钟只能请求10次，接口已经授权用户342个，请邮件联系：zhanghan_198@163.com 着急请加微信：272885400"
 */
public class SlzBssJyjlApi {

    private final String stockId;

    public SlzBssJyjlApi(String stockId) {
        this.stockId = stockId;
    }

    public Map<String, Object> getMacdInTenDays() {
        String url = "http://47.92.152.143/api/tech/indicators/macd?code=" + stockId + "&interval=10";

        String responseMACD;
        try {
            responseMACD = UrlConnectionUtils.getConnection(url, "GET", "UTF-8");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Map<String, Object> responseMap = new LinkedHashMap<>();
        responseMap.put("stockId", stockId);
        List<Object> responseList;
        try {
            responseList = JsonConverter.JsonStringToListObject(responseMACD);
            responseMap.put("status", "1");
            responseMap.put("msg", "success");
            responseMap.put("data", responseList);
        } catch (Exception e) {
            responseMap.put("status", "-1");
            responseMap.put("msg", "数据错误, 接口返回数据转译失败");
        }

        return responseMap;
    }
}
