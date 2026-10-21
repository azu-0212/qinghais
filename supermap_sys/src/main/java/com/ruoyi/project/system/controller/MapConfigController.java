package com.ruoyi.project.system.controller;

import java.util.List;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.framework.aspectj.lang.annotation.Log;
import com.ruoyi.framework.aspectj.lang.enums.BusinessType;
import com.ruoyi.project.system.domain.MapConfig;
import com.ruoyi.project.system.service.IMapConfigService;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.framework.web.page.TableDataInfo;

/**
 * 地图配置Controller
 * 
 * @author ruoyi
 * @date 2020-06-28
 */
@RestController
@RequestMapping("/system/mapConfig")
public class MapConfigController extends BaseController
{
    @Autowired
    private IMapConfigService mapConfigService;

    /**
     * 查询地图配置列表
     */
    //@PreAuthorize("@ss.hasPermi('system:mapConfig:list')")
    @GetMapping("/list")
    public TableDataInfo list(MapConfig mapConfig)
    {
        startPage();
        List<MapConfig> list = mapConfigService.selectMapConfigList(mapConfig);
        return getDataTable(list);
    }

    /**
     * 导出地图配置列表
     */
    @PreAuthorize("@ss.hasPermi('system:mapConfig:export')")
    @Log(title = "地图配置", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(MapConfig mapConfig)
    {
        List<MapConfig> list = mapConfigService.selectMapConfigList(mapConfig);
        ExcelUtil<MapConfig> util = new ExcelUtil<MapConfig>(MapConfig.class);
        return util.exportExcel(list, "mapConfig");
    }

    /**
     * 获取地图配置详细信息
     */
    //@PreAuthorize("@ss.hasPermi('system:mapConfig:query')")
    @GetMapping(value = "/{configId}")
    public AjaxResult getInfo(@PathVariable("configId") Long configId)
    {
        return AjaxResult.success(mapConfigService.selectMapConfigById(configId));
    }

    /**
     * 新增地图配置
     */
    @PreAuthorize("@ss.hasPermi('system:mapConfig:add')")
    @Log(title = "地图配置", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody MapConfig mapConfig)
    {
        return toAjax(mapConfigService.insertMapConfig(mapConfig));
    }

    /**
     * 修改地图配置
     */
    @PreAuthorize("@ss.hasPermi('system:mapConfig:edit')")
    @Log(title = "地图配置", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody MapConfig mapConfig)
    {
        return toAjax(mapConfigService.updateMapConfig(mapConfig));
    }

    /**
     * 删除地图配置
     */
    @PreAuthorize("@ss.hasPermi('system:mapConfig:remove')")
    @Log(title = "地图配置", businessType = BusinessType.DELETE)
	@DeleteMapping("/{configIds}")
    public AjaxResult remove(@PathVariable Long[] configIds)
    {
        return toAjax(mapConfigService.deleteMapConfigByIds(configIds));
    }
}
