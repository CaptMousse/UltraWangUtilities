package wang.ultra.my_utilities.blog.service;

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

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service("imageUploadService")
public class ImageUploadService {

    @Autowired
    FileTransferMapper fileTransferMapper;

    private final String subFileFolder = "Blog" + File.separator + "images";

    public String imageAdd(MultipartFile image, String imageName) {

        FileTransferEntity entity = new FileTransferEntity();
        String uuid = StringUtils.getMyUUID();
        String realName = uuid + "." + StringUtils.getFileType(imageName);
        entity.setId(uuid);
        entity.setReal_name(realName);
        entity.setShow_name(imageName);
        entity.setCreate_time(DateConverter.getNoSymbolTime());
        entity.setCreator("tiny");
        entity.setStatus(1);
        List<FileTransferEntity> entityList = new ArrayList<>();
        entityList.add(entity);

        fileTransferMapper.fileAdd(entityList);

        FileIOUtils fileIOUtils = new FileIOUtils();
        fileIOUtils.uploadFile(subFileFolder, realName, image);

        return realName;
    }

    public void imageShow(String imageName, HttpServletResponse response) {

        List<Map<String, Object>> resultList = fileTransferMapper.fileSelectByRealName(imageName);
        Map<String, String> resultMap = ListConverter.mapValueToString(resultList).get(0);
        String id = resultMap.get("id");
        String realName = resultMap.get("real_name");
        long amount = Long.parseLong(resultMap.get("amount"));
        amount++;
        // 更新访问量
        fileTransferMapper.fileAmountCount(amount, id);

        FileIOUtils fileIOUtils = new FileIOUtils();
        fileIOUtils.downloadFile(subFileFolder, realName, response);

    }
}
