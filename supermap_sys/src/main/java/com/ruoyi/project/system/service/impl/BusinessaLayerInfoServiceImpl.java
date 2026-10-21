package com.ruoyi.project.system.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.project.system.mapper.BusinessaLayerInfoMapper;
import com.ruoyi.project.system.domain.BusinessaLayerInfo;
import com.ruoyi.project.system.service.IBusinessaLayerInfoService;

/**
 * 三维图层资源Service业务层处理
 *
 * @author ruoyi
 * @date 2020-06-18
 */
@Service
public class BusinessaLayerInfoServiceImpl implements IBusinessaLayerInfoService
{
    @Autowired
    private BusinessaLayerInfoMapper businessaLayerInfoMapper;

    /**
     * 查询三维图层资源
     *
     * @param id 三维图层资源ID
     * @return 三维图层资源
     */
    @Override
    public BusinessaLayerInfo selectBusinessaLayerInfoById(Long id)
    {
        return businessaLayerInfoMapper.selectBusinessaLayerInfoById(id);
    }

    /**
     * 查询三维图层资源列表
     *
     * @param businessaLayerInfo 三维图层资源
     * @return 三维图层资源
     */
    @Override
    public List<BusinessaLayerInfo> selectBusinessaLayerInfoList(BusinessaLayerInfo businessaLayerInfo)
    {
        return businessaLayerInfoMapper.selectBusinessaLayerInfoList(businessaLayerInfo);
    }

    /**
     * 新增三维图层资源
     *
     * @param businessaLayerInfo 三维图层资源
     * @return 结果
     */
    @Override
    public int insertBusinessaLayerInfo(BusinessaLayerInfo businessaLayerInfo)
    {
        businessaLayerInfo.setCreateTime(DateUtils.getNowDate());
        return businessaLayerInfoMapper.insertBusinessaLayerInfo(businessaLayerInfo);
    }

    /**
     * 修改三维图层资源
     *
     * @param businessaLayerInfo 三维图层资源
     * @return 结果
     */
    @Override
    public int updateBusinessaLayerInfo(BusinessaLayerInfo businessaLayerInfo)
    {
        businessaLayerInfo.setUpdateTime(DateUtils.getNowDate());
        return businessaLayerInfoMapper.updateBusinessaLayerInfo(businessaLayerInfo);
    }

    /**
     * 批量删除三维图层资源
     *
     * @param ids 需要删除的三维图层资源ID
     * @return 结果
     */
    @Override
    public int deleteBusinessaLayerInfoByIds(Long[] ids)
    {
        return businessaLayerInfoMapper.deleteBusinessaLayerInfoByIds(ids);
    }

    /**
     * 删除三维图层资源信息
     *
     * @param id 三维图层资源ID
     * @return 结果
     */
    @Override
    public int deleteBusinessaLayerInfoById(Long id)
    {
        return businessaLayerInfoMapper.deleteBusinessaLayerInfoById(id);
    }

    @Override
    public List<BusinessaLayerInfo> selectLayerArea() {
        return businessaLayerInfoMapper.selectLayerArea();
    }

    @Override
    public List<BusinessaLayerInfo> countInfo() {
        return businessaLayerInfoMapper.countInfo();
    }


}
