package wang.ultra.my_utilities.attached_storage.service;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import wang.ultra.my_utilities.attached_storage.entity.FileEntity;
import wang.ultra.my_utilities.attached_storage.entity.FolderEntity;
import wang.ultra.my_utilities.attached_storage.entity.dto.FileDto;
import wang.ultra.my_utilities.attached_storage.entity.vo.FolderAndFileVo;
import wang.ultra.my_utilities.attached_storage.entity.vo.FolderVo;
import wang.ultra.my_utilities.attached_storage.mapper.FileMapper;
import wang.ultra.my_utilities.attached_storage.mapper.FolderAndFileMapper;
import wang.ultra.my_utilities.attached_storage.mapper.FolderMapper;
import wang.ultra.my_utilities.attached_storage.utils.FileUtils;
import wang.ultra.my_utilities.common.utils.DateConverter;
import wang.ultra.my_utilities.common.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Service("attachedStorageService")
public class AttachedStorageService {

    @Autowired
    FileMapper fileMapper;

    @Autowired
    FolderMapper folderMapper;

    @Autowired
    FolderAndFileMapper folderAndFileMapper;

    public String addFile(MultipartFile file, FileEntity fileEntity) {
        String realName = StringUtils.getMyUUID() + "." + StringUtils.getFileType(fileEntity.getShowName());

        // 校验文件的MD5
        String md5 = FileUtils.getFileMd5(file);
        System.out.println("MD5 = " + md5);
        if (null == md5) {
            return "-1";
        }
        FileDto md5FileEntity = new FileDto();
        md5FileEntity.setFileMd5(md5);
        List<FileEntity> getMd5FileList = fileMapper.getFile(md5FileEntity);
        if (!getMd5FileList.isEmpty()) {
            return getMd5FileList.get(0).getShowName();
        }

        FileUtils.uploadFile("AttachedStorage", realName, file);

        fileEntity.setRealName(realName);
        fileEntity.setFileSize(String.valueOf(file.getSize()));
        fileEntity.setCreator("Shark");
        fileEntity.setCreateTime(DateConverter.getNoSymbolTime());
        fileEntity.setFileMd5(md5);

        // 持久化
        List<FileEntity> fileList = new ArrayList<>();
        fileList.add(fileEntity);
        fileMapper.addFile(fileList);

        return "0";
    }

    public int getFile(FileDto dto, HttpServletResponse response) {
        List<FileEntity> fileList = fileMapper.getFile(dto);

        if (fileList.isEmpty()) {
            return -1;
        }

        FileEntity fileEntity = fileList.get(0);

        // 更新访问量
        FileEntity addAmountEntity = new FileEntity(fileEntity.getRealName());
        addAmountEntity.setLastAccessTime(DateConverter.getNoSymbolTime());
        fileMapper.updateFile(addAmountEntity);

//        FileIOUtils fileIOUtils = new FileIOUtils();
//        fileIOUtils.downloadFile("AttachedStorage", fileEntity.getRealName(), response);

        int downloadResult = FileUtils.downloadFile("AttachedStorage", fileEntity.getRealName(), fileEntity.getShowName(), response);
        if (downloadResult < 0) {
            return -1;
        }
        return 0;
    }



    public int addFolder(FolderEntity entity) {
        entity.setFolderId(StringUtils.getMyUUID());
        if (null == entity.getParentId() || entity.getParentId().isEmpty()) {
            entity.setParentId("root");
        }
        entity.setCreator("shark");
        entity.setCreateTime(DateConverter.getNoSymbolTime());
        entity.setStatus("0");

        List<FolderEntity> folderList = new ArrayList<>();
        folderList.add(entity);

        try {
            folderMapper.addFolder(folderList);
        } catch (Exception e) {
            return -1;
        }
        return 0;
    }

    public int updateFolder(FolderEntity entity) {
        try {
            folderMapper.updateFolder(entity);
        } catch (Exception e) {
            return -1;
        }
        return 0;
    }

    public List<FolderVo> getFolders(FolderEntity entity) {
        List<FolderVo> folderList = folderMapper.getFolder(entity);
        return generateFolderTree(folderList);
    }

    /**
     * 获取不含目录树的目录
     * @param entity
     * @return
     */
    public List<FolderVo> getFlatFolders(FolderEntity entity) {
        return folderMapper.getFolder(entity);
    }

    /**
     * 生成目录树
     * @param folderList
     * @return
     */
    private List<FolderVo> generateFolderTree(List<FolderVo> folderList) {

        List<FolderVo> treeList = new ArrayList<>();
        for (FolderVo vo : folderList) {
            if ("root".equals(vo.getParentId())) {
                treeList.add(vo);
            }
            for (FolderVo child : folderList) {
                if (child.getParentId().equals(vo.getFolderId())) {
                    vo.addChild(child);
                }
            }
        }
        return treeList;
    }

    public List<FolderAndFileVo> getFolderAndFile(FolderEntity entity) {
        return folderAndFileMapper.getFolderAndFile(entity.getFolderId());
    }
}
