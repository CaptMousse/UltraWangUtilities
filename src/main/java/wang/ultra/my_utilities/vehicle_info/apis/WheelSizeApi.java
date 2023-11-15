package wang.ultra.my_utilities.vehicle_info.apis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import wang.ultra.my_utilities.common.utils.JsonConverter;
import wang.ultra.my_utilities.vehicle_info.utils.UrlConnectionUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WheelSizeApi {

    private static final Logger LOG = LoggerFactory.getLogger(WheelSizeApi.class);

    public List<Object> getBrand() {
        String url = "https://services.wheel-size.com/widget/173807dd5d3b45d6ae52fc1453d7258e/api/mk";

        String responseString = getConnection(url);
        List<Object> responseList;
        if (responseString != null && responseString.contains("slug") && responseString.contains("name")) {
            try {
                responseList = JsonConverter.JsonStringToListObject(responseString);
                LOG.info("responseList is ready! ");
                return responseList;

            } catch (Exception e) {
                return null;
            }
        }

        return null;

    }

    public List<Object> getYear(String brand) {
        String url = "https://services.wheel-size.com/widget/173807dd5d3b45d6ae52fc1453d7258e/api/yr?make=" + brand;

        String responseString = getConnection(url);
        List<Object> responseList;
        if (responseString != null && responseString.contains("slug") && responseString.contains("name")) {
            try {
                responseList = JsonConverter.JsonStringToListObject(responseString);
                LOG.info("responseList is ready! ");
                return responseList;

            } catch (Exception e) {
                return null;
            }
        }

        return null;
    }


    public List<Object> getModel(String brand, String year) {
        String url = "https://services.wheel-size.com/widget/173807dd5d3b45d6ae52fc1453d7258e/api/ml?make=" + brand + "&year=" + year;

        String responseString = getConnection(url);
        List<Object> responseList;
        if (responseString != null && responseString.contains("slug") && responseString.contains("name")) {
            try {
                responseList = JsonConverter.JsonStringToListObject(responseString);
                LOG.info("responseList is ready! ");
                return responseList;

            } catch (Exception e) {
                return null;
            }
        }

        return null;
    }

    public List<Object> getSpec(String brand, String year, String model) {
        String url = "https://services.wheel-size.com/widget/173807dd5d3b45d6ae52fc1453d7258e/api/sm?make=" + brand + "&year=" + year + "&model=" + model;

        String responseString = getConnection(url);
        List<Object> responseList;
        if (responseString != null && responseString.contains("slug") && responseString.contains("market")) {
            try {
                responseList = JsonConverter.JsonStringToListObject(responseString);
                LOG.info("responseList is ready! ");
                return responseList;

            } catch (Exception e) {
                return null;
            }
        }

        return null;
    }

    private Map<String, String> getHeader() {
        String referer = "https://services.wheel-size.com/widget/173807dd5d3b45d6ae52fc1453d7258e/";
        String userAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/118.0.0.0 Safari/537.36 Edg/118.0.2088.57";
        String xCsrfToken = "gbSDMgaMm8sgKWTR1TTFxAvLWldsudj9";
        Map<String, String> headerMap = new HashMap<>();
        headerMap.put("Referer", referer);
        headerMap.put("User-Agent", userAgent);
        headerMap.put("X-Csrf-Token", xCsrfToken);
        return headerMap;
    }

    /**
     * 这个API来自于: <a href="https://www.wheelmax.com.cn/Tool/Cardemo.html">...</a>
     * 实测需要加上header作为认证才能正常访问
     *
     * @param url
     * @return
     */
    private String getConnection(String url) {
        Map<String, String> headerMap = getHeader();


        String responseString = null;
        try {
            responseString = UrlConnectionUtils.getHeaderConnection(url, "GET", headerMap, "UTF-8");

            if (responseString.contains("<!DOCTYPE html>") || responseString.contains("<html>")) {
                LOG.info("接口返回了网页, 数据错误了! ");
                return null;
            }

            return responseString;

        } catch (IOException e) {
            return null;
        }
    }


}
