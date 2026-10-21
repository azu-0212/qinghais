package com.ruoyi.project.system.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.framework.aspectj.lang.annotation.Excel;
import com.ruoyi.framework.web.domain.TreeEntity;

/**
 * 行政区划对象 businessa_dministrative_division
 *
 * @author ruoyi
 * @date 2020-06-19
 */
public class BusinessaDministrativeDivision extends TreeEntity
{
    private static final long serialVersionUID = 1L;

    /** 行政区划id */
    private Long id;

    /** 行政区划名称 */
    @Excel(name = "行政区划名称")
    private String name;

    /** 行政区划code */
    @Excel(name = "行政区划code")
    private String code;

    /** 类型（0倾斜摄影 1全景 2街景 ） */
    @Excel(name = "类型", readConverterExp = "0=倾斜摄影,1=全景,2=街景")
    private String type;

    /** 删除标志（0代表存在 2代表删除） */
    private String delFlag;

    /**父级名称 */
    private BusinessaDministrativeDivision parentName;

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }
    public void setName(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return name;
    }
    public void setCode(String code)
    {
        this.code = code;
    }

    public String getCode()
    {
        return code;
    }
    public void setType(String type)
    {
        this.type = type;
    }

    public String getType()
    {
        return type;
    }
    public void setDelFlag(String delFlag)
    {
        this.delFlag = delFlag;
    }

    public String getDelFlag()
    {
        return delFlag;
    }

    public BusinessaDministrativeDivision getParentName() {
        return parentName;
    }

    public void setParentName(BusinessaDministrativeDivision parentName) {
        this.parentName = parentName;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("parentId", getParentId())
            .append("ancestors", getAncestors())
            .append("name", getName())
            .append("code", getCode())
            .append("type", getType())
            .append("orderNum", getOrderNum())
            .append("delFlag", getDelFlag())
            .append("createTime", getCreateTime())
            .append("parentName", getParentName())
            .append("remark", getRemark())
            .toString();
    }
}
