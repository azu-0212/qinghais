package com.ruoyi.project.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.project.system.mapper.SourcesFileMapper;
import com.ruoyi.project.system.domain.SourcesFile;
import com.ruoyi.project.system.service.ISourcesFileService;

/**
 * 重点区域图片Service业务层处理
 * 
 * @author ruoyi
 * @date 2020-06-22
 */
@Service
public class SourcesFileServiceImpl implements ISourcesFileService 
{
    @Autowired
    private SourcesFileMapper sourcesFileMapper;

    /**
     * 查询重点区域图片
     * 
     * @param fileId 重点区域图片ID
     * @return 重点区域图片
     */
    @Override
    public SourcesFile selectSourcesFileById(Long fileId)
    {
        return sourcesFileMapper.selectSourcesFileById(fileId);
    }

    /**
     * 查询重点区域图片列表
     * 
     * @param sourcesFile 重点区域图片
     * @return 重点区域图片
     */
    @Override
    public List<SourcesFile> selectSourcesFileList(SourcesFile sourcesFile)
    {
        return sourcesFileMapper.selectSourcesFileList(sourcesFile);
    }

    /**
     * 新增重点区域图片
     * 
     * @param sourcesFile 重点区域图片
     * @return 结果
     */
    @Override
    public int insertSourcesFile(SourcesFile sourcesFile)
    {
        return sourcesFileMapper.insertSourcesFile(sourcesFile);
    }

    /**
     * 修改重点区域图片
     * 
     * @param sourcesFile 重点区域图片
     * @return 结果
     */
    @Override
    public int updateSourcesFile(SourcesFile sourcesFile)
    {
        return sourcesFileMapper.updateSourcesFile(sourcesFile);
    }

    /**
     * 批量删除重点区域图片
     * 
     * @param fileIds 需要删除的重点区域图片ID
     * @return 结果
     */
    @Override
    public int deleteSourcesFileByIds(Long[] fileIds)
    {
        return sourcesFileMapper.deleteSourcesFileByIds(fileIds);
    }

    /**
     * 删除重点区域图片信息
     * 
     * @param fileId 重点区域图片ID
     * @return 结果
     */
    @Override
    public int deleteSourcesFileById(Long fileId)
    {
        return sourcesFileMapper.deleteSourcesFileById(fileId);
    }
}
