package com.nosql.redis.factory;

import com.nosql.redis.JedisInstance;
import com.nosql.redis.counter.Counter;
import redis.clients.jedis.Jedis;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName:    TypeFactory
 * @Package:      com.nosql.redis.factory
 * @File:         TypeFactory.java
 * @Description:  Declare a factory, use to construct different Counter
 *                for different data types with factory mode.
 * @Author:       Shadow Zhu
 */
public class TypeFactory {

    private Map<String, DataTypeCounter> Datatype_Counter = new HashMap<>();

    private Jedis jedis = JedisInstance.getJedisPoolInstance().getResource();

    /**
     * @Description: Constractor
     * @Param:
    */
    public TypeFactory() {
        Datatype_Counter.put("num", new NumCounter());
        Datatype_Counter.put("freq", new FreqCounter());
        Datatype_Counter.put("str", new StrCounter());
        Datatype_Counter.put("initiate", new SetCounter());
    }

    /**
     * @Description: Get DataTypeCounter that fits the given type.
     * @param:       [String type, Counter counter]
     * @return:      DataTypeCounter
     * @throws
     * @auther:      Shadow Zhu
     */
    public DataTypeCounter getCounter(String type, Counter counter) {
        Datatype_Counter.get(type).initiate(counter, jedis);
        return Datatype_Counter.get(type);
    }

}
