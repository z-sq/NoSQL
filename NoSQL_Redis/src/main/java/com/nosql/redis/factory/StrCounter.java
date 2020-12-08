package com.nosql.redis.factory;

import com.nosql.redis.counter.Counter;
import redis.clients.jedis.Jedis;

import java.util.concurrent.atomic.AtomicReference;

/**
 * @ClassName:    NumCounter
 * @Package:      com.nosql.redis.factory
 * @File:         NumCounter.java
 * @Description:  Declare a string counter.
 * @Author:       Shadow Zhu
 */
public class StrCounter implements DataTypeCounter {

    private Counter counter;
    private Jedis jedis;

    @Override
    public void initiate(Counter counter, Jedis jedis) {
        this.counter = counter;
        this.jedis = jedis;
    }

    @Override
    public String executeCount() {
        //Assign key, change and time period
        String key = counter.getKeyFields();
        String change = counter.getValueFields();
        int expireTime = counter.getExpireTime();

        AtomicReference<String> info = new AtomicReference<>("Error: Operation failed. Type: String.");
        if(key != null) {
            //If key is string and there are string stored in redis
            if(jedis.exists(key) && jedis.type(key).equals("string")) {
                if(change != null) {
                    if(expireTime != 0) {
                        jedis.setex(key, expireTime, change);
                        info.set("Key: " + key + " - Changed: " + change + "to" + jedis.get(key) +
                                " - Expire Time: " + expireTime + "sec.");
                    } else {
                        jedis.set(key, change);
                        info.set("Key: " + key + " - Changed: " + change + "to" + jedis.get(key) +
                                " - No expire time.");
                    }
                } else {
                    if(expireTime != 0) {
                        jedis.expire(key, expireTime);
                        info.set("Key value: " + jedis.get(key) + " - Expire time:" + expireTime + " sec.");
                    } else {
                        info.set("Key value: " + jedis.get(key) + " - Expire time: " + jedis.ttl(key) + " sec.");
                    }
                }
            } else {
                if (change != null) {
                    if (expireTime != 0) {
                        jedis.setex(key, expireTime, change);
                        info.set("Add key: " + key + " - Value: " + change + " - Expire time:" + expireTime + " sec.");
                    } else {
                        jedis.set(key, change);
                        info.set("Add key: " + key + " - Value: " + change + ".");
                    }
                } else {
                    info.set("Sorry, current Key cannot be found in storage.");
                }
            }
        }
        return info.get();
    }

}
