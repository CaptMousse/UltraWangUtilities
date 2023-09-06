package wang.ultra.my_utilities.blog.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import wang.ultra.my_utilities.blog.entity.ContextEntity;
import wang.ultra.my_utilities.blog.service.BlogContextService;
import wang.ultra.my_utilities.blog.service.ImageUploadService;
import wang.ultra.my_utilities.common.cache.blogContextVisits.BlogContextVisitsMap;
import wang.ultra.my_utilities.common.cache.username.UserLoginCacheMap;
import wang.ultra.my_utilities.common.utils.AjaxUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("/blog/context")
public class BlogController {
    @Autowired
    ImageUploadService imageUploadService;
    @Autowired
    BlogContextService blogContextService;

    /**
     *
     * @param contextId         文章ID
     * @param title             标题
     * @param context           正文
     * @param coverImgLocation  封面图片地址
     * @param brief             简介
     * @param contextPriority   优先级
     * @param ifPrivate         私密(0公开, 1私密)
     * @param request
     * @return
     */
    @PostMapping("/upload/uploadContext")
    public AjaxUtils uploadContext(String contextId, String title, String context, String coverImgLocation, String brief, String contextPriority, String ifPrivate, HttpServletRequest request) {

        if (title == null || title.trim().isEmpty()) {
            return AjaxUtils.failed("标题为空! ");
        }
        if (context == null || context.trim().isEmpty()) {
            return AjaxUtils.failed("正文为空! ");
        }

        int priority;
        try {
            priority = Integer.parseInt(contextPriority);
        } catch (Exception e) {
            return AjaxUtils.failed("优先级输入错误! ");
        }
        int isPrivate;
        try {
            isPrivate = Integer.parseInt(ifPrivate);
        } catch (Exception e) {
            return AjaxUtils.failed("是否私密输入错误! ");
        }


        
        UserLoginCacheMap userLoginCacheMap = new UserLoginCacheMap();
        String username = userLoginCacheMap.getLoginUser(request.getHeader("LoginToken"));
        if (username == null || username.isEmpty()) {
            return AjaxUtils.failed("请重新登录! ");
        }

        Map<String, String> uuidMap = blogContextService.contextSelectByUuid(contextId);
        if (uuidMap != null && !uuidMap.isEmpty()) {
            return AjaxUtils.failed("您已提交过, 请勿重复提交! ");
        }

        ContextEntity contextEntity = new ContextEntity();
        contextEntity.setUuid(contextId);
        contextEntity.setTitle(title);
        contextEntity.setCoverImgLocation(coverImgLocation);
        contextEntity.setBrief(brief);
        contextEntity.setContextPriority(priority);
        contextEntity.setIfPrivate(isPrivate);
        contextEntity.setUser(username);

        int result = blogContextService.contextUpload(contextEntity, context);

        return switch (result) {
            case -1 -> AjaxUtils.failed("保存失败! ");
            case 0 -> AjaxUtils.success("保存成功! ", "SAVE_SUCCESS");
            default -> AjaxUtils.success("保存约等于成功! ");
        };
    }

    @PostMapping("/upload/uploadImage")
    public AjaxUtils upload(MultipartFile image, String imageName, HttpServletRequest request) {
        // 根据LoginToken获取用户名
        UserLoginCacheMap userLoginCacheMap = new UserLoginCacheMap();
        String username = userLoginCacheMap.getLoginUser(request.getHeader("LoginToken"));
        
        String realName = imageUploadService.imageAdd(image, imageName, username);
        
        String downloadImageUrl = "blog/context/downloadImage?image=" + realName;
        System.out.println("downloadImageUrl = " + downloadImageUrl);
        Map<String, String> map = new HashMap<>();
        map.put("location", downloadImageUrl);
        return AjaxUtils.success(map);
    }

    @GetMapping("/contextListByUsername")
    public AjaxUtils getContextList(HttpServletRequest request) {
        HttpSession session = request.getSession();
        String username = String.valueOf(session.getAttribute("username"));
        // 根据用户名获取文章列表
        List<Map<String, String>> contextList = blogContextService.contextListSelectByUsername(username);
        return AjaxUtils.success(contextList);
    }

    @GetMapping("/getContextList")
    public AjaxUtils getContextList(int pageNum, int pageSize) {

        // 测序不能乱, startPage这一行一定要在service前面
        PageHelper.startPage(pageNum, pageSize);

        // 根据用户名获取文章列表
        List<Map<String, Object>> contextList = blogContextService.contextList();

        PageInfo<Map<String, Object>> pageInfo = new PageInfo<>(contextList);
        Map<String, String> pageMap = new HashMap<>();
        pageMap.put("pageSize", String.valueOf(pageInfo.getSize()));
        pageMap.put("pageTotal", String.valueOf(pageInfo.getTotal()));
        pageMap.put("pageNum", String.valueOf(pageInfo.getPageNum()));
        pageMap.put("pagePages", String.valueOf(pageInfo.getPages()));

        List<Object> returnList = new ArrayList<>();
        returnList.add(pageMap);
        returnList.addAll(contextList);
        
        return AjaxUtils.success(returnList);
    }

    @GetMapping("/contextRead")
    public AjaxUtils getContext(String contextId, HttpServletRequest request) {
        String visitId = request.getHeader("VisitId");
        if (contextId.trim().isEmpty()) {
            return AjaxUtils.failed("uuid参数错误! ");
        }
        Map<String, String> contextMap = blogContextService.contextSelectByUuid(contextId);

        // 判断是否要增加访问量
        BlogContextVisitsMap blogContextVisitsMap = new BlogContextVisitsMap();
        if (!blogContextVisitsMap.availableVisits(contextId, visitId)) {
            blogContextVisitsMap.setVisits(contextId, visitId);
            blogContextService.contextAmount(contextId);
        }

        return AjaxUtils.success(contextMap);
    }

    @GetMapping("/downloadImage")
    public void downloadImage(String image, HttpServletResponse response) {
        imageUploadService.imageShow(image, response);
    }

    @GetMapping("/getContextRecommend")
    public AjaxUtils getContextRecommend() {
        return AjaxUtils.success(blogContextService.contextListRecommendIn3());
    }
}
