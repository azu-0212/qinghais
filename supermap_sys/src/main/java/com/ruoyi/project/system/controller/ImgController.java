package com.ruoyi.project.system.controller;

import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.framework.web.page.TableDataInfo;
import com.ruoyi.project.exception.FileFormatErrorException;
import com.ruoyi.project.exception.FileHasDirectory;
import com.ruoyi.project.exception.FileNullException;
import com.ruoyi.project.system.domain.BusinessaFly;
import com.ruoyi.project.system.domain.ImgBean;
import com.ruoyi.project.system.domain.MetadataBean;
import com.ruoyi.project.system.domain.SysImgCode;
import com.ruoyi.project.system.service.ImgService;
import com.ruoyi.project.system.service.impl.ImgServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.FileNotFoundException;
import java.util.List;

@RestController
@RequestMapping("/img")
public class ImgController extends BaseController {

    @Autowired
    ImgServiceImpl imgService;

    @Autowired
    ImgService imgServiceN;

    /**
     * 根据图片对象
     * @param imgBean
     * @return
     */
    @PostMapping("/save")
    public String saveFilesMore(@RequestBody List<ImgBean> imgBean){
        for(ImgBean i : imgBean){
           /* try{
                imgService.save(i);
            }catch (Exception e){
                return (i.getFileName() + "存储失败，请重试!");
            }*/
        }
        return "OK";
    }

    /**
     * 批量保存文件夹图片信息
     * @param metadataBean
     * @return
     */
    @PostMapping("/saveFileImgsMore")
    public String saveFileImgs(@RequestBody MetadataBean metadataBean) {
        try{
            imgService.saveFileImgs(metadataBean);
            return "ok";
        }catch (FileNotFoundException e){
            e.printStackTrace();
            return e.getMessage();
        }catch (FileNullException e){
            e.printStackTrace();
            return e.getMessage();
        }catch (FileHasDirectory e){
            e.printStackTrace();
            return e.getMessage();
        }catch (FileFormatErrorException e){
            e.printStackTrace();
            return e.getMessage();
        }catch (Exception e){
            e.printStackTrace();
            return (metadataBean.getFileFolder() + "路径下的文件存储失败，请重试!");
        }

    }

    /**
     * 批量保存图片信息
     * @param metadataBean
     * @return
     */
    @PostMapping("/saveImages")
    public String saveImgs(@RequestBody MetadataBean metadataBean) {
        try{
            imgService.saveImgs(metadataBean);
            return "ok";
        }catch (FileNotFoundException e){
            e.printStackTrace();
            return e.getMessage();
        }catch (FileNullException e){
            e.printStackTrace();
            return e.getMessage();
        }catch (FileHasDirectory e){
            e.printStackTrace();
            return e.getMessage();
        }catch (FileFormatErrorException e){
            e.printStackTrace();
            return e.getMessage();
        }catch (Exception e){
            e.printStackTrace();
            return (metadataBean.getFileFolder() + "路径下的文件存储失败，请重试!");
        }
    }

    //    @PreAuthorize("@ss.hasPermi('system:resource:list')")
    @GetMapping("/getOne/{filename}")
    public Object getOne(@PathVariable("filename") String filename){
        return imgService.getOne(filename);
    }

    //    @PreAuthorize("@ss.hasPermi('system:resource:list')")
    @GetMapping("/get/{fileCode}/{filename}")
    public Object get(@PathVariable("fileCode") String fileCode, @PathVariable("filename") String filename){
        return imgService.get(fileCode, filename);
    }

    @GetMapping("/getSatellite/{fileCode}")
    public Object getSatellite(@PathVariable("fileCode") String fileCode){
        return imgService.getSatellite(fileCode);
    }
}
