package com.ruoyi.project.system.service;

import java.util.List;

import com.ruoyi.project.system.domain.SysGroup;
import com.ruoyi.project.system.domain.SysImgCode;
import com.ruoyi.project.system.domain.SysResource;

/**
 * 数据资源Service接口
 *
 * @author hash
 * @date 2020-08-19
 */
public interface ISysResourceService
{
    /**
     * 查询数据资源
     *
     * @param resourceId 数据资源ID
     * @return 数据资源
     */
    public SysResource selectSysResourceById(Long resourceId);

    /**
     * 根据单级树节点类型查询数据资源
     *
     * @param typeCode
     * @return 数据资源
     */
    public List<SysResource> selectSysResourceByDataType(Long typeCode);

    /**
     * 根据用户权限查询数据资源列表
     *
     * @param role
     * @return 数据资源
     */
    public List<SysResource> selectSysRoleResourceList(String role);

    /**
     * 根据数据类型查询数据资源
     *
     * @param dataType
     * @return 数据资源
     */
    public List<SysResource> selectSysResourceByType(Long dataType);

    /**
     * 根据数据类型查询年份列表
     *
     * @param dataType
     * @return 数据资源
     */
    public List<String> selectSysResourceByDataYear(Long dataType);

    /**
     * 根据数据所属区域年份分组查询--倾斜摄影扇形图
     *
     * @param sysResource
     * @return 数据资源
     */
    public List<SysGroup> selectSysResourceRegionYearByGroup(SysResource sysResource);

    /**
     * 根据数据所属区域分组查询--图表
     *
     * @param dataType
     * @return 数据资源
     */
    public List<SysGroup> selectSysResourceRegionByGroup(Long dataType);

    /**
     * 根据数据类型 年份 查询数据列表
     *
     * @param dataType
     * @return 数据资源
     */
    public List<SysResource> selectSysResourceByDataTypeYear(Long dataType, Long year);

    /**
     * 根据多级树节点类型查询数据资源
     *
     * @param typeCode
     * @return 数据资源
     */
    public List<SysResource> selectSysResourceByDataTypeTree(Long typeCode);


    /**
     * 查询数据资源列表
     *
     * @param sysResource 数据资源
     * @return 数据资源集合
     */
    public List<SysResource> selectSysResourceList(SysResource sysResource);

    /**
     * 查询数据资源列表树结构
     *
     * @return 数据资源集合
     */
    public List<SysResource> selectSysResourceListByTree();

    /**
     * 查询数据资源列表叶子节点
     *
     * @return 数据资源集合
     */
    public List<SysResource> selectSysResourceListByLeaf();

    /**
     * 新增数据资源
     *
     * @param sysResource 数据资源
     * @return 结果
     */
    public int insertSysResource(SysResource sysResource);

    /**
     * 修改数据资源
     *
     * @param sysResource 数据资源
     * @return 结果
     */
    public int updateSysResource(SysResource sysResource);

    /**
     * 根据用户角色进行自然资源目录分配
     *
     * @param
     * @return 结果
     */
    public int updateResourceAssignRoles(String roleType, Long[] resourceAutoIds);

    /**
     * 清空历史分配
     *
     * @param
     * @return 结果
     */
    public int deleteAssignRoles(String roleType);

    /**
     * 修改数据状态
     *
     * @param
     * @return 结果
     */
    public int updateSysResourceStatus(Long resourceId, Long status);

    /**
     * 批量删除数据资源
     *
     * @param resourceIds 需要删除的数据资源ID
     * @return 结果
     */
    public int deleteSysResourceByIds(Long[] resourceIds);

    /**
     * 删除数据资源信息
     *
     * @param resourceId 数据资源ID
     * @return 结果
     */
    public int deleteSysResourceById(Long resourceId);

    /**
     * 查询照片地址
     *
     * @param sysImgCode 照片地址ID
     * @return 照片地址
     */
    public List<SysImgCode> selectPictureAddress(SysImgCode sysImgCode);
}
