package com.nosql.redis.factory;

import com.nosql.redis.counter.Counter;
import redis.clients.jedis.Jedis;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @ClassName:    NumCounter
 * @Package:      com.nosql.redis.factory
 * @File:         NumCounter.java
 * @Description:  Declare a list counter.
 * @Author:       Shadow Zhu
 */
public class ListCounter implements DataTypeCounter{

    private Counter counter;
    private Jedis jedis;


    @Override
    public void initiate(Counter counter, Jedis jedis) {
        this.counter = counter;
        this.jedis = jedis;
    }

    @Override
    public String executeCount() {
        //Assign key, changedValue and time period
        String key = counter.getKeyFields();
        String change = counter.getValueFields();
        int expireTime = counter.getExpireTime();

        AtomicReference<String> info = new AtomicReference<>("Error: Operation failed. Type: List.");
        if(key != null) {
            if(jedis.exists(key)) {
                if(jedis.type(key).equals("list")) {
                    if(change != null) {
                        jedis.lpush(key, change);
                        if(expireTime != 0) {
                            jedis.expire(key, expireTime);
                            info.set("key: " + key + " - Insert value to List: " + change +
                                    " - Expire time: " + jedis.ttl(key) + "sec.");
                        } else {
                            info.set("key: " + key + " - Insert value to List: " + change);
                        }
                    } else {
                        if(expireTime != 0) {
                            jedis.expire(key, expireTime);
                            List<String> list = jedis.lrange(key, 0, -1);
                            info.set("Key: " + key + " - Value of item in the key List: \n");
                            for(int i = 0; i < list.size(); i++) {
                                info.set(info + list.get(i) + "\n");
                            }
                        }
                    }
                }
            } else {
                if(change != null) {
                    jedis.lpush(key, change);
                    if(expireTime != 0) {
                        jedis.expire(key, expireTime);
                        info.set("key: " + key + " - Insert value to List: " + change +
                                " - Expire time: " + expireTime + "sec.");
                    } else {
                        info.set("key: " + key + " - Insert value to List: " + change);
                    }
                }
            }
        }
        return info.get();
    }

}
