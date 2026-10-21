package com.ruoyi.project.system.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.project.system.mapper.BusinessaFlyMapper;
import com.ruoyi.project.system.domain.BusinessaFly;
import com.ruoyi.project.system.service.IBusinessaFlyService;

/**
 * 飞行路线Service业务层处理
 *
 * @author ruoyi
 * @date 2020-06-18
 */
@Service
public class BusinessaFlyServiceImpl implements IBusinessaFlyService
{
    @Autowired
    private BusinessaFlyMapper businessaFlyMapper;

    /**
     * 查询飞行路线
     *
     * @param id 飞行路线ID
     * @return 飞行路线
     */
    @Override
    public BusinessaFly selectBusinessaFlyById(Long id)
    {
        return businessaFlyMapper.selectBusinessaFlyById(id);
    }

    /**
     * 查询飞行路线列表
     *
     * @param businessaFly 飞行路线
     * @return 飞行路线
     */
    @Override
    public List<BusinessaFly> selectBusinessaFlyList(BusinessaFly businessaFly)
    {
        return businessaFlyMapper.selectBusinessaFlyList(businessaFly);
    }

    /**
     * 新增飞行路线
     *
     * @param businessaFly 飞行路线
     * @return 结果
     */
    @Override
    public int insertBusinessaFly(BusinessaFly businessaFly)
    {
        businessaFly.setCreateTime(DateUtils.getNowDate());
        int insert = businessaFlyMapper.insertBusinessaFly(businessaFly);
        return insert;
    }

    /**
     * 修改飞行路线
     *
     * @param businessaFly 飞行路线
     * @return 结果
     */
    @Override
    public int updateBusinessaFly(BusinessaFly businessaFly)
    {
        businessaFly.setUpdateTime(DateUtils.getNowDate());
        return businessaFlyMapper.updateBusinessaFly(businessaFly);
    }

    /**
     * 批量删除飞行路线
     *
     * @param ids 需要删除的飞行路线ID
     * @return 结果
     */
    @Override
    public int deleteBusinessaFlyByIds(Long[] ids)
    {
        return businessaFlyMapper.deleteBusinessaFlyByIds(ids);
    }

    /**
     * 删除飞行路线信息
     *
     * @param id 飞行路线ID
     * @return 结果
     */
    @Override
    public int deleteBusinessaFlyById(Long id)
    {
        return businessaFlyMapper.deleteBusinessaFlyById(id);
    }
}
