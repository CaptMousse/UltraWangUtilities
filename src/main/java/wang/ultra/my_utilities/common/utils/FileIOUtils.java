package wang.ultra.my_utilities.common.utils;

import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.multipart.MultipartFile;
import wang.ultra.my_utilities.common.constant.ConstantFromFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class FileIOUtils {

    private static final Log LOG = LogFactory.getLog(FileIOUtils.class);

    /**
     * 构建目录
     *
     * @param dirPath    输出目录
     * @param subDirPath 子目录
     * @return -1 失败, 0 新建成功, 1 已存在无需新建
     */
    public static Integer createDir(String dirPath, String subDirPath) {
        File file = new File(dirPath);
        if (subDirPath != null) {
            if (!subDirPath.trim().equals("")) {
                String totalDir = dirPath + File.separator + subDirPath;
                file = new File(totalDir);
            }
        }
        if (!file.exists()) {
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            file.mkdirs();
            return 0; // 不存在, 新建成功
        } else {
            return 1; // 存在
        }
    }

    /**
     * 写文件(追加或覆盖)
     *
     * @param subPath  文件目录
     * @param fileName 文件名
     * @param method   0 追加, 1 覆盖
     * @return -1 失败, 0 新建成功, 11 已存在并追加, 12 已存在并覆盖
     */
    public static Integer createFile(String subPath, String fileName, Integer method, String text) {

        // 创建目录, 存在也无所谓
        createDir(System.getProperty("user.dir"), subPath);

        String filePath = System.getProperty("user.dir") + File.separator + subPath;
        File file = new File(filePath, fileName);
        try {
            int flag = -1;
            if (!file.exists()) {
                file.createNewFile();
                flag = 0;
            }
            FileOutputStream fileOutputStream = new FileOutputStream(file, true); // true代表追加
            if (method == 1) {
                fileOutputStream = new FileOutputStream(file, false); // false代表覆盖
                if (flag != 0) {
                    flag = 12;
                }
            }

            fileOutputStream.write(text.getBytes());
            fileOutputStream.write("\r\n".getBytes());
            fileOutputStream.close();

            if (flag == 0) {
                return flag; // 新建成功
            }
            if (flag == 12) {
                return flag; // 已存在并覆盖
            } else {
                return 11; // 已存在并追加
            }
        } catch (IOException e) {
            return -1;
        }
    }

    /**
     * 读文件到Map, 用" = " 分隔, 放进map
     * 例如配置文件
     *
     * @param dirPath  文件路径
     * @param fileName 文件名
     * @return Map<String, String>
     */
    public static Map<String, String> readConfigFileToMap(String dirPath, String fileName) {

        File file = new File(dirPath, fileName);
        String strText;
        Map<String, String> stringMap = new LinkedHashMap<>();
        if (file.exists() && file.isFile()) {
            InputStreamReader inputStreamReader = null;
            BufferedReader bufferedReader = null;

            try {
                inputStreamReader = new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8);
                bufferedReader = new BufferedReader(inputStreamReader);
                while ((strText = bufferedReader.readLine()) != null) {
                    // System.out.println("strText = " + strText);
                    if (!"".equals(strText)) { // 略过空行
                        String strSubString = strText.substring(0, 1);
                        if (!"#".equals(strSubString)) { // 略过注释符号
                            String[] strArr = strText.split(" = ");
                            stringMap.put(strArr[0], strArr[1]);
                        }
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            } finally {
                if (inputStreamReader != null) {
                    try {
                        inputStreamReader.close();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
                if (bufferedReader != null) {
                    try {
                        bufferedReader.close();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
        return stringMap;
    }

    /**
     * 读取文件到字符串
     */
    public static String readFileToString(String subFileFolder, String fileName) {

        String filePath = System.getProperty("user.dir") + File.separator + subFileFolder + File.separator + fileName;
        File file = new File(filePath);

        String strText;
        StringBuilder stringBuilder = new StringBuilder();
        if (file.exists() && file.isFile()) {
            InputStreamReader inputStreamReader = null;
            BufferedReader bufferedReader = null;

            try {
                inputStreamReader = new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8);
                bufferedReader = new BufferedReader(inputStreamReader);
                while ((strText = bufferedReader.readLine()) != null) {
                    // System.out.println("strText = " + strText);
                    if (!"".equals(strText)) { // 略过空行
                        stringBuilder.append(strText);
                    }
                }
            } catch (IOException e) {
                // throw new RuntimeException(e);
                return String.valueOf(e);
            } finally {
                if (inputStreamReader != null) {
                    try {
                        inputStreamReader.close();
                    } catch (IOException e) {
                        // throw new RuntimeException(e);
                        return String.valueOf(e);
                    }
                }
                if (bufferedReader != null) {
                    try {
                        bufferedReader.close();
                    } catch (IOException e) {
                        // throw new RuntimeException(e);
                        return String.valueOf(e);
                    }
                }
            }
        }
        return stringBuilder.toString();
    }

    /**
     * 下载文件或者打开图片
     *
     * @param subFileFolder
     * @param fileName
     * @param response
     */
    public void downloadFile(String subFileFolder, String fileName, HttpServletResponse response) {

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
                    response.setContentType("application/x-msdownload"); // 下载
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
            } finally {
                try {
                    if (in != null) {
                        in.close();
                    }
                    response.getOutputStream().flush();
                    float finishTimeStamp = ((float) (System.currentTimeMillis() - initTimeStamp)) / 1000;
                    LOG.info("文件下载完成!");
                    LOG.info("下载用时 = " + finishTimeStamp + "秒");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 获取图片文件
     *
     * @param subFileFolder
     * @param fileName
     * @return
     * @throws IOException
     */
    public File getImageFile(String subFileFolder, String fileName) throws IOException {
        String folder = System.getProperty("user.dir") + File.separator + ConstantFromFile.getFileFolder()
                + File.separator + subFileFolder + File.separator;
        String filePath = folder + fileName;
        String formatName = fileName.substring(fileName.lastIndexOf(".") + 1);
        File file = new File(filePath);

        BufferedImage bufferedImage = ImageIO.read(file);
        ImageIO.write(bufferedImage, formatName, file);
        return file;
    }

    /**
     * 上传文件
     *
     * @param subFileFolder
     * @param fileName
     * @param file
     */
    public int uploadFile(String subFileFolder, String fileName, File file) {
        String folder = System.getProperty("user.dir") + File.separator + subFileFolder + File.separator;
        String filePath = folder + fileName;

        System.out.println("文件开始上传, 文件名: " + fileName);

        InputStream inputStream = null;
        FileOutputStream fileOutputStream = null;
        try {
            inputStream = new DataInputStream(new FileInputStream(file));
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
                System.out.println("文件上传完成! ");
                return 0;
            } catch (IOException e) {
                e.printStackTrace();
                return -1;
            }
        }
    }

    /**
     * 上传MultipartFile文件
     *
     * @param subFileFolder
     * @param fileName
     * @param multipartFile
     */
    public int uploadFile(String subFileFolder, String fileName, MultipartFile multipartFile) {
        String folder = System.getProperty("user.dir") + File.separator + ConstantFromFile.getFileFolder()
                + File.separator + subFileFolder + File.separator;
        String filePath = folder + fileName;

        System.out.println("文件开始上传, 文件名: " + fileName);

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
                System.out.println("文件上传完成! ");
                return 0;
            } catch (IOException e) {
                e.printStackTrace();
                return -1;
            }
        }
    }

    /**
     * MultipartFile 2 File
     *
     * @param multipartFile
     * @return
     */
    public File multipartFile2File(MultipartFile multipartFile) {

        String fileName = multipartFile.getOriginalFilename();
        File file = new File(fileName);
        OutputStream outputStream = null;
        try {
            // 获取文件流，以文件流的方式输出到新文件
            outputStream = new FileOutputStream(file);
            byte[] byteArr = multipartFile.getBytes();
            for (int i = 0; i < byteArr.length; i++) {
                outputStream.write(byteArr[i]);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return file;
    }

    public boolean ifFileExist(String subFileFolder, String fileName) {

        String folder = System.getProperty("user.dir") + File.separator + ConstantFromFile.getFileFolder()
                + File.separator + subFileFolder + File.separator;
        // String folder = System.getProperty("user.dir") + File.separator +
        // ConstantFromFile.getFileFolder() + File.separator + subFileFolder +
        // File.separator;
        String filePath = folder + fileName;
        File file = new File(filePath);
        return file.exists() && file.isFile();
    }

    public static boolean fileDelete(String subFileFolder, String fileName) {
        String folder = System.getProperty("user.dir") + File.separator + ConstantFromFile.getFileFolder()
                + File.separator + subFileFolder + File.separator;
        // String folder = System.getProperty("user.dir") + File.separator +
        // ConstantFromFile.getFileFolder() + File.separator + subFileFolder +
        // File.separator;
        String filePath = folder + fileName;
        File file = new File(filePath);

        boolean isDeleted = false;
        if (file.exists() && file.isFile()) {
            isDeleted = file.delete();
        }

        return isDeleted;
    }

    public static void main(String[] args) {
        String fileFolder = "WabbyWabbo";
        String fileName = "Alley.JPG";
        // boolean isDeleted = fileDelete(fileFolder, fileName);
        // if (isDeleted) {
        // System.out.println("文件 " + fileFolder + fileName + " 已被删除");
        // } else if (!ifFileExist(fileFolder, fileName)){
        // System.out.println("文件 " + fileFolder + fileName + " 不存在");
        // } else {
        // System.out.println("文件 " + fileFolder + fileName + " 删除失败");
        // }

        System.out.println(DateConverter.getNoSymbolTime(System.currentTimeMillis() - (1000 * 3600 * 24 * 7)));
    }
}