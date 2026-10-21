package com.ruoyi.project.system.controller;

import java.io.IOException;
import java.util.List;

import com.ruoyi.common.utils.file.FileUploadUtils;
import com.ruoyi.framework.config.RuoYiConfig;
import com.ruoyi.project.system.domain.FlightFile;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.ruoyi.framework.aspectj.lang.annotation.Log;
import com.ruoyi.framework.aspectj.lang.enums.BusinessType;
import com.ruoyi.project.system.domain.BusinessaKeyAreas;
import com.ruoyi.project.system.service.IBusinessaKeyAreasService;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.framework.web.page.TableDataInfo;
import org.springframework.web.multipart.MultipartFile;

import static org.apache.poi.poifs.crypt.CipherAlgorithm.des;

/**
 * 重点区域定位Controller
 *
 * @author ruoyi
 * @date 2020-06-18
 */
@RestController
@RequestMapping("/system/areas")
public class BusinessaKeyAreasController extends BaseController
{
    @Autowired
    private IBusinessaKeyAreasService businessaKeyAreasService;

    /**
     * 查询重点区域定位列表
     */
    //@PreAuthorize("@ss.hasPermi('system:areas:list')")
    @GetMapping("/list")
    public TableDataInfo list(BusinessaKeyAreas businessaKeyAreas)
    {
        startPage();
        List<BusinessaKeyAreas> list = businessaKeyAreasService.selectBusinessaKeyAreasList(businessaKeyAreas);
        return getDataTable(list);
    }

    /**
     * 导出重点区域定位列表
     */
    @PreAuthorize("@ss.hasPermi('system:areas:export')")
    @Log(title = "重点区域定位", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(BusinessaKeyAreas businessaKeyAreas)
    {
        List<BusinessaKeyAreas> list = businessaKeyAreasService.selectBusinessaKeyAreasList(businessaKeyAreas);
        ExcelUtil<BusinessaKeyAreas> util = new ExcelUtil<BusinessaKeyAreas>(BusinessaKeyAreas.class);
        return util.exportExcel(list, "areas");
    }

    /**
     * 获取重点区域定位详细信息
     */
    //@PreAuthorize("@ss.hasPermi('system:areas:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(businessaKeyAreasService.selectBusinessaKeyAreasById(id));
    }

    /**
     * 新增重点区域定位
     */
    @PreAuthorize("@ss.hasPermi('system:areas:add')")
    @Log(title = "重点区域定位", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody BusinessaKeyAreas businessaKeyAreas)
    {
        AjaxResult ajaxResult = toAjax(businessaKeyAreasService.insertBusinessaKeyAreas(businessaKeyAreas));
        return ajaxResult;
    }

    /**
     * 修改重点区域定位
     */
    @PreAuthorize("@ss.hasPermi('system:areas:edit')")
    @Log(title = "重点区域定位", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody BusinessaKeyAreas businessaKeyAreas)
    {
        return toAjax(businessaKeyAreasService.updateBusinessaKeyAreas(businessaKeyAreas));
    }

    /**
     * 删除重点区域定位
     */
    @PreAuthorize("@ss.hasPermi('system:areas:remove')")
    @Log(title = "重点区域定位", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(businessaKeyAreasService.deleteBusinessaKeyAreasByIds(ids));
    }
}
