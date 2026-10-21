package com.ruoyi.project.system.controller;
import com.actionsoft.bpms.api.OpenApiClient;
import com.actionsoft.sdk.service.response.StringResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/authenlogin")
public class SysAuthencationLoginController {

    @GetMapping(value = "/param/{username}/{password}")
    public String getAuthencationResult(@PathVariable("username") String username,@PathVariable("password") String password){
        String apiServer = "http://10.63.101.4:8088/portal/api";
//        String apiServer = "http://221.207.18.113:8088/portal/api";
        String apiMethod = "portal.session.create";
        String accessKey = "62a7afa0";
        String secret = "918933b10464";
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("uid", username);
        map.put("pwd", password);
        OpenApiClient client = new OpenApiClient(apiServer, accessKey, secret);
        StringResponse r = client.exec(apiMethod, map, StringResponse.class);
        return r.getData();
        //return "username-------------"+username+"----password-------"+password;
    }
}
