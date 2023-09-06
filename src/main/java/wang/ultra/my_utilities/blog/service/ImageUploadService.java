package wang.ultra.my_utilities.blog.service;

import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import wang.ultra.my_utilities.common.download.entity.FileTransferEntity;
import wang.ultra.my_utilities.common.download.mapper.FileTransferMapper;
import wang.ultra.my_utilities.common.utils.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service("imageUploadService")
public class ImageUploadService {

    @Autowired
    FileTransferMapper fileTransferMapper;

    public static String subFileFolder = "Blog" + File.separator + "images";
    public static String thumbnailFileFolder = subFileFolder + File.separator + "thumbnail";

    public String imageAdd(MultipartFile image, String imageName, String username) {

        FileTransferEntity entity = new FileTransferEntity();
        String uuid = StringUtils.getMyUUID();
        String realName = uuid + "." + StringUtils.getFileType(imageName);
        entity.setId(uuid);
        entity.setReal_name(realName);
        entity.setShow_name(imageName);
        entity.setCreate_time(DateConverter.getNoSymbolTime());
        entity.setCreator(username);
        entity.setStatus(1);
        List<FileTransferEntity> entityList = new ArrayList<>();
        entityList.add(entity);

        fileTransferMapper.fileAdd(entityList);

        FileIOUtils fileIOUtils = new FileIOUtils();
        fileIOUtils.uploadFile(subFileFolder, realName, image);

//        File imageFile = fileIOUtils.multipartFile2File(image);

        if (imageName.contains("coverImg_")) {
            try {
                File imageFile = fileIOUtils.getImageFile(subFileFolder, realName);
                ImageIOUtils.createThumbnailImage(imageFile, 200, thumbnailFileFolder);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }




        return realName;
    }

    public void imageShow(String imageName, HttpServletResponse response) {

        FileIOUtils fileIOUtils = new FileIOUtils();
        String id;
        // 如果请求图片名里含有缩略图字样
        if (imageName.contains("thumbnail")) {
            imageName = imageName.substring(10);
            List<Map<String, Object>> resultList = fileTransferMapper.fileSelectByRealName(imageName);
            Map<String, String> resultMap = ListConverter.mapValueToString(resultList).get(0);
            id = resultMap.get("id");
            String thumbnailRealName = "thumbnail_200_200_" + resultMap.get("real_name");

            if (fileIOUtils.ifFileExist(thumbnailFileFolder, thumbnailRealName)) {
                fileIOUtils.downloadFile(thumbnailFileFolder, thumbnailRealName, response);
            } else {
                fileIOUtils.downloadFile(subFileFolder, resultMap.get("real_name"), response);
            }

        } else {
            List<Map<String, Object>> resultList = fileTransferMapper.fileSelectByRealName(imageName);
            Map<String, String> resultMap = ListConverter.mapValueToString(resultList).get(0);
            id = resultMap.get("id");
            fileIOUtils.downloadFile(subFileFolder, resultMap.get("real_name"), response);
        }

        // 更新访问量
        fileTransferMapper.fileAmountCount(id);











    }
}
