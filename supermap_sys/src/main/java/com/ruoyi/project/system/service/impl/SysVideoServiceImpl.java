package com.ruoyi.project.system.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.project.system.mapper.SysVideoMapper;
import com.ruoyi.project.system.domain.SysVideo;
import com.ruoyi.project.system.service.ISysVideoService;

/**
 * 视频管理Service业务层处理
 *
 * @author ruoyi
 * @date 2021-03-24
 */
@Service
public class SysVideoServiceImpl implements ISysVideoService
{
    @Autowired
    private SysVideoMapper sysVideoMapper;

    /**
     * 查询视频管理
     *
     * @param id 视频管理ID
     * @return 视频管理
     */
    @Override
    public SysVideo selectSysVideoById(Long id)
    {
        return sysVideoMapper.selectSysVideoById(id);
    }

    /**
     * 查询视频管理列表
     *
     * @param sysVideo 视频管理
     * @return 视频管理
     */
    @Override
    public List<SysVideo> selectSysVideoList(SysVideo sysVideo)
    {
        return sysVideoMapper.selectSysVideoList(sysVideo);
    }

    /**
     * 根据类型查询视频管理列表
     *
     * @param type
     * @return 视频管理
     */
    @Override
    public List<SysVideo> selectSysVideoListByType(Long type)
    {
        return sysVideoMapper.selectSysVideoListByType(type);
    }

    /**
     * 新增视频管理
     *
     * @param sysVideo 视频管理
     * @return 结果
     */
    @Override
    public int insertSysVideo(SysVideo sysVideo)
    {
        sysVideo.setCreateTime(DateUtils.getNowDate());
        return sysVideoMapper.insertSysVideo(sysVideo);
    }

    /**
     * 修改视频管理
     *
     * @param sysVideo 视频管理
     * @return 结果
     */
    @Override
    public int updateSysVideo(SysVideo sysVideo)
    {
        sysVideo.setUpdateTime(DateUtils.getNowDate());
        return sysVideoMapper.updateSysVideo(sysVideo);
    }

    /**
     * 批量删除视频管理
     *
     * @param ids 需要删除的视频管理ID
     * @return 结果
     */
    @Override
    public int deleteSysVideoByIds(Long[] ids)
    {
        return sysVideoMapper.deleteSysVideoByIds(ids);
    }

    /**
     * 删除视频管理信息
     *
     * @param id 视频管理ID
     * @return 结果
     */
    @Override
    public int deleteSysVideoById(Long id)
    {
        return sysVideoMapper.deleteSysVideoById(id);
    }
}
