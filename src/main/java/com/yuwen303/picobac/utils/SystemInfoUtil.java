package com.yuwen303.picobac.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import org.springframework.stereotype.Component;
import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.GlobalMemory;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.software.os.FileSystem;
import oshi.software.os.OSFileStore;
import oshi.software.os.OperatingSystem;
import oshi.util.Util;

import java.net.UnknownHostException;
import java.text.DecimalFormat;
import java.net.InetAddress;
import java.util.Properties;

/**
 * @author YuWen
 * @Date 2023/07/06 1:11
 */
@Data
@Component
public class SystemInfoUtil {
    private static final int OSHI_WAIT_SECOND = 1000;
    private static final SystemInfo SYSTEM_INFO = new SystemInfo();
    private static final HardwareAbstractionLayer HARDWARE = SYSTEM_INFO.getHardware();
    private static final OperatingSystem OPERATING_SYSTEM = SYSTEM_INFO.getOperatingSystem();
    public static JSONObject getCpuInfo(){
        JSONObject cpuInfo = new JSONObject();
        CentralProcessor processor = HARDWARE.getProcessor();
        //CPU信息
        long[] prevTicks = processor.getSystemCpuLoadTicks();
        Util.sleep(OSHI_WAIT_SECOND);
        long[] ticks = processor.getSystemCpuLoadTicks();
        long nice = ticks[CentralProcessor.TickType.NICE.getIndex()] - prevTicks[CentralProcessor.TickType.NICE.getIndex()];
        long irq = ticks[CentralProcessor.TickType.IRQ.getIndex()] - prevTicks[CentralProcessor.TickType.IRQ.getIndex()];
        long softirq = ticks[CentralProcessor.TickType.SOFTIRQ.getIndex()] - prevTicks[CentralProcessor.TickType.SOFTIRQ.getIndex()];
        long steal = ticks[CentralProcessor.TickType.STEAL.getIndex()] - prevTicks[CentralProcessor.TickType.STEAL.getIndex()];
        long cSys = ticks[CentralProcessor.TickType.SYSTEM.getIndex()] - prevTicks[CentralProcessor.TickType.SYSTEM.getIndex()];
        long user = ticks[CentralProcessor.TickType.USER.getIndex()] - prevTicks[CentralProcessor.TickType.USER.getIndex()];
        long iowait = ticks[CentralProcessor.TickType.IOWAIT.getIndex()] - prevTicks[CentralProcessor.TickType.IOWAIT.getIndex()];
        long idle = ticks[CentralProcessor.TickType.IDLE.getIndex()] - prevTicks[CentralProcessor.TickType.IDLE.getIndex()];
        long totalCpu = user + nice + cSys + idle + iowait + irq + softirq + steal;
        //cpu核数
        cpuInfo.put("cpuNum", processor.getLogicalProcessorCount());
        //cpu系统使用率
        cpuInfo.put("cSys",new DecimalFormat("#.##").format(cSys * 1.0 / totalCpu));
        //cpu用户使用率
        cpuInfo.put("user",new DecimalFormat("#.##").format(user * 1.0 / totalCpu));
        //cpu当前等待率
        cpuInfo.put("iowait",new DecimalFormat("#.##").format(iowait * 1.0 / totalCpu));
        //cpu当前使用率
        cpuInfo.put("idle",new DecimalFormat("#.##").format(idle * 1.0 / totalCpu));
        System.out.println (cpuInfo);
        return cpuInfo;
    }
    public static JSONObject getJvmInfo(){
        JSONObject jvmInfo = new JSONObject();
        Properties props = System.getProperties();
        Runtime runtime = Runtime.getRuntime();
        long jvmTotalMemoryByte = runtime.totalMemory();
        long freeMemoryByte = runtime.freeMemory();
        jvmInfo.put("totalMemory",formatByte(runtime.totalMemory()) );
        jvmInfo.put("freeMemory",formatByte(runtime.freeMemory()) );
        jvmInfo.put("maxMemory",formatByte(runtime.maxMemory()) );
        jvmInfo.put("usedMemory",formatByte(jvmTotalMemoryByte - freeMemoryByte) );
        //jvm版本
        jvmInfo.put("version", props.getProperty("java.version"));
        //jvm路径
        jvmInfo.put("home", props.getProperty("java.home"));
        return jvmInfo;

    }

    /*
    * 系统内存信息*/
    public static JSONObject getMemInfo(){
        JSONObject memInfo = new JSONObject();
        GlobalMemory memory = HARDWARE.getMemory();
        long totalByte = memory.getTotal();
        long acailableByte = memory.getAvailable();
        memInfo.put("total", formatByte(totalByte));
        memInfo.put("free", formatByte(acailableByte));
        memInfo.put("used", formatByte(totalByte - acailableByte));
        memInfo.put("usage", new DecimalFormat("#.##%").format((totalByte - acailableByte) * 1.0 / totalByte));
        return memInfo;
    }

    //系统盘符信息
    public static JSONArray getSysFileInfo(){
        JSONArray sysFileInfo = new JSONArray();
        FileSystem fileSystem = OPERATING_SYSTEM.getFileSystem();
        OSFileStore[] fsArray = fileSystem.getFileStores().toArray(new OSFileStore[0]);
        for (OSFileStore fileStore:fsArray){
            JSONObject sysFile = new JSONObject();
            sysFile.put("dirName", fileStore.getMount());
            sysFile.put("sysTypeName", fileStore.getType());
            sysFile.put("typeName", fileStore.getName());
            sysFile.put("total", formatByte(fileStore.getTotalSpace()));
            sysFile.put("free", formatByte(fileStore.getUsableSpace()));
            sysFile.put("used", formatByte(fileStore.getTotalSpace() - fileStore.getUsableSpace()));
            if (fileStore.getTotalSpace() > 0) {
                sysFile.put("usage", 0);
            } else {
                double usage = (fileStore.getTotalSpace() - fileStore.getUsableSpace()) * 1.0 / fileStore.getTotalSpace();
                sysFile.put("usage", new DecimalFormat("#.##%").format(usage));
            }
            sysFileInfo.add(sysFile);
        }
        return sysFileInfo;
    }

    //系统信息
    public static JSONObject getSysInfo() throws UnknownHostException {
        JSONObject sysInfo = new JSONObject();
        Properties props = System.getProperties();
        sysInfo.put("computerName", InetAddress.getLocalHost().getHostName());
        sysInfo.put("computerIp", InetAddress.getLocalHost().getHostAddress());
        sysInfo.put("osName", props.getProperty("os.name"));
        sysInfo.put("osArch", props.getProperty("os.arch"));
        sysInfo.put("osVersion", props.getProperty("os.version"));
        sysInfo.put("computerIP", InetAddress.getLocalHost().getHostAddress());
        //项目目录
        sysInfo.put("userDir", props.getProperty("user.dir"));
        return sysInfo;

    }
    //所有系统信息
    public static JSONObject getInfo() throws UnknownHostException {
        JSONObject info = new JSONObject();
        info.put("cpuInfo", getCpuInfo());
        info.put("jvmInfo", getJvmInfo());
        info.put("memInfo", getMemInfo());
        info.put("sysFileInfo", getSysFileInfo());
        info.put("sysInfo", getSysInfo());
//      System.out.println(info);
        return info;
    }
    /*
    * 单位换算*/
    public static String formatByte(long byteNumber){
        double format = 1024.0;
        double kbNumber = byteNumber / format;
        if(kbNumber < format){
            return new DecimalFormat("#.##KB").format(kbNumber);
        }
        double mbNumber = kbNumber / format;
        if(mbNumber < format){
            return new DecimalFormat("#.##MB").format(mbNumber);
        }
        double gbNumber = mbNumber / format;
        if(gbNumber < format){
            return new DecimalFormat("#.##GB").format(gbNumber);
        }
        double tbNumber = gbNumber / format;
        return new DecimalFormat("#.##TB").format(tbNumber);

    }
}
