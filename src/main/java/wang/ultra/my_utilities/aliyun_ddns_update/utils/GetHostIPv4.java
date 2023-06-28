package wang.ultra.my_utilities.aliyun_ddns_update.utils;

import wang.ultra.my_utilities.common.utils.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 遍历四个网站返回的页面, 获取本地公网IP
 */
public class GetHostIPv4 {

    private String getHostIPv4_1() throws IOException {
        String ip = null;
        String chinaz = "http://ip.chinaz.com";
        StringBuilder inputLine = new StringBuilder();
        String read = "";
        URL url = null;
        HttpURLConnection urlConnection = null;
        BufferedReader in = null;
        try {
            url = new URL(chinaz);
            urlConnection = (HttpURLConnection) url.openConnection();
            in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), StandardCharsets.UTF_8));
            while ((read = in.readLine()) != null) {
                inputLine.append(read).append("\r\n");
            }
            Pattern p = Pattern.compile("\\<dd class\\=\"fz24\">(.*?)\\<\\/dd>");
            Matcher m = p.matcher(inputLine.toString());
            if (m.find()) {
                ip = m.group(1);
            }
        } finally {
            if (in != null) {
                in.close();
            }
        }
        if (StringUtils.isEmpty(ip)) {
            throw new RuntimeException();
        }
        return ip;
    }

    private String getHostIPv4_2() throws IOException {
        String ip;
        BufferedReader br = null;
        try {
            URL url = new URL("https://v6r.ipip.net/?format=callback");
            br = new BufferedReader(new InputStreamReader(url.openStream()));
            String s;
            StringBuilder sb = new StringBuilder();
            String webContent = "";
            while ((s = br.readLine()) != null) {
                sb.append(s).append("\r\n");
            }
            webContent = sb.toString();
            int start = webContent.indexOf("(") + 2;
            int end = webContent.indexOf(")") - 1;
            webContent = webContent.substring(start, end);
            ip = webContent;
        } finally {
            if (br != null)
                br.close();
        }
        if (StringUtils.isEmpty(ip)) {
            throw new RuntimeException();
        }
        return ip;
    }

    private String getHostIPv4_3() throws IOException {
        String ip = null;
        String objWebURL = "https://ip.900cha.com/";
        BufferedReader br = null;
        try {
            URL url = new URL(objWebURL);
            br = new BufferedReader(new InputStreamReader(url.openStream()));
            String s;
            while ((s = br.readLine()) != null) {
                if (s.contains("我的IP:")) {
                    ip = s.substring(s.indexOf(":") + 2);
                    break;
                }
            }
        } finally {
            if (br != null)
                br.close();
        }
        if (StringUtils.isEmpty(ip)) {
            throw new RuntimeException();
        }
        return ip;
    }

    private String getHostIPv4_4() throws IOException {
        String ip = null;
        String objWebURL = "https://bajiu.cn/ip/";
        BufferedReader br = null;
        try {
            URL url = new URL(objWebURL);
            br = new BufferedReader(new InputStreamReader(url.openStream()));
            String s = "";
            String webContent = "";
            while ((s = br.readLine()) != null) {
                if (s.contains("互联网IP")) {
                    ip = s.substring(s.indexOf("'") + 1, s.lastIndexOf("'"));
                    break;
                }
            }
        } finally {
            if (br != null)
                br.close();
        }
        if (StringUtils.isEmpty(ip)) {
            throw new RuntimeException();
        }
        return ip;
    }

    public String getHostIPv4() {

        String ip;



        // 第二种方式
        try {
            ip = this.getHostIPv4_2();
            ip.trim();
            if (!StringUtils.isEmpty(ip)) {
                return ip;
            }
        } catch (Exception ignored) {
        }

        // 第三种方式
        try {
            ip = this.getHostIPv4_3();
            ip.trim();
            if (!StringUtils.isEmpty(ip)) {
                return ip;
            }
        } catch (Exception ignored) {
        }

        // 第四种方式
        try {
            ip = this.getHostIPv4_4();
            ip.trim();
            if (!StringUtils.isEmpty(ip)) {
                return ip;
            }
        } catch (Exception ignored) {
        }

        // 第一种方式测试失败了, 挪到最后执行
        try {
            ip = this.getHostIPv4_1();
            ip.trim();
            if (!StringUtils.isEmpty(ip)) {
                return ip;
            }
        } catch (Exception ignored) {
        }

        System.out.println("获取IPv4地址失败");

        return "-1";
    }
}
