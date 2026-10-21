package com.ruoyi.project.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.project.system.mapper.SysSourceTypeMapper;
import com.ruoyi.project.system.domain.SysSourceType;
import com.ruoyi.project.system.service.ISysSourceTypeService;

/**
 * 来源类型Service业务层处理
 *
 * @author ruoyi
 * @date 2021-05-05
 */
@Service
public class SysSourceTypeServiceImpl implements ISysSourceTypeService
{
    @Autowired
    private SysSourceTypeMapper sysSourceTypeMapper;

    /**
     * 查询来源类型
     *
     * @param id 来源类型ID
     * @return 来源类型
     */
    @Override
    public SysSourceType selectSysSourceTypeById(Long id)
    {
        return sysSourceTypeMapper.selectSysSourceTypeById(id);
    }

    /**
     * 查询来源类型列表
     *
     * @param sysSourceType 来源类型
     * @return 来源类型
     */
    @Override
    public List<SysSourceType> selectSysSourceTypeList(SysSourceType sysSourceType)
    {
        return sysSourceTypeMapper.selectSysSourceTypeList(sysSourceType);
    }

    /**
     * 查询来源类型所有列表
     *
     * @return 来源类型
     */
    @Override
    public List<SysSourceType> selectSysSourceTypeListAll()
    {
        return sysSourceTypeMapper.selectSysSourceTypeListAll();
    }

    /**
     * 新增来源类型
     *
     * @param sysSourceType 来源类型
     * @return 结果
     */
    @Override
    public int insertSysSourceType(SysSourceType sysSourceType)
    {
        return sysSourceTypeMapper.insertSysSourceType(sysSourceType);
    }

    /**
     * 修改来源类型
     *
     * @param sysSourceType 来源类型
     * @return 结果
     */
    @Override
    public int updateSysSourceType(SysSourceType sysSourceType)
    {
        return sysSourceTypeMapper.updateSysSourceType(sysSourceType);
    }

    /**
     * 批量删除来源类型
     *
     * @param ids 需要删除的来源类型ID
     * @return 结果
     */
    @Override
    public int deleteSysSourceTypeByIds(Long[] ids)
    {
        return sysSourceTypeMapper.deleteSysSourceTypeByIds(ids);
    }

    /**
     * 删除来源类型信息
     *
     * @param id 来源类型ID
     * @return 结果
     */
    @Override
    public int deleteSysSourceTypeById(Long id)
    {
        return sysSourceTypeMapper.deleteSysSourceTypeById(id);
    }
}
