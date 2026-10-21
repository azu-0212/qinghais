package com.ruoyi.project.system.controller;

import java.io.IOException;
import java.util.List;

import com.ruoyi.common.utils.file.FileUploadUtils;
import com.ruoyi.framework.config.RuoYiConfig;
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
import com.ruoyi.project.system.domain.BusinessaFly;
import com.ruoyi.project.system.service.IBusinessaFlyService;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.framework.web.page.TableDataInfo;
import org.springframework.web.multipart.MultipartFile;

/**
 * 飞行路线Controller
 *
 * @author ruoyi
 * @date 2020-06-18
 */
@RestController
@RequestMapping("/system/fly")
public class BusinessaFlyController extends BaseController
{
    @Autowired
    private IBusinessaFlyService businessaFlyService;

    /**
     * 查询飞行路线列表
     */
    //@PreAuthorize("@ss.hasPermi('system:fly:list')")
    @GetMapping("/list")
    public TableDataInfo list(BusinessaFly businessaFly)
    {
        startPage();
        List<BusinessaFly> list = businessaFlyService.selectBusinessaFlyList(businessaFly);
        return getDataTable(list);
    }

    /**
     * 导出飞行路线列表
     */
    @PreAuthorize("@ss.hasPermi('system:fly:export')")
    @Log(title = "飞行路线", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(BusinessaFly businessaFly)
    {
        List<BusinessaFly> list = businessaFlyService.selectBusinessaFlyList(businessaFly);
        ExcelUtil<BusinessaFly> util = new ExcelUtil<BusinessaFly>(BusinessaFly.class);
        return util.exportExcel(list, "fly");
    }

    /**
     * 获取飞行路线详细信息
     */
    //@PreAuthorize("@ss.hasPermi('system:fly:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(businessaFlyService.selectBusinessaFlyById(id));
    }

    /**
     * 新增飞行路线
     */
//    @ApiOperation(value = "新增保存资源", httpMethod = "POST")
//    @ApiImplicitParams({
//            @ApiImplicitParam(paramType = "from", name = "flyName", value = "飞行路线名称", required = true, dataType = "String"),
//    })
    @PreAuthorize("@ss.hasPermi('system:fly:add')")
    @Log(title = "飞行路线", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody BusinessaFly businessaFly)
//            ,
//                          @ApiParam(value = "上传的文件", required = true)
//                          @RequestParam("file") MultipartFile[] files) throws IOException
    {
//        String filePath = null; String fileName = null;
//        if (files != null && files.length > 0){
//            for(MultipartFile e : files){
//                // 上传文件路径
//                filePath = RuoYiConfig.getUploadPath();
//                // 上传并返回新文件名称
//                fileName = FileUploadUtils.upload(filePath, e);
//                businessaFly.setImgPath(filePath + fileName);
//            }
            return toAjax(businessaFlyService.insertBusinessaFly(businessaFly));
//        }else{
//            return AjaxResult.error("文件为空");
//        }
    }

    /**
     * 修改飞行路线
     */
    @PreAuthorize("@ss.hasPermi('system:fly:edit')")
    @Log(title = "飞行路线", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody BusinessaFly businessaFly)
    {
        return toAjax(businessaFlyService.updateBusinessaFly(businessaFly));
    }

    /**
     * 删除飞行路线
     */
    @PreAuthorize("@ss.hasPermi('system:fly:remove')")
    @Log(title = "飞行路线", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(businessaFlyService.deleteBusinessaFlyByIds(ids));
    }


    /**
     * 飞行路线文件上传
     */
    @ApiOperation(value = "新增保存资源", httpMethod = "POST")
    @PostMapping("/upload")
    public AjaxResult uploadFile( @ApiParam(value = "上传的文件", required = true)
                                      @RequestParam("file") MultipartFile[] files) throws IOException {
        String filePath = null; String fileName = null;
        if (files != null && files.length > 0){
            for(MultipartFile e : files){
                // 上传文件路径
                filePath = RuoYiConfig.getUploadPath();
                // 上传并返回新文件名称
                fileName = FileUploadUtils.upload(filePath, e);
            }
            return AjaxResult.success(filePath + fileName);
        }else{
            return AjaxResult.error("文件为空");
        }
    }

}
