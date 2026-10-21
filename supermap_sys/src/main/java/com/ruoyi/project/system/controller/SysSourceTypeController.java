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
import com.ruoyi.project.system.domain.SysSourceType;
import com.ruoyi.project.system.service.ISysSourceTypeService;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.framework.web.page.TableDataInfo;

/**
 * 来源类型Controller
 *
 * @author ruoyi
 * @date 2021-05-05
 */
@RestController
@RequestMapping("/system/source")
public class SysSourceTypeController extends BaseController
{
    @Autowired
    private ISysSourceTypeService sysSourceTypeService;

    /**
     * 查询来源类型列表
     */
    //@PreAuthorize("@ss.hasPermi('system:source:list')")
    @GetMapping("/list")
    public TableDataInfo list(SysSourceType sysSourceType)
    {
        startPage();
        List<SysSourceType> list = sysSourceTypeService.selectSysSourceTypeList(sysSourceType);
        return getDataTable(list);
    }

    /**
     * 查询来源类型所有列表
     */
    @GetMapping("/all")
    public TableDataInfo list()
    {
        startPage();
        List<SysSourceType> list = sysSourceTypeService.selectSysSourceTypeListAll();
        return getDataTable(list);
    }

    /**
     * 导出来源类型列表
     */
    @PreAuthorize("@ss.hasPermi('system:source:export')")
    @Log(title = "来源类型", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(SysSourceType sysSourceType)
    {
        List<SysSourceType> list = sysSourceTypeService.selectSysSourceTypeList(sysSourceType);
        ExcelUtil<SysSourceType> util = new ExcelUtil<SysSourceType>(SysSourceType.class);
        return util.exportExcel(list, "source");
    }

    /**
     * 获取来源类型详细信息
     */
    //@PreAuthorize("@ss.hasPermi('system:source:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(sysSourceTypeService.selectSysSourceTypeById(id));
    }

    /**
     * 新增来源类型
     */
    @PreAuthorize("@ss.hasPermi('system:source:add')")
    @Log(title = "来源类型", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SysSourceType sysSourceType)
    {
        return toAjax(sysSourceTypeService.insertSysSourceType(sysSourceType));
    }

    /**
     * 修改来源类型
     */
    @PreAuthorize("@ss.hasPermi('system:source:edit')")
    @Log(title = "来源类型", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SysSourceType sysSourceType)
    {
        return toAjax(sysSourceTypeService.updateSysSourceType(sysSourceType));
    }

    /**
     * 删除来源类型
     */
    @PreAuthorize("@ss.hasPermi('system:source:remove')")
    @Log(title = "来源类型", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(sysSourceTypeService.deleteSysSourceTypeByIds(ids));
    }
}
