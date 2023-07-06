package wang.ultra.my_utilities.blog.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import wang.ultra.my_utilities.blog.service.BlogContextService;
import wang.ultra.my_utilities.blog.service.ImageUploadService;
import wang.ultra.my_utilities.common.utils.AjaxUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("/blog")
public class BlogController {
    @Autowired
    ImageUploadService imageUploadService;
    @Autowired
    BlogContextService blogContextService;

    @PostMapping("/uploadContext")
    public AjaxUtils uploadContext(String title, String context) {
        System.out.println("context = \n" + context);

        int result = blogContextService.contextUpload(title, context);

        return switch (result) {
            case -1 -> AjaxUtils.failed("保存失败! ");
            case 0 -> AjaxUtils.success("保存成功! ");
            default -> AjaxUtils.success("保存约等于成功! ");
        };
    }

    @GetMapping("/contextList")
    public AjaxUtils getContextList(HttpServletRequest request) {

        // 根据用户名获取文章列表
        List<Map<String, String>> contextList = blogContextService.contextListSelectByUsername("default");
        return AjaxUtils.success(contextList);
    }

    @GetMapping("/contextRead")
    public AjaxUtils getContext(String contextId) {
        Map<String, String> contextMap = blogContextService.contextSelectByUuid(contextId);
        return AjaxUtils.success(contextMap);
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
