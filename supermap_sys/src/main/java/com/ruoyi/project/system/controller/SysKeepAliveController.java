package com.ruoyi.project.system.controller;

import com.ruoyi.common.utils.ServletUtils;
import com.ruoyi.framework.security.LoginUser;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.project.system.domain.SysUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

/**
 * 心跳保活
 *
 * @author Hash
 */
@RestController
@RequestMapping("/keepAlvie")
public class SysKeepAliveController {
    /**
     * 心跳保活
     * @return 心跳保活结果
     */
    @GetMapping("/send")
    public AjaxResult getInfo()
    {
        AjaxResult ajax = AjaxResult.success();
        ajax.put("Alvie", true);
        return ajax;
    }
}
