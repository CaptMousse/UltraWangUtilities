package wang.ultra.my_utilities.common.download.service;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import wang.ultra.my_utilities.common.download.entity.FileTransferEntity;
import wang.ultra.my_utilities.common.download.mapper.FileTransferMapper;
import wang.ultra.my_utilities.common.utils.DateConverter;
import wang.ultra.my_utilities.common.utils.FileIOUtils;
import wang.ultra.my_utilities.common.utils.ListConverter;
import wang.ultra.my_utilities.common.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service("fileTransferService")
public class FileTransferService {
    @Autowired
    FileTransferMapper fileTransferMapper;

    public int fileAdd(MultipartFile file, String fileName) {

        FileTransferEntity entity = new FileTransferEntity();
        String uuid = StringUtils.getMyUUID();
        String realName = uuid + "." + StringUtils.getFileType(fileName);
        entity.setId(uuid);
        entity.setReal_name(realName);
        entity.setShow_name(fileName);
        entity.setCreate_time(DateConverter.getNoSymbolTime());
        entity.setCreator("default");
        entity.setStatus(1);

        List<FileTransferEntity> entityList = new ArrayList<>();
        entityList.add(entity);
        fileTransferMapper.fileAdd(entityList);

        FileIOUtils fileIOUtils = new FileIOUtils();
        return fileIOUtils.uploadFile("transferFiles", realName, file);
    }

    public void fileSelectByShowName(String showName, HttpServletResponse response) {
        List<Map<String, Object>> resultList = fileTransferMapper.fileSelectByShowName(showName);
        Map<String, String> resultMap = ListConverter.mapValueToString(resultList).get(0);
        String id = resultMap.get("id");
        String realName = resultMap.get("real_name");

        response.addHeader("Content-Disposition", "attachment; filename=" + showName);
        
        // 更新访问量
        fileTransferMapper.fileAmountCount(id);

        FileIOUtils fileIOUtils = new FileIOUtils();
        fileIOUtils.downloadFile("transferFiles", realName, response);
    }





}
