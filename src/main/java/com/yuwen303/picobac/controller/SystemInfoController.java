package com.yuwen303.picobac.controller;

import com.alibaba.fastjson.JSONObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.UnknownHostException;

import static com.yuwen303.picobac.utils.SystemInfoUtil.getInfo;

/**
 * @author YuWen
 */
@RestController
@RequestMapping("/system")
public class SystemInfoController {
    @GetMapping("/info")
    public JSONObject systemInfoUtil() throws UnknownHostException {
        return getInfo();
    }
}
