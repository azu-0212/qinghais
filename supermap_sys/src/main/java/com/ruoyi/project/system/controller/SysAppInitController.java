
package com.ruoyi.project.system.controller;

import com.github.pagehelper.PageInfo;
import com.ruoyi.common.constant.HttpStatus;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.framework.web.page.TableDataInfo;
import com.ruoyi.project.system.domain.MapConfig;
import com.ruoyi.project.system.service.IMapConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 心跳保活
 *
 * @author Hash
 */
@RestController
public class SysAppInitController {

    @Autowired
    private IMapConfigService mapConfigService;

    /**
     * 查询地图配置列表
     */
    @GetMapping("/baseCfg")
    public AjaxResult getBaseCfg()
    {
        MapConfig mapConfig = mapConfigService.selectMapConfigById(-1L);
        if(mapConfig!=null){
            return AjaxResult.success("查询成功",mapConfig);
        }
        return AjaxResult.error("查询失败",null);
    }
}
