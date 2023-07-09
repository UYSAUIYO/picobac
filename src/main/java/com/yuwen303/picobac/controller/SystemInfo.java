package com.yuwen303.picobac.controller;

import com.alibaba.fastjson.JSONObject;
import com.yuwen303.picobac.utils.SystemInfoUtil;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.net.UnknownHostException;

/**
 * @author YuWen
 */
@Controller
@RequestMapping(value = "/system", produces = MediaType.APPLICATION_JSON_VALUE)
public class SystemInfo {
   @GetMapping("/info")
    public JSONObject getInfo() throws UnknownHostException {
        return SystemInfoUtil.getInfo();
    }
}
