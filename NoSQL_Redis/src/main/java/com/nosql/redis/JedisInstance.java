package com.nosql.redis;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * @ClassName:    JedisInstance
 * @Package:      com.nosql.redis
 * @File:         JedisInstance.java
 * @Description:  Declare a Jedis Instance
 * @Author:       Shadow Zhu
 */
public class JedisInstance {

    //Use private constructor to creat a instance.
    private JedisInstance(){ }

    //Use enum class to declare a singleton.
    static enum SingleJedisPool {

        INSTANCE;
        private JedisPool jPool;

        private SingleJedisPool(){
            JedisPoolConfig config = new JedisPoolConfig();
            config.setMaxTotal(30);
            config.setMaxIdle(10);

            this.jPool = new JedisPool(config, "127.0.0.1", 6379);
        }

        public JedisPool getJedisPool(){
            return this.jPool;
        }
    }
 
    //A method to get the instance
    public static JedisPool getJedisPoolInstance(){
        return SingleJedisPool.INSTANCE.getJedisPool();
    }
}

