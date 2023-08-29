package wang.ultra.my_utilities.common.captcha.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import wang.ultra.my_utilities.common.cache.captcha.CaptchaCacheMap;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;


@RestController
@CrossOrigin
@RequestMapping("/captcha")
public class CaptchaGenerater2 {

    public final String VERIFY_CODES = "23456789ABCDEFGHJKLMNPQRSTUVWXYZ";

    private final Random random = new Random();

    /**
     * 输出指定验证码图片流
     */
    @GetMapping("/getNewCaptcha")
    public void outputImage(String id, HttpServletRequest request, HttpServletResponse response) throws IOException {

        String verifyCode = generateVerifyCode();

        int verifyCodeLength = verifyCode.length();
        int captchaHeight = 40;
        int captchaWidth = 80;

        BufferedImage image = new BufferedImage(captchaWidth, captchaHeight, BufferedImage.TYPE_INT_RGB);
        Random rand = new Random();
        Graphics2D graphics2D = image.createGraphics();
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics2D.setColor(Color.GRAY);// 设置边框色
        graphics2D.fillRect(0, 0, captchaWidth, captchaHeight);
        Color backgroundColor = getRandColor(200, 250);
        graphics2D.setColor(backgroundColor);// 设置背景色
        graphics2D.fillRect(0, 2, captchaWidth, captchaHeight - 4);

        //绘制干扰线
        Random random = new Random();
        graphics2D.setColor(getRandColor(160, 200));// 设置线条的颜色
        for (int i = 0; i < 20; i++) {
            int x = random.nextInt(captchaWidth - 1);
            int y = random.nextInt(captchaHeight - 1);
            int xl = random.nextInt(6) + 1;
            int yl = random.nextInt(12) + 1;
            graphics2D.drawLine(x, y, x + xl + 40, y + yl + 20);
        }

        // 使图片扭曲
        shear(graphics2D, captchaWidth, captchaHeight, backgroundColor);

        graphics2D.setColor(getRandColor(100, 160));
        int fontSize = captchaHeight / 2;
        Font font = new Font("Arial Black", Font.ITALIC, fontSize);
        graphics2D.setFont(font);
        char[] chars = verifyCode.toCharArray();
        for (int i = 0; i < verifyCodeLength; i++) {
            AffineTransform affine = new AffineTransform();
            affine.setToRotation(Math.PI / 4 * rand.nextDouble() * (rand.nextBoolean() ? 1 : -1), ((double) captchaWidth / verifyCodeLength) * i + (double) fontSize / 2, (double) captchaHeight / 2);
            graphics2D.setTransform(affine);
            graphics2D.drawChars(chars, i, 1, ((captchaWidth - 10) / verifyCodeLength) * i + 5, captchaHeight / 2 + fontSize / 2 - 3);
        }
        graphics2D.dispose();

        // 把验证码存入Map
        CaptchaCacheMap captchaCacheMap = new CaptchaCacheMap();
        captchaCacheMap.setCaptcha(id, verifyCode);


        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0L);
        response.setContentType("image/jpeg");
        ImageIO.write(image, "PNG", response.getOutputStream());
    }

    /**
     * 使用指定源生成验证码
     */
    private String generateVerifyCode() {

        String sources = VERIFY_CODES;  // 字符源
        int codesLen = sources.length();
        Random rand = new Random(System.currentTimeMillis());
        int captchaLength = 4;
        StringBuilder verifyCode = new StringBuilder(captchaLength);
        for (int i = 0; i < captchaLength; i++) {
            verifyCode.append(sources.charAt(rand.nextInt(codesLen - 1)));
        }
        return verifyCode.toString();
    }

    /**
     * 生成随机颜色
     */
    private Color getRandColor(int fc, int bc) {
        if (fc > 255) {
            fc = 255;
        }
        if (bc > 255) {
            bc = 255;
        }
        int r = fc + random.nextInt(bc - fc);
        int g = fc + random.nextInt(bc - fc);
        int b = fc + random.nextInt(bc - fc);
        return new Color(r, g, b);
    }

//    private int getRandomIntColor() {
//        int[] rgb = getRandomRgb();
//        int color = 0;
//        for (int c : rgb) {
//            color = color << 8;
//            color = color | c;
//        }
//        return color;
//    }

    /**
     * 生成随机的RGB颜色
     */
//    private int[] getRandomRgb() {
//        int[] rgb = new int[3];
//        for (int i = 0; i < 3; i++) {
//            rgb[i] = random.nextInt(255);
//        }
//        return rgb;
//    }

    private void shear(Graphics graphics2D, int captchaWidth, int captchaHeight, Color color) {
        int periodX = random.nextInt(2);
        int framesX = 1;
        int phaseX = random.nextInt(2);
        for (int i = 0; i < captchaHeight; i++) {
            double d = (double) (0) * Math.sin((double) i / (double) periodX + (6.2831853071795862D * (double) phaseX) / (double) framesX);
            graphics2D.copyArea(0, i, captchaWidth, 1, (int) d, 0);
            graphics2D.setColor(color);
            graphics2D.drawLine((int) d, i, 0, i);
            graphics2D.drawLine((int) d + captchaWidth, i, captchaWidth, i);
        }

        int periodY = random.nextInt(40) + 10; // 50;
        int framesY = 20;
        int phaseY = 7;
        for (int i = 0; i < captchaWidth; i++) {
            double d = (double) (periodY >> 1) * Math.sin((double) i / (double) periodY + (6.2831853071795862D * (double) phaseY) / (double) framesY);
            graphics2D.copyArea(i, 0, 1, captchaHeight, 0, (int) d);
            graphics2D.setColor(color);
            graphics2D.drawLine(i, (int) d, i, 0);
            graphics2D.drawLine(i, (int) d + captchaHeight, i, captchaHeight);

        }
    }

//    private void shearX(Graphics graphics2D, int captchaWidth, int captchaHeight, Color color) {
//        int periodX = random.nextInt(2);
//        int framesX = 1;
//        int phaseX = random.nextInt(2);
//        for (int i = 0; i < captchaHeight; i++) {
//            double d = (double) (0) * Math.sin((double) i / (double) periodX + (6.2831853071795862D * (double) phaseX) / (double) framesX);
//            graphics2D.copyArea(0, i, captchaWidth, 1, (int) d, 0);
//            graphics2D.setColor(color);
//            graphics2D.drawLine((int) d, i, 0, i);
//            graphics2D.drawLine((int) d + captchaWidth, i, captchaWidth, i);
//        }
//    }

//    private void shearY(Graphics graphics2D, int captchaWidth, int captchaHeight, Color color) {
//        int periodY = random.nextInt(40) + 10; // 50;
//        int framesY = 20;
//        int phaseY = 7;
//        for (int i = 0; i < captchaWidth; i++) {
//            double d = (double) (periodY >> 1) * Math.sin((double) i / (double) periodY + (6.2831853071795862D * (double) phaseY) / (double) framesY);
//            graphics2D.copyArea(i, 0, 1, captchaHeight, 0, (int) d);
//            graphics2D.setColor(color);
//            graphics2D.drawLine(i, (int) d, i, 0);
//            graphics2D.drawLine(i, (int) d + captchaHeight, i, captchaHeight);
//        }
//    }

}