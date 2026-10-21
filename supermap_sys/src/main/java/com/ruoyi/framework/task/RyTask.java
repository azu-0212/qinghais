package com.ruoyi.framework.task;

import com.ruoyi.project.system.domain.BusinessaFly;
import com.ruoyi.project.system.domain.BusinessaKeyAreas;
import com.ruoyi.project.system.service.IBusinessaFlyService;
import com.ruoyi.project.system.service.IBusinessaKeyAreasService;
import com.ruoyi.project.system.utils.DeleteFileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.ruoyi.common.utils.StringUtils;
import java.util.List;

/**
 * 定时任务调度测试
 *
 * @author ruoyi
 */
@Component("ryTask")
public class RyTask
{

    @Autowired
    private IBusinessaFlyService businessaFlyService;

    @Autowired
    private IBusinessaKeyAreasService businessaKeyAreasService;

    public void ryMultipleParams(String s, Boolean b, Long l, Double d, Integer i)
    {
        System.out.println(StringUtils.format("执行多参方法： 字符串类型{}，布尔类型{}，长整型{}，浮点型{}，整形{}", s, b, l, d, i));
    }

    public void ryParams(String params)
    {
        System.out.println("执行有参方法：" + params);
    }

    public void ryNoParams()
    {
        System.out.println("执行无参方法");
    }

    public void ryDelFiles(){
        //飞行路线   重点区域
        List<BusinessaFly> flys = businessaFlyService.selectBusinessaFlyList(new BusinessaFly());
        List<BusinessaKeyAreas> areas = businessaKeyAreasService.selectBusinessaKeyAreasList(new BusinessaKeyAreas());
        DeleteFileUtil.delFile(flys,areas);
    }

}
