package com.ruoyi.project.system.service;

import java.util.List;
import com.ruoyi.project.system.domain.FlightFile;

/**
 * 飞行路径Service接口
 * 
 * @author 王俊
 * @date 2020-06-22
 */
public interface IFlightFileService 
{
    /**
     * 查询飞行路径
     * 
     * @param fileId 飞行路径ID
     * @return 飞行路径
     */
    public FlightFile selectFlightFileById(Long fileId);

    /**
     * 查询飞行路径列表
     * 
     * @param flightFile 飞行路径
     * @return 飞行路径集合
     */
    public List<FlightFile> selectFlightFileList(FlightFile flightFile);

    /**
     * 新增飞行路径
     * 
     * @param flightFile 飞行路径
     * @return 结果
     */
    public int insertFlightFile(FlightFile flightFile);

    /**
     * 修改飞行路径
     * 
     * @param flightFile 飞行路径
     * @return 结果
     */
    public int updateFlightFile(FlightFile flightFile);

    /**
     * 批量删除飞行路径
     * 
     * @param fileIds 需要删除的飞行路径ID
     * @return 结果
     */
    public int deleteFlightFileByIds(Long[] fileIds);

    /**
     * 删除飞行路径信息
     * 
     * @param fileId 飞行路径ID
     * @return 结果
     */
    public int deleteFlightFileById(Long fileId);
}
