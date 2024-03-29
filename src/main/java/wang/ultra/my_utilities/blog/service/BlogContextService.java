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

    public int contextUpload(ContextEntity contextEntity, String context) {

        // 为了防止重复提交, UUID由前台提供
        // String uuid = StringUtils.getMyUUID();
        // contextEntity.setUuid(uuid);
        String create_time = DateConverter.getTime();
        contextEntity.setCreate_time(create_time);
        contextEntity.setUpdate_time(create_time);
        contextEntity.setStatus(0);
        List<ContextEntity> contextEntityList = new ArrayList<>();
        contextEntityList.add(contextEntity);
        contextMapper.contextAdd(contextEntityList);

        // 写入文件
        String fileName = contextEntity.getUuid() + ".html";
        String subFileFolder = ConstantFromFile.getFileFolder() + File.separator + "Blog" + File.separator + "contexts";
        return FileIOUtils.createFile(subFileFolder, fileName, 1, context);
    }

    public int contextUpgrade(ContextEntity entity, String context) {
        entity.setUpdate_time(DateConverter.getTime());
        contextMapper.contextUpgrade(entity);

        // 写入文件
        String fileName = entity.getUuid() + ".html";
        String subFileFolder = ConstantFromFile.getFileFolder() + File.separator + "Blog" + File.separator + "contexts";
        return FileIOUtils.createFile(subFileFolder, fileName, 1, context);
    }

    /**
     * 返回用户文章列表
     * 
     * @param username
     * @return
     */
    public List<Map<String, String>> contextListSelectByUsername(String username) {
        List<Map<String, Object>> resultList = contextMapper.contextSearchByUser(username);
        return ListConverter.mapValueToString(resultList);
    }

    /**
     * 返回所有未封禁的文章列表
     * 
     * @return
     */
    public List<Map<String, Object>> contextList() {
        return contextMapper.contextList();
    }

    /**
     * 根据文章id返回文章
     * 
     * @param contextId
     * @return
     */
    public Map<String, String> contextSelectByUuid(String contextId) {

        List<Map<String, Object>> returnList = contextMapper.contextSearchByUuid(contextId);
        if (returnList.isEmpty()) {
            return null;
        }

        Map<String, String> returnMap = ListConverter.mapValueToString(returnList).get(0);

        String subFileFolder = ConstantFromFile.getFileFolder() + File.separator + "Blog" + File.separator + "contexts";
        String context = FileIOUtils.readFileToString(subFileFolder, contextId + ".html");

        returnMap.put("context", context);

        return returnMap;
    }
    
    /**
     * 增加访问量
     * @param contextId
     */
    public void contextAmount(String contextId) {
        contextMapper.contextAmount(contextId);
    }

    /**
     * 推荐3个文章
     * @return
     */
    public List<Map<String, String>> contextListRecommendIn3() {
        List<Map<String, Object>> resultList = contextMapper.contextListRecommendIn3();
        return ListConverter.mapValueToString(resultList);
    }
}
