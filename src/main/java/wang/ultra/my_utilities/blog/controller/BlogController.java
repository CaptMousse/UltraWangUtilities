package wang.ultra.my_utilities.blog.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import wang.ultra.my_utilities.blog.service.ImageUploadService;
import wang.ultra.my_utilities.common.utils.AjaxUtils;

import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("/blog")
public class BlogController {
    @Autowired
    ImageUploadService imageUploadService;

    @PostMapping("/uploadContext")
    public AjaxUtils upload(String context) {
        System.out.println("context = \n" + context);
        return AjaxUtils.success();
    }

    @PostMapping("/uploadImage")
    public AjaxUtils upload(MultipartFile image, String imageName, HttpServletRequest request) {
//        String imageName = image.getName();
        String localAddr = String.valueOf(request.getLocalAddr());
        String serverPort = String.valueOf(request.getServerPort());
        String realName = imageUploadService.imageAdd(image, imageName);
        String downloadImageUrl = "http://" + localAddr + ":" + serverPort + "/blog/downloadImage?image=" + realName;
        System.out.println("downloadImageUrl = " + downloadImageUrl);
        Map<String, String> map = new HashMap<>();
        map.put("location", downloadImageUrl);
        return AjaxUtils.success(map);
    }

    @GetMapping("/downloadImage")
    public void downloadImage(String image, HttpServletResponse response) {
        imageUploadService.imageShow(image, response);
    }
}
