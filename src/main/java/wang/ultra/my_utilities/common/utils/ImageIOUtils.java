package wang.ultra.my_utilities.common.utils;

import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageIOUtils {

    /**
     * 裁剪图片
     * @param srcImage
     * @param x
     * @param y
     * @param width
     * @param height
     * @param newImage
     * @return
     */
    public static boolean cut(File srcImage, int x, int y, int width, int height, File newImage) {
        try {
            // 使用ImageIO的read方法读取图片
            BufferedImage srcBufferedImage = ImageIO.read(srcImage);
            // 调用裁剪方法
            BufferedImage newBufferedImage = srcBufferedImage.getSubimage(x, y, width, height);
            // 获取到文件的后缀名
            String fileName = srcImage.getName();
            String formatName = fileName.substring(fileName.lastIndexOf(".") + 1);
            // 使用ImageIOd的write方法进行输出
            ImageIO.write(newBufferedImage, formatName, newImage);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }


    public static boolean scale(File srcImageFile, File destImageFile, float scale) {





        return false;
    }

    public static boolean scale(File srcImageFile, File destImageFile, int side) {
        return scale(srcImageFile, destImageFile, side, side);
    }

    public static boolean scale(File srcImageFile, File destImageFile, int width, int height) {

        try {
            // 使用ImageIO的read方法读取图片
            BufferedImage read = ImageIO.read(srcImageFile);
            // 调用缩放方法获取缩放后的图片
            Image img = read.getScaledInstance(width, height, Image.SCALE_DEFAULT);
            // 创建一个新的缓存图片
            BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            // 获取画笔
            Graphics2D graphics = image.createGraphics();
            // 将Image对象画在画布上, 最后一个参数, ImageObserver: 接收有关Image信息同步的异步更新接口, 没用到直接传null
            graphics.drawImage(img, 0, 0, null);
            // 一定要释放资源
            graphics.dispose();
            // 获取到文件的后缀名
            String fileName = srcImageFile.getName();
            String formatName = fileName.substring(fileName.lastIndexOf(".") + 1);
            // 使用ImageIO的write方法进行输出
            ImageIO.write(image, formatName, destImageFile);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static void main(String[] args) {
        String fileFolder = "WabbyWabbo";
        String fileName = "LAN.jpg";
        String newFileName = "thumbnail_100x100_" + fileName;

        String folder = System.getProperty("user.dir") + File.separator + fileFolder + File.separator;
        File file = new File(folder + fileName);


        // 获取图片尺寸
        FastImageInfo imageInfo;
        try {
            imageInfo = new FastImageInfo(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        int width = imageInfo.getWidth();
        int height = imageInfo.getHeight();

        int minSide = Math.min(width, height);
        int x, y;
        if (width < height) {
            x = 0;
            y = (height - width) / 2;
        } else {
            x = (width - height) / 2;
            y = 0;
        }

        try {
            FileIOUtils fileIOUtils = new FileIOUtils();
            File imageFile = fileIOUtils.getImageFile(fileFolder, fileName);

            File thumbnail = new File(folder + newFileName);
            // 裁剪图片
            ImageIOUtils.cut(imageFile, x, y, minSide, minSide, thumbnail);
            // 缩放图片
            ImageIOUtils.scale(thumbnail, thumbnail, 100);

//            fileIOUtils.uploadFile(fileFolder, newFileName, imageFileScale);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }








    }
}
