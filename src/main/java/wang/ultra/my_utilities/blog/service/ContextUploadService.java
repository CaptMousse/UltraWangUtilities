package wang.ultra.my_utilities.blog.service;

import java.io.File;

import org.springframework.stereotype.Service;

import com.github.pagehelper.util.StringUtil;

import wang.ultra.my_utilities.common.utils.FileIOUtils;
import wang.ultra.my_utilities.common.utils.StringUtils;

@Service("contextUploadService")
public class ContextUploadService {

    private final String subFileFolder = "Blog" + File.separator + "images";

    public void contextUpload(String context) {


        String fileName = StringUtils.getMyUUID() + ".html";

        
        int flag = FileIOUtils.createFile(subFileFolder, fileName, 0, context);
    }
}
