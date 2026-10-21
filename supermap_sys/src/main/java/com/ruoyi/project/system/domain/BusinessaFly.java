package com.ruoyi.project.system.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.framework.aspectj.lang.annotation.Excel;
import com.ruoyi.framework.web.domain.BaseEntity;

/**
 * 飞行路线对象 businessa_fly
 *
 * @author ruoyi
 * @date 2020-06-18
 */
public class BusinessaFly extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 飞行路线 */
    private Long id;

    /** 飞行路线路径 */
    @Excel(name = "飞行路线路径")
    private String flyPath;

    /** 关联倾斜摄影模型 */
    @Excel(name = "关联模型")
    private Long resourceId;

    /** 飞行路线名称 */
    @Excel(name = "飞行路线名称")
    private String flyName;

    @Excel(name = "飞行图片名称")
    private String imgPath;

    /** 模型信息 */
    private SysResource sysResource;

    /** 删除标志（0代表存在 2代表删除） */
    private String delFlag;

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }
    public void setFlyPath(String flyPath)
    {
        this.flyPath = flyPath;
    }

    public String getFlyPath()
    {
        return flyPath;
    }
    public void setFlyName(String flyName)
    {
        this.flyName = flyName;
    }

    public String getFlyName()
    {
        return flyName;
    }
    public void setDelFlag(String delFlag)
    {
        this.delFlag = delFlag;
    }

    public String getDelFlag()
    {
        return delFlag;
    }

    public Long getResourceId() {
        return resourceId;
    }

    public void setResourceId(Long resourceId) {
        this.resourceId = resourceId;
    }


    public SysResource getSysResource() {
        return sysResource;
    }

    public void setSysResource(SysResource sysResource) {
        this.sysResource = sysResource;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("resourceId", getResourceId())
            .append("flyPath", getFlyPath())
            .append("flyName", getFlyName())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .append("delFlag", getDelFlag())
            .append("imgPath", getImgPath())
            .append("sysResource", getSysResource())
            .toString();
    }

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }
}
