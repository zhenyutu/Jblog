package cn.tzy.Jblog.service;

import com.sun.javafx.binding.StringFormatter;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Transaction;

import java.io.IOException;
import java.util.List;
import java.util.Set;

/**
 * Created by tuzhenyu on 17-7-25.
 * @author tuzhenyu
 */
@Service
public class JedisService implements InitializingBean{

    private JedisPool pool;

    @Override
    public void afterPropertiesSet() throws Exception {
        pool = new JedisPool("redis://localhost:6379/1");
    }

    public Jedis getJedis(){
        return pool.getResource();
    }

    public Transaction multi(Jedis jedis){
        return jedis.multi();
    }

    public List<Object> exec(Transaction tx,Jedis jedis){
        try{
            return tx.exec();
        }finally {
            try {
                if (tx!=null)
                    tx.close();
            }catch (IOException e){
                e.printStackTrace();
            }
            if (tx!=null)
                jedis.close();
        }
    }

    public void put(String key, String value){
        Jedis jedis = pool.getResource();
        jedis.set(key,value);
        jedis.close();
    }

    public void incr(String key){
        Jedis jedis = pool.getResource();
        jedis.incr(key);
        jedis.close();
    }

    public String get(String key){
        Jedis jedis = pool.getResource();
        String result = jedis.get(key);
        jedis.close();
        return result;
    }

    public void sadd(String key,String value){
        Jedis jedis = pool.getResource();
        jedis.sadd(key,value);
        jedis.close();
    }

    public void srem(String key,String value){
        Jedis jedis = pool.getResource();
        jedis.srem(key,value);
        jedis.close();
    }

    public boolean sismember(String key,String value){
        Jedis jedis = pool.getResource();
        boolean result = jedis.sismember(key,value);
        jedis.close();

        return result;
    }

    public long scard(String key){
        Jedis jedis = pool.getResource();
        long result = jedis.scard(key);
        jedis.close();

        return result;
    }

    public void lpush(String key,String value){
        Jedis jedis = pool.getResource();
        jedis.lpush(key,value);
        jedis.close();
    }

    public List<String> brpop(int time, String key){
        Jedis jedis = pool.getResource();
        List<String> list = jedis.brpop(time,key);
        jedis.close();
        return list;
    }

    public long zadd(String key,double score,String value){
        Jedis jedis = pool.getResource();
        long result = jedis.zadd(key,score,value);
        jedis.close();

        return result;
    }

    public double zincrby(String key,String value){
        Jedis jedis = pool.getResource();
        double result = jedis.zincrby(key,1,value);
        jedis.close();

        return result;
    }

    public Set<String> zrevrange(String key,int start, int end){
        Jedis jedis = pool.getResource();
        Set<String> set = jedis.zrevrange(key,start,end);
        jedis.close();
        return set;
    }

    public long zcard(String key){
        Jedis jedis = pool.getResource();
        long result = jedis.zcard(key);
        jedis.close();

        return result;
    }

    public Double zscore(String key,String member){
        Jedis jedis = pool.getResource();
        Double result = jedis.zscore(key,member);
        jedis.close();

        return result;
    }
}
