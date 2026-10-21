package com.ruoyi.project.system.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.framework.aspectj.lang.annotation.Excel;
import com.ruoyi.framework.web.domain.BaseEntity;

/**
 * 来源类型对象 sys_source_type
 *
 * @author ruoyi
 * @date 2021-05-05
 */
public class SysSourceType extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long id;

    /** 父节点id */
    @Excel(name = "父节点id")
    private Long parentId;

    /** 关联资源id */
    @Excel(name = "关联资源id")
    private Long resourceId;

    /** 节点名 */
    @Excel(name = "节点名")
    private String nodeName;

    /** 节点状态 */
    @Excel(name = "节点状态")
    private Long nodeStatus;

    /** 创建日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "创建日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date createDate;

    /** 更新日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "更新日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date updateDate;

    /** 节点类型 */
    @Excel(name = "节点类型")
    private Long nodeType;

    private SysResource resource;

    public SysResource getResource() {
        return resource;
    }

    public void setResource(SysResource resource) {
        this.resource = resource;
    }

    public Long getResourceId() {
        return resourceId;
    }

    public void setResourceId(Long resourceId) {
        this.resourceId = resourceId;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }
    public void setParentId(Long parentId)
    {
        this.parentId = parentId;
    }

    public Long getParentId()
    {
        return parentId;
    }
    public void setNodeName(String nodeName)
    {
        this.nodeName = nodeName;
    }

    public String getNodeName()
    {
        return nodeName;
    }
    public void setNodeStatus(Long nodeStatus)
    {
        this.nodeStatus = nodeStatus;
    }

    public Long getNodeStatus()
    {
        return nodeStatus;
    }
    public void setCreateDate(Date createDate)
    {
        this.createDate = createDate;
    }

    public Date getCreateDate()
    {
        return createDate;
    }
    public void setUpdateDate(Date updateDate)
    {
        this.updateDate = updateDate;
    }

    public Date getUpdateDate()
    {
        return updateDate;
    }
    public void setNodeType(Long nodeType)
    {
        this.nodeType = nodeType;
    }

    public Long getNodeType()
    {
        return nodeType;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("parentId", getParentId())
                .append("nodeName", getNodeName())
                .append("nodeStatus", getNodeStatus())
                .append("createDate", getCreateDate())
                .append("updateDate", getUpdateDate())
                .append("updateBy", getUpdateBy())
                .append("resource", getResource())
                .append("remark", getRemark())
                .append("nodeType", getNodeType())
                .toString();
    }
}
