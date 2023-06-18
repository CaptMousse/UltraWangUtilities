package wang.ultra.my_utilities.common.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListConverter {

    public static List<Map<String, String>> mapValueToString(List<Map<String, Object>> resultList) {
        List<Map<String, String>> returnList = new ArrayList<>();
        for (Map<String, Object> map : resultList) {
            Map<String, String> returnMap = new HashMap<>();
            for (String key : map.keySet()) {
                returnMap.put(key, String.valueOf(map.get(key)));
            }
            returnList.add(returnMap);
        }

        return returnList;
    }
}
