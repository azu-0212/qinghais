package com.ruoyi.project.system.domain;

//import javafx.scene.text.Text;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.framework.aspectj.lang.annotation.Excel;
import com.ruoyi.framework.web.domain.BaseEntity;

/**
 * 重点区域定位对象 businessa_key_areas
 *
 * @author ruoyi
 * @date 2020-06-18
 */
public class BusinessaKeyAreas extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 重点区域定位id */
    private Long id;

    /** 全景id */
    private String resourceId;

    /** 视频 */
    @Excel(name = "视频")
    private Long videoId;

    /** 重点区域图片path */
    @Excel(name = "重点区域图片path")
    private String imgPath;

    /** 重点区域名称 */
    @Excel(name = "重点区域名称")
    private String imgName;

    /** 重点区域坐标 */
    @Excel(name = "重点区域坐标")
    private String position;

    /** 矢量数据服务地址 */
    @Excel(name = "服务地址")
    private String serverUrl;

    /** 描述 */
    @Excel(name = "描述")
    private String description;

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
    public void setImgPath(String imgPath)
    {
        this.imgPath = imgPath;
    }

    public String getImgPath()
    {
        return imgPath;
    }
    public void setImgName(String imgName)
    {
        this.imgName = imgName;
    }

    public String getImgName()
    {
        return imgName;
    }
    public void setPosition(String position)
    {
        this.position = position;
    }

    public String getPosition()
    {
        return position;
    }
    public void setDelFlag(String delFlag)
    {
        this.delFlag = delFlag;
    }

    public String getDelFlag()
    {
        return delFlag;
    }

    public String getServerUrl() {
        return serverUrl;
    }

    public void setServerUrl(String serverUrl) {
        this.serverUrl = serverUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getResourceId() {
        return resourceId;
    }
    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }

    public Long getVideoId() {
        return videoId;
    }

    public void setVideoId(Long videoId) {
        this.videoId = videoId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("imgPath", getImgPath())
            .append("resourceIs", getResourceId())
            .append("imgName", getImgName())
            .append("position", getPosition())
            .append("serverUrl", getServerUrl())
            .append("description", getDescription())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .append("delFlag", getDelFlag())
            .append("videoId", getVideoId())
            .toString();
    }
}
