package com.ruoyi.project.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.project.system.mapper.SysContrastMapper;
import com.ruoyi.project.system.domain.SysContrast;
import com.ruoyi.project.system.service.ISysContrastService;

/**
 * 影像对比Service业务层处理
 *
 * @author ruoyi
 * @date 2021-04-01
 */
@Service
public class SysContrastServiceImpl implements ISysContrastService
{
    @Autowired
    private SysContrastMapper sysContrastMapper;

    /**
     * 查询影像对比
     *
     * @param id 影像对比ID
     * @return 影像对比
     */
    @Override
    public SysContrast selectSysContrastById(Long id)
    {
        return sysContrastMapper.selectSysContrastById(id);
    }

    /**
     * 查询影像对比列表
     *
     * @param sysContrast 影像对比
     * @return 影像对比
     */
    @Override
    public List<SysContrast> selectSysContrastList(SysContrast sysContrast)
    {
        return sysContrastMapper.selectSysContrastList(sysContrast);
    }

    /**
     * 根据类型查询影像对比列表
     *
     * @param
     * @return 影像对比
     */
    @Override
    public List<SysContrast> selectSysContrastListByType(Long pid ,Long dataType)
    {
        return sysContrastMapper.selectSysContrastListByType(pid, dataType);
    }

    /**
     * 新增影像对比
     *
     * @param sysContrast 影像对比
     * @return 结果
     */
    @Override
    public int insertSysContrast(SysContrast sysContrast)
    {
        return sysContrastMapper.insertSysContrast(sysContrast);
    }

    /**
     * 修改影像对比
     *
     * @param sysContrast 影像对比
     * @return 结果
     */
    @Override
    public int updateSysContrast(SysContrast sysContrast)
    {
        return sysContrastMapper.updateSysContrast(sysContrast);
    }

    /**
     * 批量删除影像对比
     *
     * @param ids 需要删除的影像对比ID
     * @return 结果
     */
    @Override
    public int deleteSysContrastByIds(Long[] ids)
    {
        return sysContrastMapper.deleteSysContrastByIds(ids);
    }

    /**
     * 删除影像对比信息
     *
     * @param id 影像对比ID
     * @return 结果
     */
    @Override
    public int deleteSysContrastById(Long id)
    {
        return sysContrastMapper.deleteSysContrastById(id);
    }
}
