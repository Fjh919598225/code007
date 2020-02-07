package com.java1234.init;

import com.java1234.entity.ArcType;
import com.java1234.entity.Link;
import com.java1234.service.ArcTypeService;
import com.java1234.service.LinkService;
import com.java1234.util.RedisUtil;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Date 2020/1/22 23:28
 * @Author JianHui
 */
@Component("initSystem")
public class InitSystem implements ServletContextListener , ApplicationContextAware {

    private static ApplicationContext applicationContext;

    public static Map<Integer,ArcType> arcTypeMap  =new HashMap<>();

    @Autowired
    private RedisUtil<Integer> redisUtil;

    /**
     * 加载数据到application缓存中
     * @param application
     */
    public void loadData(ServletContext application){
        ArcTypeService arcTypeService = (ArcTypeService) applicationContext.getBean("arcTypeService");
        LinkService linkService = (LinkService) applicationContext.getBean("linkService");
        List<ArcType> allArcTypeList = arcTypeService.listAll(Sort.Direction.ASC, "sort");
        List<Link> linkList = linkService.listAll(Sort.Direction.ASC, "sort");
        for (ArcType arcType:allArcTypeList) {
            arcTypeMap.put(arcType.getId(),arcType);
        }
        application.setAttribute("allArcTypeList",allArcTypeList);
        application.setAttribute("linkList",linkList);

        if(redisUtil.get("signTotal")!=null){
            Integer signTotal=(Integer) redisUtil.get("signTotal");
            application.setAttribute("signTotal", signTotal);
        }else{
            redisUtil.set("signTotal",0);
            application.setAttribute("signTotal", 0);
        }

    }

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        System.out.println("先执行context");
        this.loadData(sce.getServletContext());

    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        System.out.println("先执行set");
        this.applicationContext=applicationContext;
    }

}
