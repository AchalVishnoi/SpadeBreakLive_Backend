package com.org.SpadeBreak;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class SpadeBreakApplication {

	public static void main(String[] args) {

        ApplicationContext context = SpringApplication.run(SpadeBreakApplication.class, args);

//        RedisService redisService = context.getBean(RedisService.class);
//        redisService.saveValue("testKey", "Hello Redis!");
//      redisService.saveValue("myKey","Achal");
//        System.out.println(redisService.getValue("testKey"));



    }

}
