package com.nosql.redis.factory;

import com.nosql.redis.counter.Counter;
import redis.clients.jedis.Jedis;

import java.text.ParseException;

public interface DataTypeCounter {

    /**
     * @Description: Initiate DataTypeData's variables.
     * @param:       [counter, jedis]
     * @return:      void
     * @throws
     * @auther:      Shadow Zhu
     */
    void initiate(Counter counter, Jedis jedis);

    /**
     * @Description: Execute Count operation while changing/inserting key.
     * @param:       []
     * @return:      java.lang.String
     * @throws       ParseException
     * @auther:      Shadow Zhu
     */
    String executeCount() throws ParseException;
}
