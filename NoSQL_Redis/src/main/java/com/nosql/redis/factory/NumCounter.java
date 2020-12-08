package com.nosql.redis.factory;

import com.nosql.redis.counter.Counter;
import redis.clients.jedis.Jedis;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @ClassName:    NumCounter
 * @Package:      com.nosql.redis.factory
 * @File:         NumCounter.java
 * @Description:  Declare a number counter.
 * @Author:       Shadow Zhu
 */
public class NumCounter implements DataTypeCounter {

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
        String changedValue = counter.getValueFields();
        int expireTime = counter.getExpireTime();

        AtomicReference<String> info = new AtomicReference<>("Error: Operation failed. Type: Number.");
        if(key != null) {
            //If key is string and there are string stored in redis
            if(jedis.exists(key) && jedis.type(key).equals("string")) {
                if(changedValue != null) {
                    long change = Long.parseLong(changedValue);
                    jedis.incrBy(key, change);
                    if(expireTime != 0) {
                        jedis.expire(key, expireTime);
                        info.set("Key: " + key + " - Changed: " + change + "to" + jedis.get(key) +
                                " - Expire Time: " + expireTime + "sec.");
                    } else {
                        //ExpireTime is 0; which means there are no expire time.
                        info.set("Key: " + key + " - Changed: " + change + "to" + jedis.get(key) +
                                " - No expire time.");
                    }
                } else {    // valueFields is empty
                    if(expireTime != 0) {
                        jedis.expire(key, expireTime);
                        info.set("Key value: " + jedis.get(key) + " - Expire time:" + expireTime + " sec.");
                    } else {
                        info.set("Key value: " + jedis.get(key) + " - Expire time: " + jedis.ttl(key) + " sec.");
                    }
                }
            } else {    //When key hasn't been found.
                if(changedValue != null) {
                    if(expireTime != 0) {
                        jedis.setex(key, expireTime, changedValue);
                        info.set("Add key: " + key + " - Value: " + changedValue + " - Expire time:" + expireTime + " sec.");
                    } else {
                        jedis.set(key, changedValue);
                        info.set("Add key: " + key + " - Value: " + changedValue + ".");
                    }
                } else {
                    info.set("Sorry, current Key cannot be found in storage.");
                }
            }
        }
        return info.get();
    }

}
