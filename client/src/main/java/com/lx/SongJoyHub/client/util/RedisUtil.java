package com.lx.SongJoyHub.client.util;

import cn.hutool.core.bean.BeanUtil;
import com.lx.SongJoyHub.client.common.constant.RedisConstant;
import com.lx.SongJoyHub.client.dao.entity.RoomDO;
import com.lx.SongJoyHub.client.dto.req.RoomQueryReviewReqDTO;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Redis工具类
 */
public final class RedisUtil {

    /**
     * 使用lua脚本将对象转为Redis Hash类型 并设置有效期
     */
    public static void convertHash(String key, StringRedisTemplate stringRedisTemplate, Map<String, Object> cacheTargetMap,String timeOut) {
        Map<String, String> actualCacheTargetMap = cacheTargetMap.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey,
                entry -> entry.getValue() != null ? entry.getValue().toString() : ""));
        String luaScript = "redis.call('HMSET',KEYS[1],unpack(ARGV, 1, #ARGV - 1))" +
                "redis.call('EXPIREAT',KEYS[1],ARGV[#ARGV])";
        List<String> keys = Collections.singletonList(key);
        List<String> args = new ArrayList<>(actualCacheTargetMap.size() * 2 + 1);
        actualCacheTargetMap.forEach((k, v) -> {
            args.add(k);
            args.add(v);
        });
        args.add(String.valueOf(timeOut));
        //执行lua脚本
        stringRedisTemplate.execute(new DefaultRedisScript<>(luaScript, Long.class), keys, args.toArray());
    }
}
