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

    public void fileAdd(MultipartFile file, String fileName) {

        FileTransferEntity entity = new FileTransferEntity();
        String uuid = StringUtils.getMyUUID();
        String realName = uuid + StringUtils.getFileType(fileName);
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
        fileIOUtils.uploadFile(file, realName);
    }

    public void fileSelectByShowName(String showName, HttpServletResponse response) {
        List<Map<String, Object>> resultList = fileTransferMapper.fileSelectByShowName(showName);
        Map<String, String> resultMap = ListConverter.mapValueToString(resultList).get(0);
        String id = resultMap.get("id");
        String realName = resultMap.get("real_name");
        long amount = Long.parseLong(resultMap.get("amount"));
        amount++;
        // 更新访问量
        fileTransferMapper.fileAmountCount(amount, id);

        FileIOUtils fileIOUtils = new FileIOUtils();
        fileIOUtils.downloadFile(showName, realName, response);
    }





}
