package com.ruoyi.project.system.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.framework.aspectj.lang.annotation.Excel;
import com.ruoyi.framework.web.domain.BaseEntity;

/**
 * 三维图层资源对象 businessa_layer_info
 *
 * @author ruoyi
 * @date 2020-06-18
 */
public class BusinessaLayerInfo extends BaseEntity
{
    private static final String QINGXIE = "倾斜摄影";

    private static final String QUANJING = "全景";

    private static final String JIEJING = "街景";

    private static final long serialVersionUID = 1L;

    /** 三维图层id */
    private Long id;

    /** 行政区划外键id */
    @Excel(name = "行政区划外键id")
    private Long divisionId;

    /** 三维图层名称 */
    @Excel(name = "三维图层名称")
    private String name;

    /** 类型（0倾斜摄影 1全景 2街景 ） */
    @Excel(name = "类型", readConverterExp = "0=倾斜摄影,1=全景,2=街景")
    private String type;

    /** 翻滚角 */
    @Excel(name = "翻滚角")
    private String roll;

    /** 仰俯角 */
    @Excel(name = "仰俯角")
    private String pitch;

    /** 偏航角 */
    @Excel(name = "偏航角")
    private String heading;

    /** $column.columnComment */
    @Excel(name = "偏航角")
    private String postion;

    /** 三维图层服务地址 */
    @Excel(name = "三维图层服务地址")
    private String url;

    /** 数据年份 */
    @Excel(name = "数据年份")
    private String dataYear;

    /** 数据体量 */
    @Excel(name = "数据体量")
    private String dataVolume;

    /** 数据体量 */
    @Excel(name = "所属区县")
    private String divisionName;

    private String num;

    private String typeName;

    private String total;

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
    public void setDivisionId(Long divisionId)
    {
        this.divisionId = divisionId;
    }

    public Long getDivisionId()
    {
        return divisionId;
    }
    public void setName(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return name;
    }
    public void setType(String type)
    {
        this.type = type;
    }

    public String getType()
    {
        return type;
    }
    public void setRoll(String roll)
    {
        this.roll = roll;
    }

    public String getRoll()
    {
        return roll;
    }
    public void setPitch(String pitch)
    {
        this.pitch = pitch;
    }

    public String getPitch()
    {
        return pitch;
    }
    public void setHeading(String heading)
    {
        this.heading = heading;
    }

    public String getHeading()
    {
        return heading;
    }
    public void setPostion(String postion)
    {
        this.postion = postion;
    }

    public String getPostion()
    {
        return postion;
    }
    public void setUrl(String url)
    {
        this.url = url;
    }

    public String getUrl()
    {
        return url;
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
    public void setDelFlag(String delFlag)
    {
        this.delFlag = delFlag;
    }

    public String getDelFlag()
    {
        return delFlag;
    }

    public String getDivisionName() {
        return divisionName;
    }


    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("divisionId", getDivisionId())
            .append("name", getName())
            .append("type", getType())
            .append("roll", getRoll())
            .append("pitch", getPitch())
            .append("heading", getHeading())
            .append("postion", getPostion())
            .append("url", getUrl())
            .append("dataYear", getDataYear())
            .append("dataVolume", getDataVolume())
            .append("updateTime", getUpdateTime())
            .append("createTime", getCreateTime())
            .append("delFlag", getDelFlag())
            .append("divisionName", getDelFlag())
            .append("typeName", getTypeName())
            .append("total", getTotal())
            .append("num", getNum())
            .append("divisionName",getDivisionName())
            .toString();
    }



    public String getTypeName() {
        if(this.type != null){
            switch (this.type){
                case "0":
                    this.typeName = this.QINGXIE;
                    break;
                case "1":
                    this.typeName = this.QUANJING;
                    break;
                case "2":
                    this.typeName = this.JIEJING;
                    break;
                default:
                    break;
            }
        }
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getTotal() {
        return total;
    }

    public String getNum() {
        return num;
    }
}
