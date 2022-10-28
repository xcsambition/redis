package com.zfx.data;

import redis.clients.jedis.Jedis;

public class test {
    public static void main(String[] args) {
        test("127.0.0.1", 7081, null);
//        test("47.98.43.238",6379,"123456");

    }

    private static void test(String host, int port, String pwd) {
        Jedis jedis1 = new Jedis(host, port);
        if (pwd != null) {
            jedis1.auth(pwd);
        }
        jedis1.select(0);
        jedis1.set("ic", "0");




        for (int i = 0; i < 100; i++) {
            int finalI = i;
            new Thread(() -> {
                Jedis jedis = new Jedis(host, port);
                if (pwd != null) {
                    jedis.auth("123456");
                }
                jedis.select(0);
                jedis.incrBy("ic", finalI);
                jedis.disconnect();
            }).start();
        }
        int sum = 0;
        for (int i = 0; i < 100; i++) {
            sum += i;
        }
        System.out.println(sum);
    }

}
