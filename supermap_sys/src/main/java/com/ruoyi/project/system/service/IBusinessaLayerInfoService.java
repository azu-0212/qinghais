package com.ruoyi.project.system.service;

import java.util.List;

import com.ruoyi.project.system.domain.BusinessaLayerInfo;

/**
 * 三维图层资源Service接口
 *
 * @author ruoyi
 * @date 2020-06-18
 */
public interface IBusinessaLayerInfoService
{
    /**
     * 查询三维图层资源
     *
     * @param id 三维图层资源ID
     * @return 三维图层资源
     */
    public BusinessaLayerInfo selectBusinessaLayerInfoById(Long id);

    /**
     * 查询三维图层资源列表
     *
     * @param businessaLayerInfo 三维图层资源
     * @return 三维图层资源集合
     */
    public List<BusinessaLayerInfo> selectBusinessaLayerInfoList(BusinessaLayerInfo businessaLayerInfo);

    /**
     * 新增三维图层资源
     *
     * @param businessaLayerInfo 三维图层资源
     * @return 结果
     */
    public int insertBusinessaLayerInfo(BusinessaLayerInfo businessaLayerInfo);

    /**
     * 修改三维图层资源
     *
     * @param businessaLayerInfo 三维图层资源
     * @return 结果
     */
    public int updateBusinessaLayerInfo(BusinessaLayerInfo businessaLayerInfo);

    /**
     * 批量删除三维图层资源
     *
     * @param ids 需要删除的三维图层资源ID
     * @return 结果
     */
    public int deleteBusinessaLayerInfoByIds(Long[] ids);

    /**
     * 删除三维图层资源信息
     *
     * @param id 三维图层资源ID
     * @return 结果
     */
    public int deleteBusinessaLayerInfoById(Long id);

    public List<BusinessaLayerInfo> selectLayerArea();

    public List<BusinessaLayerInfo> countInfo();
}
