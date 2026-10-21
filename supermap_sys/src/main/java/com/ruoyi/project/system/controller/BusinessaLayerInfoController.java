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
import com.ruoyi.project.system.domain.BusinessaLayerInfo;
import com.ruoyi.project.system.service.IBusinessaLayerInfoService;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.framework.web.page.TableDataInfo;

/**
 * 三维图层资源Controller
 *
 * @author ruoyi
 * @date 2020-06-18
 */
@RestController
@RequestMapping("/system/info")
public class BusinessaLayerInfoController extends BaseController
{
    @Autowired
    private IBusinessaLayerInfoService businessaLayerInfoService;

    /**
     * 查询三维图层资源列表
     */
    //@PreAuthorize("@ss.hasPermi('system:info:list')")
    @GetMapping("/list")
    public TableDataInfo list(BusinessaLayerInfo businessaLayerInfo)
    {
        startPage();
        List<BusinessaLayerInfo> list = businessaLayerInfoService.selectBusinessaLayerInfoList(businessaLayerInfo);
        return getDataTable(list);
    }

    /**
     * 导出三维图层资源列表
     */
    @PreAuthorize("@ss.hasPermi('system:info:export')")
    @Log(title = "三维图层资源", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(BusinessaLayerInfo businessaLayerInfo)
    {
        List<BusinessaLayerInfo> list = businessaLayerInfoService.selectBusinessaLayerInfoList(businessaLayerInfo);
        ExcelUtil<BusinessaLayerInfo> util = new ExcelUtil<BusinessaLayerInfo>(BusinessaLayerInfo.class);
        return util.exportExcel(list, "info");
    }

    /**
     * 获取三维图层资源详细信息
     */
    //@PreAuthorize("@ss.hasPermi('system:info:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(businessaLayerInfoService.selectBusinessaLayerInfoById(id));
    }

    /**
     * 新增三维图层资源
     */
    @PreAuthorize("@ss.hasPermi('system:info:add')")
    @Log(title = "三维图层资源", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody BusinessaLayerInfo businessaLayerInfo)
    {
        return toAjax(businessaLayerInfoService.insertBusinessaLayerInfo(businessaLayerInfo));
    }

    /**
     * 修改三维图层资源
     */
    @PreAuthorize("@ss.hasPermi('system:info:edit')")
    @Log(title = "三维图层资源", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody BusinessaLayerInfo businessaLayerInfo)
    {
        return toAjax(businessaLayerInfoService.updateBusinessaLayerInfo(businessaLayerInfo));
    }

    /**
     * 删除三维图层资源
     */
    @PreAuthorize("@ss.hasPermi('system:info:remove')")
    @Log(title = "三维图层资源", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(businessaLayerInfoService.deleteBusinessaLayerInfoByIds(ids));
    }

    @GetMapping("/getLayerArea")
    public TableDataInfo getLayerArea()
    {
        List<BusinessaLayerInfo> list = businessaLayerInfoService.selectLayerArea();
        return getDataTable(list);
    }

    @GetMapping("/count")
    public AjaxResult countInfo(){
        return AjaxResult.success(businessaLayerInfoService.countInfo());
    }

}
