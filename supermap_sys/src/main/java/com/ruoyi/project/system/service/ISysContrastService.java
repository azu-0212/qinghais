package com.ruoyi.project.system.service;

import java.util.List;
import com.ruoyi.project.system.domain.SysContrast;

/**
 * 影像对比Service接口
 *
 * @author ruoyi
 * @date 2021-04-01
 */
public interface ISysContrastService
{
    /**
     * 查询影像对比
     *
     * @param id 影像对比ID
     * @return 影像对比
     */
    public SysContrast selectSysContrastById(Long id);

    /**
     * 查询影像对比列表
     *
     * @param sysContrast 影像对比
     * @return 影像对比集合
     */
    public List<SysContrast> selectSysContrastList(SysContrast sysContrast);

    /**
     * 根据类型查询影像对比列表
     *
     * @param
     * @return 影像对比集合
     */
    public List<SysContrast> selectSysContrastListByType(Long pid, Long dataType);

    /**
     * 新增影像对比
     *
     * @param sysContrast 影像对比
     * @return 结果
     */
    public int insertSysContrast(SysContrast sysContrast);

    /**
     * 修改影像对比
     *
     * @param sysContrast 影像对比
     * @return 结果
     */
    public int updateSysContrast(SysContrast sysContrast);

    /**
     * 批量删除影像对比
     *
     * @param ids 需要删除的影像对比ID
     * @return 结果
     */
    public int deleteSysContrastByIds(Long[] ids);

    /**
     * 删除影像对比信息
     *
     * @param id 影像对比ID
     * @return 结果
     */
    public int deleteSysContrastById(Long id);
}
