package com.ruoyi.project.system.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.project.system.mapper.BusinessaKeyAreasMapper;
import com.ruoyi.project.system.domain.BusinessaKeyAreas;
import com.ruoyi.project.system.service.IBusinessaKeyAreasService;

/**
 * 重点区域定位Service业务层处理
 * 
 * @author ruoyi
 * @date 2020-06-18
 */
@Service
public class BusinessaKeyAreasServiceImpl implements IBusinessaKeyAreasService 
{
    @Autowired
    private BusinessaKeyAreasMapper businessaKeyAreasMapper;

    /**
     * 查询重点区域定位
     * 
     * @param id 重点区域定位ID
     * @return 重点区域定位
     */
    @Override
    public BusinessaKeyAreas selectBusinessaKeyAreasById(Long id)
    {
        return businessaKeyAreasMapper.selectBusinessaKeyAreasById(id);
    }

    /**
     * 查询重点区域定位列表
     * 
     * @param businessaKeyAreas 重点区域定位
     * @return 重点区域定位
     */
    @Override
    public List<BusinessaKeyAreas> selectBusinessaKeyAreasList(BusinessaKeyAreas businessaKeyAreas)
    {
        return businessaKeyAreasMapper.selectBusinessaKeyAreasList(businessaKeyAreas);
    }

    /**
     * 新增重点区域定位
     * 
     * @param businessaKeyAreas 重点区域定位
     * @return 结果
     */
    @Override
    public int insertBusinessaKeyAreas(BusinessaKeyAreas businessaKeyAreas)
    {
        businessaKeyAreas.setCreateTime(DateUtils.getNowDate());
        return businessaKeyAreasMapper.insertBusinessaKeyAreas(businessaKeyAreas);
    }

    /**
     * 修改重点区域定位
     * 
     * @param businessaKeyAreas 重点区域定位
     * @return 结果
     */
    @Override
    public int updateBusinessaKeyAreas(BusinessaKeyAreas businessaKeyAreas)
    {
        businessaKeyAreas.setUpdateTime(DateUtils.getNowDate());
        return businessaKeyAreasMapper.updateBusinessaKeyAreas(businessaKeyAreas);
    }

    /**
     * 批量删除重点区域定位
     * 
     * @param ids 需要删除的重点区域定位ID
     * @return 结果
     */
    @Override
    public int deleteBusinessaKeyAreasByIds(Long[] ids)
    {
        return businessaKeyAreasMapper.deleteBusinessaKeyAreasByIds(ids);
    }

    /**
     * 删除重点区域定位信息
     * 
     * @param id 重点区域定位ID
     * @return 结果
     */
    @Override
    public int deleteBusinessaKeyAreasById(Long id)
    {
        return businessaKeyAreasMapper.deleteBusinessaKeyAreasById(id);
    }
}
