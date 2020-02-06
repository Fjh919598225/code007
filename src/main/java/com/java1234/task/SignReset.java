package com.java1234.task;

import com.java1234.service.UserService;
import com.java1234.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * 每天凌晨0点重置用户所有签到信息
 * @Date 2020/2/5 21:38
 * @Author JianHui
 */
@Component
public class SignReset implements ServletContextListener {

    @Autowired
   private UserService userService;

    @Autowired
    private RedisUtil<Integer> redisUtil;

    private static ServletContext application;

    /**
     *
     */
    @Scheduled(cron = "0 0 0 * * ?")
    private void process(){
        application.setAttribute("signTotal",0);
        redisUtil.set("signTotal",0);
        userService.updateAllSignInfo();
    }

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        application=sce.getServletContext();
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

    }
}
