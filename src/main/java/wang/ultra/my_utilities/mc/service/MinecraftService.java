package wang.ultra.my_utilities.mc.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("minecraftService")
public class MinecraftService {

    public List<Map<String, String>> stringToJsonMap() {

        // 读取黑名单
        String subPath = "WabbyWabbo";
        String fileName = ConstantFromFile.getMinecraftBlackListFileName();

        String resultString = FileIOUtils.readFileToString(subPath, fileName);

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(resultString, List.class);
        } catch (JsonProcessingException e) {
            return null;
        }
    }

    public String getBannedPlayerUUID(String name) {

        String urlString = "https://api.mojang.com/users/profiles/minecraft/" + name;
        URL url;
        StringBuilder stringBuilder = new StringBuilder();
        try {
            url = new URL(urlString);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.connect();

            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
            String strRead;
            while ((strRead = bufferedReader.readLine()) != null) {
                stringBuilder.append(strRead);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
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
}
