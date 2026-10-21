package com.ruoyi.project.system.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.framework.aspectj.lang.annotation.Excel;
import com.ruoyi.framework.web.domain.BaseEntity;

/**
 * 地图配置对象 map_config
 * 
 * @author ruoyi
 * @date 2020-06-28
 */
public class MapConfig extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long configId;

    /** 名称 */
    @Excel(name = "名称")
    private String configName;

    /** json字符串 */
    @Excel(name = "json字符串")
    private String configJson;

    /** 标识码 */
    @Excel(name = "标识码")
    private String configCode;

    /** 备注 */
    @Excel(name = "备注")
    private String configRemark;

    /** 修改时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "修改时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date configUptime;

    public void setConfigId(Long configId) 
    {
        this.configId = configId;
    }

    public Long getConfigId() 
    {
        return configId;
    }
    public void setConfigName(String configName) 
    {
        this.configName = configName;
    }

    public String getConfigName() 
    {
        return configName;
    }
    public void setConfigJson(String configJson) 
    {
        this.configJson = configJson;
    }

    public String getConfigJson() 
    {
        return configJson;
    }
    public void setConfigCode(String configCode) 
    {
        this.configCode = configCode;
    }

    public String getConfigCode() 
    {
        return configCode;
    }
    public void setConfigRemark(String configRemark) 
    {
        this.configRemark = configRemark;
    }

    public String getConfigRemark() 
    {
        return configRemark;
    }
    public void setConfigUptime(Date configUptime) 
    {
        this.configUptime = configUptime;
    }

    public Date getConfigUptime() 
    {
        return configUptime;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("configId", getConfigId())
            .append("configName", getConfigName())
            .append("configJson", getConfigJson())
            .append("configCode", getConfigCode())
            .append("configRemark", getConfigRemark())
            .append("configUptime", getConfigUptime())
            .toString();
    }
}
