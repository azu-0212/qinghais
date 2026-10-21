package com.ruoyi.project.system.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.project.system.mapper.SysUnitMapper;
import com.ruoyi.project.system.domain.SysUnit;
import com.ruoyi.project.system.service.ISysUnitService;

/**
 * 地理单元Service业务层处理
 *
 * @author ruoyi
 * @date 2021-03-25
 */
@Service
public class SysUnitServiceImpl implements ISysUnitService
{
    @Autowired
    private SysUnitMapper sysUnitMapper;

    /**
     * 查询地理单元
     *
     * @param id 地理单元ID
     * @return 地理单元
     */
    @Override
    public SysUnit selectSysUnitById(Long id)
    {
        return sysUnitMapper.selectSysUnitById(id);
    }

    /**
     * 查询地理单元列表
     *
     * @param sysUnit 地理单元
     * @return 地理单元
     */
    @Override
    public List<SysUnit> selectSysUnitList(SysUnit sysUnit)
    {
        return sysUnitMapper.selectSysUnitList(sysUnit);
    }

    /**
     * 新增地理单元
     *
     * @param sysUnit 地理单元
     * @return 结果
     */
    @Override
    public int insertSysUnit(SysUnit sysUnit)
    {
        sysUnit.setCreateTime(DateUtils.getNowDate());
        return sysUnitMapper.insertSysUnit(sysUnit);
    }

    /**
     * 修改地理单元
     *
     * @param sysUnit 地理单元
     * @return 结果
     */
    @Override
    public int updateSysUnit(SysUnit sysUnit)
    {
        return sysUnitMapper.updateSysUnit(sysUnit);
    }

    /**
     * 批量删除地理单元
     *
     * @param ids 需要删除的地理单元ID
     * @return 结果
     */
    @Override
    public int deleteSysUnitByIds(Long[] ids)
    {
        return sysUnitMapper.deleteSysUnitByIds(ids);
    }

    /**
     * 删除地理单元信息
     *
     * @param id 地理单元ID
     * @return 结果
     */
    @Override
    public int deleteSysUnitById(Long id)
    {
        return sysUnitMapper.deleteSysUnitById(id);
    }
}
