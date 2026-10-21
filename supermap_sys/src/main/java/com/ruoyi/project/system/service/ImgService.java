package com.ruoyi.project.system.service;

import com.ruoyi.project.exception.FileFormatErrorException;
import com.ruoyi.project.exception.FileHasDirectory;
import com.ruoyi.project.exception.FileNullException;
import com.ruoyi.project.system.domain.ImgBean;
import com.ruoyi.project.system.domain.MetadataBean;
import com.ruoyi.project.system.domain.SysImgCode;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface ImgService {
    String save( ImgBean imgBean);
    String saveImgs(MetadataBean metadataBean) throws FileNotFoundException, IOException, FileNullException, FileHasDirectory, FileFormatErrorException;
    List<Map<String,Object>> get(String fileCode, String filename);
    List<Map<String,Object>> getOne(String id);
    List<Map<String,Object>> getSatellite(String fileCode);
}
