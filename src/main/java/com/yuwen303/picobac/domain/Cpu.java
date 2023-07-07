package com.yuwen303.picobac.domain;

import cn.hutool.core.util.NumberUtil;
import lombok.Data;

import java.io.Serializable;

@Data
public class Cpu implements Serializable {
    private static final long serialVersionUID = 1L;
    //核心数
    private int cpuMum;
    //cpu使用率
    private double total;
    //cpu系统使用率
    private double sys;
    //cpu用户使用率
    private double used;
    //cpu当前等待率
    private double wait;
    //cpu当前空闲率
    private double free;

    public double getTotal() {
        return NumberUtil.round(NumberUtil.mul(total, 100), 2).doubleValue();
    }

    public double getSys() {
        return NumberUtil.round(NumberUtil.mul(sys / total, 100), 2).doubleValue();
    }

    public double getUsed() {
        return NumberUtil.round(NumberUtil.mul(used / total, 100), 2).doubleValue();
    }

    public double getWait() {
        return NumberUtil.round(NumberUtil.mul(wait / total, 100), 2).doubleValue();
    }

    public double getFree() {
        return NumberUtil.round(NumberUtil.mul(free / total, 100), 2).doubleValue();
    }

}
