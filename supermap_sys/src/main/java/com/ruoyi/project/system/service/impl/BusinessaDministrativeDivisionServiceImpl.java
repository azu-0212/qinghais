package com.ruoyi.project.system.service.impl;

import java.util.ArrayList;
import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.project.system.mapper.BusinessaDministrativeDivisionMapper;
import com.ruoyi.project.system.domain.BusinessaDministrativeDivision;
import com.ruoyi.project.system.service.IBusinessaDministrativeDivisionService;

/**
 * 行政区划Service业务层处理
 *
 * @author ruoyi
 * @date 2020-06-19
 */
@Service
public class BusinessaDministrativeDivisionServiceImpl implements IBusinessaDministrativeDivisionService
{
    @Autowired
    private BusinessaDministrativeDivisionMapper businessaDministrativeDivisionMapper;

    /**
     * 查询行政区划
     *
     * @param id 行政区划ID
     * @return 行政区划
     */
    @Override
    public BusinessaDministrativeDivision selectBusinessaDministrativeDivisionById(Long id)
    {
        return businessaDministrativeDivisionMapper.selectBusinessaDministrativeDivisionById(id);
    }

    /**
     * 查询行政区划列表
     *
     * @param businessaDministrativeDivision 行政区划
     * @return 行政区划
     */
    @Override
    public List<BusinessaDministrativeDivision> selectBusinessaDministrativeDivisionList(BusinessaDministrativeDivision businessaDministrativeDivision)
    {
        return businessaDministrativeDivisionMapper.selectBusinessaDministrativeDivisionList(businessaDministrativeDivision);
    }

    /**
     * 查询行政区划子父级信息--关键字搜索
     *
     * @return 行政区划
     */
    @Override
    public List<BusinessaDministrativeDivision> selectBusinessaDministrativeDivisionSearchList()
    {
        return businessaDministrativeDivisionMapper.selectBusinessaDministrativeDivisionSearchList();
    }

    /**
     * 新增行政区划
     *
     * @param businessaDministrativeDivision 行政区划
     * @return 结果
     */
    @Override
    public int insertBusinessaDministrativeDivision(BusinessaDministrativeDivision businessaDministrativeDivision)
    {
        businessaDministrativeDivision.setCreateTime(DateUtils.getNowDate());
        return businessaDministrativeDivisionMapper.insertBusinessaDministrativeDivision(businessaDministrativeDivision);
    }

    /**
     * 修改行政区划
     *
     * @param businessaDministrativeDivision 行政区划
     * @return 结果
     */
    @Override
    public int updateBusinessaDministrativeDivision(BusinessaDministrativeDivision businessaDministrativeDivision)
    {
        return businessaDministrativeDivisionMapper.updateBusinessaDministrativeDivision(businessaDministrativeDivision);
    }

    /**
     * 批量删除行政区划
     *
     * @param ids 需要删除的行政区划ID
     * @return 结果
     */
    @Override
    public int deleteBusinessaDministrativeDivisionByIds(Long[] ids)
    {
        return businessaDministrativeDivisionMapper.deleteBusinessaDministrativeDivisionByIds(ids);
    }

    /**
     * 删除行政区划信息
     *
     * @param id 行政区划ID
     * @return 结果
     */
    @Override
    public int deleteBusinessaDministrativeDivisionById(Long id)
    {
        return businessaDministrativeDivisionMapper.deleteBusinessaDministrativeDivisionById(id);
    }

    @Override
    public List<BusinessaDministrativeDivision> getAll() {
        List<BusinessaDministrativeDivision> divitions = getDivitionByPid((long) 0);
        if(divitions != null && !divitions.isEmpty()){
            for(BusinessaDministrativeDivision d : divitions){
                List<BusinessaDministrativeDivision> list = loadChild(d.getId(),null);
                boolean ok = (list != null && !list.isEmpty()) ? divitions.addAll(list) : false;
            }
        }
        return divitions;
    }

    public List<BusinessaDministrativeDivision> loadChild(Long  pid, List<BusinessaDministrativeDivision> list){
        List<BusinessaDministrativeDivision> divitions = getDivitionByPid(pid);
        if(divitions != null && !divitions.isEmpty()){
            list = new ArrayList<BusinessaDministrativeDivision>();
            for(BusinessaDministrativeDivision d : divitions){
                list.add(d);
                loadChild(d.getId(),list);
            }
        }
        return list;
    }



    @Override
    public List<BusinessaDministrativeDivision> getDivitionByPid(Long parentId) {
        return businessaDministrativeDivisionMapper.getDivitionByPid(parentId);
    }

}
