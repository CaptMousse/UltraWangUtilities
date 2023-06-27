package wang.ultra.my_utilities.common.utils;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.multipart.MultipartFile;
import wang.ultra.my_utilities.common.constant.ConstantFromFile;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class FileIOUtils {

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
            return 0;   // 不存在, 新建成功
        } else {
            return 1;   // 存在
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
            FileOutputStream fileOutputStream = new FileOutputStream(file, true);   // true代表追加
            if (method == 1) {
                fileOutputStream = new FileOutputStream(file, false);               // false代表覆盖
                if (flag != 0) {
                    flag = 12;
                }
            }

            fileOutputStream.write(text.getBytes());
            fileOutputStream.write("\r\n".getBytes());
            fileOutputStream.close();

            if (flag == 0) {
                return flag;    // 新建成功
            }
            if (flag == 12) {
                return flag;    // 已存在并覆盖
            } else {
                return 11;      // 已存在并追加
            }
        } catch (IOException e) {
            return -1;
        }
    }

    /**
     * 读文件, 用" = " 分隔, 放进map
     *
     * @param dirPath  文件路径
     * @param fileName 文件名
     * @return Map<String, String>
     */
    public static Map<String, String> readFile(String dirPath, String fileName) {

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
//                    System.out.println("strText = " + strText);
                    if (!"".equals(strText)) {                          // 略过空行
                        String strSubString = strText.substring(0, 1);
                        if (!"#".equals(strSubString)) {                // 略过注释符号
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

    public static void main(String[] args) {
        String subPath = "Logs";
        String fileName = "Logs.txt";
//        String text = DateConverter.getTime() + " = " + GetMyIPv6.getIPv6();

        // 创建文件夹
//        Integer createDirResult = createDir(dirPath, subDirPath);
//        String dirText = "文件夹 " + subDirPath;
//        printResult(dirText, createDirResult);

        // 创建文件
//        Integer createFileResult = createFile(subPath, fileName, 1, text);
//        fileName = "文件 " + fileName;
//        printResult(fileName, createFileResult);

        // 读文件
        Map<String, String> readFileMap = readFile(subPath, fileName);
        System.out.println("readFileString = \n" + readFileMap);
        String previousUpdateTime = null;
        for (Map.Entry<String, String> entryMap : readFileMap.entrySet()) {
            previousUpdateTime = entryMap.getKey();
        }
        System.out.println("previousUpdateTime = " + previousUpdateTime);
    }

    public void downloadFile(String subFileFolder, String showName, String realName, HttpServletResponse response) {

        String folder = System.getProperty("user.dir") + File.separator + ConstantFromFile.getFileFolder() + File.separator + subFileFolder + File.separator;
        String filePath = folder + realName;
        File file = new File(filePath);

        InputStream in = null;
        try {
            String type = StringUtils.getFileType(realName);
            List<String> imageTypeList = new ArrayList<>();
            imageTypeList.add("jpg");
            imageTypeList.add("jpeg");
            imageTypeList.add("gif");
            imageTypeList.add("png");

            if (imageTypeList.contains(type)) {
                response.setContentType("image/jpeg");
            } else {
                response.setContentType("application/x-msdownload");
            }

            response.setContentLength((int) file.length());
//            response.addHeader("Content-Disposition", "attachment;filename=" + new String(showName.getBytes(), StandardCharsets.ISO_8859_1));

            System.out.println("文件开始下载...");
            System.out.println("文件名: " + file.getName());

            in = new FileInputStream(file);

            int b = 0;
            byte[] buffer = new byte[1024];
            while (b != -1) {
                b = in.read(buffer);
                if (b != -1) {
                    response.getOutputStream().write(buffer, 0, b);
                }
            }
        } catch (Exception ignored) {
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
                response.getOutputStream().flush();
                System.out.println("文件下载完成! ");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void uploadFile(String subFileFolder, String fileName, MultipartFile multipartFile) {
        String folder = System.getProperty("user.dir") + File.separator + ConstantFromFile.getFileFolder() + File.separator + subFileFolder + File.separator;
        String filePath = folder + fileName;

        System.out.println("文件开始上传...");
        System.out.println("文件名: " + fileName);

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
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (fileOutputStream != null) {
                    fileOutputStream.close();
                }
                System.out.println("文件上传完成! ");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


}