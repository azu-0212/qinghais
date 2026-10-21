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
import com.ruoyi.project.system.domain.SysUnit;
import com.ruoyi.project.system.service.ISysUnitService;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.framework.web.page.TableDataInfo;

/**
 * 地理单元Controller
 *
 * @author ruoyi
 * @date 2021-03-25
 */
@RestController
@RequestMapping("/system/unit")
public class SysUnitController extends BaseController
{
    @Autowired
    private ISysUnitService sysUnitService;

    /**
     * 查询地理单元列表
     */
    //@PreAuthorize("@ss.hasPermi('system:unit:list')")
    @GetMapping("/list")
    public TableDataInfo list(SysUnit sysUnit)
    {
        startPage();
        List<SysUnit> list = sysUnitService.selectSysUnitList(sysUnit);
        return getDataTable(list);
    }

    /**
     * 导出地理单元列表
     */
    @PreAuthorize("@ss.hasPermi('system:unit:export')")
    @Log(title = "地理单元", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(SysUnit sysUnit)
    {
        List<SysUnit> list = sysUnitService.selectSysUnitList(sysUnit);
        ExcelUtil<SysUnit> util = new ExcelUtil<SysUnit>(SysUnit.class);
        return util.exportExcel(list, "unit");
    }

    /**
     * 获取地理单元详细信息
     */
    //@PreAuthorize("@ss.hasPermi('system:unit:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(sysUnitService.selectSysUnitById(id));
    }

    /**
     * 新增地理单元
     */
    @PreAuthorize("@ss.hasPermi('system:unit:add')")
    @Log(title = "地理单元", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SysUnit sysUnit)
    {
        System.out.println("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXX  "+sysUnit);
        return toAjax(sysUnitService.insertSysUnit(sysUnit));
    }

    /**
     * 修改地理单元
     */
    @PreAuthorize("@ss.hasPermi('system:unit:edit')")
    @Log(title = "地理单元", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SysUnit sysUnit)
    {
        return toAjax(sysUnitService.updateSysUnit(sysUnit));
    }

    /**
     * 删除地理单元
     */
    @PreAuthorize("@ss.hasPermi('system:unit:remove')")
    @Log(title = "地理单元", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(sysUnitService.deleteSysUnitByIds(ids));
    }
}
