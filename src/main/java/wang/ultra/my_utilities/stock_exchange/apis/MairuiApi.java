package wang.ultra.my_utilities.stock_exchange.apis;

import wang.ultra.my_utilities.common.utils.JsonConverter;
import wang.ultra.my_utilities.stock_exchange.utils.UrlConnectionUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 麦蕊的股票API
 * 需要申请免费的License
 * 目前免费的License是63245b556da38dd039
 */
public class MairuiApi {

    private String stockId;
    private String licence = "63245b556da38dd039";

    public MairuiApi(String stockId) {
        this.stockId = stockId;
    }

    public MairuiApi(String stockId, String licence) {
        this.stockId = stockId;
        this.licence = licence;
    }

    public Map<String, Object> getHistoryMA(String level) {

//        level = "dn";
        // 分时级别
        // 5m, 15m, 30m, 60m,
        // dn (日线未复权) , dq (日线前复权) , dh (日线后复权) ,
        // wn (周线未复权) , wq (周线前复权) ,wh (周线后复权) ,
        // mn (月线未复权) , mq (月线前复权) , mh(月线后复权) ,
        // yn (年线未复权) , yq (年线前复权) , yh (年线后复权)

        String url = "https://api.mairui.club/hszbl/ma/" + stockId + "/" + level + "/" + licence;

        String responseHistoryMA = null;
        try {
            responseHistoryMA = UrlConnectionUtils.getConnection(url, "GET", "UTF-8");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Map<String, Object> responseMap = new LinkedHashMap<>();
        responseMap.put("stockId", stockId);
        List<Object> responseList = new ArrayList<>();
        try {
            responseList = JsonConverter.JsonStringToListObject(responseHistoryMA);
            responseMap.put("status", "1");
            responseMap.put("msg", "success");
            responseMap.put("data", responseList);
        } catch (Exception e) {
            responseMap.put("status", "1");
            responseMap.put("msg", "failed");
        }

        return responseMap;
    }

    public Map<String, Object> getMA(String level) {

//        level = "dn";


        // 分时级别
        // 5m, 15m, 30m, 60m,
        // dn (日线未复权) , dq (日线前复权) , dh (日线后复权) ,
        // wn (周线未复权) , wq (周线前复权) ,wh (周线后复权) ,
        // mn (月线未复权) , mq (月线前复权) , mh(月线后复权) ,
        // yn (年线未复权) , yq (年线前复权) , yh (年线后复权)

        String url = "https://api.mairui.club/hszb/ma/" + stockId + "/" + level + "/" + licence;

        String responseMA = null;
        try {
            responseMA = UrlConnectionUtils.getConnection(url, "GET", "UTF-8");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Map<String, Object> responseMap = new LinkedHashMap<>();
        responseMap.put("stockId", stockId);
        try {
            responseMap.put("status", "1");
            responseMap.put("msg", "success");
            responseMap.put("data", JsonConverter.JsonStringToMap(responseMA));
        } catch (Exception e) {
            responseMap.put("status", "1");
            responseMap.put("msg", "failed");
        }

        return responseMap;
    }

    public static void main(String[] args) {
        MairuiApi mairui = new MairuiApi("600919");
        Map<String, Object> responseMap = mairui.getMA("dn");
        System.out.println(responseMap);
    }
}
