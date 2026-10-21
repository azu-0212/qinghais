package com.ruoyi.project.system.service;

import java.util.List;
import com.ruoyi.project.system.domain.SysSourceType;

/**
 * 来源类型Service接口
 *
 * @author ruoyi
 * @date 2021-05-05
 */
public interface ISysSourceTypeService
{
    /**
     * 查询来源类型
     *
     * @param id 来源类型ID
     * @return 来源类型
     */
    public SysSourceType selectSysSourceTypeById(Long id);

    /**
     * 查询来源类型列表
     *
     * @param sysSourceType 来源类型
     * @return 来源类型集合
     */
    public List<SysSourceType> selectSysSourceTypeList(SysSourceType sysSourceType);

    /**
     * 查询来源类型所有列表
     *
     * @return 来源类型集合
     */
    public List<SysSourceType> selectSysSourceTypeListAll();


    /**
     * 新增来源类型
     *
     * @param sysSourceType 来源类型
     * @return 结果
     */
    public int insertSysSourceType(SysSourceType sysSourceType);

    /**
     * 修改来源类型
     *
     * @param sysSourceType 来源类型
     * @return 结果
     */
    public int updateSysSourceType(SysSourceType sysSourceType);

    /**
     * 批量删除来源类型
     *
     * @param ids 需要删除的来源类型ID
     * @return 结果
     */
    public int deleteSysSourceTypeByIds(Long[] ids);

    /**
     * 删除来源类型信息
     *
     * @param id 来源类型ID
     * @return 结果
     */
    public int deleteSysSourceTypeById(Long id);
}
