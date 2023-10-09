package wang.ultra.my_utilities.common.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListConverter {

    /**
     * Map里面的Object转String
     * @param resultList    List<Map<String, Object>>
     * @return returnList   List<Map<String, String>>
     */
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

    /**
     * Map里面的Object转String
     * @param resultMap     Map<String, Object>
     * @return returnMap    Map<String, String>
     */
    public static Map<String, String> mapValueToString(Map<String, Object> resultMap) {
        Map<String, String> returnMap = new HashMap<>();
        for (String key : resultMap.keySet()) {
            returnMap.put(key, String.valueOf(resultMap.get(key)));
        }
        return returnMap;
    }
}
