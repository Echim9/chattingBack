package com.example.chattingback.utils;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.*;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Slf4j
@Component
public class RedisUtil {
    public static String isValidCodeSHA;
    private static JedisPool jedisPool;

    @Autowired
    public void init(JedisPool jedisPool) {
        RedisUtil.jedisPool = jedisPool;
        loadScript();
    }

    public static String isValidCodcoeSHA;
    public static final String isValidCode=
            "local code=redis.call('get',KEYS[1]) "+
                    "if code==false then "+
                    "return 0 "+
                    "end "+
                    "if code~=ARGV[1] then "+
                    "return 0 "+
                    "end "+
                    "redis.call('del',KEYS[1]) "+
                    "return 1 ";

    private static void loadScript() {
        try (Jedis jedis = jedisPool.getResource()) {
            isValidCodeSHA = jedis.scriptLoad(isValidCode);
        } catch (Exception e) {
            log.error("ERROR:",e);
        }
    }

    public static Object eval(String script, List<String> KEYS, List<String> ARGV) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.eval(script, KEYS, ARGV);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Object evalSHA(String scriptSHA, List<String> KEYS, List<String> ARGV) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.evalsha(scriptSHA, KEYS, ARGV);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    //插入
    public static String set(String key,String value){
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.set(key, value);
        } catch (Exception e) {
            log.error("ERROR:",e);
            return null;
        }
    }

    //插入带过期时间
    public static String set(String key, String val, long ex) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.setex(key, ex, val);
        } catch (Exception e) {
            return null;
        }
    }

    //获取
    public static String get(String key) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.get(key);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    //获取过期时间
    public static Long ttl(String key){
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.ttl(key);
        } catch (Exception e) {
            return null;
        }
    }

    //
    public static Long zadd(String key, Double score, String f) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.zadd(key, score, f);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Long zadd(String key, Map<String, Double> fv) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.zadd(key,fv);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Set<Tuple> zrevrangeWithScores(String key, Long start, Long stop){
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.zrevrangeWithScores(key,start,stop);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Double zscore(String key,String member){
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.zscore(key, member);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Long zrank(String key, String member) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.zrank(key, member);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Long zrem(String key,String... member){
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.zrem(key, member);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Boolean exists(String key) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.exists(key);
        } catch (Exception e) {
            return null;
        }
    }

    public static Long zcard(String key) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.zcard(key);
        } catch (Exception e) {
            return null;
        }
    }

    public static ScanResult<String> scan(String cursor, ScanParams scanParams){
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.scan(cursor,scanParams);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Long del(String key){
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.del(key);
        } catch (Exception e) {
            return null;
        }
    }

    public static <T> T isExitInCache(String id, T obj){
        boolean isExit = StringUtils.isNotEmpty(get(id));
        JSONObject jsonObject = JSONObject.parseObject(get(id));
        if (isExit) {
            return (T) JSONObject.toJavaObject(jsonObject, obj.getClass());
        }
        return null;
    }

}

