package com.ruoyi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.freemarker.FreeMarkerAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * 启动程序
 * 
 * @author ruoyi
 */
@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class,FreeMarkerAutoConfiguration.class })
public class RuoYiApplication
{
    public static void main(String[] args)
    {
        System.setProperty("spring.devtools.restart.enabled", "false");
        SpringApplication.run(RuoYiApplication.class, args);
        System.out.println("" +
                ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>\n"+
                "  _____ _             _   _    _       \n" +
                " / ____| |           | | | |  | |      \n" +
                "| (___ | |_ __ _ _ __| |_| |  | |_ __  \n" +
                " \\___ \\| __/ _` | '__| __| |  | | '_ \\ \n" +
                " ____) | || (_| | |  | |_| |__| | |_) |\n" +
                "|_____/ \\__\\__,_|_|   \\__|\\____/| .__/ \n" +
                "                                | |    \n" +
                "                                |_| \n"+
                ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>\n");
    }
}
