package com.ruoyi.project.system.service;

import java.util.List;
import com.ruoyi.project.system.domain.SourcesFile;

/**
 * 重点区域图片Service接口
 * 
 * @author ruoyi
 * @date 2020-06-22
 */
public interface ISourcesFileService 
{
    /**
     * 查询重点区域图片
     * 
     * @param fileId 重点区域图片ID
     * @return 重点区域图片
     */
    public SourcesFile selectSourcesFileById(Long fileId);

    /**
     * 查询重点区域图片列表
     * 
     * @param sourcesFile 重点区域图片
     * @return 重点区域图片集合
     */
    public List<SourcesFile> selectSourcesFileList(SourcesFile sourcesFile);

    /**
     * 新增重点区域图片
     * 
     * @param sourcesFile 重点区域图片
     * @return 结果
     */
    public int insertSourcesFile(SourcesFile sourcesFile);

    /**
     * 修改重点区域图片
     * 
     * @param sourcesFile 重点区域图片
     * @return 结果
     */
    public int updateSourcesFile(SourcesFile sourcesFile);

    /**
     * 批量删除重点区域图片
     * 
     * @param fileIds 需要删除的重点区域图片ID
     * @return 结果
     */
    public int deleteSourcesFileByIds(Long[] fileIds);

    /**
     * 删除重点区域图片信息
     * 
     * @param fileId 重点区域图片ID
     * @return 结果
     */
    public int deleteSourcesFileById(Long fileId);
}
