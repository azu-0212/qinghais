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
import com.ruoyi.project.system.domain.BusinessaDministrativeDivision;
import com.ruoyi.project.system.service.IBusinessaDministrativeDivisionService;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;

/**
 * 行政区划Controller
 *
 * @author ruoyi
 * @date 2020-06-19
 */
@RestController
@RequestMapping("/system/division")
public class BusinessaDministrativeDivisionController extends BaseController
{
    @Autowired
    private IBusinessaDministrativeDivisionService businessaDministrativeDivisionService;

    /**
     * 查询行政区划列表
     */
    //@PreAuthorize("@ss.hasPermi('system:division:list')")
    @GetMapping("/list")
    public AjaxResult list(BusinessaDministrativeDivision businessaDministrativeDivision)
    {
        List<BusinessaDministrativeDivision> list = businessaDministrativeDivisionService.selectBusinessaDministrativeDivisionList(businessaDministrativeDivision);
        return AjaxResult.success(list);
    }

    /**
     * 查询行政区划子父级信息--关键字搜索
     */
    @GetMapping("/search")
    public AjaxResult searchList()
    {
        List<BusinessaDministrativeDivision> list = businessaDministrativeDivisionService.selectBusinessaDministrativeDivisionSearchList();
        return AjaxResult.success(list);
    }

    /**
     * 导出行政区划列表
     */
    @PreAuthorize("@ss.hasPermi('system:division:export')")
    @Log(title = "行政区划", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(BusinessaDministrativeDivision businessaDministrativeDivision)
    {
        List<BusinessaDministrativeDivision> list = businessaDministrativeDivisionService.selectBusinessaDministrativeDivisionList(businessaDministrativeDivision);
        ExcelUtil<BusinessaDministrativeDivision> util = new ExcelUtil<BusinessaDministrativeDivision>(BusinessaDministrativeDivision.class);
        return util.exportExcel(list, "division");
    }

    /**
     * 获取行政区划详细信息
     */
    //@PreAuthorize("@ss.hasPermi('system:division:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(businessaDministrativeDivisionService.selectBusinessaDministrativeDivisionById(id));
    }

    /**
     * 新增行政区划
     */
    @PreAuthorize("@ss.hasPermi('system:division:add')")
    @Log(title = "行政区划", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody BusinessaDministrativeDivision businessaDministrativeDivision)
    {
        return toAjax(businessaDministrativeDivisionService.insertBusinessaDministrativeDivision(businessaDministrativeDivision));
    }

    /**
     * 修改行政区划
     */
    @PreAuthorize("@ss.hasPermi('system:division:edit')")
    @Log(title = "行政区划", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody BusinessaDministrativeDivision businessaDministrativeDivision)
    {
        return toAjax(businessaDministrativeDivisionService.updateBusinessaDministrativeDivision(businessaDministrativeDivision));
    }

    /**
     * 删除行政区划
     */
    @PreAuthorize("@ss.hasPermi('system:division:remove')")
    @Log(title = "行政区划", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(businessaDministrativeDivisionService.deleteBusinessaDministrativeDivisionByIds(ids));
    }



    @GetMapping("/getByPid")
    public List<BusinessaDministrativeDivision> getByPid(@PathVariable Long parentId){
        return businessaDministrativeDivisionService.getDivitionByPid(parentId);
    }


    @GetMapping("/getAll")
    public List<BusinessaDministrativeDivision> getAll(){
        return businessaDministrativeDivisionService.getAll();
    }

}
