package com.ruoyi.project.system.mapper;

import java.util.List;
import com.ruoyi.project.system.domain.BusinessaKeyAreas;

/**
 * 重点区域定位Mapper接口
 * 
 * @author ruoyi
 * @date 2020-06-18
 */
public interface BusinessaKeyAreasMapper 
{
    /**
     * 查询重点区域定位
     * 
     * @param id 重点区域定位ID
     * @return 重点区域定位
     */
    public BusinessaKeyAreas selectBusinessaKeyAreasById(Long id);

    /**
     * 查询重点区域定位列表
     * 
     * @param businessaKeyAreas 重点区域定位
     * @return 重点区域定位集合
     */
    public List<BusinessaKeyAreas> selectBusinessaKeyAreasList(BusinessaKeyAreas businessaKeyAreas);

    /**
     * 新增重点区域定位
     * 
     * @param businessaKeyAreas 重点区域定位
     * @return 结果
     */
    public int insertBusinessaKeyAreas(BusinessaKeyAreas businessaKeyAreas);

    /**
     * 修改重点区域定位
     * 
     * @param businessaKeyAreas 重点区域定位
     * @return 结果
     */
    public int updateBusinessaKeyAreas(BusinessaKeyAreas businessaKeyAreas);

    /**
     * 删除重点区域定位
     * 
     * @param id 重点区域定位ID
     * @return 结果
     */
    public int deleteBusinessaKeyAreasById(Long id);

    /**
     * 批量删除重点区域定位
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteBusinessaKeyAreasByIds(Long[] ids);
}
