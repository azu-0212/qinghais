package com.ruoyi.project.system.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.framework.aspectj.lang.annotation.Excel;
import com.ruoyi.framework.web.domain.BaseEntity;

/**
 * 数据资源对象 sys_resource
 *
 * @author hash
 * @date 2020-08-19
 */
public class SysResource extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 资源id */
    private Long resourceId;

    /** 父节点id */
    @Excel(name = "父节点id")
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

    /** 图例 */
    @Excel(name = "图例")
    private String legend;

    /** 是否筛选图层 */
    @Excel(name = "是否筛选图层")
    private Long layerSearch;

    /** 查询参数 */
    @Excel(name = "查询参数")
    private String queryPara;

    /** 图层名称 */
    @Excel(name = "图层名称")
    private String childNames;

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

    /** 所属区域id */
    @Excel(name = "所属区域")
    private String dataRegion;

    /** 数据源 */
    @Excel(name = "数据源")
    private String dataSource;

    /** 数据源 */
    @Excel(name = "数据集")
    private String dataSet;

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

    /** 父级节点code */
    @Excel(name = "父级节点")
    private Long parentCode;

    /** 服务地址 */
    @Excel(name = "数据服务地址")
    private String dataServerUrl;

    /** 高程值 */
    @Excel(name = "高程值")
    private String height;

    public String getDataServerUrl() {
        return dataServerUrl;
    }

    public void setDataServerUrl(String dataServerUrl) {
        this.dataServerUrl = dataServerUrl;
    }

    public Long getParentCode() {
        return parentCode;
    }

    public void setParentCode(Long parentCode) {
        this.parentCode = parentCode;
    }

    public void setResourceId(Long resourceId)
    {
        this.resourceId = resourceId;
    }

    public Long getResourceId()
    {
        return resourceId;
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
    public void setLegend(String legend)
    {
        this.legend = legend;
    }

    public String getLegend()
    {
        return legend;
    }
    public void setLayerSearch(Long layerSearch)
    {
        this.layerSearch = layerSearch;
    }

    public Long getLayerSearch()
    {
        return layerSearch;
    }
    public void setQueryPara(String queryPara)
    {
        this.queryPara = queryPara;
    }

    public String getQueryPara()
    {
        return queryPara;
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

    public String getDataYear() {
        return dataYear;
    }

    public void setDataYear(String dataYear) {
        this.dataYear = dataYear;
    }

    public String getDataVolume() {
        return dataVolume;
    }

    public void setDataVolume(String dataVolume) {
        this.dataVolume = dataVolume;
    }

    public String getDataParam() {
        return dataParam;
    }

    public void setDataParam(String dataParam) {
        this.dataParam = dataParam;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getDataRegion() {
        return dataRegion;
    }

    public void setDataRegion(String dataRegion) {
        this.dataRegion = dataRegion;
    }

    public String getDataSource() {
        return dataSource;
    }

    public void setDataSource(String dataSource) {
        this.dataSource = dataSource;
    }

    public String getDataSet() {
        return dataSet;
    }

    public void setDataSet(String dataSet) {
        this.dataSet = dataSet;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("resourceId", getResourceId())
            .append("parentId", getParentId())
            .append("nodeName", getNodeName())
            .append("nodeType", getNodeType())
            .append("dataType", getDataType())
            .append("serverUrl", getServerUrl())
            .append("dataServerUrl", getDataServerUrl())
            .append("orderBy", getOrderBy())
            .append("nodeStatus", getNodeStatus())
            .append("legend", getLegend())
            .append("layerSearch", getLayerSearch())
            .append("queryPara", getQueryPara())
            .append("childNames", getChildNames())
            .append("createDate", getCreateDate())
            .append("dataYear", getDataYear())
            .append("parentCode", getParentCode())
            .append("dataVolume", getDataVolume())
            .append("dataParam", getDataParam())
            .append("position", getPosition())
            .append("updateDate", getUpdateDate())
            .append("dataSource", getDataSource())
            .append("dataSet", getDataSet())
            .append("updateBy", getUpdateBy())
            .append("bindArea", getBindArea())
            .append("height", getHeight())
            .append("remark", getRemark())
            .toString();
    }
}
