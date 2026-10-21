package com.ruoyi.project.system.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.framework.aspectj.lang.annotation.Excel;
import com.ruoyi.framework.web.domain.BaseEntity;

/**
 * 影像对比对象 sys_contrast
 *
 * @author ruoyi
 * @date 2021-04-01
 */
public class SysContrast extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long id;

    /** 父id */
    @Excel(name = "父id")
    private Long parentId;

    /** 节点名 */
    @Excel(name = "节点名")
    private String nodeName;

    /** 节点类型 */
    @Excel(name = "节点类型")
    private Long nodeType;

    /** 数据类型 */
    @Excel(name = "数据类型")
    private Long dataType;

    /** 服务地址 */
    @Excel(name = "服务地址")
    private String serverUrl;

    /** 排序依据 */
    @Excel(name = "排序依据")
    private Long orderBy;

    /** 节点状态 */
    @Excel(name = "节点状态")
    private Long nodeStatus;

    /** 图层名称 */
    @Excel(name = "图层名称")
    private String childNames;

    /** 创建日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "创建日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date createDate;

    /** 更新日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "更新日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date updateDate;

    /** 绑定区域 */
    @Excel(name = "绑定区域")
    private Long bindArea;

    /** 数据年份 */
    @Excel(name = "数据年份")
    private String dataYear;

    /** 数据体量 */
    @Excel(name = "数据体量")
    private String dataVolume;

    /** WMTS参数 */
    @Excel(name = "WMTS参数")
    private String dataParam;

    /** 经纬度 */
    @Excel(name = "经纬度")
    private String position;

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
    public void setNodeType(Long nodeType)
    {
        this.nodeType = nodeType;
    }

    public Long getNodeType()
    {
        return nodeType;
    }
    public void setDataType(Long dataType)
    {
        this.dataType = dataType;
    }

    public Long getDataType()
    {
        return dataType;
    }
    public void setServerUrl(String serverUrl)
    {
        this.serverUrl = serverUrl;
    }

    public String getServerUrl()
    {
        return serverUrl;
    }
    public void setOrderBy(Long orderBy)
    {
        this.orderBy = orderBy;
    }

    public Long getOrderBy()
    {
        return orderBy;
    }
    public void setNodeStatus(Long nodeStatus)
    {
        this.nodeStatus = nodeStatus;
    }

    public Long getNodeStatus()
    {
        return nodeStatus;
    }
    public void setChildNames(String childNames)
    {
        this.childNames = childNames;
    }

    public String getChildNames()
    {
        return childNames;
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
    public void setBindArea(Long bindArea)
    {
        this.bindArea = bindArea;
    }

    public Long getBindArea()
    {
        return bindArea;
    }
    public void setDataYear(String dataYear)
    {
        this.dataYear = dataYear;
    }

    public String getDataYear()
    {
        return dataYear;
    }
    public void setDataVolume(String dataVolume)
    {
        this.dataVolume = dataVolume;
    }

    public String getDataVolume()
    {
        return dataVolume;
    }
    public void setDataParam(String dataParam)
    {
        this.dataParam = dataParam;
    }

    public String getDataParam()
    {
        return dataParam;
    }
    public void setPosition(String position)
    {
        this.position = position;
    }

    public String getPosition()
    {
        return position;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("parentId", getParentId())
                .append("nodeName", getNodeName())
                .append("nodeType", getNodeType())
                .append("dataType", getDataType())
                .append("serverUrl", getServerUrl())
                .append("orderBy", getOrderBy())
                .append("nodeStatus", getNodeStatus())
                .append("childNames", getChildNames())
                .append("createDate", getCreateDate())
                .append("updateDate", getUpdateDate())
                .append("updateBy", getUpdateBy())
                .append("bindArea", getBindArea())
                .append("remark", getRemark())
                .append("dataYear", getDataYear())
                .append("dataVolume", getDataVolume())
                .append("dataParam", getDataParam())
                .append("position", getPosition())
                .toString();
    }
}
