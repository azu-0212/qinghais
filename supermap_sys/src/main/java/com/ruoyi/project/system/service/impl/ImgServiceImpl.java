package com.ruoyi.project.system.service.impl;

import com.google.common.collect.FluentIterable;
import com.google.common.io.Files;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSFindIterable;
import com.mongodb.client.gridfs.model.GridFSFile;
import com.ruoyi.common.utils.img.GetRunTimeUtil;
import com.ruoyi.common.utils.img.GridFSUtil;
import com.ruoyi.common.utils.img.MyThread;
import com.ruoyi.project.exception.FileFormatErrorException;
import com.ruoyi.project.exception.FileHasDirectory;
import com.ruoyi.project.exception.FileNullException;
import com.ruoyi.project.system.domain.ImgBean;
import com.ruoyi.project.system.domain.MetadataBean;
import com.ruoyi.project.system.domain.SysImgCode;
import com.ruoyi.project.system.mapper.ImgMapper;
import com.ruoyi.project.system.service.ImgService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsCriteria;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;
import java.io.*;
import java.util.*;

@Slf4j
@Service
public class ImgServiceImpl implements ImgService {
    @Autowired
    GridFsTemplate gridFsTemplate;

    @Autowired
    GridFSBucket gridFSBucket;

    @Autowired
    GridFsOperations operations;

    @Autowired
    ImgMapper imgMapper;

    @SneakyThrows
    //添加
    @Override
    public String save(ImgBean imgBean) {
        long startTime = System.currentTimeMillis(); //获取开始时间
//        for(int i =0 ; i<6;i++) {
        //获取要存储的文件
          /*  File file = new File(imgBean.getUrl());
            //将要存储的文件写入输入流
            FileInputStream fileInputStream = new FileInputStream(file);
            //文件开始存储
            ObjectId objectId = gridFsTemplate.store(fileInputStream, imgBean.getFileName(), GridFSUtil.setMetaData(imgBean));
            //获取存储的文件id
            String fileId = objectId.toString();
            System.out.println("获取的存储id为"+fileId);*/
//        }
        GetRunTimeUtil.getRunTime(startTime, System.currentTimeMillis(), "save存储所用时间");
        return "ok";
    }

    //批量添加  一对一
    @Override
    public String saveImgs(MetadataBean metadataBean) throws FileNotFoundException,FileNullException,FileHasDirectory,FileFormatErrorException {
        long startTime = System.currentTimeMillis(); //获取开始时间
        File files = new File(metadataBean.getFileFolder());
        //判断是否为目录文件
        if(files.isDirectory()){
            File[] subs = files.listFiles();

            // 判断文件夹下是否为空
            if(subs.length < 1){
                throw new FileNullException("该文件夹下无内容，请重新输入！");
            }
            for(File file : subs){
                // 判断是否存在目录文件
                if(file.isDirectory()){
                    throw new FileHasDirectory("该文件夹下存在目录文件，请重新输入！");
                }

                //判断文件是否为图片文件
                String fileName = file.getName();
                String fileSuffix = fileName.substring(fileName.indexOf(".")+1);
                if(!(fileSuffix.equals("png")) && !(fileSuffix.equals("jpg")) && !(fileSuffix.equals("jpeg")) && !(fileSuffix.equals("JPG")) && !(fileSuffix.equals("PNG")) && !(fileSuffix.equals("JPEG"))){
                    throw new FileFormatErrorException("该文件夹下存在其它文件，请重新输入！");
                }

                //for(int i=0;i<2500;i++){//执行1000次
                //将要存储的文件写入输入流
                FileInputStream fileInputStream  = new FileInputStream(file);
                //文件开始存储
                ObjectId objectId = gridFsTemplate.store(fileInputStream, file.getName(), GridFSUtil.setMetaData(metadataBean));
                //获取存储的文件id
                String fileId = objectId.toString();
                System.out.println("获取的存储id为"+fileId);
                GetRunTimeUtil.getRunTime(startTime, System.currentTimeMillis(), "save存储所用时间");
                //}

            }
            return "OK";
        }else{
            return "您输入的不是一个文件夹，请重新输入！";
        }
    }

    //批量添加  一对多

    public String saveFileImgs(MetadataBean metadataBean) throws FileNotFoundException,FileNullException,FileHasDirectory,FileFormatErrorException {
        long startTime = System.currentTimeMillis(); //获取开始时间
        File files = new File(metadataBean.getFileFolder());
        FluentIterable<File> rootFiles = Files.fileTreeTraverser().preOrderTraversal(files);
        for (File childFiles : rootFiles) {
            String fileName = childFiles.getName();
            String fileSuffix = fileName.substring(fileName.indexOf(".") + 1);
            if ((fileSuffix.equals("png")) || (fileSuffix.equals("jpg")) || (fileSuffix.equals("jpeg")) || (fileSuffix.equals("JPG")) || (fileSuffix.equals("PNG")) || (fileSuffix.equals("JPEG"))) {
                //for(int i=0;i<2500;i++){//执行1000次
                //System.out.println("全目录: " + childFiles)
                String path = childFiles.getAbsolutePath();
                String[] levelFileArr = path.split("\\\\");
                // F:\项目照片\照片库\矿业核查\海北州\刚察县\3（CZX002、CZX003）\后
                // 0     1     2      3      4     5           6          7

                //保存照片根目录标签
                metadataBean.setImageLabel(levelFileArr[3]);
                //保存照片区县字段
                metadataBean.setArea(levelFileArr[5]);
                //保存照片类区县编码字段
                metadataBean.setFileCode(levelFileArr[6]);
                //保存照片期类字段
                metadataBean.setImageType(levelFileArr[7]);
                //将要存储的文件写入输入流
                FileInputStream fileInputStream = new FileInputStream(childFiles);
                //文件开始存储
                ObjectId objectId = gridFsTemplate.store(fileInputStream, childFiles.getName(), GridFSUtil.setMetaData(metadataBean));
                //获取存储的文件id
                String fileId = objectId.toString();
                System.out.println("获取的存储id为" + fileId);
                GetRunTimeUtil.getRunTime(startTime, System.currentTimeMillis(), "save存储所用时间");
            }
        }
        return "";

    }

    //查询多个
    @Override
    public List<Map<String, Object>> get(String fileCode, String filesName) {
        long startTime = System.currentTimeMillis(); //获取开始时间
        List<Map<String, Object>> list = Collections.synchronizedList(new LinkedList<>());
        // 1.Query.query(criteria.andOperator(Criteria.where("metadata.metadata.fileCode").is(filesName),Criteria.where("metadata.metadata.area").is(area)))
        //Query.query(GridFsCriteria.whereMetaData("metadata.fileCode").regex(".*" + filesName + ".*")
        //.andOperator(GridFsCriteria.whereMetaData("metadata.area").is(area))

        GridFSFindIterable gridFSFindIterable = null;
        // 只根据fileCode字段查询
        if(filesName.length()!=0 && filesName.equals("null")) {
            gridFSFindIterable = gridFsTemplate.find(
                    Query.query(GridFsCriteria.whereMetaData("metadata.fileCode").regex(".*" + fileCode + ".*"))
            );
        } else {
            // 同时根据fileCode和filesName 查询
            gridFSFindIterable = gridFsTemplate.find(
                    Query.query(GridFsCriteria.whereMetaData("metadata.fileCode").regex(".*" + fileCode + ".*")
                            .andOperator(GridFsCriteria.whereMetaData("metadata.area").is(filesName)))
            );
        }
        Iterator<GridFSFile> iterator_temp = gridFSFindIterable.iterator();
        if(GridFSUtil.getIteratorSize(iterator_temp) !=0){
            Iterator<GridFSFile> iterator = gridFSFindIterable.iterator();
            Vector<Thread> vectors = new Vector<Thread>();
            while (iterator.hasNext()) {
                GridFSFile gridFSFileTemp = iterator.next();
                MyThread myThread = new MyThread(gridFSFileTemp, gridFSBucket, list);
                vectors.add(myThread);
                myThread.start();
            }
            //主线程
            for (Thread thread : vectors) {
                try {
                    thread.join(); //使用join来保证thread的5个线程都执行完后，才执行主线程
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            GetRunTimeUtil.getRunTime(startTime, System.currentTimeMillis(), "查询多个用时");  //获取程序运行时间
            return list;
        }else {
            /*Map<String, Object> map_temp = new LinkedHashMap<>();
            map_temp.put("查询结果","未查询到");
            list.add(map_temp);
            return list;*/
            throw new RuntimeException("未查询到该点照片");
        }
    }

    // 卫片执法项目--查询多个数据
    @Override
    public List<Map<String, Object>> getSatellite(String fileCode) {
        long startTime = System.currentTimeMillis(); //获取开始时间
        List<Map<String, Object>> list = Collections.synchronizedList(new LinkedList<>());
        GridFSFindIterable gridFSFindIterable = null;
        // 只根据fileCode字段查询
        gridFSFindIterable = gridFsTemplate.find(
                Query.query(GridFsCriteria.whereMetaData("metadata.imageType").regex(".*" + fileCode + ".*"))
        );
        Iterator<GridFSFile> iterator_temp = gridFSFindIterable.iterator();
        if(GridFSUtil.getIteratorSize(iterator_temp) !=0){
            Iterator<GridFSFile> iterator = gridFSFindIterable.iterator();
            Vector<Thread> vectors = new Vector<Thread>();
            while (iterator.hasNext()) {
                GridFSFile gridFSFileTemp = iterator.next();
                MyThread myThread = new MyThread(gridFSFileTemp, gridFSBucket, list);
                vectors.add(myThread);
                myThread.start();
            }
            //主线程
            for (Thread thread : vectors) {
                try {
                    thread.join(); //使用join来保证thread的5个线程都执行完后，才执行主线程
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            GetRunTimeUtil.getRunTime(startTime, System.currentTimeMillis(), "查询多个用时");  //获取程序运行时间
            return list;
        }else {
            throw new RuntimeException("未查询到文件：可能出现采集照片缺失或服务器连接失败等问题，请管理员核查！！！");
        }
    }

    //查询单个
    @Override
    public List<Map<String, Object>> getOne(String name) {
        long startTime = System.currentTimeMillis(); //获取开始时间
        List<Map<String, Object>> list = new LinkedList<>();
        Map<String, Object> map = new LinkedHashMap<>();
        GridFSFile gridFSFileTemp = gridFsTemplate.findOne(Query.query(GridFsCriteria.whereFilename().is(name)));
        System.out.println("gridFSFileTemp---------------"+gridFSFileTemp);

        if (gridFSFileTemp != null) {
            MetadataBean metadataBean = GridFSUtil.getMetaData(gridFSFileTemp);
            map.put("base64", GridFSUtil.readFs(gridFSFileTemp, gridFSBucket));
            log.info("metadataBean:{}", metadataBean);
            /*map.put("name", metadataBean.getName());
            map.put("size", metadataBean.getSize());
            map.put("uid", metadataBean.getUid());
            map.put("filePath", metadataBean.getFilePath());
            map.put("Project_type1", metadataBean.isProject_type1());
            map.put("Project_type2", metadataBean.isProject_type2());
            map.put("Project_type3", metadataBean.isProject_type3());
            map.put("Project_type4", metadataBean.isProject_type4());*/
            list.add(map);
            GetRunTimeUtil.getRunTime(startTime, System.currentTimeMillis(), "查询一个用时");  //获取程序运行时间
            return list;
        } else {
            throw new RuntimeException("未查询到文件");
        }
    }

}
