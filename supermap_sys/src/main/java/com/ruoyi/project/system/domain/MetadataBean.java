package com.ruoyi.project.system.domain;

import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

@Data
public class MetadataBean {
    // 上传图片文件
    private String fileFolder;
    // 照片文件县区
    private String area;
    // 照片文件编码类型字段
    private String fileCode;
    // 照片类型(前/中/后)
    private String imageType;
    // 照片根目录标签
    private String imageLabel;

    public String getImageType() {
        return imageType;
    }

    public void setImageType(String imageType) {
        this.imageType = imageType;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getFileCode() {
        return fileCode;
    }

    public void setFileCode(String fileCode) {
        this.fileCode = fileCode;
    }

    public String getImageLabel() {
        return imageLabel;
    }

    public void setImageLabel(String imageLabel) {
        this.imageLabel = imageLabel;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("area", getArea())
                .append("imageType", getImageType())
                .append("fileCode", getFileCode())
                .append("imageLabel", getFileCode())
                .toString();
    }
}
