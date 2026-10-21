package com.ruoyi.common.utils.img;

import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.model.GridFSFile;
import com.mongodb.client.gridfs.model.GridFSUploadOptions;
import com.ruoyi.project.system.domain.MetadataBean;
//import javafx.scene.ImageCursor;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class SaveThread extends Thread {

    @Autowired
    GridFsTemplate gridFsTemplate;

    private MetadataBean metadataBean;
    private File file;

    public SaveThread(File file, MetadataBean metadataBean) {
        this.metadataBean = metadataBean;
        this.file = file;
        this.gridFsTemplate = gridFsTemplate;
    }

    @Override
    public void run() {
        long startTime = System.currentTimeMillis(); //获取开始时间
        //将要存储的文件写入输入流
        FileInputStream fileInputStream  = null;
        try {
            fileInputStream = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        System.out.println(fileInputStream+"-------------"+file.getName()+"--------------"+GridFSUtil.setMetaData(metadataBean));


        //文件开始存储
        ObjectId objectId = gridFsTemplate.store(fileInputStream, file.getName(), GridFSUtil.setMetaData(metadataBean));
        System.out.println(objectId+"bbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbb");
        //获取存储的文件id
        String fileId = objectId.toString();
        System.out.println("获取的存储id为"+fileId);
        GetRunTimeUtil.getRunTime(startTime, System.currentTimeMillis(), "save存储所用时间");
    }
}
