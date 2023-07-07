package com.yuwen303.picobac.domain;

import lombok.Data;

import java.io.Serializable;
import java.net.InetAddress;
import java.net.UnknownHostException;

@Data
public class Sys implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 服务器名称
     */
    private String computerName;

    /**
     * 服务器Ip
     */
    public String getComputerIp() {
        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
            return "Unknown";
        }
    }

    /**
     * 项目路径
     */
    private String userDir;

    /**
     * 操作系统
     */
    private String osName;

    /**
     * 系统架构
     */
    private String osArch;


}
