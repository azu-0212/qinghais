package com.ruoyi.project.system.domain;

public class SysGroup {

    /** 所属区域类型 */
    private String dataRegion;

    /** 个数 */
    private String num;

    /** 字典标签 */
    private String dictLabel;

    /** 节点状态  */
    private Long nodeStatus;

    public Long getNodeStatus() {
        return nodeStatus;
    }

    public void setNodeStatus(Long nodeStatus) {
        this.nodeStatus = nodeStatus;
    }

    public String getDataRegion() {
        return dataRegion;
    }

    public void setDataRegion(String dataRegion) {
        this.dataRegion = dataRegion;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getDictLabel() {
        return dictLabel;
    }

    public void setDictLabel(String dictLabel) {
        this.dictLabel = dictLabel;
    }
}
