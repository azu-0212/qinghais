package com.ruoyi.project.system.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.framework.aspectj.lang.annotation.Excel;
import com.ruoyi.framework.web.domain.BaseEntity;

/**
 * 地理单元对象 sys_unit
 *
 * @author ruoyi
 * @date 2021-03-25
 */
public class SysUnit extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long id;

    /** 父id */
    @Excel(name = "父id")
    private Long parentId;

    /** 更新日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "更新日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date updateDate;

    /** 服务地址 */
    @Excel(name = "服务地址")
    private String serverUrl;

    /** 节点名 */
    @Excel(name = "节点名")
    private String nodeName;

    /** 节点状态 */
    @Excel(name = "节点状态")
    private Long nodeStatus;

    /** 节点类型 */
    @Excel(name = "节点类型")
    private Long nodeType;

    /** 节点类型 */
    @Excel(name = "数据类型")
    private Long dataType;

    public Long getDataType() {
        return dataType;
    }

    public void setDataType(Long dataType) {
        this.dataType = dataType;
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
    public void setUpdateDate(Date updateDate)
    {
        this.updateDate = updateDate;
    }

    public Date getUpdateDate()
    {
        return updateDate;
    }
    public void setServerUrl(String serverUrl)
    {
        this.serverUrl = serverUrl;
    }

    public String getServerUrl()
    {
        return serverUrl;
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
                .append("createTime", getCreateTime())
                .append("updateDate", getUpdateDate())
                .append("updateBy", getUpdateBy())
                .append("remark", getRemark())
                .append("serverUrl", getServerUrl())
                .append("nodeName", getNodeName())
                .append("nodeStatus", getNodeStatus())
                .append("nodeType", getNodeType())
                .append("dataType",getDataType())
                .toString();
    }
}
