package com.ruoyi.project.system.service;

import java.util.List;
import com.ruoyi.project.system.domain.BusinessaFly;

/**
 * 飞行路线Service接口
 * 
 * @author ruoyi
 * @date 2020-06-18
 */
public interface IBusinessaFlyService 
{
    /**
     * 查询飞行路线
     * 
     * @param id 飞行路线ID
     * @return 飞行路线
     */
    public BusinessaFly selectBusinessaFlyById(Long id);

    /**
     * 查询飞行路线列表
     * 
     * @param businessaFly 飞行路线
     * @return 飞行路线集合
     */
    public List<BusinessaFly> selectBusinessaFlyList(BusinessaFly businessaFly);

    /**
     * 新增飞行路线
     * 
     * @param businessaFly 飞行路线
     * @return 结果
     */
    public int insertBusinessaFly(BusinessaFly businessaFly);

    /**
     * 修改飞行路线
     * 
     * @param businessaFly 飞行路线
     * @return 结果
     */
    public int updateBusinessaFly(BusinessaFly businessaFly);

    /**
     * 批量删除飞行路线
     * 
     * @param ids 需要删除的飞行路线ID
     * @return 结果
     */
    public int deleteBusinessaFlyByIds(Long[] ids);

    /**
     * 删除飞行路线信息
     * 
     * @param id 飞行路线ID
     * @return 结果
     */
    public int deleteBusinessaFlyById(Long id);
}
