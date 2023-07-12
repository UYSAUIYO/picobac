package com.yuwen303.picobac.controller;

import com.alibaba.fastjson.JSONObject;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.UnknownHostException;

import static com.yuwen303.picobac.utils.SystemInfoUtil.getInfo;
import static com.yuwen303.picobac.utils.SystemInfoUtil.reGetInfo;

/**
 * @author YuWen
 */
@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/system")
public class SystemInfoController {
    @GetMapping("/info")
    public JSONObject systemInfoUtil() throws UnknownHostException {
        return getInfo();
    }
    @GetMapping("/regetinfo")
    public JSONObject reInfo(){
        return reGetInfo();
    }
}
