package com.ruoyi.project.system.service.impl;

import java.util.List;

import com.ruoyi.project.system.domain.SysGroup;
import com.ruoyi.project.system.domain.SysImgCode;
import com.ruoyi.project.system.domain.SysSourceType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.project.system.mapper.SysResourceMapper;
import com.ruoyi.project.system.domain.SysResource;
import com.ruoyi.project.system.service.ISysResourceService;

/**
 * 数据资源Service业务层处理
 *
 * @author hash
 * @date 2020-08-19
 */
@Service
public class SysResourceServiceImpl implements ISysResourceService
{
    @Autowired
    private SysResourceMapper sysResourceMapper;

    /**
     * 查询数据资源
     *
     * @param resourceId 数据资源ID
     * @return 数据资源
     */
    @Override
    public SysResource selectSysResourceById(Long resourceId)
    {
        return sysResourceMapper.selectSysResourceById(resourceId);
    }

    /**
     * 根据类型查询数据资源
     *
     * @param typeCode
     * @return 数据资源
     */
    @Override
    public List<SysResource> selectSysResourceByDataType(Long typeCode)
    {
        return sysResourceMapper.selectSysResourceByDataType(typeCode);
    }

    /**
     * 根据用户权限查询数据资源列表
     *
     * @param role 用户
     */
    @Override
    public List<SysResource> selectSysRoleResourceList(String role)
    {
        return sysResourceMapper.selectSysRoleResourceList(role);
    }

    /**
     * 根据数据类型查询数据资源
     *
     * @param dataType
     * @return 数据资源
     */
    @Override
    public List<SysResource> selectSysResourceByType(Long dataType)
    {
        return sysResourceMapper.selectSysResourceByType(dataType);
    }

    /**
     * 根据数据类型查询年份列表
     *
     * @param dataType
     * @return 数据资源
     */
    @Override
    public List<String> selectSysResourceByDataYear(Long dataType)
    {
        return sysResourceMapper.selectSysResourceByDataYear(dataType);
    }

    /**
     * 根据数据所属区域分组查询
     *
     * @param dataType
     * @return 数据资源
     */
    @Override
    public List<SysGroup> selectSysResourceRegionByGroup(Long dataType)
    {
        //数据类型为倾斜摄影数据
        if(dataType == 1){
            return sysResourceMapper.selectSysResourceRegionByGroup(dataType);
        }
        //数据类型为街景数据
        else{
            return sysResourceMapper.selectSysResourceStreetByGroup(dataType);
        }

    }

    /**
     * 根据数据类型  年份 查询数据列表
     *
     * @param dataType
     * @return 数据资源
     */
    @Override
    public List<SysResource> selectSysResourceByDataTypeYear(Long dataType, Long year)
    {
        return sysResourceMapper.selectSysResourceByDataTypeYear(dataType,year);
    }

    /**
     * 根据数据类型  不同年份 查询区域
     *
     * @param sysResource
     * @return 数据资源
     */
    @Override
    public List<SysGroup> selectSysResourceRegionYearByGroup(SysResource sysResource)
    {
        return sysResourceMapper.selectSysResourceRegionYearByGroup(sysResource);
    }

    /**
     * 根据多级树节点类型查询数据资源
     *
     * @param typeCode
     * @return 数据资源
     */
    @Override
    public List<SysResource> selectSysResourceByDataTypeTree(Long typeCode)
    {
        return sysResourceMapper.selectSysResourceByDataTypeTree(typeCode);
    }

    /**
     * 查询数据资源列表
     *
     * @param sysResource 数据资源
     * @return 数据资源
     */
    @Override
    public List<SysResource> selectSysResourceList(SysResource sysResource)
    {
        return sysResourceMapper.selectSysResourceList(sysResource);
    }

    /**
     * 查询数据资源列表树结构  状态大于0的
     *
     * @return 数据资源
     */
    @Override
    public List<SysResource> selectSysResourceListByTree() {
        return sysResourceMapper.selectSysResourceListByTree();

    }

    /**
     * 查询数据资源列表叶子节点
     *
     * @return 数据资源
     */
    @Override
    public List<SysResource> selectSysResourceListByLeaf()
    {
        return sysResourceMapper.selectSysResourceListByLeaf();
    }

    /**
     * 新增数据资源
     *
     * @param sysResource 数据资源
     * @return 结果
     */
    @Override
    public int insertSysResource(SysResource sysResource)
    {
        return sysResourceMapper.insertSysResource(sysResource);
    }

    /**
     * 修改数据资源
     *
     * @param sysResource 数据资源
     * @return 结果
     */
    @Override
    public int updateSysResource(SysResource sysResource)
    {
        return sysResourceMapper.updateSysResource(sysResource);
    }

    /**
     * 修改数据状态
     *
     * @param
     * @return 结果
     */
    @Override
    public int updateSysResourceStatus(Long resourceId,Long status)
    {
        return sysResourceMapper.updateSysResourceStatus(resourceId,status);
    }

    /**
     * 根据用户角色进行自然资源目录分配
     *
     * @param
     * @return 结果
     */
    @Override
    public int updateResourceAssignRoles(String roleType, Long[] resourceAutoIds)
    {
        System.out.println(roleType + "-----" + resourceAutoIds.toString());
        return sysResourceMapper.updateResourceAssignRoles(roleType, resourceAutoIds);
    }

    /**
     * 清空历史分配
     *
     * @param
     * @return 结果
     */
    @Override
    public int deleteAssignRoles(String roleType)
    {
        return sysResourceMapper.deleteAssignRoles(roleType);
    }

    /**
     * 批量删除数据资源
     *
     * @param resourceIds 需要删除的数据资源ID
     * @return 结果
     */
    @Override
    public int deleteSysResourceByIds(Long[] resourceIds)
    {
        return sysResourceMapper.deleteSysResourceByIds(resourceIds);
    }

    /**
     * 删除数据资源信息
     *
     * @param resourceId 数据资源ID
     * @return 结果
     */
    @Override
    public int deleteSysResourceById(Long resourceId)
    {
        return sysResourceMapper.deleteSysResourceById(resourceId);
    }

    /**
     * 查询照片地址
     *
     * @param sysImgCode 照片地址ID
     * @return 照片地址
     */
    @Override
    public List<SysImgCode> selectPictureAddress(SysImgCode sysImgCode)
    {
        return sysResourceMapper.selectPictureAddress(sysImgCode);
    }
}
