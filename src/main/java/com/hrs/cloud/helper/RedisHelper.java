package com.hrs.cloud.helper;


import com.hrs.cloud.helper.json.JsonHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.*;
import redis.clients.jedis.exceptions.JedisDataException;
import redis.clients.jedis.exceptions.JedisException;
import redis.clients.util.Hashing;
import redis.clients.util.SafeEncoder;

import java.util.*;


@Slf4j
@Component
public class RedisHelper {

    @Autowired
    private ShardedJedisPool pool;

    public ShardedJedis getResource() {
        return pool.getResource();
    }

    /**
     * 回收 ShardedJedis
     *
     * @param shardedJedis
     */
    @SuppressWarnings("deprecation")
    public void returnResource(ShardedJedis shardedJedis) {
        if (shardedJedis == null) {
            return;
        }
        try {
            pool.returnResource(shardedJedis);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }

    }

    /**
     * 销毁 ShardedJedis
     * @param shardedJedis
     */
    @SuppressWarnings("deprecation")
    public void returnBrokenResource(ShardedJedis shardedJedis) {
        if (shardedJedis == null) {
            return;
        }
        try {
            pool.returnBrokenResource(shardedJedis);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }

    }

    /**
     * list尾部添加元素
     * @return list长度
     */
    public long rpush(String key, String ... string) {
        ShardedJedis shardedJedis = null;
        try {
            shardedJedis = getResource();
            long ret = shardedJedis.rpush(key, string);
            return ret;
        } catch (Exception e) {
            returnBrokenResource(shardedJedis);
            log.error(e.getMessage(), e);
            throw new JedisException(e);
        } finally {
            returnResource(shardedJedis);
        }
    }

    /**
     * Description	： list头部添加元素<br>
     *
     * @param key
     * @param string
     * @return list的长度
     *
     */
    public long lpush(String key, String ... string) {
        ShardedJedis shardedJedis = null;
        try {
            shardedJedis = getResource();
            long ret = shardedJedis.lpush(key, string);
            return ret;
        } catch (Exception e) {
            returnBrokenResource(shardedJedis);
            log.error(e.getMessage(), e);
            throw new JedisException(e);
        } finally {
            returnResource(shardedJedis);
        }
    }


    /**
     * set
     *
     * @return
     */
    public long sadd(String key, String... members) {
        ShardedJedis shardedJedis = null;
        try {
            shardedJedis = getResource();
            long res = shardedJedis.sadd(key, members);
            return res;
        } catch (Exception e) {
            returnBrokenResource(shardedJedis);
            log.error(e.getMessage(), e);
            throw new JedisException(e);
        } finally {
            returnResource(shardedJedis);
        }
    }

    public long sadd(String key, Object... object) {
        String[] arr = new String[object.length];
        for (int i = 0; i < object.length; i++) {
            arr[i] = JsonHelper.toJson(object[i]);
        }
        return sadd(key, arr);
    }

    /**
     * 弹出头部元素
     *
     * @return string 头部元素
     */
    public String lpop(String key) {
        ShardedJedis shardedJedis = null;
        try {
            shardedJedis = getResource();
            return shardedJedis.lpop(key);
        } catch (Exception e) {
            returnBrokenResource(shardedJedis);
            log.error(e.getMessage(), e);
            throw new JedisException(e);
        } finally {
            returnResource(shardedJedis);
        }
    }

    public List<String> blpop(String key) {
        ShardedJedis shardedJedis = null;
        try {
            shardedJedis = getResource();
            List<String> ret = shardedJedis.blpop(key);
            return ret;
        } catch (Exception e) {
            returnBrokenResource(shardedJedis);
            log.error(e.getMessage(), e);
            throw new JedisException(e);
        } finally {
            returnResource(shardedJedis);
        }
    }

    public List<String> brpop(String key) {
        ShardedJedis shardedJedis = null;
        try {
            shardedJedis = getResource();
            List<String> ret = shardedJedis.brpop(key);
            return ret;
        } catch (Exception e) {
            returnBrokenResource(shardedJedis);
            log.error(e.getMessage(), e);
            throw new JedisException(e);
        } finally {
            returnResource(shardedJedis);
        }
    }

    public <T> T lpop(String key, Class<T> clazz) {
        return JsonHelper.fromJson(lpop(key), clazz);
    }

    /**
     * 弹出头部元素
     *
     * @return
     */
    public String rpop(String key) {
        ShardedJedis shardedJedis = null;
        try {
            shardedJedis = getResource();
            String ret = shardedJedis.rpop(key);
            return ret;
        } catch (Exception e) {
            returnBrokenResource(shardedJedis);
            log.error(e.getMessage(), e);
            throw new JedisException(e);
        } finally {
            returnResource(shardedJedis);
        }
    }

    public <T> T rpop(String key, Class<T> clazz) {
        return JsonHelper.fromJson(rpop(key), clazz);
    }

    /**
     * 获取LIST长度
     *
     * @return list长度
     */
    public long llen(String key) {
        ShardedJedis shardedJedis = null;
        try {
            shardedJedis = getResource();
            long ret = shardedJedis.llen(key);
            return ret;
        } catch (Exception e) {
            returnBrokenResource(shardedJedis);
            log.error(e.getMessage(), e);
            throw new JedisException(e);
        } finally {
            returnResource(shardedJedis);
        }
    }

    /**
     * 删除LIST中的值
     *
     * @return list长度
     */
    public long lrem(String key, long count, String value) {
        ShardedJedis shardedJedis = null;
        try {
            shardedJedis = getResource();
            long ret = shardedJedis.lrem(key, count, value);
            return ret;
        } catch (Exception e) {
            returnBrokenResource(shardedJedis);
            log.error(e.getMessage(), e);
            throw new JedisException(e);
        } finally {
            returnResource(shardedJedis);
        }
    }

    public long lrem(String key, long count, Object obj) {
        return lrem(key, count, JsonHelper.toJson(obj));
    }

    /**
     * 修剪
     *
     * @return list长度
     */
    public String ltrim(String key, long start, long end) {
        ShardedJedis shardedJedis = null;
        try {
            shardedJedis = getResource();
            String ret = shardedJedis.ltrim(key, start, end);
            return ret;
        } catch (Exception e) {
            returnBrokenResource(shardedJedis);
            log.error(e.getMessage(), e);
            throw new JedisException(e);
        } finally {
            returnResource(shardedJedis);
        }
    }

    /**
     * 获取key这个List，从第几个元素到第几个元素 LRANGE key start
     *
     * @param key
     *            List别名
     * @param start
     *            开始下标
     * @param end
     *            结束下标
     * @return
     */
    public List<String> lrange(String key, long start, long end) {
        ShardedJedis shardedJedis = null;
        try {
            shardedJedis = getResource();
            List<String> list = shardedJedis.lrange(key, start, end);
            return list;
        } catch (Exception e) {
            returnBrokenResource(shardedJedis);
            log.error(e.getMessage(), e);
            throw new JedisException(e);
        } finally {
            returnResource(shardedJedis);
        }
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public <T> List<T> lrange(String key, long start, long end, Class<T> clazz) {
        List<String> lrange = lrange(key, start, end);
        List<T> returnList = new ArrayList();
        for (String string : lrange) {
            returnList.add(JsonHelper.fromJson(string, clazz));
        }
        return returnList;
    }

    /**
     * 将哈希表key中的域field的值设为value。
     *
     * @param key
     *            哈希表别名
     * @param value
     *            值
     */
    public void hset(String key, String field, String value) {
        ShardedJedis shardedJedis = null;
        try {
            shardedJedis = getResource();
            shardedJedis.hset(key, field, value);
        } catch (Exception e) {
            returnBrokenResource(shardedJedis);
            log.error(e.getMessage(), e);
            throw new JedisException(e);
        } finally {
            returnResource(shardedJedis);
        }
    }

    public void hset(String key, String field, Object value) {
        hset(key, field, JsonHelper.toJson(value));
    }

    /**
     *
     * @param key
     * @param value
     */
    public void set(String key, String value) {
        ShardedJedis shardedJedis = null;
        try {
            shardedJedis = getResource();
            shardedJedis.set(key, value);
        } catch (Exception e) {
            returnBrokenResource(shardedJedis);
            log.error(e.getMessage(), e);
            throw new JedisException(e);
        } finally {
            returnResource(shardedJedis);
        }
    }

    public void set(String key, Object value) {
        set(key, JsonHelper.toJson(value));
    }

    /**
     * 获取key的值
     *
     * @param key
     * @return
     */
    public String get(String key) {
        ShardedJedis shardedJedis = null;
        try {
            shardedJedis = getResource();
            String value = shardedJedis.get(key);
            return value;
        } catch (Exception e) {
            returnBrokenResource(shardedJedis);
            log.error(e.getMessage(), e);
            throw new JedisException(e);
        } finally {
            returnResource(shardedJedis);
        }
    }

    public byte[] get(byte[] key) {
        ShardedJedis shardedJedis = null;
        try {
            shardedJedis = getResource();
            byte[] value = shardedJedis.get(key);
            return value;
        } catch (Exception e) {
            returnBrokenResource(shardedJedis);
            log.error(e.getMessage(), e);
            throw new JedisException(e);
        } finally {
            returnResource(shardedJedis);
        }
    }

    public <T> T get(String key, Class<T> clazz) {
        return JsonHelper.fromJson(get(key), clazz);
    }

    /**
     * 获取key的值
     *
     * @param key
     * @return
     */
    public long del(String key) {
        ShardedJedis shardedJedis = null;
        try {
            shardedJedis = getResource();
            long value = shardedJedis.del(key);
            return value;
        } catch (Exception e) {
            returnBrokenResource(shardedJedis);
            log.error(e.getMessage(), e);
            throw new JedisException(e);
        } finally {
            returnResource(shardedJedis);
        }
    }

    /**
     * 将多个field - value(域-值)对设置到哈希表key中。
     *
     * @param key
     * @param map
     */
    public void hmset(String key, Map<String, String> map) {
        ShardedJedis shardedJedis = null;
        try {
            shardedJedis = getResource();
            shardedJedis.hmset(key, map);
        } catch (Exception e) {
            returnBrokenResource(shardedJedis);
            log.error(e.getMessage(), e);
            throw new JedisException(e);
        } finally {
            returnResource(shardedJedis);
        }
    }

    public void hmset2(String key, Map<String, Object> map) {
        Map<String, String> bMap = new LinkedHashMap<String, String>();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            bMap.put(entry.getKey(), JsonHelper.toJson(entry.getValue()));
        }
        hmset(key, bMap);
    }

    /**
     * 给key赋值，并生命周期设置为seconds
     *
     * @param key
     * @param seconds
     *            生命周期 秒为单位
     * @param value
     */
    public void setex(String key, int seconds, String value) {
        ShardedJedis shardedJedis = null;
        try {
            shardedJedis = getResource();
            shardedJedis.setex(key, seconds, value);
        } catch (Exception e) {
            returnBrokenResource(shardedJedis);
            log.error(e.getMessage(), e);
            throw new JedisException(e);
        } finally {
            returnResource(shardedJedis);
        }
    }

    public void setex(String key, int seconds, Object value) {
        setex(key, seconds, JsonHelper.toJson(value));
    }

    /**
     * 为给定key设置生命周期
     *
     * @param key
     * @param seconds
     *            生命周期 秒为单位
     *
     */
    public void expire(String key, int seconds) {
        ShardedJedis shardedJedis = null;
        try {
            shardedJedis = getResource();
            shardedJedis.expire(key, seconds);
        } catch (Exception e) {
            returnBrokenResource(shardedJedis);
            log.error(e.getMessage(), e);
            throw new JedisException(e);
        } finally {
            returnResource(shardedJedis);
        }
    }

    public long setnx(String key, String string) {
        ShardedJedis shardedJedis = null;

        long var6;
        try {
            shardedJedis = this.getResource();
            long ret = shardedJedis.setnx(key, string);
            var6 = ret;
        } catch (Exception var11) {
            this.returnBrokenResource(shardedJedis);
            log.error(var11.getMessage(), var11);
            throw new JedisException(var11);
        } finally {
            this.returnResource(shardedJedis);
        }

        return var6;
    }

    /**
     * 获取key的有效时间
     *
     * @param key
     * @return seconds 生命时间 有效时间
     *
     */
    public Long ttl(String key) {
        ShardedJedis shardedJedis = null;
        try {
            shardedJedis = getResource();
            return shardedJedis.ttl(key);
        } catch (Exception e) {
            returnBrokenResource(shardedJedis);
            log.error(e.getMessage(), e);
            throw new JedisException(e);
        } finally {
            returnResource(shardedJedis);
        }
    }

    /**
     * 检查key是否存在
     *
     * @param key
     * @return
     */
    public boolean exists(String key) {
        ShardedJedis shardedJedis = null;
        try {
            shardedJedis = getResource();
            boolean bool = shardedJedis.exists(key);
            return bool;
        } catch (Exception e) {
            returnBrokenResource(shardedJedis);
            log.error(e.getMessage(), e);
            throw new JedisException(e);
        } finally {
            returnResource(shardedJedis);
        }
    }

    /**
     * 返回key值的类型 none(key不存在),string(字符串),list(列表),set(集合),zset(有序集),hash(哈希表)
     *
     * @param key
     * @return
     */
    public String type(String key) {
        ShardedJedis shardedJedis = null;
        try {
            shardedJedis = getResource();
            String type = shardedJedis.type(key);
            return type;
        } catch (Exception e) {
            returnBrokenResource(shardedJedis);
            log.error(e.getMessage(), e);
            throw new JedisException(e);
        } finally {
            returnResource(shardedJedis);
        }
    }

    /**
     * 从哈希表key中获取field的value
     *
     * @param key
     * @param field
     */
    public String hget(String key, String field) {
        ShardedJedis shardedJedis = null;
        try {
            shardedJedis = getResource();
            String value = shardedJedis.hget(key, field);
            return value;
        } catch (Exception e) {
            returnBrokenResource(shardedJedis);
            log.error(e.getMessage(), e);
            throw new JedisException(e);
        } finally {
            returnResource(shardedJedis);
        }
    }

    /**
     * 返回哈希表key中，所有的域和值 默认无序
     *
     * @param key
     * @return
     */
    public Map<String, String> hgetAll(String key) {
        return hgetAll(key, false);
    }

    /**
     * 返回哈希表key中，所有的域和值
     *
     * @param key
     * @param order
     *            是否保持原始顺序
     * @return
     */
    public Map<String, String> hgetAll(String key, boolean order) {
        ShardedJedis shardedJedis = null;
        try {
            shardedJedis = getResource();
            if (order) {
                Jedis shard = shardedJedis.getShard(key);
                if (shard.getClient().isInMulti()) {
                    throw new JedisDataException(
                            "Cannot use Jedis when in Multi. Please use JedisTransaction instead.");
                }
                shard.getClient().hgetAll(key);
                return STRING_LINKEDHASHMAP.build(shard.getClient()
                        .getBinaryMultiBulkReply());
            } else {
                return shardedJedis.hgetAll(key);
            }
        } catch (Exception e) {
            returnBrokenResource(shardedJedis);
            log.error(e.getMessage(), e);
            throw new JedisException(e);
        } finally {
            returnResource(shardedJedis);
        }

    }

    /**
     * 返回哈希表key中，所有的域和值 默认有序
     *
     * @param key
     * @return
     */
    public Map<String, String> hgetAllToLinkedHashMap(String key) {
        return hgetAll(key, true);
    }

    public static final Builder<Set<String>> STRING_LINKEDHASHSET = new Builder<Set<String>>() {
        @SuppressWarnings("unchecked")
        public Set<String> build(Object data) {
            if (null == data) {
                return null;
            }
            List<byte[]> l = (List<byte[]>) data;
            final Set<String> result = new LinkedHashSet<String>(l.size());
            for (final byte[] barray : l) {
                if (barray == null) {
                    result.add(null);
                } else {
                    result.add(SafeEncoder.encode(barray));
                }
            }
            return result;
        }

        public String toString() {
            return "Set<String>";
        }
    };

    public static final Builder<Map<String, String>> STRING_LINKEDHASHMAP = new Builder<Map<String, String>>() {
        @SuppressWarnings("unchecked")
        public Map<String, String> build(Object data) {
            final List<byte[]> flatHash = (List<byte[]>) data;
            final Map<String, String> hash = new LinkedHashMap<String, String>();
            final Iterator<byte[]> iterator = flatHash.iterator();
            while (iterator.hasNext()) {
                hash.put(SafeEncoder.encode(iterator.next()),
                        SafeEncoder.encode(iterator.next()));
            }

            return hash;
        }

        public String toString() {
            return "Map<String, String>";
        }

    };

    /**
     * 返回哈希表key中，所有值
     *
     * @param key
     * @return
     */
    public Set<?> smembers(String key) {
        ShardedJedis shardedJedis = null;
        try {
            shardedJedis = getResource();
            Set<?> set = shardedJedis.smembers(key);
            return set;
        } catch (Exception e) {
            returnBrokenResource(shardedJedis);
            log.error(e.getMessage(), e);
            throw new JedisException(e);
        } finally {
            returnResource(shardedJedis);
        }
    }

    /**
     * 移除集合中的member元素
     *
     * @param key
     *            List别名
     * @param field
     *            键
     */
    public void srem(String key, String... field) {
        ShardedJedis shardedJedis = null;
        try {
            shardedJedis = getResource();
            shardedJedis.srem(key, field);
        } catch (Exception e) {
            returnBrokenResource(shardedJedis);
            log.error(e.getMessage(), e);
            throw new JedisException(e);
        } finally {
            returnResource(shardedJedis);
        }
    }

    public void srem(String key, Object... field) {
        String[] arr = new String[field.length];
        for (int i = 0; i < field.length; i++) {
            arr[i] = JsonHelper.toJson(field[i]);
        }
        srem(key, arr);
    }

    /**
     * 判断member元素是否是集合key的成员。是（true），否则（false）
     *
     * @param key
     * @param field
     * @return
     */
    public boolean sismember(String key, String field) {
        ShardedJedis shardedJedis = null;
        try {
            shardedJedis = getResource();
            boolean bool = shardedJedis.sismember(key, field);
            return bool;
        } catch (Exception e) {
            returnBrokenResource(shardedJedis);
            log.error(e.getMessage(), e);
            throw new JedisException(e);
        } finally {
            returnResource(shardedJedis);
        }
    }

    public boolean sismember(String key, Object field) {
        return sismember(key, JsonHelper.toJson(field));
    }

    /**
     * 如果key已经存在并且是一个字符串，将value追加到key原来的值之后
     *
     * @param key
     * @param value
     */
    public void append(String key, String value) {
        ShardedJedis shardedJedis = null;
        try {
            shardedJedis = getResource();
            shardedJedis.append(key, value);
        } catch (Exception e) {
            returnBrokenResource(shardedJedis);
            log.error(e.getMessage(), e);
            throw new JedisException(e);
        } finally {
            returnResource(shardedJedis);
        }
    }

    /**
     * -- key
     *
     * @param key
     */
    public Long decr(String key) {
        ShardedJedis shardedJedis = null;
        try {
            shardedJedis = getResource();
            return shardedJedis.decr(key);
        } catch (Exception e) {
            returnBrokenResource(shardedJedis);
            log.error(e.getMessage(), e);
            throw new JedisException(e);
        } finally {
            returnResource(shardedJedis);
        }
    }

    /**
     * key 减指定数值
     *
     * @param key
     */
    public Long decrBy(String key, Integer integer) {
        ShardedJedis shardedJedis = null;
        try {
            shardedJedis = getResource();
            return shardedJedis.decrBy(key, integer);
        } catch (Exception e) {
            returnBrokenResource(shardedJedis);
            log.error(e.getMessage(), e);
            throw new JedisException(e);
        } finally {
            returnResource(shardedJedis);
        }
    }

    /**
     * 删除key
     *
     * @param key
     */
    public Long decrBy(String key) {
        ShardedJedis shardedJedis = null;
        try {
            shardedJedis = getResource();
            return shardedJedis.del(key);
        } catch (Exception e) {
            returnBrokenResource(shardedJedis);
            log.error(e.getMessage(), e);
            throw new JedisException(e);
        } finally {
            returnResource(shardedJedis);
        }
    }

    /**
     * 这里的N是返回的string的长度。复杂度是由返回的字符串长度决定的，但是因为从一个已经存在的字符串创建一个子串是很容易的，所以对于较小的字符串，
     * 可以认为是O(1)的复杂度。
     *
     * @param key
     */
    public String decrBy(String key, int startOffset, int endOffset) {
        ShardedJedis shardedJedis = null;
        try {
            shardedJedis = getResource();
            return shardedJedis.getrange(key, startOffset, endOffset);
        } catch (Exception e) {
            returnBrokenResource(shardedJedis);
            log.error(e.getMessage(), e);
            throw new JedisException(e);
        } finally {
            returnResource(shardedJedis);
        }
    }

    /**
     * 自动将key对应到value并且返回原来key对应的value。如果key存在但是对应的value不是字符串，就返回错误。
     *
     * @param key
     * @param value
     */
    public String decrBy(String key, String value) {
        ShardedJedis shardedJedis = null;
        try {
            shardedJedis = getResource();
            return shardedJedis.getSet(key, value);
        } catch (Exception e) {
            returnBrokenResource(shardedJedis);
            log.error(e.getMessage(), e);
            throw new JedisException(e);
        } finally {
            returnResource(shardedJedis);
        }
    }

    /**
     * 从 key 指定的哈希集中移除指定的域。在哈希集中不存在的域将被忽略。如果 key
     * 指定的哈希集不存在，它将被认为是一个空的哈希集，该命令将返回0。
     *
     * 返回值 整数：返回从哈希集中成功移除的域的数量，不包括指出但不存在的那些域
     *
     *
     *
     * @param key
     * @param fields
     */
    public Long hdel(String key, String... fields) {
        ShardedJedis shardedJedis = null;
        try {
            shardedJedis = getResource();
            return shardedJedis.hdel(key, fields);
        } catch (Exception e) {
            returnBrokenResource(shardedJedis);
            log.error(e.getMessage(), e);
            throw new JedisException(e);
        } finally {
            returnResource(shardedJedis);
        }
    }

    /**
     * 返回字段是否是 key 指定的哈希集中存在的字段。
     *
     * 返回值 整数, 含义如下：
     *
     * 1 哈希集中含有该字段。 0 哈希集中不含有该存在字段，或者key不存在。
     *
     *
     *
     *
     * @param key
     * @param fields
     */
    public Boolean hexists(String key, String fields) {
        ShardedJedis shardedJedis = null;
        try {
            shardedJedis = getResource();
            return shardedJedis.hexists(key, fields);
        } catch (Exception e) {
            returnBrokenResource(shardedJedis);
            log.error(e.getMessage(), e);
            throw new JedisException(e);
        } finally {
            returnResource(shardedJedis);
        }
    }

    public Boolean hexists(String key, Object field) {
        return hexists(key, JsonHelper.toJson(field));
    }

    /**
     * 增加 key 指定的哈希集中指定字段的数值。如果 key 不存在，会创建一个新的哈希集并与 key
     * 关联。如果字段不存在，则字段的值在该操作执行前被设置为 0
     *
     * HINCRBY 支持的值的范围限定在 64位 有符号整数
     *
     * 返回值 整数：增值操作执行后的该字段的值。
     *
     *
     * @param key
     * @param value
     */
    public Long hincrBy(String key, String field, int value) {
        ShardedJedis shardedJedis = null;
        try {
            shardedJedis = getResource();
            return shardedJedis.hincrBy(key, field, value);
        } catch (Exception e) {
            returnBrokenResource(shardedJedis);
            log.error(e.getMessage(), e);
            throw new JedisException(e);
        } finally {
            returnResource(shardedJedis);
        }
    }

    /**
     * 返回 key 指定的哈希集中所有字段的名字。
     *
     * 返回值 多个返回值：哈希集中的字段列表，当 key 指定的哈希集不存在时返回空列表。
     *
     * @param key
     *
     * @return 返回值为linkedhashset有序
     */
    public Set<String> hkeys(String key) {
        ShardedJedis shardedJedis = null;
        try {
            shardedJedis = getResource();
            Jedis shard = shardedJedis.getShard(key);
            if (shard.getClient().isInMulti()) {
                throw new JedisDataException(
                        "Cannot use Jedis when in Multi. Please use JedisTransaction instead.");
            }
            shard.getClient().hkeys(key);
            return STRING_LINKEDHASHSET.build(shard.getClient()
                    .getBinaryMultiBulkReply());
        } catch (Exception e) {
            returnBrokenResource(shardedJedis);
            log.error(e.getMessage(), e);
            throw new JedisException(e);
        } finally {
            returnResource(shardedJedis);
        }
    }

    /**
     * key field 原子性incr value
     *
     * 返回值incr后的值
     *
     *
     * @param key
     * @param field
     * @param value
     * @return
     *
     */
    public Double hincrbyfloat(String key, String field, double value) {
        ShardedJedis shardedJedis = null;
        try {
            shardedJedis = getResource();
            return shardedJedis.hincrByFloat(key, field, value);
        } catch (Exception e) {
            returnBrokenResource(shardedJedis);
            log.error(e.getMessage(), e);
            throw new JedisException(e);
        } finally {
            returnResource(shardedJedis);
        }
    }

    /**
     * 增量迭代一个集合元素 SCAN
     * 命令是一个基于游标的迭代器。这意味着命令每次被调用都需要使用上一次这个调用返回的游标作为该次调用的游标参数，以此来延续之前的迭代过程 当 SCAN
     * 命令的游标参数被设置为 0 时， 服务器将开始一次新的迭代， 而当服务器向用户返回值为 0 的游标时， 表示迭代已结束。<br>
     * 量式迭代命令， 在进行完整遍历的情况下可以为用户带来以下保证 ： 从完整遍历开始直到完整遍历结束期间，
     * 一直存在于数据集内的所有元素都会被完整遍历返回； 这意味着， 如果有一个元素， 它从遍历开始直到遍历结束期间都存在于被遍历的数据集当中， 那么
     * SCAN 命令总会在某次迭代中将这个元素返回给用户。
     * 同样，如果一个元素在开始遍历之前被移出集合，并且在遍历开始直到遍历结束期间都没有再加入，那么在遍历返回的元素集中就不会出现该元素。
     * 然而因为增量式命令仅仅使用游标来记录迭代状态， 所以这些命令带有以下缺点： 同一个元素可能会被返回多次。 处理重复元素的工作交由应用程序负责，
     * 比如说， 可以考虑将迭代返回的元素仅仅用于可以安全地重复执行多次的操作上。 如果一个元素是在迭代过程中被添加到数据集的，
     * 又或者是在迭代过程中从数据集中被删除的， 那么这个元素可能会被返回， 也可能不会。
     *
     *
     * @param key
     * @param cursor
     *            游标
     * @return
     *
     */
    public ScanResult<Map.Entry<String, String>> hscan(String key, String cursor) {
        ShardedJedis shardedJedis = null;
        try {
            shardedJedis = getResource();
            return shardedJedis.hscan(key, cursor);
        } catch (Exception e) {
            returnBrokenResource(shardedJedis);
            log.error(e.getMessage(), e);
            throw new JedisException(e);
        } finally {
            returnResource(shardedJedis);
        }
    }

    /**
     * 返回 key 指定的哈希集包含的字段的数量。
     *
     * 返回值 整数：哈希集中字段的数量，当 key 指定的哈希集不存在时返回 0
     *
     * @param key
     *
     */
    public Long hlen(String key) {
        ShardedJedis shardedJedis = null;
        try {
            shardedJedis = getResource();
            return shardedJedis.hlen(key);
        } catch (Exception e) {
            returnBrokenResource(shardedJedis);
            log.error(e.getMessage(), e);
            throw new JedisException(e);
        } finally {
            returnResource(shardedJedis);
        }
    }

    /**
     * 返回 key 指定的哈希集中指定字段的值。
     *
     * 对于哈希集中不存在的每个字段，返回 nil 值。因为不存在的keys被认为是一个空的哈希集，对一个不存在的 key 执行 HMGET
     * 将返回一个只含有 nil 值的列表
     *
     * 返回值 多个返回值：含有给定字段及其值的列表，并保持与请求相同的顺序。
     *
     *
     *
     * @param key
     * @param fields
     */
    public List<String> hmget(String key, String... fields) {
        ShardedJedis shardedJedis = null;
        try {
            shardedJedis = getResource();
            return shardedJedis.hmget(key, fields);
        } catch (Exception e) {
            returnBrokenResource(shardedJedis);
            log.error(e.getMessage(), e);
            throw new JedisException(e);
        } finally {
            returnResource(shardedJedis);
        }
    }

    /**
     * 只在 key 指定的哈希集中不存在指定的字段时，设置字段的值。如果 key 指定的哈希集不存在，会创建一个新的哈希集并与 key
     * 关联。如果字段已存在，该操作无效果。
     *
     * 返回值 整数：含义如下
     *
     * 1：如果字段是个新的字段，并成功赋值 0：如果哈希集中已存在该字段，没有操作被执行
     *
     *
     * @param key
     *
     */
    public Long hsetnx(String key, String field, String value) {
        ShardedJedis shardedJedis = null;
        try {
            shardedJedis = getResource();
            return shardedJedis.hsetnx(key, field, value);
        } catch (Exception e) {
            returnBrokenResource(shardedJedis);
            log.error(e.getMessage(), e);
            throw new JedisException(e);
        } finally {
            returnResource(shardedJedis);
        }
    }

    public Long hsetnx(String key, String field, Object value) {
        return hsetnx(key, field, JsonHelper.toJson(value));
    }

    /**
     * 返回 key 指定的哈希集中所有字段的值。
     *
     * 返回值 多个返回值：哈希集中的值的列表，当 key 指定的哈希集不存在时返回空列表。
     *
     *
     * @param key
     * @return 返回值为arraylist 有序
     */
    public List<String> hvals(String key) {
        ShardedJedis shardedJedis = null;
        try {
            shardedJedis = getResource();
            return shardedJedis.hvals(key);
        } catch (Exception e) {
            returnBrokenResource(shardedJedis);
            log.error(e.getMessage(), e);
            throw new JedisException(e);
        } finally {
            returnResource(shardedJedis);
        }
    }

    /**
     * ++key
     *
     * @param key
     *
     */
    public Long incr(String key) {
        ShardedJedis shardedJedis = null;
        try {
            shardedJedis = getResource();
            return shardedJedis.incr(key);
        } catch (Exception e) {
            returnBrokenResource(shardedJedis);
            log.error(e.getMessage(), e);
            throw new JedisException(e);
        } finally {
            returnResource(shardedJedis);
        }
    }

    /**
     * 参选，如果参选成功将为key续期,如果已有被选举人则判断是否为自己 为自己则续期
     *
     * @param key
     *            参选项目
     * @param candidates
     *            候选人
     * @param timeOut
     *            参选成功后的过期时间
     * @return true 参选成功并续期成功 或 被选举人为自己并续期成功， false 参选失败或被选举人不为自己
     */
    public boolean electioneer(String key, String candidates, int timeOut) {
        ShardedJedis shardedJedis = null;
        try {
            shardedJedis = getResource();
            int setnx = shardedJedis.setnx(key, candidates).intValue();
            boolean bool = false;
            if (setnx == 1) {
                shardedJedis.expire(key, timeOut);
                bool = true;
            } else {
                String electee = shardedJedis.get(key);
                if (electee != null && electee.equals(candidates)) {
                    shardedJedis.expire(key, timeOut);
                    bool = true;
                }
            }
            return bool;
        } catch (Exception e) {
            returnBrokenResource(shardedJedis);
            log.error(e.getMessage(), e);
            throw new JedisException(e);
        } finally {
            returnResource(shardedJedis);
        }
    }

    /**
     *
     * 将key对应的数字加decrement。如果key不存在，操作之前，key就会被置为0。
     * 如果key的value类型错误或者是个不能表示成数字的字符串，就返回错误。这个操作最多支持64位有符号的正型数字。
     *
     * 查看命令INCR了解关于增减操作的额外信息。
     *
     * 返回值 数字：增加之后的value值。
     *
     *
     * @param key
     * @param integer
     */
    public Long incrBy(String key, int integer) {
        ShardedJedis shardedJedis = null;
        try {
            shardedJedis = getResource();
            return shardedJedis.incrBy(key, integer);
        } catch (Exception e) {
            returnBrokenResource(shardedJedis);
            log.error(e.getMessage(), e);
            throw new JedisException(e);
        } finally {
            returnResource(shardedJedis);
        }
    }

    public Long zadd(String key, Map<String, Double> scoreMembers) {
        ShardedJedis shardedJedis = null;
        try {
            shardedJedis = getResource();
            return shardedJedis.zadd(key, scoreMembers);
        } catch (Exception e) {
            returnBrokenResource(shardedJedis);
            log.error(e.getMessage(), e);
            throw new JedisException(e);
        } finally {
            returnResource(shardedJedis);
        }
    }

    public Long zadd(String key, double score, String member) {
        ShardedJedis shardedJedis = null;
        try {
            shardedJedis = getResource();
            return shardedJedis.zadd(key, score, member);
        } catch (Exception e) {
            returnBrokenResource(shardedJedis);
            log.error(e.getMessage(), e);
            throw new JedisException(e);
        } finally {
            returnResource(shardedJedis);
        }
    }

    public Long zcard(String key) {
        ShardedJedis shardedJedis = null;
        try {
            shardedJedis = getResource();
            return shardedJedis.zcard(key);
        } catch (Exception e) {
            returnBrokenResource(shardedJedis);
            log.error(e.getMessage(), e);
            throw new JedisException(e);
        } finally {
            returnResource(shardedJedis);
        }
    }

    public Long zcount(String key, double min, double max) {
        ShardedJedis shardedJedis = null;
        try {
            shardedJedis = getResource();
            return shardedJedis.zcount(key, min, max);
        } catch (Exception e) {
            returnBrokenResource(shardedJedis);
            log.error(e.getMessage(), e);
            throw new JedisException(e);
        } finally {
            returnResource(shardedJedis);
        }
    }

    public Long zcount(String key, String min, String max) {
        ShardedJedis shardedJedis = null;
        try {
            shardedJedis = getResource();
            return shardedJedis.zcount(key, min, max);
        } catch (Exception e) {
            returnBrokenResource(shardedJedis);
            log.error(e.getMessage(), e);
            throw new JedisException(e);
        } finally {
            returnResource(shardedJedis);
        }
    }

    public Double zincrby(String key, double score, String member) {
        ShardedJedis shardedJedis = null;
        try {
            shardedJedis = getResource();
            return shardedJedis.zincrby(key, score, member);
        } catch (Exception e) {
            returnBrokenResource(shardedJedis);
            log.error(e.getMessage(), e);
            throw new JedisException(e);
        } finally {
            returnResource(shardedJedis);
        }
    }

    public Set<String> zrange(String key, long start, long end) {
        ShardedJedis shardedJedis = null;
        try {
            shardedJedis = getResource();
            return shardedJedis.zrange(key, start, end);
        } catch (Exception e) {
            returnBrokenResource(shardedJedis);
            log.error(e.getMessage(), e);
            throw new JedisException(e);
        } finally {
            returnResource(shardedJedis);
        }
    }

    public Set<Tuple> zrangeByScoreWithScores(String key, double min, double max) {
        ShardedJedis shardedJedis = null;
        try {
            shardedJedis = getResource();
            return shardedJedis.zrangeByScoreWithScores(key, min, max);
        } catch (Exception e) {
            returnBrokenResource(shardedJedis);
            log.error(e.getMessage(), e);
            throw new JedisException(e);
        } finally {
            returnResource(shardedJedis);
        }
    }

    public Set<Tuple> zrangeByScoreWithScores(String key, String min, String max) {
        ShardedJedis shardedJedis = null;
        try {
            shardedJedis = getResource();
            return shardedJedis.zrangeByScoreWithScores(key, min, max);
        } catch (Exception e) {
            returnBrokenResource(shardedJedis);
            log.error(e.getMessage(), e);
            throw new JedisException(e);
        } finally {
            returnResource(shardedJedis);
        }
    }

    public Set<Tuple> zrangeWithScores(String key, long start, long end) {
        ShardedJedis shardedJedis = null;
        try {
            shardedJedis = getResource();
            return shardedJedis.zrangeWithScores(key, start, end);
        } catch (Exception e) {
            returnBrokenResource(shardedJedis);
            log.error(e.getMessage(), e);
            throw new JedisException(e);
        } finally {
            returnResource(shardedJedis);
        }
    }

    public Set<String> zrangeByScore(String key, String min, String max) {
        ShardedJedis shardedJedis = null;
        try {
            shardedJedis = getResource();
            return shardedJedis.zrangeByScore(key, min, max);
        } catch (Exception e) {
            returnBrokenResource(shardedJedis);
            log.error(e.getMessage(), e);
            throw new JedisException(e);
        } finally {
            returnResource(shardedJedis);
        }
    }

    public Set<String> zrangeByScore(String key, double min, double max) {
        ShardedJedis shardedJedis = null;
        try {
            shardedJedis = getResource();
            return shardedJedis.zrangeByScore(key, min, max);
        } catch (Exception e) {
            returnBrokenResource(shardedJedis);
            log.error(e.getMessage(), e);
            throw new JedisException(e);
        } finally {
            returnResource(shardedJedis);
        }
    }

    public Set<Tuple> zrangeByScoreWithScores(String key, double min,
                                              double max, int offset, int count) {
        ShardedJedis shardedJedis = null;
        try {
            shardedJedis = getResource();
            return shardedJedis.zrangeByScoreWithScores(key, min, max, offset,
                    count);
        } catch (Exception e) {
            returnBrokenResource(shardedJedis);
            log.error(e.getMessage(), e);
            throw new JedisException(e);
        } finally {
            returnResource(shardedJedis);
        }
    }

    public Set<Tuple> zrangeByScoreWithScores(String key, String min,
                                              String max, int offset, int count) {
        ShardedJedis shardedJedis = null;
        try {
            shardedJedis = getResource();
            return shardedJedis.zrangeByScoreWithScores(key, min, max, offset,
                    count);
        } catch (Exception e) {
            returnBrokenResource(shardedJedis);
            log.error(e.getMessage(), e);
            throw new JedisException(e);
        } finally {
            returnResource(shardedJedis);
        }
    }

    public Long zrank(String key, String member) {
        ShardedJedis shardedJedis = null;
        try {
            shardedJedis = getResource();
            return shardedJedis.zrank(key, member);
        } catch (Exception e) {
            returnBrokenResource(shardedJedis);
            log.error(e.getMessage(), e);
            throw new JedisException(e);
        } finally {
            returnResource(shardedJedis);
        }
    }

    public Long zrem(String key, String... members) {
        ShardedJedis shardedJedis = null;
        try {
            shardedJedis = getResource();
            return shardedJedis.zrem(key, members);
        } catch (Exception e) {
            returnBrokenResource(shardedJedis);
            log.error(e.getMessage(), e);
            throw new JedisException(e);
        } finally {
            returnResource(shardedJedis);
        }
    }

    public Long zremrangeByRank(String key, long start, long end) {
        ShardedJedis shardedJedis = null;
        try {
            shardedJedis = getResource();
            return shardedJedis.zremrangeByRank(key, start, end);
        } catch (Exception e) {
            returnBrokenResource(shardedJedis);
            log.error(e.getMessage(), e);
            throw new JedisException(e);
        } finally {
            returnResource(shardedJedis);
        }
    }

    public Long zremrangeByScore(String key, double start, double end) {
        ShardedJedis shardedJedis = null;
        try {
            shardedJedis = getResource();
            return shardedJedis.zremrangeByScore(key, start, end);
        } catch (Exception e) {
            returnBrokenResource(shardedJedis);
            log.error(e.getMessage(), e);
            throw new JedisException(e);
        } finally {
            returnResource(shardedJedis);
        }
    }

    public Long zremrangeByScore(String key, String start, String end) {
        ShardedJedis shardedJedis = null;
        try {
            shardedJedis = getResource();
            return shardedJedis.zremrangeByScore(key, start, end);
        } catch (Exception e) {
            returnBrokenResource(shardedJedis);
            log.error(e.getMessage(), e);
            throw new JedisException(e);
        } finally {
            returnResource(shardedJedis);
        }
    }

    public Set<String> zrevrange(String key, long start, long end) {
        ShardedJedis shardedJedis = null;
        try {
            shardedJedis = getResource();
            return shardedJedis.zrevrange(key, start, end);
        } catch (Exception e) {
            returnBrokenResource(shardedJedis);
            log.error(e.getMessage(), e);
            throw new JedisException(e);
        } finally {
            returnResource(shardedJedis);
        }
    }

    public Set<String> zrevrangeByScore(String key, double max, double min) {
        ShardedJedis shardedJedis = null;
        try {
            shardedJedis = getResource();
            return shardedJedis.zrevrangeByScore(key, max, min);
        } catch (Exception e) {
            returnBrokenResource(shardedJedis);
            log.error(e.getMessage(), e);
            throw new JedisException(e);
        } finally {
            returnResource(shardedJedis);
        }
    }

    public Set<String> zrevrangeByScore(String key, String max, String min) {
        ShardedJedis shardedJedis = null;
        try {
            shardedJedis = getResource();
            return shardedJedis.zrevrangeByScore(key, max, min);
        } catch (Exception e) {
            returnBrokenResource(shardedJedis);
            log.error(e.getMessage(), e);
            throw new JedisException(e);
        } finally {
            returnResource(shardedJedis);
        }
    }

    public Set<Tuple> zrevrangeByScoreWithScores(String key, String max,
                                                 String min) {
        ShardedJedis shardedJedis = null;
        try {
            shardedJedis = getResource();
            return shardedJedis.zrevrangeByScoreWithScores(key, max, min);
        } catch (Exception e) {
            returnBrokenResource(shardedJedis);
            log.error(e.getMessage(), e);
            throw new JedisException(e);
        } finally {
            returnResource(shardedJedis);
        }
    }

    public Set<Tuple> zrevrangeByScoreWithScores(String key, double max,
                                                 double min) {
        ShardedJedis shardedJedis = null;
        try {
            shardedJedis = getResource();
            return shardedJedis.zrevrangeByScoreWithScores(key, max, min);
        } catch (Exception e) {
            returnBrokenResource(shardedJedis);
            log.error(e.getMessage(), e);
            throw new JedisException(e);
        } finally {
            returnResource(shardedJedis);
        }
    }

    public Set<Tuple> zrevrangeByScoreWithScores(String key, double max,
                                                 double min, int offset, int count) {
        ShardedJedis shardedJedis = null;
        try {
            shardedJedis = getResource();
            return shardedJedis.zrevrangeByScoreWithScores(key, max, min,
                    offset, count);
        } catch (Exception e) {
            returnBrokenResource(shardedJedis);
            log.error(e.getMessage(), e);
            throw new JedisException(e);
        } finally {
            returnResource(shardedJedis);
        }
    }

    public Set<Tuple> zrevrangeByScoreWithScores(String key, String max,
                                                 String min, int offset, int count) {
        ShardedJedis shardedJedis = null;
        try {
            shardedJedis = getResource();
            return shardedJedis.zrevrangeByScoreWithScores(key, max, min,
                    offset, count);
        } catch (Exception e) {
            returnBrokenResource(shardedJedis);
            log.error(e.getMessage(), e);
            throw new JedisException(e);
        } finally {
            returnResource(shardedJedis);
        }
    }

    public Set<Tuple> zrevrangeWithScores(String key, long start, long end) {
        ShardedJedis shardedJedis = null;
        try {
            shardedJedis = getResource();
            return shardedJedis.zrevrangeWithScores(key, start, end);
        } catch (Exception e) {
            returnBrokenResource(shardedJedis);
            log.error(e.getMessage(), e);
            throw new JedisException(e);
        } finally {
            returnResource(shardedJedis);
        }
    }

    public Long zrevrank(String key, String member) {
        ShardedJedis shardedJedis = null;
        try {
            shardedJedis = getResource();
            return shardedJedis.zrevrank(key, member);
        } catch (Exception e) {
            returnBrokenResource(shardedJedis);
            log.error(e.getMessage(), e);
            throw new JedisException(e);
        } finally {
            returnResource(shardedJedis);
        }
    }

    public ScanResult<Tuple> zscan(String key, String cursor) {
        ShardedJedis shardedJedis = null;
        try {
            shardedJedis = getResource();
            return shardedJedis.zscan(key, cursor);
        } catch (Exception e) {
            returnBrokenResource(shardedJedis);
            log.error(e.getMessage(), e);
            throw new JedisException(e);
        } finally {
            returnResource(shardedJedis);
        }
    }

    /**
     * 订阅指定shardkey下的某些频道
     *
     * @param shardkey
     *            用于shard定向
     * @param jedisPubSub
     *            用于回调
     * @param channels
     *            频道名称
     */
    public void subscribe(String shardkey, JedisPubSub jedisPubSub,
                          String... channels) {
        ShardedJedis shardedJedis = null;
        try {
            shardedJedis = getResource();
            shardedJedis.getShard(shardkey).subscribe(jedisPubSub, channels);
        } catch (Exception e) {
            returnBrokenResource(shardedJedis);
            log.error(e.getMessage(), e);
            throw new JedisException(e);
        } finally {
            returnResource(shardedJedis);
        }
    }

    /**
     * 向指定shardkey下的某频道发送消息
     *
     * @param shardkey
     *            用于shard定向
     * @param channel
     *            频道名称
     * @param message
     *            消息内容
     */
    public void publish(String shardkey, String channel, String message) {
        ShardedJedis shardedJedis = null;
        try {
            shardedJedis = getResource();
            shardedJedis.getShard(shardkey).publish(channel, message);
        } catch (Exception e) {
            returnBrokenResource(shardedJedis);
            log.error(e.getMessage(), e);
            throw new JedisException(e);
        } finally {
            returnResource(shardedJedis);
        }
    }


    /**
     * 根据shardKey获取jedis
     *
     * @param shardKey
     */
    public Jedis getJedisByShardKey(String shardKey) {
        try {
            return getResource().getShard(shardKey);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new JedisException(e);
        }
    }

    /**
     * 判断是否能提交
     */
    public boolean canBeSubmit(String key, String value, int seconds) {
        ShardedJedis shardedJedis = null;
        try {
            shardedJedis = getResource();
            return "OK".equalsIgnoreCase(shardedJedis.set(key, value, "NX", "EX", seconds));
        } catch (Exception e) {
            returnBrokenResource(shardedJedis);
            log.error(e.getMessage(), e);
        } finally {
            returnResource(shardedJedis);
        }
        return false;
    }

    /**
     * 获取key在 shard分片中的index
     *
     * @param uri
     * @param key
     * @return
     */
    public static int getShardIndex(String uri, String key) {
        List<JedisShardInfo> shards = new ArrayList<JedisShardInfo>();
        for (String ips : uri.split(",")) {
            JedisShardInfo info = new JedisShardInfo(ips.split(":")[0],
                    Integer.parseInt(ips.split(":")[1]));
            shards.add(info);
        }
        TreeMap<Long, JedisShardInfo> nodes = new TreeMap<Long, JedisShardInfo>();
        for (int i = 0; i != shards.size(); ++i) {
            final JedisShardInfo shardInfo = shards.get(i);
            if (shardInfo.getName() == null)
                for (int n = 0; n < 160 * shardInfo.getWeight(); n++) {
                    nodes.put(
                            Hashing.MURMUR_HASH.hash("SHARD-" + i + "-NODE-"
                                    + n), shardInfo);
                }
            else
                for (int n = 0; n < 160 * shardInfo.getWeight(); n++) {
                    nodes.put(
                            Hashing.MURMUR_HASH.hash(shardInfo.getName() + "*"
                                    + shardInfo.getWeight() + n), shardInfo);
                }
        }
        SortedMap<Long, JedisShardInfo> tail = nodes
                .tailMap(Hashing.MURMUR_HASH.hash(SafeEncoder.encode(key)));
        JedisShardInfo currJedisShardInfo = null;
        if (tail.size() == 0) {
            currJedisShardInfo = nodes.get(nodes.firstKey());
        } else {
            currJedisShardInfo = tail.get(tail.firstKey());
        }
        System.out.println(key + " saved " + currJedisShardInfo);
        for (int i = 0; i < shards.size(); i++) {
            if (shards.get(i).equals(currJedisShardInfo)) {
                return i;
            }
        }
        return -1;
    }



}
