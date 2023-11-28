
package com.hrs.cloud.core.utils.ID;


import com.hrs.cloud.entity.Tickets64;
import com.hrs.cloud.dao.Tickets64Mapper;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Random;

/**
 * description:ID生成器
 *
 * @author bingcong huang
 */
@Component
public class IDUtils {


    @Resource
    private Tickets64Mapper tickets64Mapper;



    public long getTicket(){
        Tickets64 tickets64 = new Tickets64();
        tickets64Mapper.getTickId(tickets64);
        return tickets64.getId();
    }

    private Integer getRandom(int min,int max){

        Random random = new Random();
        int s = random.nextInt(max)%(max-min+1) + min;
        return s;
    }



}
