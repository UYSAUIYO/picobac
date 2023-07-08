package com.yuwen303.picobac.utils;


import cn.hutool.core.util.NumberUtil;
import com.yuwen303.picobac.domain.*;
import lombok.Data;
import org.springframework.stereotype.Component;
import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.util.Util;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

@Data
@Component
public class SystemHardwareInfo implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final int OSHI_WAIT_SECOND = 1000;

    private Cpu cpu = new Cpu();
    private Mem mem = new Mem();
    private Sys sys = new Sys();
    private Jvm jvm = new Jvm();
    private List<SysFile> sysFiles = new LinkedList<>();

    public void copyTo(){
        SystemInfo si = new SystemInfo();
        HardwareAbstractionLayer hal = si.getHardware();
        //cpu信息
        setCpuInfo(hal.getProcessor());
        //内存信息
        setMemInfo(hal.getMemory());
        //系统信息
        setSysInfo();
        //java虚拟机
        setJvmInfo();
        //磁盘信息
        setSysFiles(si.getOperatingSystem());
    }
    private void setCpuInfo(CentralProcessor processor){
        //cpu信息
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
        cpu.setCpuMum(processor.getLogicalProcessorCount());
        cpu.setTotal(totalCpu);
        cpu.setSys(cSys);
        cpu.setUsed(user);
        cpu.setWait(iowait);
        cpu.setFree(idle);
    }
    private void setMemInfo(oshi.hardware.GlobalMemory memory){
        //内存信息
        mem.setTotal(memory.getTotal());
        mem.setUsed(memory.getTotal() - memory.getAvailable());
        mem.setFree(memory.getAvailable());
    }
    //系统信息
    private void setSysInfo() {
        Properties props = System.getProperties();
        sys.setComputerName(IPUtils.getHostName());
        sys.getComputerIp();
        sys.setOsName(props.getProperty("os.name"));
        sys.setOsArch(props.getProperty("os.arch"));
        sys.setUserDir(props.getProperty("user.dir"));
    }
//java虚拟机
    private void setJvmInfo(){
        Properties props = System.getProperties();
        jvm.setTotal(Runtime.getRuntime().totalMemory());
        jvm.setMax(Runtime.getRuntime().maxMemory());
        jvm.setFree(Runtime.getRuntime().freeMemory());
        jvm.setVersion(props.getProperty("java.version"));
        jvm.setHome(props.getProperty("java.home"));
    }
    //磁盘信息
    private void setSysFiles(oshi.software.os.OperatingSystem os) {
        sysFiles = new LinkedList<>();
        oshi.software.os.OSFileStore[] fsArray = os.getFileSystem().getFileStores().toArray(new oshi.software.os.OSFileStore[0]);
        for (oshi.software.os.OSFileStore fs : fsArray) {
            long free = fs.getUsableSpace();
            long total = fs.getTotalSpace();
            long used = total - free;
            SysFile sysFile = new SysFile();
            sysFile.setDirName(fs.getMount());
            sysFile.setSysTypeName(fs.getType());
            sysFile.setTypeName(fs.getName());
            sysFile.setTotal(convertFileSize(total));
            sysFile.setFree(convertFileSize(free));
            sysFile.setUsed(convertFileSize(used));
            sysFile.setUsage(NumberUtil.round(NumberUtil.mul(used / total, 100), 2).doubleValue());
            sysFiles.add(sysFile);
        }
    }
    //convertFileSize
    private String convertFileSize(long size) {
        long kb = 1024;
        long mb = kb * 1024;
        long gb = mb * 1024;
        if (size >= gb) {
            return String.format("%.1f GB", (float) size / gb);
        } else if (size >= mb) {
            float f = (float) size / mb;
            return String.format(f > 100 ? "%.0f MB" : "%.1f MB", f);
        } else if (size >= kb) {
            float f = (float) size / kb;
            return String.format(f > 100 ? "%.0f KB" : "%.1f KB", f);
        } else {
            return String.format("%d B", size);
        }
    }

}
