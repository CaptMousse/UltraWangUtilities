package wang.ultra.my_utilities.common.download.controller;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import wang.ultra.my_utilities.common.download.service.FileTransferService;
import wang.ultra.my_utilities.common.utils.AjaxUtils;

@RestController
@CrossOrigin
@RequestMapping("/fileTransfer")
public class FileTransferController {
    @Autowired
    FileTransferService fileTransferService;

    @GetMapping("download")
    public void downloadFile(String fileName, HttpServletResponse response) {
        fileTransferService.fileSelectByShowName(fileName, response);
    }

    @PostMapping("upload")
    public AjaxUtils uploadFile(MultipartFile file, String fileName) {
        int result = fileTransferService.fileAdd(file, fileName);
        if (result == 0) {
            return AjaxUtils.success("上传成功!", "上传成功!");
        } else {
            return AjaxUtils.failed("上传失败!", "上传失败!");
        }
    }
}
