package wang.ultra.my_utilities.common.utils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageIOUtils {

    /**
     * 裁剪图片
     * 
     * @param srcImage
     * @param x
     * @param y
     * @param width
     * @param height
     * @param newImage
     * @return
     */
    public static boolean cut(File srcImageFile, int x, int y, int width, int height, File newImageFile) {
        try {
            // 使用ImageIO的read方法读取图片, 然后裁剪
            BufferedImage srcBufferedImage = ImageIO.read(srcImageFile);
            BufferedImage newBufferedImage = srcBufferedImage.getSubimage(x, y, width, height);

            // 获取到文件的后缀名, 用ImageIO输出
            String fileName = srcImageFile.getName();
            String formatName = fileName.substring(fileName.lastIndexOf(".") + 1);
            ImageIO.write(newBufferedImage, formatName, srcImageFile);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 正方形缩放
     * 
     * @param srcImageFile 原图
     * @param newImageFile 新图
     * @param side         边长
     * @return
     */
    public static boolean scale(File srcImageFile, int side, File newImageFile) {
        return scale(srcImageFile, side, side, newImageFile);
    }

    /**
     * 长方形缩放
     * 
     * @param srcImageFile 原图
     * @param newImageFile 新图
     * @param width        宽度
     * @param height       高度
     * @return
     */

    public static boolean scale(File srcImageFile, int width, int height, File newImageFile) {

        try {
            // 使用ImageIO的read方法读取图片, 然后缩放
            BufferedImage srcBufferedImage = ImageIO.read(srcImageFile);
            Image newImage = srcBufferedImage.getScaledInstance(width, height, Image.SCALE_DEFAULT);

            // 创建一个新的缓存图片
            BufferedImage newBufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            Graphics2D graphics = newBufferedImage.createGraphics();
            graphics.drawImage(newImage, 0, 0, null);
            graphics.dispose(); // 释放资源

            // 输出
            String fileName = srcImageFile.getName();
            String formatName = fileName.substring(fileName.lastIndexOf(".") + 1);
            ImageIO.write(newBufferedImage, formatName, newImageFile);

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 生成缩略图
     * 
     * @param imageFile
     * @param scalePixel
     * @return
     */
    public static boolean createThumbnailImage(File imageFile, int scalePixel) {

        int width = 0;
        int height = 0;
        // 获取图片长宽
        FastImageInfo imageInfo = null;
        try {
            imageInfo = new FastImageInfo(imageFile);
            width = imageInfo.getWidth();
            height = imageInfo.getHeight();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        if (width == 0 || height == 0) {
            return false;
        }

        String newImageFileName = "thumbnail_" + scalePixel + "_" + scalePixel + "_" + imageFile.getName();
        File thumbnailImageFile = new File("WabbyWabbo", newImageFileName);

        // 获取裁剪坐标
        int minSide = Math.min(width, height);
        int x, y;
        if (width == height) {
            x = 0;
            y = 0;
        } else {
            if (width < height) {
                x = 0;
                y = (height - width) / 2;
            } else {
                x = (width - height) / 2;
                y = 0;
            }
        }

        boolean result;
        result = ImageIOUtils.cut(imageFile, x, y, minSide, minSide, thumbnailImageFile);
        if (!result) {
            return result;
        }
        return ImageIOUtils.scale(thumbnailImageFile, scalePixel, thumbnailImageFile);
    }

    public static void main(String[] args) {

        String fileFolder = "WabbyWabbo";
        String fileName = "Alley.JPG";

        FileIOUtils fileIOUtils = new FileIOUtils();
        File imageFile = null;
        try {
            imageFile = fileIOUtils.getImageFile(fileFolder, fileName);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if (imageFile == null) {
            System.out.println("File is not exist! ");
            return;
        }

        boolean result = createThumbnailImage(imageFile, 100);
        if (result) {
            System.out.println("Create thumbnail success! ");
        } else {
            System.out.println("Create thumbnail fail! ");
        }

        // String fileFolder = "WabbyWabbo";
        // String fileName = "LAN.jpg";
        // String folder = System.getProperty("user.dir") + File.separator + fileFolder
        // + File.separator;
        // File file = new File(folder + fileName);

        // int width = 0;
        // int height = 0;
        // // 获取图片尺寸
        // FastImageInfo imageInfo = null;
        // try {
        // imageInfo = new FastImageInfo(file);
        // width = imageInfo.getWidth();
        // height = imageInfo.getHeight();
        // } catch (IOException e) {
        // e.printStackTrace();
        // }

        // if (width == 0 || height == 0) {
        // return;
        // }

        // try {
        // FileIOUtils fileIOUtils = new FileIOUtils();
        // File imageFile = fileIOUtils.getImageFile(fileFolder, fileName);
        // String newFileName = "thumbnail_100x100_" + fileName;
        // File thumbnailImageFile = new File(folder + newFileName);

        // // 获取裁剪坐标
        // int minSide = Math.min(width, height);
        // int x, y;
        // if (width == height) {
        // x = 0;
        // y = 0;
        // } else {
        // if (width < height) {
        // x = 0;
        // y = (height - width) / 2;
        // } else {
        // x = (width - height) / 2;
        // y = 0;
        // }
        // }

        // // 裁剪成正方形
        // ImageIOUtils.cut(imageFile, x, y, minSide, minSide, thumbnailImageFile);
        // // 缩放
        // ImageIOUtils.scale(thumbnailImageFile, thumbnailImageFile, 100);

        // // fileIOUtils.uploadFile(fileFolder, newFileName, imageFileScale);
        // } catch (IOException e) {
        // throw new RuntimeException(e);
        // }

    }
}
