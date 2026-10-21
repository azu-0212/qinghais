package com.ruoyi.project.system.controller;

import java.io.IOException;
import java.util.List;

import com.ruoyi.common.utils.file.FileUploadUtils;
import com.ruoyi.framework.config.RuoYiConfig;
import com.ruoyi.project.system.domain.FlightFile;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.ruoyi.framework.aspectj.lang.annotation.Log;
import com.ruoyi.framework.aspectj.lang.enums.BusinessType;
import com.ruoyi.project.system.domain.SourcesFile;
import com.ruoyi.project.system.service.ISourcesFileService;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.framework.web.page.TableDataInfo;
import org.springframework.web.multipart.MultipartFile;

/**
 * 重点区域图片Controller
 *
 * @author ruoyi
 * @date 2020-06-22
 */
@RestController
@RequestMapping("/system/sourcesFile")
public class SourcesFileController extends BaseController
{
    @Autowired
    private ISourcesFileService sourcesFileService;

    /**
     * 查询重点区域图片列表
     */
    //@PreAuthorize("@ss.hasPermi('system:sourcesFile:list')")
    @GetMapping("/list")
    public TableDataInfo list(SourcesFile sourcesFile)
    {
        startPage();
        List<SourcesFile> list = sourcesFileService.selectSourcesFileList(sourcesFile);
        return getDataTable(list);
    }

    /**
     * 导出重点区域图片列表
     */
    @PreAuthorize("@ss.hasPermi('system:sourcesFile:export')")
    @Log(title = "重点区域图片", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(SourcesFile sourcesFile)
    {
        List<SourcesFile> list = sourcesFileService.selectSourcesFileList(sourcesFile);
        ExcelUtil<SourcesFile> util = new ExcelUtil<SourcesFile>(SourcesFile.class);
        return util.exportExcel(list, "sourcesFile");
    }

    /**
     * 获取重点区域图片详细信息
     */
    //@PreAuthorize("@ss.hasPermi('system:sourcesFile:query')")
    @GetMapping(value = "/{fileId}")
    public AjaxResult getInfo(@PathVariable("fileId") Long fileId)
    {
        return AjaxResult.success(sourcesFileService.selectSourcesFileById(fileId));
    }

    /**
     * 新增重点区域图片
     */
    @PreAuthorize("@ss.hasPermi('system:sourcesFile:add')")
    @Log(title = "重点区域图片", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SourcesFile sourcesFile)
    {
        return toAjax(sourcesFileService.insertSourcesFile(sourcesFile));
    }

    /**
     * 修改重点区域图片
     */
    @PreAuthorize("@ss.hasPermi('system:sourcesFile:edit')")
    @Log(title = "重点区域图片", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SourcesFile sourcesFile)
    {
        return toAjax(sourcesFileService.updateSourcesFile(sourcesFile));
    }

    /**
     * 删除重点区域图片
     */
    @PreAuthorize("@ss.hasPermi('system:sourcesFile:remove')")
    @Log(title = "重点区域图片", businessType = BusinessType.DELETE)
	@DeleteMapping("/{fileIds}")
    public AjaxResult remove(@PathVariable Long[] fileIds)
    {
        return toAjax(sourcesFileService.deleteSourcesFileByIds(fileIds));
    }

    /**
     * 添加图片文件
     * @param file
     * @return
     * @throws IOException
     */
    @PostMapping("/addFile")
    @ResponseBody
    public AjaxResult addFile(@RequestParam("file") MultipartFile file) throws IOException
    {
        String filePath = null; String fileName = null;
        if (!file.isEmpty()){
            // 上传文件路径
            filePath = RuoYiConfig.getUploadPath();
            // 上传并返回新文件名称
            fileName = FileUploadUtils.upload(filePath, file);
            SourcesFile fileInfo = new SourcesFile();
            fileInfo.setFileName(fileName);
            fileInfo.setFilePath(filePath);
            return toAjax(sourcesFileService.insertSourcesFile(fileInfo));
        }else{
            return AjaxResult.error("文件为空");
        }
    }
}
