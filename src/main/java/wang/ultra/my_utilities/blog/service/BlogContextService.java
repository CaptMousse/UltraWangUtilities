package wang.ultra.my_utilities.blog.service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import wang.ultra.my_utilities.blog.entity.ContextEntity;
import wang.ultra.my_utilities.blog.mapper.ContextMapper;
import wang.ultra.my_utilities.common.constant.ConstantFromFile;
import wang.ultra.my_utilities.common.utils.DateConverter;
import wang.ultra.my_utilities.common.utils.FileIOUtils;
import wang.ultra.my_utilities.common.utils.ListConverter;
import wang.ultra.my_utilities.common.utils.StringUtils;

@Service("blogContextService")
public class BlogContextService {

    @Autowired
    ContextMapper contextMapper;



    public int contextUpload(String title, String context) {

        String uuid = StringUtils.getMyUUID();

        // 写入数据库
        ContextEntity contextEntity = new ContextEntity();
        contextEntity.setUuid(uuid);
        contextEntity.setTitle(title);
        contextEntity.setUser("default");
        String create_time = DateConverter.getTime();
        contextEntity.setCreate_time(create_time);
        contextEntity.setUpdate_time(create_time);
        contextEntity.setStatus(0);
        List<ContextEntity> contextEntityList = new ArrayList<>();
        contextEntityList.add(contextEntity);
        contextMapper.contextAdd(contextEntityList);

        // 写入文件
        String fileName = uuid + ".html";

        String subFileFolder = ConstantFromFile.getFileFolder() + File.separator + "Blog" + File.separator + "contexts";
        int intFlag = FileIOUtils.createFile(subFileFolder, fileName, 1, context);
        System.out.println("intFlag = " + intFlag);

        return intFlag;

    }


    /**
     * 返回用户文章列表
     * @param username
     * @return
     */
    public List<Map<String, String>> contextListSelectByUsername(String username) {
        List<Map<String, Object>> resultList = contextMapper.contextSearchByUser(username);
        return ListConverter.mapValueToString(resultList);
    }

    public Map<String, String> contextSelectByUuid(String uuid) {
        List<Map<String, Object>> resultList = contextMapper.contextSearchByUuid(uuid);

        List<Map<String, String>> returnList = ListConverter.mapValueToString(resultList);

        Map<String, String> returnMap = returnList.get(0);

        String subFileFolder = ConstantFromFile.getFileFolder() + File.separator + "Blog" + File.separator + "contexts";
        String context = FileIOUtils.readFileToString(subFileFolder, uuid + ".html");

        returnMap.put("context", context);



        return returnMap;


    }
}