package com.ruoyi.project.system.controller;

import java.util.List;

import com.ruoyi.project.system.domain.SysGroup;
import com.ruoyi.project.system.domain.SysImgCode;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.framework.aspectj.lang.annotation.Log;
import com.ruoyi.framework.aspectj.lang.enums.BusinessType;
import com.ruoyi.project.system.domain.SysResource;
import com.ruoyi.project.system.service.ISysResourceService;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.framework.web.page.TableDataInfo;

/**
 * 数据资源Controller
 *
 * @author hash
 * @date 2020-08-19
 */
@RestController
@RequestMapping("/system/resource")
public class SysResourceController extends BaseController
{
    @Autowired
    private ISysResourceService sysResourceService;

    /**
     * 查询数据资源列表
     */
    //@PreAuthorize("@ss.hasPermi('system:resource:list')")
    @GetMapping("/list")
    public TableDataInfo list(SysResource sysResource)
    {
        startPage();
        List<SysResource> list = sysResourceService.selectSysResourceList(sysResource);
        return getDataTable(list);
    }

    /**
     * 根据用户权限查询数据资源列表
     */
    //@PreAuthorize("@ss.hasPermi('system:resource:list')")
    @GetMapping("/webRole/{roleType}")
    public TableDataInfo getRolesResourceList(@PathVariable("roleType") String role)
    {
        startPage();
        List<SysResource> list = sysResourceService.selectSysRoleResourceList(role);
        return getDataTable(list);
    }

    /**
     * 查询数据资源列表树结构 ，只显示状态大于0的
     */
    @GetMapping("/tree")
    public TableDataInfo listByTree()
    {
        startPage();
        List<SysResource> list = sysResourceService.selectSysResourceListByTree();
        return getDataTable(list);
    }

    /**
     * 查询数据资源列表叶子节点
     */
    @GetMapping("/leaf")
    public TableDataInfo listByLeaf()
    {
        startPage();
        List<SysResource> list = sysResourceService.selectSysResourceListByLeaf();
        return getDataTable(list);
    }

    /**
     * 导出数据资源列表
     */
    //@PreAuthorize("@ss.hasPermi('system:resource:export')")
    @Log(title = "数据资源", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(SysResource sysResource)
    {
        List<SysResource> list = sysResourceService.selectSysResourceList(sysResource);
        ExcelUtil<SysResource> util = new ExcelUtil<SysResource>(SysResource.class);
        return util.exportExcel(list, "resource");
    }

    /**
     * 获取数据资源详细信息
     */
    //@PreAuthorize("@ss.hasPermi('system:resource:query')")
    @GetMapping(value = "/{resourceId}")
    public AjaxResult getInfo(@PathVariable("resourceId") Long resourceId)
    {
        return AjaxResult.success(sysResourceService.selectSysResourceById(resourceId));
    }

    /**
     * 根据父级节点类型获取数据资源详细信息
     */
    @GetMapping(value = "/code/{typeCode}")
    public TableDataInfo getInfoByDataType(@PathVariable("typeCode") Long typeCode)
    {
        startPage();
        List<SysResource> list = sysResourceService.selectSysResourceByDataType(typeCode);
        return getDataTable(list);
    }

    /**
     * 根据多级父级节点类型获取数据资源详细信息
     */
    @GetMapping(value = "/tree/{typeCode}")
    public TableDataInfo getInfoByDataTypeTree(@PathVariable("typeCode") Long typeCode)
    {
        startPage();
        List<SysResource> list = sysResourceService.selectSysResourceByDataTypeTree(typeCode);
        return getDataTable(list);
    }

    /**
     * 根据数据类型查询年份列表
     */
    @GetMapping(value = "/year/{dataType}")
    public List getInfoByDataYear(@PathVariable("dataType") Long dataType)
    {
        List<String> list = sysResourceService.selectSysResourceByDataYear(dataType);
        return list;
    }

    /**
     * 根据数据所属区域(州市)分组查询
     */
    @GetMapping(value = "/region/{dataType}")
    public List getResourceRegionByGroup(@PathVariable("dataType") Long dataType)
    {
        List<SysGroup> list = sysResourceService.selectSysResourceRegionByGroup(dataType);
        return list;
    }

    /**
     * 根据数据所属区域(州市)分组查询
     */
    @GetMapping(value = "/yearRegion/")
    public List getResourceRegionYearByGroup(SysResource sysResource)
    {
        List<SysGroup> list = sysResourceService.selectSysResourceRegionYearByGroup(sysResource);
        return list;
    }

    /**
     * 根据数据类型  年份 查询数据列表
     */
    @GetMapping(value = "/type/{dataType}/year/{year}")
    public TableDataInfo getInfoByDataTypeYear(@PathVariable("dataType") Long dataType,@PathVariable("year") Long year)
    {
        List<SysResource> list = sysResourceService.selectSysResourceByDataTypeYear(dataType,year);
        return getDataTable(list);
    }

    /**
     * 根据用户角色进行自然资源目录分配
     */
    @GetMapping(value = "/role/{roleType}/resourceIds/{resourceAutoIds}")
    public AjaxResult updateResourceAssignRoles(@PathVariable("roleType") String roleType,@PathVariable("resourceAutoIds") Long[] resourceAutoIds)
    {
        return toAjax(sysResourceService.updateResourceAssignRoles(roleType, resourceAutoIds));
    }

    /**
     * 清空历史分配
     */
    @GetMapping(value = "/delRole/{roleType}")
    public AjaxResult deleteAssignRoles(@PathVariable("roleType") String roleType)
    {
        return toAjax(sysResourceService.deleteAssignRoles(roleType));
    }

    /**
     * 根据数据类型获取数据资源详细信息
     */
    @GetMapping(value = "/type/{dataType}")
    public TableDataInfo getInfoByType(@PathVariable("dataType") Long dataType)
    {
        startPage();
        List<SysResource> list = sysResourceService.selectSysResourceByType(dataType);
        return getDataTable(list);
    }

    /**
     * 新增数据资源
     */
    //@PreAuthorize("@ss.hasPermi('system:resource:add')")
    @Log(title = "数据资源", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SysResource sysResource)
    {
        return toAjax(sysResourceService.insertSysResource(sysResource));
    }

    /**
     * 修改数据资源
     */
    //@PreAuthorize("@ss.hasPermi('system:resource:edit')")
    @Log(title = "数据资源", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SysResource sysResource)
    {
        return toAjax(sysResourceService.updateSysResource(sysResource));
    }

    /**
     * 修改数据状态
     */
    @PutMapping("/id/{resourceId}/status/{status}")
    public AjaxResult updateStatus(@PathVariable("resourceId") Long resourceId,@PathVariable("status") Long status)
    {
        return toAjax(sysResourceService.updateSysResourceStatus(resourceId,status));
    }

    /**
     * 删除数据资源
     */
    @PreAuthorize("@ss.hasPermi('system:resource:remove')")
    @Log(title = "数据资源", businessType = BusinessType.DELETE)
	@DeleteMapping("/{resourceIds}")
    public AjaxResult remove(@PathVariable Long[] resourceIds)
    {
        return toAjax(sysResourceService.deleteSysResourceByIds(resourceIds));
    }

    @GetMapping("/address")
    public TableDataInfo getPictureAddress(SysImgCode sysImgCode){

        startPage();
        List<SysImgCode> list = sysResourceService.selectPictureAddress(sysImgCode);
        return getDataTable(list);
    }
}
