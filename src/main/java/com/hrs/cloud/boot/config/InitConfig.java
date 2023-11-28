package com.hrs.cloud.boot.config;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hrs.cloud.core.utils.MBean.EndPointsUtil;
import com.hrs.cloud.entity.SysPort;
import com.hrs.cloud.helper.RedisHelper;
import com.hrs.cloud.dao.SysPortMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.management.*;
import java.net.UnknownHostException;

@Component
public class InitConfig {

    @Resource
    private SysPortMapper sysPortMapper;

    private String ipport;
    @Resource
    private RedisHelper redisUtils;

    @PostConstruct
    public void InitConfig(){
        try {
            String s = EndPointsUtil.getEndPoints();
            System.out.println("----------:"+s);
            ipport=s;
        } catch (MalformedObjectNameException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (AttributeNotFoundException e) {
            e.printStackTrace();
        } catch (InstanceNotFoundException e) {
            e.printStackTrace();
        } catch (MBeanException e) {
            e.printStackTrace();
        } catch (ReflectionException e) {
            e.printStackTrace();
        }
    }



    public int getWorkId(){

        String workdIdstr = redisUtils.get(ipport);
        if(StringUtils.isNotEmpty(workdIdstr)){

            return Integer.parseInt(workdIdstr);
        }else{
            SysPort sysPortKey = sysPortMapper.selectOne(new QueryWrapper<SysPort>().eq("IP_PORT", ipport));
            if(null != sysPortKey){
                Integer workId = sysPortKey.getId();
                redisUtils.set(ipport,workId);
                return workId;
            }else{

                SysPort sysPort = new SysPort();
                sysPort.setIpPort(ipport);
                sysPortMapper.insert(sysPort);
                Integer workId =sysPort.getId();
                redisUtils.set(ipport,workId);
                return workId;
            }
        }

    }

}
