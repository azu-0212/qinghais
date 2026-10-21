package com.ruoyi.project.system.utils;

import com.ruoyi.framework.config.RuoYiConfig;
import com.ruoyi.project.system.domain.BusinessaFly;
import com.ruoyi.project.system.domain.BusinessaKeyAreas;
import org.springframework.security.core.parameters.P;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class DeleteFileUtil {


    public static void delFile(List<BusinessaFly> flys, List<BusinessaKeyAreas> areas) {
        //飞行路线   重点区域所有的文件
        List<String> proPaths = getAllProPath(flys,areas);
        //系统目录下的所有文件
        List<String> sysPaths = new ArrayList<String>();
        List<String> list = folderMethod(System.getProperty("user.dir") + RuoYiConfig.getProfile(),sysPaths);
        //比对paths 和
        queryDiffert(proPaths,list);
    }


    private static List<String> getAllProPath(List<BusinessaFly> flys, List<BusinessaKeyAreas> areas) {
        List<String> list = new ArrayList<String>();
        String sys = (System.getProperty("user.dir"));
        if(flys != null && flys.size() > 0){
            for(BusinessaFly fly : flys) {
                if(fly.getFlyPath() != null)
                    list.add(sys + fly.getFlyPath().replace("/","\\"));
                if(fly.getImgPath() != null){
                    list.add(sys + fly.getImgPath().replace("/","\\"));
                }
            }
            for(BusinessaKeyAreas area : areas){
                if(area.getImgPath() != null){
                    list.add(sys + area.getImgPath().replace("/","\\"));
                }
            }
        }
        return list;
    }

    public static List<String> folderMethod(String path, List<String> list) {
        File file = new File(path);
        if (file.exists()) {
            File[] files = file.listFiles();
            if (null != files) {
                for (File file2 : files) {
                    if (file2.isDirectory()) {
                        System.out.println("文件夹:" + file2.getAbsolutePath());
                        folderMethod(file2.getAbsolutePath(),list);
                    } else {
                        String filePath = file2.getAbsolutePath();
                        if(filePath.endsWith(".jgeg") ||filePath.endsWith(".fpf") ||filePath.endsWith(".png") ||filePath.endsWith(".jpg") ){
                            list.add(filePath);
                        }
                    }
                }
            }
        } else {
            System.out.println("文件不存在!");
        }
        return list;
    }

    public static void queryDiffert(List<String> proPaths, List<String> sysPaths){
        if(sysPaths != null && sysPaths.size() > 0){
            for (int i = 0; i < sysPaths.size(); i++) {
                if (proPaths.contains(sysPaths.get(i))) {
                    continue;
                } else {
                    File file = new File(sysPaths.get(i));
                    if (file.exists()) {//文件是否存在
                        file.delete();
                    } else {
                        continue;
                    }
                }
            }
        }
    }
}
