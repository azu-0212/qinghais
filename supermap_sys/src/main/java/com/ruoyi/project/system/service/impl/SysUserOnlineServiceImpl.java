package com.ruoyi.project.system.service.impl;

import com.ruoyi.common.constant.Constants;
import com.ruoyi.framework.redis.RedisCache;
import com.ruoyi.project.monitor.mapper.SysLogininforMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.framework.security.LoginUser;
import com.ruoyi.project.monitor.domain.SysUserOnline;
import com.ruoyi.project.system.service.ISysUserOnlineService;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * 在线用户 服务层处理
 *
 * @author ruoyi
 */
@Service
public class SysUserOnlineServiceImpl implements ISysUserOnlineService
{

    @Autowired
    private RedisCache redisCache;
    /**
     * 通过登录地址查询信息
     *
     * @param ipaddr 登录地址
     * @param user 用户信息
     * @return 在线用户信息
     */
    @Override
    public SysUserOnline selectOnlineByIpaddr(String ipaddr, LoginUser user)
    {
        if (StringUtils.equals(ipaddr, user.getIpaddr()))
        {
            return loginUserToUserOnline(user);
        }
        return null;
    }

    /**
     * 通过用户名称查询信息
     *
     * @param userName 用户名称
     * @param user 用户信息
     * @return 在线用户信息
     */
    @Override
    public SysUserOnline selectOnlineByUserName(String userName, LoginUser user)
    {
        if (StringUtils.equals(userName, user.getUsername()))
        {
            return loginUserToUserOnline(user);
        }
        return null;
    }

    /**
     * 通过登录地址/用户名称查询信息
     *
     * @param ipaddr 登录地址
     * @param userName 用户名称
     * @param user 用户信息
     * @return 在线用户信息
     */
    @Override
    public SysUserOnline selectOnlineByInfo(String ipaddr, String userName, LoginUser user)
    {
        if (StringUtils.equals(ipaddr, user.getIpaddr()) && StringUtils.equals(userName, user.getUsername()))
        {
            return loginUserToUserOnline(user);
        }
        return null;
    }

    /**
     * 设置在线用户信息
     *
     * @param user 用户信息
     * @return 在线用户
     */
    public SysUserOnline loginUserToUserOnline(LoginUser user)
    {
        if (StringUtils.isNull(user) && StringUtils.isNull(user.getUser()))
        {
            return null;
        }
        SysUserOnline sysUserOnline = new SysUserOnline();
        sysUserOnline.setTokenId(user.getToken());
        sysUserOnline.setUserName(user.getUsername());
        sysUserOnline.setIpaddr(user.getIpaddr());
        sysUserOnline.setLoginLocation(user.getLoginLocation());
        sysUserOnline.setBrowser(user.getBrowser());
        sysUserOnline.setOs(user.getOs());
        sysUserOnline.setLoginTime(user.getLoginTime());
        if (StringUtils.isNotNull(user.getUser().getDept()))
        {
            sysUserOnline.setDeptName(user.getUser().getDept().getDeptName());
        }
        return sysUserOnline;
    }

    /**
     * 查询在线用户信息
     *
     * @param
     * @param sysUserOnline
     */
    @Override
    public List<SysUserOnline> selectSysLoginInforByTime(List<SysUserOnline> sysUserOnline, HttpServletRequest request)
    {
        // 获取当前时间戳
        Long currentTime = new Date().getTime();
        // 获取半小时前时间戳
        Long startTime = new Date().getTime() - Math.round(0.6 * 3600000);
        //System.out.println(dateToStamp("2020-08-19 10:02:58"));
        List<SysUserOnline> userOnlineList = new ArrayList<SysUserOnline>();
        //List<String> listIp = new ArrayList<String>();
        for(SysUserOnline list : sysUserOnline){
            if(startTime <= list.getLoginTime() && list.getLoginTime() <= currentTime){
               /* if(listIp.contains(request.getRemoteAddr())){
                    userOnlineList.add(list);
                    break;
                }
                listIp.add(list.getIpaddr());*/
                userOnlineList.add(list);
            }
        }
        return userOnlineList;
    }

    public String dateToStamp(String time){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String stamp = "";
        if(!"".equals(time)){
            try{
                stamp = String.valueOf(sdf.parse(time).getTime());
            }catch (Exception e){
                System.out.println("参数为空！");
            }
        }else{
            long current_time = System.currentTimeMillis();
            stamp = String.valueOf(current_time);
        }
        return stamp;
    }
}
