package wang.ultra.my_utilities.attached_storage.utils;

import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.multipart.MultipartFile;
import wang.ultra.my_utilities.common.constant.ConstantFromFile;
import wang.ultra.my_utilities.common.utils.FileIOUtils;
import wang.ultra.my_utilities.common.utils.StringUtils;

import java.io.*;
import java.math.BigInteger;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

public class FileUtils {

    private static final Log LOG = LogFactory.getLog(FileIOUtils.class);

    /**
     * 上传MultipartFile文件
     *
     * @param subFileFolder
     * @param fileName
     * @param multipartFile
     */
    public static int uploadFile(String subFileFolder, String fileName, MultipartFile multipartFile) {
        String folder = System.getProperty("user.dir") + File.separator + ConstantFromFile.getFileFolder()
                + File.separator + subFileFolder + File.separator;
        String filePath = folder + fileName;

        LOG.info("文件开始上传, 文件名: " + fileName);

        InputStream inputStream = null;
        FileOutputStream fileOutputStream = null;
        try {

            inputStream = multipartFile.getInputStream();
            fileOutputStream = new FileOutputStream(filePath);
            int length;
            byte[] bytes = new byte[1024];
            while ((length = inputStream.read(bytes)) != -1) {
                fileOutputStream.write(bytes, 0, length);
            }

        } catch (Exception ignored) {
            return -1;
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (fileOutputStream != null) {
                    fileOutputStream.close();
                }
                LOG.info("文件上传完成! ");

            } catch (IOException e) {
                e.printStackTrace();
                return -2;
            }
        }
        return 0;
    }

    /**
     * 获取文件的MD5
     * @param file
     * @return
     */
    public static String getFileMd5(MultipartFile file) {
        InputStream inputStream = null;
        MessageDigest digest;
        BigInteger bigInt = null;
        try {
            digest = MessageDigest.getInstance("MD5");
            inputStream = file.getInputStream();
            int length;
            byte[] bytes = new byte[1024];
            while ((length = inputStream.read(bytes)) != -1) {
                digest.update(bytes, 0, length);
            }
            bigInt = new BigInteger(1, digest.digest());

        } catch (NoSuchAlgorithmException | IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (null != bigInt) {
                    return bigInt.toString();
                }

            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }
        return null;
    }

    /**
     * 上传MultipartFile文件并生成MD5
     *
     * @param subFileFolder
     * @param fileName
     * @param multipartFile
     */
    public static String uploadFileAndGenerateMd5(String subFileFolder, String fileName, MultipartFile multipartFile) {
        String folder = System.getProperty("user.dir") + File.separator + ConstantFromFile.getFileFolder()
                + File.separator + subFileFolder + File.separator;
        String filePath = folder + fileName;

        LOG.info("文件开始上传, 文件名: " + fileName);

        InputStream inputStream = null;
        FileOutputStream fileOutputStream = null;
        MessageDigest digest;
        BigInteger bigInt = null;
        try {
            digest = MessageDigest.getInstance("MD5");

            inputStream = multipartFile.getInputStream();
            fileOutputStream = new FileOutputStream(filePath);
            int length;
            byte[] bytes = new byte[1024];
            while ((length = inputStream.read(bytes)) != -1) {
                digest.update(bytes, 0, length);
                fileOutputStream.write(bytes, 0, length);
            }
            bigInt = new BigInteger(1, digest.digest());

        } catch (Exception ignored) {
            return null;
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (fileOutputStream != null) {
                    fileOutputStream.close();
                }
                LOG.info("文件上传完成! ");
                if (null != bigInt) {
                    return bigInt.toString();
                }

            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }
        return null;
    }

    /**
     * 下载文件或者打开图片
     *
     * @param subFileFolder
     * @param fileName
     * @param response
     */
    public static int downloadFile(String subFileFolder, String fileName, String showName, HttpServletResponse response) {

        String folder = System.getProperty("user.dir") + File.separator + ConstantFromFile.getFileFolder()
                + File.separator + subFileFolder + File.separator;
        String filePath = folder + fileName;
        File file = new File(filePath);

        if (file.exists() && file.isFile()) {
            InputStream in = null;
            int fileLength = (int) file.length();
            long initTimeStamp = 0;
            try {
                String type = StringUtils.getFileType(fileName);
                List<String> imageTypeList = new ArrayList<>();
                imageTypeList.add("jpg");
                imageTypeList.add("jpeg");
                imageTypeList.add("gif");
                imageTypeList.add("png");

                if (imageTypeList.contains(type)) {
                    response.setContentType("image/jpeg"); // 打开图片
                } else {
                    response.addHeader("Content-Disposition", "attachment; filename=" + new String(showName.getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1));
                    response.addHeader("Content-Disposition", "inline; filename=" + URLEncoder.encode(showName, StandardCharsets.UTF_8));
                    response.setContentType("application/octet-stream"); // 下载
                }
                response.setContentLength(fileLength);


                LOG.info("文件开始下载");
                LOG.info("文件名 = " + file.getName());
                LOG.info("文件大小 = " + fileLength + "字节");

                in = new FileInputStream(file);

                int hasNext = 0;
                int speedCount = 0;
                initTimeStamp = System.currentTimeMillis();
                long speedTimeStamp = initTimeStamp;
                int speedLimit = ConstantFromFile.getFileDownloadSpeedLimit();  // 获取下载限速
                if (speedLimit != 0) {
                    LOG.info("限速已开启, 限速" + speedLimit + "kb/s");
                }
                byte[] kb = new byte[1024];
                while (hasNext != -1) {

                    if (speedLimit != 0) {
                        speedCount++;
                        if (speedCount == speedLimit) {
                            // 限速原理
                            // 如果字节数组计数 = 限速, 例如1024kb, 就比对时间戳
                            // 如果间隔时间 >= 1000ms, 就只初始化计数和原始时间戳
                            // 如果间隔时间 < 1000ms, 就用Thread.sleep限速剩余时间, 然后再初始化计数和时间戳
                            long currTimeStamp = System.currentTimeMillis() - speedTimeStamp;
                            long sleepTimeStamp = 1000 - currTimeStamp;
                            if (sleepTimeStamp > 0) {
                                Thread.sleep(sleepTimeStamp);
                            }
                            speedCount = 0;
                            speedTimeStamp = System.currentTimeMillis();
                        }
                    }

                    hasNext = in.read(kb);
                    if (hasNext != -1) {
                        response.getOutputStream().write(kb, 0, hasNext);
                    }
                }
            } catch (Exception ignored) {
                return -1;
            } finally {
                try {
                    if (in != null) {
                        in.close();
                    }
                    response.getOutputStream().flush();
                    float finishTimeStamp = ((float) (System.currentTimeMillis() - initTimeStamp)) / 1000;
                    LOG.info("文件下载完成!");
                    LOG.info("下载用时 = " + finishTimeStamp + "秒");
                    return 0;
                } catch (IOException e) {
                    e.printStackTrace();
                    return -2;
                }
            }
        }
        return -3;
    }
}
