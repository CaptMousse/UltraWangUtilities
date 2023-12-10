package wang.ultra.my_utilities.attached_storage.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import wang.ultra.my_utilities.attached_storage.entity.FolderEntity;
import wang.ultra.my_utilities.attached_storage.service.FolderService;
import wang.ultra.my_utilities.common.utils.AjaxUtils;

@RestController
@CrossOrigin
@RequestMapping("/attachedStorage")
public class AttachedStorageController {

    @Autowired
    FolderService folderService;

    @PostMapping("/addFolder")
    public AjaxUtils addFolder(@RequestBody FolderEntity folderEntity) {
        if (null == folderEntity.getFolderName()) {
            return AjaxUtils.failed("请输入文件夹名称! ");
        }
        int result  = folderService.addFolder(folderEntity);

        if (result == 0) {
            return AjaxUtils.success("新建成功! ");
        } else if (result == -1) {
            return AjaxUtils.failed("新建失败! ");
        }
        else {
            return AjaxUtils.failed("文件夹被小鲨鱼吃了! ");
        }
    }

    @PostMapping("/getFolder")
    public AjaxUtils getFolder(@RequestBody FolderEntity folderEntity) {
        return AjaxUtils.success(folderService.getFolders(folderEntity));
    }
}
