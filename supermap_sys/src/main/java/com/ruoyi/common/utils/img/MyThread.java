package com.ruoyi.common.utils.img;

import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.model.GridFSFile;
import com.ruoyi.project.system.domain.MetadataBean;
import lombok.extern.slf4j.Slf4j;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class MyThread extends Thread {
    private GridFSFile gridFSFileTemp;
    private GridFSBucket gridFSBucket;
    private List<Map<String, Object>> list;

    public MyThread(GridFSFile gridFSFileTemp, GridFSBucket gridFSBucket, List<Map<String, Object>> list) {
        this.gridFSFileTemp = gridFSFileTemp;
        this.gridFSBucket = gridFSBucket;
        this.list = list;

    }

    public void run() {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("base64",GridFSUtil.readFs(gridFSFileTemp, gridFSBucket));
        MetadataBean metadataBean = GridFSUtil.getMetaData(gridFSFileTemp);
        map.put("area",metadataBean.getArea());
        map.put("imageType", metadataBean.getImageType());
        map.put("fileCode", metadataBean.getFileCode());
        map.put("imageLabel", metadataBean.getImageLabel());
        log.info("map:{}", map);
        list.add(map);


    }

}
