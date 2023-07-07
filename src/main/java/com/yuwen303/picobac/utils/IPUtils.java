package com.yuwen303.picobac.utils;

import java.net.InetAddress;

public class IPUtils {
    //获取主机名
    static InetAddress ia;

    public static String getHostName() {
        return ia.getHostName();
    }

}
