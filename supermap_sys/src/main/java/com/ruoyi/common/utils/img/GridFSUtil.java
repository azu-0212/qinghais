package com.ruoyi.common.utils.img;

import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSDownloadStream;
import com.mongodb.client.gridfs.model.GridFSFile;
import com.mongodb.client.gridfs.model.GridFSUploadOptions;
import com.ruoyi.project.system.domain.DocumentBean;
import com.ruoyi.project.system.domain.ImgBean;
import com.ruoyi.project.system.domain.MetadataBean;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import org.bson.Document;
import org.springframework.data.mongodb.gridfs.GridFsResource;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.Iterator;
import java.util.Objects;

@Slf4j
public class GridFSUtil {
    public static  String  readFs(GridFSFile gridFSFileTemp, GridFSBucket gridFSBucket){

        //使用GridFsBucket打开一个下载流对象
        GridFSDownloadStream gridFSDownloadStream = gridFSBucket.openDownloadStream(gridFSFileTemp.getObjectId());
        //创建GridFsResource对象，获取流
        GridFsResource gridFsResource = new GridFsResource(gridFSFileTemp,gridFSDownloadStream);
        String encoded="";
        byte[] bytes = null;
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            long startTime=System.currentTimeMillis(); //获取开始时间

            Thumbnails.of(gridFsResource.getInputStream()).size(400, 400).toOutputStream(out);

            //1.压缩后转换成base64

            bytes =out.toByteArray();
            com.ruoyi.common.utils.img.GetRunTimeUtil.getRunTime(startTime,System.currentTimeMillis(),"压缩时间");
            encoded = Base64.getEncoder().encodeToString(bytes).trim();
            encoded.replace("=","");

            //1.转换成 base64
//            InputStream in =gridFsResource.getInputStream();
//            bytes = IOUtils.toByteArray(in);
//            encoded = Base64.getEncoder().encodeToString(bytes).trim();
//            encoded.replace("=","");
        } catch (IOException e) {
            e.printStackTrace();
        }
        //2.转成二进制
        //   return BinaryUtil.binary(bytes, 2);

        return encoded;
    }
    public static MetadataBean getMetaData(GridFSFile gridFSFileTemp){
        Document document =  gridFSFileTemp.getMetadata()  ;
        DocumentBean documentBean = JackJsonUtil.fromJson(Objects.requireNonNull(document).toJson(),DocumentBean.class);

        if (documentBean != null ) {
            return  documentBean.getMetadata();
        }else {
            throw new NullPointerException("documentBean 为空");
        }


    }

    public static GridFSUploadOptions setMetaData(MetadataBean metadataBean){
        GridFSUploadOptions options = new GridFSUploadOptions();
        Document metadata = new Document();
        metadata.append("area", metadataBean.getArea());
        metadata.append("imageType", metadataBean.getImageType());
        metadata.append("fileCode", metadataBean.getFileCode());
        metadata.append("imageLabel", metadataBean.getImageLabel());
       /* metadata.append("project_type2", imgBean.isProject_type2());
        metadata.append("project_type3", imgBean.isProject_type3());
        metadata.append("project_type4", imgBean.isProject_type4());
        metadata.append("introduce", imgBean.getIntroduce());
        metadata.append("position_lat", imgBean.getPosition_lat());
        metadata.append("position_lon", imgBean.getPosition_lon());
        metadata.append("create_time", imgBean.getCreate_time());
        metadata.append("update_time", imgBean.getUpdate_time());
        metadata.append("update_by", imgBean.getUpdate_by());*/
        options.metadata(metadata);
        return options;
    }

    public static int getIteratorSize(Iterator<GridFSFile> iterator){
        int sum =0;
        while (iterator.hasNext()){
            iterator.next();
            sum++;
        }
        return sum;
    }
}
