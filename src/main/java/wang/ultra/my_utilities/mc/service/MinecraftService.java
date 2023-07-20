package wang.ultra.my_utilities.mc.service;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import wang.ultra.my_utilities.common.constant.ConstantFromFile;
import wang.ultra.my_utilities.common.utils.FileIOUtils;
import wang.ultra.my_utilities.common.utils.JsonConverter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service("minecraftService")
public class MinecraftService {

    public List<Map<String, String>> stringToJsonMap() {

        // 读取黑名单
        String subPath = ConstantFromFile.getMinecraftDataFolder();
        String fileName = ConstantFromFile.getMinecraftBlackListFileName();

        String resultString = FileIOUtils.readFileToString(subPath, fileName);

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(resultString, List.class);
        } catch (JsonProcessingException e) {
            return null;
        }
    }

    public String getPlayerUUID(String name) {

        String urlString = "https://api.mojang.com/users/profiles/minecraft/" + name;
        URL url;
        StringBuilder stringBuilder = new StringBuilder();
        try {
            url = new URL(urlString);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.connect();

            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(inputStream, StandardCharsets.UTF_8));
            String strRead;
            while ((strRead = bufferedReader.readLine()) != null) {
                stringBuilder.append(strRead);
            }
        } catch (IOException e) {
            return null;
            // throw new RuntimeException(e);

        }
        return JsonConverter.JsonStringToMap(stringBuilder.toString()).get("id");
    }

    public Map<String, String> bannedPlayersSearchByUUID(String uuid) {
        List<Map<String, String>> blackList = stringToJsonMap();
        for (Map<String, String> map : blackList) {
            String bannedPlayerUUID = map.get("uuid").replaceAll("-", "");
            if (uuid.equals(bannedPlayerUUID)) {
                return map;
            }
        }
        return new HashMap<>();
    }

    public Map<String, String> bannedPlayersSearchByName(String name) {
        List<Map<String, String>> blackList = stringToJsonMap();
        for (Map<String, String> map : blackList) {
            String bannedPlayerName = map.get("name");
            if (name.equals(bannedPlayerName)) {
                return map;
            }
        }
        return new HashMap<>();
    }

    public List<Map<String, String>> readWhitelistFile() {

        // String subPath = ConstantFromFile.getMinecraftDataFolder();
        String subPath = "WabbyWabbo";
        String fileName = "WhiteList.json";

        String resultString = FileIOUtils.readFileToString(subPath, fileName);

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(resultString, List.class);
        } catch (JsonProcessingException e) {
            return null;
        }
    }

    public static String mcUuidConverter(String uuid) {
        StringBuilder sb = new StringBuilder(uuid);
        sb.insert(20, "-");
        sb.insert(16, "-");
        sb.insert(12, "-");
        sb.insert(8, "-");
        return sb.toString();
    }

    public static void main(String[] args) {
        MinecraftService minecraftService = new MinecraftService();
        System.out.println();

        String user = "CaptMousse";
        String uuid = minecraftService.getPlayerUUID(user);
        uuid = mcUuidConverter(uuid);
        // System.out.println(mcUuidConverter(uuid));

        Map<String, String> userMap = new LinkedHashMap<>();
        userMap.put("uuid", uuid);
        userMap.put("name", user);

        List<Map<String, String>> whiteList = minecraftService.readWhitelistFile();
        whiteList.add(userMap);

        Map<String, List<Map<String, String>>> whitlistMap = new HashMap<>();
        whitlistMap.put("data", whiteList);

        


        System.out.println("\n" + toPrettyFormat(whitlistMap.toString()));
    }

    /**
     * 格式化输出JSON字符串
     * 
     * @return 格式化后的JSON字符串
     */
    private static String toPrettyFormat(String json) {
        JsonParser jsonParser = new JsonParser();
        JsonObject jsonObject = jsonParser.parse(json).getAsJsonObject();
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(jsonObject);
    }
}
