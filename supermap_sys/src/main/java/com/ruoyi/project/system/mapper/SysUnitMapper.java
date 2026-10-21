package com.ruoyi.project.system.mapper;

import java.util.List;
import com.ruoyi.project.system.domain.SysUnit;

/**
 * 地理单元Mapper接口
 *
 * @author ruoyi
 * @date 2021-03-25
 */
public interface SysUnitMapper
{
    /**
     * 查询地理单元
     *
     * @param id 地理单元ID
     * @return 地理单元
     */
    public SysUnit selectSysUnitById(Long id);

    /**
     * 查询地理单元列表
     *
     * @param sysUnit 地理单元
     * @return 地理单元集合
     */
    public List<SysUnit> selectSysUnitList(SysUnit sysUnit);

    /**
     * 新增地理单元
     *
     * @param sysUnit 地理单元
     * @return 结果
     */
    public int insertSysUnit(SysUnit sysUnit);

    /**
     * 修改地理单元
     *
     * @param sysUnit 地理单元
     * @return 结果
     */
    public int updateSysUnit(SysUnit sysUnit);

    /**
     * 删除地理单元
     *
     * @param id 地理单元ID
     * @return 结果
     */
    public int deleteSysUnitById(Long id);

    /**
     * 批量删除地理单元
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteSysUnitByIds(Long[] ids);
}
