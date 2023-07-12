package com.yuwen303.picobac.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yuwen303.picobac.utils.SystemInfoUtil;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.UnknownHostException;
import java.util.concurrent.*;

import static com.yuwen303.picobac.utils.SystemInfoUtil.getInfo;

/**
 * @author YuWen
 */
@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/system")
public class SystemInfoController {
    @GetMapping("/info")
    public JSONObject systemInfoUtil() throws UnknownHostException, ExecutionException, InterruptedException {
        JSONObject info = new JSONObject ();
        //创建线程池
        ExecutorService executorService = Executors.newFixedThreadPool(5);
        // 提交任务并获取Future对象
        Future<JSONObject> cpuInfoFuture = executorService.submit(SystemInfoUtil::getCpuInfo);
        Future<JSONObject> jvmInfoFuture = executorService.submit(SystemInfoUtil::getJvmInfo);
        Future<JSONObject> memInfoFuture = executorService.submit(SystemInfoUtil::getMemInfo);
        Future<JSONArray> sysFileInfoFuture = executorService.submit(SystemInfoUtil::getSysFileInfo);
        Future<JSONObject> sysInfoFuture = executorService.submit(SystemInfoUtil::getSysInfo);
        executorService.shutdown ();
        executorService.awaitTermination (Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        info.put("cpuInfo", cpuInfoFuture.get());
        info.put("jvmInfo", jvmInfoFuture.get());
        info.put("memInfo", memInfoFuture.get());
        info.put("sysFileInfo", sysFileInfoFuture.get());
        info.put("sysInfo", sysInfoFuture.get());
        return getInfo();
    }
}
