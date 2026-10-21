package com.ruoyi.project.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.project.system.mapper.FlightFileMapper;
import com.ruoyi.project.system.domain.FlightFile;
import com.ruoyi.project.system.service.IFlightFileService;

/**
 * 飞行路径Service业务层处理
 * 
 * @author 王俊
 * @date 2020-06-22
 */
@Service
public class FlightFileServiceImpl implements IFlightFileService 
{
    @Autowired
    private FlightFileMapper flightFileMapper;

    /**
     * 查询飞行路径
     * 
     * @param fileId 飞行路径ID
     * @return 飞行路径
     */
    @Override
    public FlightFile selectFlightFileById(Long fileId)
    {
        return flightFileMapper.selectFlightFileById(fileId);
    }

    /**
     * 查询飞行路径列表
     * 
     * @param flightFile 飞行路径
     * @return 飞行路径
     */
    @Override
    public List<FlightFile> selectFlightFileList(FlightFile flightFile)
    {
        return flightFileMapper.selectFlightFileList(flightFile);
    }

    /**
     * 新增飞行路径
     * 
     * @param flightFile 飞行路径
     * @return 结果
     */
    @Override
    public int insertFlightFile(FlightFile flightFile)
    {
        return flightFileMapper.insertFlightFile(flightFile);
    }

    /**
     * 修改飞行路径
     * 
     * @param flightFile 飞行路径
     * @return 结果
     */
    @Override
    public int updateFlightFile(FlightFile flightFile)
    {
        return flightFileMapper.updateFlightFile(flightFile);
    }

    /**
     * 批量删除飞行路径
     * 
     * @param fileIds 需要删除的飞行路径ID
     * @return 结果
     */
    @Override
    public int deleteFlightFileByIds(Long[] fileIds)
    {
        return flightFileMapper.deleteFlightFileByIds(fileIds);
    }

    /**
     * 删除飞行路径信息
     * 
     * @param fileId 飞行路径ID
     * @return 结果
     */
    @Override
    public int deleteFlightFileById(Long fileId)
    {
        return flightFileMapper.deleteFlightFileById(fileId);
    }
}
