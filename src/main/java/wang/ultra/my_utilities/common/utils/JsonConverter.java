package wang.ultra.my_utilities.common.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JsonConverter {
    public static String MapToJsonString(Map<String, Object> map) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(map);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static String ListMapToJsonString(List<Map<String, String>> list) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(list);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static Map<String, String> JsonStringToMap(String jsonString) {
        Map<String, String> tempMap = new HashMap<>();
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            tempMap = objectMapper.readValue(jsonString, Map.class);
        } catch (JsonProcessingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return tempMap;
    }
}
