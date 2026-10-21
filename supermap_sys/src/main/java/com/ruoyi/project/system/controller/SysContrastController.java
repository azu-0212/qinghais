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
import com.ruoyi.project.system.domain.SysContrast;
import com.ruoyi.project.system.service.ISysContrastService;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.framework.web.page.TableDataInfo;

/**
 * 影像对比Controller
 *
 * @author ruoyi
 * @date 2021-04-01
 */
@RestController
@RequestMapping("/system/contrast")
public class SysContrastController extends BaseController
{
    @Autowired
    private ISysContrastService sysContrastService;

    /**
     * 查询影像对比列表
     */
    //@PreAuthorize("@ss.hasPermi('system:contrast:list')")
    @GetMapping("/list")
    public TableDataInfo list(SysContrast sysContrast)
    {
        startPage();
        List<SysContrast> list = sysContrastService.selectSysContrastList(sysContrast);
        return getDataTable(list);
    }

    /**
     * 根据类型查询影像对比列表
     */
    //@PreAuthorize("@ss.hasPermi('system:contrast:list')")
    @GetMapping(value = "/list/{pid}/data/{dataType}")
    public TableDataInfo list(@PathVariable("pid") Long pid, @PathVariable("dataType") Long dataType)
    {
        startPage();
        List<SysContrast> list = sysContrastService.selectSysContrastListByType(pid, dataType);
        return getDataTable(list);
    }

    /**
     * 导出影像对比列表
     */
    @PreAuthorize("@ss.hasPermi('system:contrast:export')")
    @Log(title = "影像对比", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(SysContrast sysContrast)
    {
        List<SysContrast> list = sysContrastService.selectSysContrastList(sysContrast);
        ExcelUtil<SysContrast> util = new ExcelUtil<SysContrast>(SysContrast.class);
        return util.exportExcel(list, "contrast");
    }

    /**
     * 获取影像对比详细信息
     */
    //@PreAuthorize("@ss.hasPermi('system:contrast:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(sysContrastService.selectSysContrastById(id));
    }

    /**
     * 新增影像对比
     */
    @PreAuthorize("@ss.hasPermi('system:contrast:add')")
    @Log(title = "影像对比", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SysContrast sysContrast)
    {
        return toAjax(sysContrastService.insertSysContrast(sysContrast));
    }

    /**
     * 修改影像对比
     */
    @PreAuthorize("@ss.hasPermi('system:contrast:edit')")
    @Log(title = "影像对比", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SysContrast sysContrast)
    {
        return toAjax(sysContrastService.updateSysContrast(sysContrast));
    }

    /**
     * 删除影像对比
     */
    @PreAuthorize("@ss.hasPermi('system:contrast:remove')")
    @Log(title = "影像对比", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(sysContrastService.deleteSysContrastByIds(ids));
    }
}
