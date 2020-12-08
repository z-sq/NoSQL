package com.nosql.redis.factory;

import com.nosql.redis.counter.Counter;
import redis.clients.jedis.Jedis;

public class SetCounter implements DataTypeCounter {

    private Counter counter;
    private Jedis jedis;

    @Override
    public String executeCount() {
        return null;
    }

    @Override
    public void initiate(Counter counter, Jedis jedis) {
        this.counter = counter;
        this.jedis = jedis;
    }
}
