package wang.ultra.my_utilities.mc.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import org.apache.tomcat.util.bcel.Const;
import org.springframework.stereotype.Service;
import wang.ultra.my_utilities.common.constant.ConstantFromFile;
import wang.ultra.my_utilities.common.utils.DateConverter;
import wang.ultra.my_utilities.common.utils.FileIOUtils;
import wang.ultra.my_utilities.common.utils.JsonConverter;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Service("minecraftService")
public class MinecraftService {

    /**
     * 把文件转成Json数组
     *
     * @param fileName
     * @return
     */
    private List<Map<String, String>> fileToJsonArray(String fileName) {

        // 读取黑名单
        String subPath = ConstantFromFile.getMinecraftDataFolder();
//        String subPath = "WabbyWabbo";
        String resultString = FileIOUtils.readFileToString(subPath, fileName);

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(resultString, List.class);
        } catch (JsonProcessingException e) {
            return null;
        }
    }

    /**
     * 使用mojang的API获取用户名的UUID
     *
     * @param name
     * @return
     * @throws Exception
     */
    public String getPlayerUUID(String name) throws Exception {

        String urlString = "https://api.mojang.com/users/profiles/minecraft/" + name;
        URL url;
        StringBuilder stringBuilder = new StringBuilder();

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

        String uuid = JsonConverter.JsonStringToMap(stringBuilder.toString()).get("id");
        return mcUuidConverter(uuid);
    }

    private List<Map<String, String>> getBlacklist() {
        String fileName = ConstantFromFile.getMinecraftBlackListFileName();
//        String fileName = "banned-players.json";
        return Objects.requireNonNull(fileToJsonArray(fileName));
    }

    private List<Map<String, String>> getWhitelist() {
        String whitelistFileName = ConstantFromFile.getMinecraftWhiteListFileName();
        return Objects.requireNonNull(fileToJsonArray(whitelistFileName));
    }


    /**
     * UUID转换
     *
     * @param uuid 转换前: e78d0e6b1c0440829d3e89f6bf6539be
     * @return 转换后: e78d0e6b-1c04-4082-9d3e-89f6bf6539be
     */
    private static String mcUuidConverter(String uuid) {
        StringBuilder sb = new StringBuilder(uuid);
        sb.insert(20, "-");
        sb.insert(16, "-");
        sb.insert(12, "-");
        sb.insert(8, "-");
        return sb.toString();
    }

    /**
     * 使用Gson格式化输出JSON数组
     *
     * @param jsonStr
     * @return
     */
    private static String toPrettyFormat(String jsonStr) {
        JsonParser jsonParser = new JsonParser();
        JsonArray jsonArray = jsonParser.parse(jsonStr).getAsJsonArray();
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(jsonArray);
    }

    /**
     * 白名单添加, 用不到了
     *
     * @param name
     * @return
     */
    public String whitelistAdd(String name) {

        // 先获取UUID
        String uuid;
        try {
            uuid = mcUuidConverter(getPlayerUUID(name));

        } catch (Exception e) {
            return name + "可能不存在! ";

        }

        Map<String, String> userMap = new LinkedHashMap<>();
        userMap.put("uuid", uuid);
        userMap.put("name", name);

        // 再获取现在的白名单列表
        String fileName = ConstantFromFile.getMinecraftWhiteListFileName();
        List<Map<String, String>> whiteList = fileToJsonArray(fileName);
        if (whiteList.isEmpty()) {
            return "-1";
        }

        // 把新人放进去后转成String再格式化成Json数组
        whiteList.add(userMap);
        String jsonStr = JsonConverter.ListMapToJsonString(whiteList);
        String prettyJsonStr = toPrettyFormat(jsonStr);

        String subPath = ConstantFromFile.getMinecraftDataFolder();
//        String subPath = "WabbyWabbo";
        FileIOUtils.createFile(subPath, fileName, 1, prettyJsonStr);

        return "1";
    }

    public String blacklistAdd(String name, String reason) {
        // 先获取UUID
        String uuid;
        try {
            uuid = mcUuidConverter(getPlayerUUID(name));
        } catch (Exception e) {
            return String.valueOf(e);
        }
        String bannedTime = DateConverter.getTime(System.currentTimeMillis()) + " +0800";
        Map<String, String> userMap = new LinkedHashMap<>();
        userMap.put("uuid", uuid);
        userMap.put("name", name);
        userMap.put("created", bannedTime);
        userMap.put("source", "CaptMousse");
        userMap.put("expires", "forever");
        userMap.put("reason", "[UltraWangUtilities]" + reason);

        // 再获取现在的白名单列表
        String fileName = ConstantFromFile.getMinecraftBlackListFileName();
        List<Map<String, String>> blacklist = fileToJsonArray(fileName);
        if (blacklist != null && blacklist.isEmpty()) {
            return "-1";
        }

        // 把新人放进去后转成String再格式化成Json数组
        blacklist.add(userMap);
        String jsonStr = JsonConverter.ListMapToJsonString(blacklist);
        String prettyJsonStr = toPrettyFormat(jsonStr);

        String subPath = ConstantFromFile.getMinecraftDataFolder();
//        String subPath = "WabbyWabbo";
        FileIOUtils.createFile(subPath, fileName, 1, prettyJsonStr);

        return "1";
    }

    /**
     * 查询黑名单
     *
     * @param name
     * @return
     */
    public Map<String, String> blacklistSearchByName(String name) {

        // 先获取到黑名单, 根据名字查
        List<Map<String, String>> resultList = getBlacklist();
        for (Map<String, String> map : resultList) {
            if (map.get("name").equals(name)) {
                return map;
            }
        }

        // 没查到的话就去查他的UUID, 再根据UUID查
        String uuid;
        try {
            uuid = getPlayerUUID(name);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        if (uuid != null) {
            for (Map<String, String> map : resultList) {
                if (map.get("uuid").equals(uuid)) {
                    return map;
                }
            }
        }
        return new HashMap<>();
    }

    public Map<String, String> whitelistSearchByName(String name) {

        // 先获取到黑名单, 根据名字查
        List<Map<String, String>> resultList = getWhitelist();
        for (Map<String, String> map : resultList) {
            if (map.get("name").equals(name)) {
                return map;
            }
        }

        // 没查到的话就去查他的UUID, 再根据UUID查
        String uuid;
        try {
            uuid = getPlayerUUID(name);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        if (uuid != null) {
            for (Map<String, String> map : resultList) {
                if (map.get("uuid").equals(uuid)) {
                    return map;
                }
            }
        }
        return new HashMap<>();
    }


    public static void main(String[] args) {
        String s = mcUuidConverter("91f9f532c7234d3ea6dfba3e1da5f4e5");
        System.out.println(s);
    }
}
