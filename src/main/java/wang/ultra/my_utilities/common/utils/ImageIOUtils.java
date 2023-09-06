package wang.ultra.my_utilities.common.utils;

import wang.ultra.my_utilities.common.constant.ConstantFromFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageIOUtils {

    /**
     * 裁剪图片
     * 
     * @param srcImageFile  原图
     * @param x         X坐标位置
     * @param y         Y坐标位置
     * @param width     新图片宽度
     * @param height    新图片高度
     * @param newImageFile  新图片对象
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
            ImageIO.write(newBufferedImage, formatName, newImageFile);
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
     * @param imageFile     原图
     * @param scalePixel    缩略图边长
     * @return
     */
    public static void createThumbnailImage(File imageFile, int scalePixel, String thumbnailSubFileFolder) {
        String thumbnailFileFolder = System.getProperty("user.dir") + File.separator + ConstantFromFile.getFileFolder()
                + File.separator + thumbnailSubFileFolder + File.separator;
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
            return;
        }
        if (width == 0 || height == 0) {
            return;
        }

        String thumbnailImageFileName = "thumbnail_" + scalePixel + "_" + scalePixel + "_" + imageFile.getName();
        File thumbnailImageFile = new File(thumbnailFileFolder, thumbnailImageFileName);

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
            System.out.println("Thumbnail cut fail! ");
            return;
        }
        result = ImageIOUtils.scale(thumbnailImageFile, scalePixel, thumbnailImageFile);
        if (result) {
            System.out.println("Create thumbnail success! ");
        } else {
            System.out.println("Thumbnail scale fail! ");
        }
    }

    public static void main(String[] args) {

        String fileFolder = "FileFolder" + File.separator + "blog" + File.separator + "images";
        String fileName = "8cded001c93d4c488cdf9c6124585d61.jpg";

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

        String thumbnailFileFolder = fileFolder + File.separator + "thumbnail";

        createThumbnailImage(imageFile, 200, thumbnailFileFolder);

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
