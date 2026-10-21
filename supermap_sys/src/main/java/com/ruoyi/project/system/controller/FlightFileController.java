package com.ruoyi.project.system.controller;

import java.io.IOException;
import java.util.List;

import com.ruoyi.common.utils.file.FileUploadUtils;
import com.ruoyi.framework.config.RuoYiConfig;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.ruoyi.framework.aspectj.lang.annotation.Log;
import com.ruoyi.framework.aspectj.lang.enums.BusinessType;
import com.ruoyi.project.system.domain.FlightFile;
import com.ruoyi.project.system.service.IFlightFileService;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.framework.web.page.TableDataInfo;
import org.springframework.web.multipart.MultipartFile;

/**
 * 飞行路径Controller
 *
 * @author
 * @date 2020-06-22
 */
@RestController
@RequestMapping("/system/flightFile")
public class FlightFileController extends BaseController
{
    @Autowired
    private IFlightFileService flightFileService;

    /**
     * 查询飞行路径列表
     */
    //@PreAuthorize("@ss.hasPermi('system:flightFile:list')")
    @GetMapping("/list")
    public TableDataInfo list(FlightFile flightFile)
    {
        startPage();
        List<FlightFile> list = flightFileService.selectFlightFileList(flightFile);
        return getDataTable(list);
    }

    /**
     * 导出飞行路径列表
     */
    @PreAuthorize("@ss.hasPermi('system:flightFile:export')")
    @Log(title = "飞行路径", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(FlightFile flightFile)
    {
        List<FlightFile> list = flightFileService.selectFlightFileList(flightFile);
        ExcelUtil<FlightFile> util = new ExcelUtil<FlightFile>(FlightFile.class);
        return util.exportExcel(list, "flightFile");
    }

    /**
     * 获取飞行路径详细信息
     */
    //@PreAuthorize("@ss.hasPermi('system:flightFile:query')")
    @GetMapping(value = "/{fileId}")
    public AjaxResult getInfo(@PathVariable("fileId") Long fileId)
    {
        return AjaxResult.success(flightFileService.selectFlightFileById(fileId));
    }

    /**
     * 新增飞行路径
     */
    @PreAuthorize("@ss.hasPermi('system:flightFile:add')")
    @Log(title = "飞行路径", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody FlightFile flightFile)
    {
        return toAjax(flightFileService.insertFlightFile(flightFile));
    }

    /**
     * 修改飞行路径
     */
    @PreAuthorize("@ss.hasPermi('system:flightFile:edit')")
    @Log(title = "飞行路径", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody FlightFile flightFile)
    {
        return toAjax(flightFileService.updateFlightFile(flightFile));
    }

    /**
     * 删除飞行路径
     */
    @PreAuthorize("@ss.hasPermi('system:flightFile:remove')")
    @Log(title = "飞行路径", businessType = BusinessType.DELETE)
	@DeleteMapping("/{fileIds}")
    public AjaxResult remove(@PathVariable Long[] fileIds)
    {
        return toAjax(flightFileService.deleteFlightFileByIds(fileIds));
    }


    /**
     * 添加图片文件
     * @param file
     * @return
     * @throws IOException
     */
    @PostMapping("/addImgSave")
    @ResponseBody
    public AjaxResult addImgSave(@RequestParam("file") MultipartFile file) throws IOException
    {
        String filePath = null; String fileName = null;
        if (!file.isEmpty()){
            // 上传文件路径
            filePath = RuoYiConfig.getUploadPath();
            // 上传并返回新文件名称
            fileName = FileUploadUtils.upload(filePath, file);
            FlightFile fileInfo = new FlightFile();
            fileInfo.setFileName(fileName);
            fileInfo.setFilePath(filePath);
            return toAjax(flightFileService.insertFlightFile(fileInfo));
        }else{
            return AjaxResult.error("文件为空");
        }
    }
}
