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
import com.ruoyi.project.system.domain.SysVideo;
import com.ruoyi.project.system.service.ISysVideoService;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.framework.web.page.TableDataInfo;

/**
 * 视频管理Controller
 *
 * @author ruoyi
 * @date 2021-03-24
 */
@RestController
@RequestMapping("/system/video")
public class SysVideoController extends BaseController
{
    @Autowired
    private ISysVideoService sysVideoService;

    /**
     * 查询视频管理列表
     */
    //@PreAuthorize("@ss.hasPermi('system:video:list')")
    @GetMapping("/list")
    public TableDataInfo list(SysVideo sysVideo)
    {
        startPage();
        List<SysVideo> list = sysVideoService.selectSysVideoList(sysVideo);
        return getDataTable(list);
    }

    /**
     * 根据类型查询视频管理列表
     */
    //@PreAuthorize("@ss.hasPermi('system:video:list')")
    @GetMapping("/type/{type}")
    public List getListByType(@PathVariable("type") Long type)
    {
        List<SysVideo> list = sysVideoService.selectSysVideoListByType(type);
        return list;
    }

    /**
     * 导出视频管理列表
     */
    @PreAuthorize("@ss.hasPermi('system:video:export')")
    @Log(title = "视频管理", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(SysVideo sysVideo)
    {
        List<SysVideo> list = sysVideoService.selectSysVideoList(sysVideo);
        ExcelUtil<SysVideo> util = new ExcelUtil<SysVideo>(SysVideo.class);
        return util.exportExcel(list, "video");
    }

    /**
     * 获取视频管理详细信息
     */
    //@PreAuthorize("@ss.hasPermi('system:video:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(sysVideoService.selectSysVideoById(id));
    }

    /**
     * 新增视频管理
     */
    @PreAuthorize("@ss.hasPermi('system:video:add')")
    @Log(title = "视频管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SysVideo sysVideo)
    {
        return toAjax(sysVideoService.insertSysVideo(sysVideo));
    }

    /**
     * 修改视频管理
     */
    @PreAuthorize("@ss.hasPermi('system:video:edit')")
    @Log(title = "视频管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SysVideo sysVideo)
    {
        return toAjax(sysVideoService.updateSysVideo(sysVideo));
    }

    /**
     * 删除视频管理
     */
    @PreAuthorize("@ss.hasPermi('system:video:remove')")
    @Log(title = "视频管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(sysVideoService.deleteSysVideoByIds(ids));
    }
}
