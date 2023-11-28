package com.hrs.cloud.constants;

public enum RedisRoleCache {


    ROLE_PERMISSION_RELATION("role_permission_relation", 0);


    /**
     * 缓存key
     */
    private String key;
    /**
     * key过期时间
     */
    private int expire;

    private RedisRoleCache(String key, int expire) {
        this.key = key;
        this.expire = expire;
    }

    /**
     * 获取缓存key
     * @return
     */
    public String getKey() {
        return this.key;
    }

    /**
     * 获取缓存key的过期时间
     * @return
     */
    public int getExpire() {
        return this.expire;
    }
}
