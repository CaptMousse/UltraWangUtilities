package wang.ultra.my_utilities.idiompedia.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import wang.ultra.my_utilities.common.utils.StringUtils;
import wang.ultra.my_utilities.idiompedia.entity.IdiomEntity;
import wang.ultra.my_utilities.idiompedia.mapper.IdiomsMapper;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

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
                                if (!Objects.equals(strArr[0], "无")) {
                                    idiomEntity.setDerivation(strArr[0]);
                                }
                                if (!Objects.equals(strArr[1], "无")) {
                                    idiomEntity.setExample(strArr[1]);
                                }
                                if (!Objects.equals(strArr[2], "无")) {
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

    public Map<String, String> idiomsSearchByWord(String keyword) {

        System.out.println("keyword = " + keyword);

        List<Map<String, Object>> wordList = idiomsMapper.idiomsSearchByWord(keyword);

        if (wordList.isEmpty()) {
            return null;
        }

        System.out.println("wordList = " + wordList);

        Map<String, String> resultMap = new LinkedHashMap<>();
        resultMap.put("word", String.valueOf(wordList.get(0).get("word")));
        resultMap.put("pinyin", String.valueOf(wordList.get(0).get("pinyin")));
        resultMap.put("explanation", String.valueOf(wordList.get(0).get("explanation")));
        resultMap.put("derivation", String.valueOf(wordList.get(0).get("derivation")));
        resultMap.put("example", String.valueOf(wordList.get(0).get("example")));

        return resultMap;
    }

    public List<Map<String, Object>> idiomsSearchBySort(int sort, String keyword) {
        List<Map<String, Object>> resultList = new ArrayList<>();

        switch (sort) {
            case 1 -> resultList = idiomsMapper.idiomsSearchByFirst(keyword);
            case 2 -> resultList = idiomsMapper.idiomsSearchBySecond(keyword);
            case 3 -> resultList = idiomsMapper.idiomsSearchByThird(keyword);
            case 4 -> resultList = idiomsMapper.idiomsSearchByLast(keyword);
            default -> {
            }
        }

        return resultList;
    }

    public List<Map<String, Object>> idiomsSearchByQuestionMark(String keyword) {

        String key1 = StringUtils.isChinese(String.valueOf(keyword.charAt(0)));
        String key2 = StringUtils.isChinese(String.valueOf(keyword.charAt(1)));
        String key3 = StringUtils.isChinese(String.valueOf(keyword.charAt(2)));
        String key4 = StringUtils.isChinese(String.valueOf(keyword.charAt(3)));

        boolean flag = false;
        IdiomEntity idiomEntity = new IdiomEntity();

        if (key1 != null) {
            idiomEntity.setWord_1(key1);
            flag = true;
        }
        if (key2 != null) {
            idiomEntity.setWord_2(key2);
            flag = true;
        }
        if (key3 != null) {
            idiomEntity.setWord_3(key3);
            flag = true;
        }
        if (key4 != null) {
            idiomEntity.setWord_4(key4);
            flag = true;
        }

        if (flag) {
            return idiomsMapper.idiomsSearchByQuestionMark(idiomEntity);
        } else {
            List<Map<String, Object>> returnList = new ArrayList<>();
            return returnList;
        }

    }
}
