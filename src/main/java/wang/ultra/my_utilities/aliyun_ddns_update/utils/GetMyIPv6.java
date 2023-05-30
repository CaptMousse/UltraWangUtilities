package wang.ultra.my_utilities.aliyun_ddns_update.utils;

import wang.ultra.my_utilities.common.constant.ConstantFromFile;

import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Collections;
import java.util.Enumeration;

public class GetMyIPv6 {

    /**
     * 获取本机IPv6地址
     * @return  String
     */
    public static String getIPv6() {
        String[] arrString = null;

        String address;
        try {
            address = getIPv6Address();
//            System.out.println("address = " + address);
            if (address != null) {
                arrString = address.split("%");
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }

        if (arrString != null) { 
            for (String s : arrString) {
//                System.out.println(s);
                if (!s.equals(ConstantFromFile.getEthernetName())) {    // 不等于网卡名
//                    System.out.println("IPv6 = " + s);
                    return s;
                }
            }
        }

        return "-1";
    }

    private static String getIPv6Address() throws SocketException {
        Enumeration<NetworkInterface> netInts = NetworkInterface.getNetworkInterfaces();
        for (NetworkInterface netInt : Collections.list(netInts)) {
            if (netInt.isUp() && !netInt.isLoopback()) {
                for (InetAddress inetAddress : Collections.list(netInt.getInetAddresses())) {
                    if (inetAddress.isLoopbackAddress() || inetAddress.isLinkLocalAddress() || inetAddress.isMulticastAddress()) {
                        continue;
                    }
                    if (inetAddress instanceof Inet6Address) {
                        return inetAddress.getHostAddress();
                    }
                }
            }
        }
        return null;
    }

    public static void main(String[] args) {
        System.out.println(getIPv6());
    }
}
