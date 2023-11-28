package wang.ultra.my_utilities.blog.scheduler.runnable;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import wang.ultra.my_utilities.blog.service.ImageUploadService;
import wang.ultra.my_utilities.common.download.mapper.FileTransferMapper;
import wang.ultra.my_utilities.common.utils.DateConverter;
import wang.ultra.my_utilities.common.utils.FileIOUtils;
import wang.ultra.my_utilities.common.utils.ListConverter;
import wang.ultra.my_utilities.common.utils.SpringBeanUtils;

import java.util.List;
import java.util.Map;

public class BlogImageCleanRunnable implements Runnable {

    private static final Log LOG = LogFactory.getLog(BlogImageCleanRunnable.class);

    @Override
    public void run() {

        LOG.info("博客图片清理定时任务开始执行");


        /**
         * 定时任务删除七天前没有用到的图片
         * 原理是从库里找到访问量不大于1的
         */

        String createTime = DateConverter.getNoSymbolTime(System.currentTimeMillis() - (1000 * 3600 * 24 * 7));
//        System.out.println("createTime = " + createTime);

        FileTransferMapper fileTransferMapper = SpringBeanUtils.getBean(FileTransferMapper.class);
        List<Map<String, Object>> resultList = fileTransferMapper.getFileByCleaner(createTime);
//        System.out.println("resultList = " + resultList);
        List<Map<String, String>> fileList = ListConverter.mapValueToString(resultList);

        System.out.println("fileList = " + fileList);

        for (Map<String, String> map : fileList) {
            String id = map.get("id");
            String realName = map.get("real_name");

            String blogImgFolder = ImageUploadService.subFileFolder;

            boolean isDeleted = FileIOUtils.fileDelete(blogImgFolder, realName);
            FileIOUtils fileIOUtils = new FileIOUtils();
            if (isDeleted) {
                System.out.println("文件 " + realName + " 已被删除");
                fileTransferMapper.fileDeleteById(id);
            } else if (!fileIOUtils.ifFileExist(blogImgFolder, realName)) {
                System.out.println("文件 " + realName + " 不存在");
                fileTransferMapper.fileDeleteById(id);
            } else {
                System.out.println("文件 " + realName + " 删除失败");
            }
        }
    }
}
