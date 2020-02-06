package com.java1234;

import com.java1234.entity.Article;
import com.java1234.util.RedisUtil;
import redis.clients.jedis.Jedis;

/**
 * @Date 2020/2/5 14:33
 * @Author JianHui
 */
public class JedisTest {

    public static void main(String[] args) {
        Jedis jedis=new Jedis("47.97.172.3",6379); // 创建客户端 设置IP和端口
        jedis.set("name1", "java知识分享网1"); // 设置值
        String value=jedis.get("name1"); // 获取值
        System.out.println(value);
        jedis.close(); // 释放连接资源
    }
}
