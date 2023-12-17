package wang.ultra.my_utilities.attached_storage.controller;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import wang.ultra.my_utilities.attached_storage.entity.FileEntity;
import wang.ultra.my_utilities.attached_storage.entity.FolderEntity;
import wang.ultra.my_utilities.attached_storage.entity.dto.FileDto;
import wang.ultra.my_utilities.attached_storage.service.AttachedStorageService;
import wang.ultra.my_utilities.common.utils.AjaxUtils;

@RestController
@CrossOrigin
@RequestMapping("/attachedStorage")
public class AttachedStorageController {

    @Autowired
    AttachedStorageService attachedStorageService;

    @PostMapping("uploadFile")
    public AjaxUtils uploadFile(MultipartFile file, String fileName, String folderId) {
        FileEntity entity = new FileEntity();
        entity.setShowName(fileName);
        entity.setFolderId(folderId);
        String result = attachedStorageService.addFile(file, entity);

        if ("0".equals(result)) {
            return AjaxUtils.success("上传成功! ");
        } else if ("-1".equals(result)) {
            return AjaxUtils.failed("上传失败, 文件校验失败! ");
        } else if (result.contains(".")) {
            return AjaxUtils.failed("上传失败, 文件重复! 疑似文件名是: " + result);
        } else {
            return AjaxUtils.failed("上传的文件被小鲨鱼吃了! ");
        }
    }

    @GetMapping("downloadFile")
    public void downloadFile(String realName, HttpServletResponse response) {
        FileDto dto = new FileDto();
        dto.setRealName(realName);
        attachedStorageService.getFile(dto, response);
    }

    @PostMapping("/addFolder")
    public AjaxUtils addFolder(@RequestBody FolderEntity folderEntity) {
        if (null == folderEntity.getFolderName()) {
            return AjaxUtils.failed("请输入文件夹名称! ");
        }
        int result  = attachedStorageService.addFolder(folderEntity);

        if (result == 0) {
            return AjaxUtils.success("新建成功! ");
        } else if (result == -1) {
            return AjaxUtils.failed("新建失败! ");
        }
        else {
            return AjaxUtils.failed("文件夹被小鲨鱼吃了! ");
        }
    }

    @PostMapping("/getFolderInTree")
    public AjaxUtils getFolderInTree(@RequestBody FolderEntity folderEntity) {
        return AjaxUtils.success(attachedStorageService.getFolders(folderEntity));
    }

    @PostMapping("/getFolderInFlat")
    public AjaxUtils getFolderInFlat(@RequestBody FolderEntity folderEntity) {
        return AjaxUtils.success(attachedStorageService.getFlatFolders(folderEntity));
    }

    @PostMapping("getFileByFolderId")
    public AjaxUtils getFileByFolderId(@RequestBody FolderEntity folderEntity) {
        return AjaxUtils.success(attachedStorageService.getFlatFolders(folderEntity));
    }

    @PostMapping("getFolderAndFile")
    public AjaxUtils getFolderAndFile(@RequestBody FolderEntity entity) {
        return AjaxUtils.success(attachedStorageService.getFolderAndFile(entity));
    }
}
