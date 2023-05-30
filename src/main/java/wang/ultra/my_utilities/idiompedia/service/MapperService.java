package wang.ultra.my_utilities.idiompedia.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import wang.ultra.my_utilities.idiompedia.entity.IdiomEntity;
import wang.ultra.my_utilities.idiompedia.mapper.IdiomsMapper;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service("mapperService")
public class MapperService {

    @Autowired
    IdiomsMapper idiomsMapper;

    public void csvFile2Mapper(String dirPath, String fileName) {
        File file = new File(dirPath, fileName);
        String strText;

        if (file.exists() && file.isFile()) {
            InputStreamReader inputStreamReader = null;
            BufferedReader bufferedReader = null;

            try {
                inputStreamReader = new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8);
                bufferedReader = new BufferedReader(inputStreamReader);

                int i = 0;

                while ((strText = bufferedReader.readLine()) != null) {
                    // System.out.println("strText = " + strText);
                    if (i != 0) {
                        if (!"".equals(strText)) { // 略过空行
                            String strSubString = strText.substring(0, 1);
                            if (!"#".equals(strSubString)) { // 略过注释符号
                                strText = strText.replaceAll("\"", ""); // 去除引号
                                strText = strText.replaceAll("'", "''"); // 转义SQL语句要用的单引号
                                String[] strArr = strText.split(",");

                                // List<Map<String, Object>> resultList = new ArrayList<>();
                                // List<Map<String, Object>> tempList =
                                // idiomsMapper.idiomsSearchByWord(strArr[4]);
                                // if (tempList.size() > 1) {
                                // Map<String, Object> tempMap = tempList.get(0);
                                // String id = String.valueOf(tempMap.get("uuid"));
                                // System.out.println("id = " + id);
                                // idiomsMapper.idiomsDelete(id);
                                // resultList = idiomsMapper.idiomsSearchByWord(strArr[4]);
                                // } else {
                                // resultList = tempList;
                                // }
                                // if (!resultList.isEmpty()) {
                                // return;
                                // }

                                IdiomEntity idiomEntity = new IdiomEntity();
                                if (strArr[0] != "无") {
                                    idiomEntity.setDerivation(strArr[0]);
                                }
                                if (strArr[1] != "无") {
                                    idiomEntity.setExample(strArr[1]);
                                }
                                if (strArr[2] != "无") {
                                    idiomEntity.setExplanation(strArr[2]);
                                }
                                idiomEntity.setPinyin(strArr[3]);
                                String word = strArr[4];
                                String pinyin_r = strArr[6];
                                idiomEntity.setWord(word);
                                if (word.length() == 4) {
                                    idiomEntity.setWord_1(word.substring(0, 1));
                                    idiomEntity.setWord_2(word.substring(1, 2));
                                    idiomEntity.setWord_3(word.substring(2, 3));
                                    idiomEntity.setWord_4(word.substring(3, 4));
                                    String[] pinyin_r_array = pinyin_r.split(" ");
                                    idiomEntity.setPinyin_1(pinyin_r_array[0]);
                                    idiomEntity.setPinyin_2(pinyin_r_array[1]);
                                    idiomEntity.setPinyin_3(pinyin_r_array[2]);
                                    idiomEntity.setPinyin_4(pinyin_r_array[3]);
                                }
                                idiomEntity.setPinyin_simple(strArr[5]);
                                idiomEntity.setPinyin_r(pinyin_r);
                                idiomEntity.setUuid(String.valueOf(UUID.randomUUID()).replaceAll("-", ""));

                                List<IdiomEntity> idiomList = new ArrayList<>();
                                idiomList.add(idiomEntity);
                                idiomsMapper.idiomsAdd(idiomList);
                            }
                        }
                    }
                    i++;
                }

                System.out.println("总共 " + (i - 1) + " 条记录");

            } catch (IOException e) {
                throw new RuntimeException(e);
            } finally {
                if (inputStreamReader != null) {
                    try {
                        inputStreamReader.close();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
                if (bufferedReader != null) {
                    try {
                        bufferedReader.close();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
    }

    public List<Map<String, Object>> idiomsSearchByWord(String word) {

        IdiomEntity idiomEntity = new IdiomEntity();
        idiomEntity.setWord(word);

        List<Map<String, Object>> tempList = idiomsMapper.idiomsSearchByWord(word);
        List<Map<String, Object>> resultList = new ArrayList<>();

        if (tempList.size() > 1) {
            Map<String, Object> tempMap = tempList.get(0);
            String id = String.valueOf(tempMap.get("uuid"));
            idiomsMapper.idiomsDelete(id);

            resultList = idiomsMapper.idiomsSearchByWord(word);
        } else {
            resultList = tempList;
        }

        return resultList;
    }

    public List<Map<String, Object>> idiomsSearchBySort(int sort, String keyword) {
        List<Map<String, Object>> resultList = new ArrayList<>();

        switch (sort) {
            case 1:
            resultList = idiomsMapper.idiomsSearchByFirst(keyword);
                break;
            case 2:
            resultList = idiomsMapper.idiomsSearchBySecond(keyword);
                break;
            case 3:
            resultList = idiomsMapper.idiomsSearchByThird(keyword);
                break;
            case 4:
            resultList = idiomsMapper.idiomsSearchByLast(keyword);
                break;
            default:
                break;
        }

        return resultList;
    }
}
