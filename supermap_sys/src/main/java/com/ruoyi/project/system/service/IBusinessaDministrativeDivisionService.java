package com.ruoyi.project.system.service;

import java.util.List;
import com.ruoyi.project.system.domain.BusinessaDministrativeDivision;

/**
 * 行政区划Service接口
 *
 * @author ruoyi
 * @date 2020-06-19
 */
public interface IBusinessaDministrativeDivisionService
{
    /**
     * 查询行政区划
     *
     * @param id 行政区划ID
     * @return 行政区划
     */
    public BusinessaDministrativeDivision selectBusinessaDministrativeDivisionById(Long id);

    /**
     * 查询行政区划列表
     *
     * @param businessaDministrativeDivision 行政区划
     * @return 行政区划集合
     */
    public List<BusinessaDministrativeDivision> selectBusinessaDministrativeDivisionList(BusinessaDministrativeDivision businessaDministrativeDivision);

    /**
     * 查询行政区划子父级信息--关键字搜索
     *
     * @return 行政区划集合
     */
    public List<BusinessaDministrativeDivision> selectBusinessaDministrativeDivisionSearchList();

    /**
     * 新增行政区划
     *
     * @param businessaDministrativeDivision 行政区划
     * @return 结果
     */
    public int insertBusinessaDministrativeDivision(BusinessaDministrativeDivision businessaDministrativeDivision);

    /**
     * 修改行政区划
     *
     * @param businessaDministrativeDivision 行政区划
     * @return 结果
     */
    public int updateBusinessaDministrativeDivision(BusinessaDministrativeDivision businessaDministrativeDivision);

    /**
     * 批量删除行政区划
     *
     * @param ids 需要删除的行政区划ID
     * @return 结果
     */
    public int deleteBusinessaDministrativeDivisionByIds(Long[] ids);

    /**
     * 删除行政区划信息
     *
     * @param id 行政区划ID
     * @return 结果
     */
    public int deleteBusinessaDministrativeDivisionById(Long id);


    public List<BusinessaDministrativeDivision> getDivitionByPid(Long parentId);

    public List<BusinessaDministrativeDivision> getAll();
}
