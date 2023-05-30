package wang.ultra.my_utilities.common.utils;




import wang.ultra.my_utilities.common.constant.ConstantFromFile;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Properties;


import javax.mail.*;
import javax.mail.internet.*;

public class SendMailUtils {

    public String sendMail(String mailTo, String mailSubject, String mailContent) {

//        String mailHost = "smtp.qq.com";
//        String mailTransportProtocol = "smtp";
//        String mailSmtpPort = "smtp";
//        String mailFrom = "1435644267@qq.com";
//        String mailFromAutoCode = "cyaccluykuqsbacc";

        String mailHost = ConstantFromFile.getMailHost();
        String mailTransportProtocol = ConstantFromFile.getMailTransportProtocol();
        String mailSmtpPort = ConstantFromFile.getMailSmtpPort();
        String mailFrom = ConstantFromFile.getMailFrom();
        String mailFromAutoCode = ConstantFromFile.getMailFromAuthCode();



        //设置邮件协议和端口
        Properties properties = new Properties();
        properties.setProperty("mail.transport.protocol", mailTransportProtocol);
        properties.setProperty("mail.host", mailHost);
        properties.setProperty("mail.smtp.port", mailSmtpPort);

        Session session = Session.getInstance(properties, null);
//        session.setDebug(true);
        MimeMessage mimeMessage = new MimeMessage(session);

        try {
            // 发件人
            String mailFromName = MimeUtility.encodeWord("A Farmer's World") + " <" + mailFrom + ">";
            InternetAddress internetAddressMailFromName = new InternetAddress(mailFromName);
            mimeMessage.setFrom(internetAddressMailFromName);
            // 收件人
            InternetAddress internetAddressMailTo = new InternetAddress(mailTo);
            mimeMessage.setRecipient(Message.RecipientType.TO, internetAddressMailTo);

            // 邮件主题
            mimeMessage.setSubject(mailSubject);
            // 邮件正文
            mimeMessage.setContent(mailContent, "text/html;charset=UTF-8");
            // 发送时间
            mimeMessage.setSentDate(new Date());

            Transport transport = session.getTransport();
            // 验证用户名和密码
            transport.connect(mailHost, mailFrom, mailFromAutoCode);

            System.out.println("mailFrom = " + mailFrom);
            System.out.println("mailTo = " + mailTo);
            System.out.println("mailSubject = " + mailSubject);

            // 发送邮件
            transport.sendMessage(mimeMessage, mimeMessage.getAllRecipients());

            return "Mail Successful Send to " + mailTo;
        } catch (MessagingException | UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }
//
//    public static void main(String[] args) {
//
//        String mailTo = ConstantFromFile.getMailTo();
//        String mailSubject = "【UltraWang监控提醒】在" + DateConverter.getSimpleTime() + "就是想给你发个提醒";
//        String mailContent = "<h1 style=\"text-align: center;\">歪比巴卜</h1>";
//        SendMailUtils sendMailUtils = new SendMailUtils();
//        sendMailUtils.sendMail(mailTo, mailSubject, mailContent);
//    }
}
