package com.ruoyi.project.system.mapper;

import java.util.List;
import com.ruoyi.project.system.domain.SysContrast;
import org.apache.ibatis.annotations.Param;

/**
 * 影像对比Mapper接口
 *
 * @author ruoyi
 * @date 2021-04-01
 */
public interface SysContrastMapper
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
    public List<SysContrast> selectSysContrastListByType(@Param("pid") Long pid, @Param("dataType") Long dataType);

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
     * 删除影像对比
     *
     * @param id 影像对比ID
     * @return 结果
     */
    public int deleteSysContrastById(Long id);

    /**
     * 批量删除影像对比
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteSysContrastByIds(Long[] ids);
}
