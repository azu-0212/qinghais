package com.ruoyi.common.utils.img;

import com.relops.snowflake.Snowflake;
import org.apache.commons.lang3.RandomUtils;

public class UidUtil {
    private static Snowflake s = new Snowflake(RandomUtils.nextInt(0, 1024));

    public static Long generateUID(){

        return  s.next();
    }
}
