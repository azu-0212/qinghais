package com.ruoyi.project.system.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class SysResourceRole {
    private String roleType;
    private Long[] resourceAutoIds;

    public String getRoleType() {
        return roleType;
    }

    public void setRoleType(String roleType) {
        this.roleType = roleType;
    }

    public Long[] getResourceAutoIds() {
        return resourceAutoIds;
    }

    public void setResourceAutoIds(Long[] resourceAutoIds) {
        this.resourceAutoIds = resourceAutoIds;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("roleType", getRoleType())
                .append("resourceAutoIds", getResourceAutoIds())
                .toString();
    }
}

