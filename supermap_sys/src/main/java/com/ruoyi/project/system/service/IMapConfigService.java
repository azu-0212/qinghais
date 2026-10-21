package com.ruoyi.project.system.service;

import java.util.List;
import com.ruoyi.project.system.domain.MapConfig;

/**
 * 地图配置Service接口
 * 
 * @author ruoyi
 * @date 2020-06-28
 */
public interface IMapConfigService 
{
    /**
     * 查询地图配置
     * 
     * @param configId 地图配置ID
     * @return 地图配置
     */
    public MapConfig selectMapConfigById(Long configId);

    /**
     * 查询地图配置列表
     * 
     * @param mapConfig 地图配置
     * @return 地图配置集合
     */
    public List<MapConfig> selectMapConfigList(MapConfig mapConfig);

    /**
     * 新增地图配置
     * 
     * @param mapConfig 地图配置
     * @return 结果
     */
    public int insertMapConfig(MapConfig mapConfig);

    /**
     * 修改地图配置
     * 
     * @param mapConfig 地图配置
     * @return 结果
     */
    public int updateMapConfig(MapConfig mapConfig);

    /**
     * 批量删除地图配置
     * 
     * @param configIds 需要删除的地图配置ID
     * @return 结果
     */
    public int deleteMapConfigByIds(Long[] configIds);

    /**
     * 删除地图配置信息
     * 
     * @param configId 地图配置ID
     * @return 结果
     */
    public int deleteMapConfigById(Long configId);
}
