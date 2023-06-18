package wang.ultra.my_utilities.common.download.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import wang.ultra.my_utilities.common.download.service.FileTransferService;

import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("/fileTransfer")
public class FileTransferController {
    @Autowired
    FileTransferService fileTransferService;

    @GetMapping("download")
    public void downloadFile(String fileName, HttpServletRequest request, HttpServletResponse response) {
        request.getServerName();
        System.out.println("request.getServerName() = " + request.getServerName());

        fileTransferService.fileSelectByShowName(fileName, response);
    }

    @PostMapping("upload")
    public Map<String, String> uploadFile(MultipartFile file, String fileName) {
        fileTransferService.fileAdd(file, fileName);

        Map<String, String> returnMap = new HashMap<>();
        returnMap.put("status", "success");
        return returnMap;
    }
}
