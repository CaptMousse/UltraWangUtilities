package wang.ultra.my_utilities.common.captcha.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

@RestController
@CrossOrigin
@RequestMapping("/captcha")
public class CaptchaGenerater {
    private Font mFont = new Font("Arial Black", Font.ITALIC, 24);
    private int lineWidth = 5;
    private int width = 100;
    private int height = 44;
    private int count = 100;

    @GetMapping("/getCaptcha")
    public void getCaptcha(HttpServletRequest request, HttpServletResponse response) {
        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0L);
        response.setContentType("image/jpeg");
        BufferedImage image = new BufferedImage(this.width, this.height, 1);
        Graphics2D graphics2D = (Graphics2D)image.getGraphics();
        Random random = new Random();
        graphics2D.setColor(this.getRandColor(250, 300));
        graphics2D.fillRect(0, 0, this.width, this.height);
        graphics2D.setFont(this.mFont);
        graphics2D.setColor(this.getRandColor(0, 20));
        graphics2D.drawRect(0, 0, this.width - 1, this.height - 1);

        int line;
        for(int i = 0; i < this.count; ++i) {
            graphics2D.setColor(this.getRandColor(150, 200));
            line = random.nextInt(this.width - this.lineWidth - 1) + 1;
            int y = random.nextInt(this.height - this.lineWidth - 1) + 1;
            int xl = random.nextInt(this.lineWidth);
            int yl = random.nextInt(this.lineWidth);
            graphics2D.drawLine(line, y, line + xl, y + yl);
        }

        StringBuilder sRand = new StringBuilder();
        for(int i = 0; i < 4; ++i) {
            String rand = String.valueOf(this.random2Char(random.nextInt(62)));
//            if (requestURI.endsWith("CheckCode")) {
//                rand = String.valueOf(random.nextInt(10));
//            } else {
//                rand = String.valueOf(this.random2Char(random.nextInt(62)));
//            }

            sRand.append(rand);
            AffineTransform affineTransform = new AffineTransform();
            int anchorX = 2 + i * 15;
            int anchorY = this.height - 10;
            double ran = Math.random();
            affineTransform.rotate(Math.random(), anchorX, anchorY);
            graphics2D.setTransform(affineTransform);
            graphics2D.setColor(new Color(20 + random.nextInt(130), 20 + random.nextInt(130), 20 + random.nextInt(130)));
//            if (requestURI.endsWith("CheckCode")) {
//                graphics2D.drawString(rand, 13 * i + 6, 24);
//            } else {
//                graphics2D.drawString(rand, 13 * i + 6, random.nextInt(15) + 15);
//            }

//            int x = 13 * i + 6;
            int x = 20 * i;
//            int y = random.nextInt(15) + 15;
            int y = 11;
            System.out.println("rand = " + rand + "\tanchorX = " + anchorX + "\tanchorY = " + anchorY + "\tx = " + x + "\ty = " + y);
            graphics2D.drawString(rand, x, y);
        }

        request.getSession().setAttribute("RANDOM_CHECKCODE_GENERATER", sRand.toString());
        graphics2D.dispose();

        try {
            ImageIO.write(image, "PNG", response.getOutputStream());
        } catch (IOException e) {
            System.out.println("验证码生成出现错误 = " + e.getMessage());
        }
    }

    private Color getRandColor(int fc, int bc) {
        Random random = new Random();
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

    private char random2Char(int random) {
        char c = '0';
        if (random >= 0 && random <= 9) {
            c = (char)(random + 48);
        } else if (random >= 10 && random <= 35) {
            c = (char)(random + 55);
        } else if (random >= 36 && random <= 61) {
            c = (char)(random + 61);
        }
        return c;
    }
}
