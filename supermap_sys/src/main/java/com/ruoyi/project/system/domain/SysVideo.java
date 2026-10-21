package com.ruoyi.project.system.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.framework.aspectj.lang.annotation.Excel;
import com.ruoyi.framework.web.domain.BaseEntity;

/**
 * 视频管理对象 sys_video
 *
 * @author ruoyi
 * @date 2021-03-24
 */
public class SysVideo extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long id;

    /** 名称 */
    @Excel(name = "名称")
    private String videoName;

    /** 视频类型 */
    @Excel(name = "视频类型")
    private String videoType;

    /** 视频路径 */
    @Excel(name = "视频路径")
    private String videoSrc;

    /** 缩略图 */
    @Excel(name = "缩略图")
    private String imgPath;

    /** 位置 */
    @Excel(name = "位置")
    private String position;

    /** 描述 */
    @Excel(name = "描述")
    private String description;

    /** 数据体量 */
    @Excel(name = "数据体量")
    private String dataVolume;

    /** 状态 */
    @Excel(name = "状态")
    private Integer status;

    public String getDataVolume() {
        return dataVolume;
    }

    public void setDataVolume(String dataVolume) {
        this.dataVolume = dataVolume;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }
    public void setVideoName(String videoName)
    {
        this.videoName = videoName;
    }

    public String getVideoName()
    {
        return videoName;
    }
    public void setVideoType(String videoType)
    {
        this.videoType = videoType;
    }

    public String getVideoType()
    {
        return videoType;
    }
    public void setVideoSrc(String videoSrc)
    {
        this.videoSrc = videoSrc;
    }

    public String getVideoSrc()
    {
        return videoSrc;
    }
    public void setImgPath(String imgPath)
    {
        this.imgPath = imgPath;
    }

    public String getImgPath()
    {
        return imgPath;
    }
    public void setPosition(String position)
    {
        this.position = position;
    }

    public String getPosition()
    {
        return position;
    }
    public void setDescription(String description)
    {
        this.description = description;
    }

    public String getDescription()
    {
        return description;
    }
    public void setStatus(Integer status)
    {
        this.status = status;
    }

    public Integer getStatus()
    {
        return status;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("videoName", getVideoName())
                .append("videoType", getVideoType())
                .append("videoSrc", getVideoSrc())
                .append("imgPath", getImgPath())
                .append("position", getPosition())
                .append("description", getDescription())
                .append("dataVolume", getDataVolume())
                .append("status", getStatus())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .append("remark", getRemark())
                .toString();
    }
}
