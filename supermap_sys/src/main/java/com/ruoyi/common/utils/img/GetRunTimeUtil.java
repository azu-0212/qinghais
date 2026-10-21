package com.ruoyi.common.utils.img;

import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;

@Slf4j
public class GetRunTimeUtil {
    public static double getRunTime( long startTime,long endTime ,String name){
        double upTime;//方法的执行时间(秒)
        upTime = new BigDecimal(endTime-startTime).divide(new BigDecimal(1000)).doubleValue();//耗时(秒)
        log.info(name+"RunTime:{}",upTime);
       return upTime;
    }
}
