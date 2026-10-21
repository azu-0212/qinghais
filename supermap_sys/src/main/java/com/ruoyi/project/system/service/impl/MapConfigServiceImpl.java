package com.ruoyi.project.system.service.impl;

import java.util.List;
import java.util.Map;

import com.ruoyi.project.system.mapper.SysResourceMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.project.system.mapper.MapConfigMapper;
import com.ruoyi.project.system.domain.MapConfig;
import com.ruoyi.project.system.service.IMapConfigService;

/**
 * 地图配置Service业务层处理
 *
 * @author ruoyi
 * @date 2020-06-28
 */
@Service
public class MapConfigServiceImpl implements IMapConfigService
{
    @Autowired
    private MapConfigMapper mapConfigMapper;

    @Autowired
    private SysResourceMapper sysResourceMapper;

    /**
     * 查询地图配置
     *
     * @param configId 地图配置ID
     * @return 地图配置
     */
    @Override
    public MapConfig selectMapConfigById(Long configId)
    {
        return mapConfigMapper.selectMapConfigById(configId);
    }

    /**
     * 查询地图配置列表
     *
     * @param mapConfig 地图配置
     * @return 地图配置
     */
    @Override
    public List<MapConfig> selectMapConfigList(MapConfig mapConfig)
    {
        return mapConfigMapper.selectMapConfigList(mapConfig);
    }

    /**
     * 新增地图配置
     *
     * @param mapConfig 地图配置
     * @return 结果
     */
    @Override
    public int insertMapConfig(MapConfig mapConfig)
    {
        return mapConfigMapper.insertMapConfig(mapConfig);
    }

    /**
     * 修改地图配置
     *
     * @param mapConfig 地图配置
     * @return 结果
     */
    @Override
    public int updateMapConfig(MapConfig mapConfig)
    {
        return mapConfigMapper.updateMapConfig(mapConfig);
    }

    /**
     * 批量删除地图配置
     *
     * @param configIds 需要删除的地图配置ID
     * @return 结果
     */
    @Override
    public int deleteMapConfigByIds(Long[] configIds)
    {
        return mapConfigMapper.deleteMapConfigByIds(configIds);
    }

    /**
     * 删除地图配置信息
     *
     * @param configId 地图配置ID
     * @return 结果
     */
    @Override
    public int deleteMapConfigById(Long configId)
    {
        return mapConfigMapper.deleteMapConfigById(configId);
    }
}
