package com.ruoyi.project.system.mapper;

import java.util.List;
import com.ruoyi.project.system.domain.SysVideo;

/**
 * 视频管理Mapper接口
 *
 * @author ruoyi
 * @date 2021-03-24
 */
public interface SysVideoMapper
{
    /**
     * 查询视频管理
     *
     * @param id 视频管理ID
     * @return 视频管理
     */
    public SysVideo selectSysVideoById(Long id);

    /**
     * 查询视频管理列表
     *
     * @param sysVideo 视频管理
     * @return 视频管理集合
     */
    public List<SysVideo> selectSysVideoList(SysVideo sysVideo);

    /**
     * 根据类型查询视频管理列表
     *
     * @param type
     * @return 视频管理集合
     */
    public List<SysVideo> selectSysVideoListByType(Long type);

    /**
     * 新增视频管理
     *
     * @param sysVideo 视频管理
     * @return 结果
     */
    public int insertSysVideo(SysVideo sysVideo);

    /**
     * 修改视频管理
     *
     * @param sysVideo 视频管理
     * @return 结果
     */
    public int updateSysVideo(SysVideo sysVideo);

    /**
     * 删除视频管理
     *
     * @param id 视频管理ID
     * @return 结果
     */
    public int deleteSysVideoById(Long id);

    /**
     * 批量删除视频管理
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteSysVideoByIds(Long[] ids);
}
