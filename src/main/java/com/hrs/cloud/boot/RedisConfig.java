package com.hrs.cloud.boot;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedisPool;

import java.util.Collection;
import java.util.List;

@Slf4j
@Configuration
public class RedisConfig {

    @Bean(name= "jedis.pool")
    @Autowired
    public ShardedJedisPool shardedJedis(@Qualifier("jedis.pool.config") JedisPoolConfig redisConfig,
                               @Value("${jedis.pool.url}")String url) {
        List<JedisShardInfo> list = Lists.newArrayList();
        String[] addressArr = url.split(",");
        for (String str : addressArr) {
            String[] split = str.split(":");
            String ip = split[0];
            String port = split[1];
            if(split.length > 2){
                String pwd = split[2];
                JedisShardInfo js = new JedisShardInfo(ip,Integer.valueOf(port));
                js.setPassword(pwd);
                list.add(js);
            }else{
                list.add(new JedisShardInfo(ip, port));
            }
            log.info("redis服务端 ip:" + ip + "  port:" + port);
        }
        ShardedJedisPool pool = new ShardedJedisPool(redisConfig, list);
        Collection<Jedis> allShards = pool.getResource().getAllShards();
        for (Jedis jedis : allShards) {
            jedis.ping();
            log.error("redis-pingtest    ip:" + jedis.getClient().getHost()
                    + "  port:" + jedis.getClient().getPort());
        }
        return pool;
    }

    @Bean(name= "jedis.pool.config")
    public JedisPoolConfig jedisPoolConfig (@Value("${jedis.pool.config.maxTotal}")int maxTotal,
                                            @Value("${jedis.pool.config.maxIdle}")int maxIdle,
                                            @Value("${jedis.pool.config.minIdle}")int minIdle,
                                            @Value("${jedis.pool.config.testOnBorrow}") boolean testOnBorrow,
                                            @Value("${jedis.pool.config.testOnReturn}") boolean testOnReturn,
                                            @Value("${jedis.pool.config.testWhileIdle}") boolean testWhileIdle,
                                            @Value("${jedis.pool.config.timeBetweenEvictionRunsMillis}") int timeBetweenEvictionRunsMillis,
                                            @Value("${jedis.pool.config.maxWaitMillis}")int maxWaitMillis) {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(maxTotal);
        config.setMaxIdle(maxIdle);
        config.setMinIdle(minIdle);
        config.setTestOnBorrow(testOnBorrow);
        config.setTestOnReturn(testOnReturn);
        config.setTestWhileIdle(testWhileIdle);
        config.setMaxWaitMillis(maxWaitMillis);
        config.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);
        return config;
    }

}
