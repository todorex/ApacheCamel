package com.example.demo.config;

import com.example.demo.route.BaseRoute;
import org.apache.camel.CamelContext;
import org.apache.camel.Endpoint;
import org.apache.camel.Route;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.*;

/**
 * Created by pengwan on 2017/7/14.
 */
@Configuration
@EnableScheduling
public class RouteManager {
    private static Logger logger = Logger.getLogger(RouteManager.class);
    private Map<String, Set<String>> fileNameFilters = new HashMap<>();
    private static long RouteExpireTime = 30*60*1000; //ms
    @Autowired
    private CamelContext camelContext;

    public Map<String, Set<String>> getFileNameFilters() {
        return fileNameFilters;
    }

    public void setFileNameFilters(Map<String, Set<String>> fileNameFilters) {
        this.fileNameFilters = fileNameFilters;
    }

    public boolean insertUpdateRoute(String fromUri, String toUri, String keyword) throws Exception {
        Endpoint endpoint = this.camelContext.hasEndpoint(toUri);
        if(endpoint==null){
            //insert
            BaseRoute newRoute = new BaseRoute(fromUri, toUri);
            this.camelContext.addRoutes(newRoute);
            this.camelContext.setTracing(true);
            this.camelContext.start();
            Set<String> keywordSet = new HashSet<>();
            keywordSet.add(keyword);
            this.fileNameFilters.put(toUri, keywordSet);
        }else{
            //update
            this.fileNameFilters.get(toUri).add(keyword);
        }
        return true;
    }




    @Scheduled(cron = "0/20 * * * * ?") // 每20秒执行一次
    public void scheduler() throws Exception {
        logger.info(">>>>>>>>>>>>> scheduled ... ");
        List<Route> routes = this.camelContext.getRoutes();
        Iterator<Route> iterator = routes.iterator();
        while(iterator.hasNext()){
            Route route = iterator.next();
            if(route.getUptimeMillis()>RouteManager.RouteExpireTime){
                String id = route.getId();
                logger.info("Removing "+id+" ...");
                this.camelContext.stopRoute(id);
                this.camelContext.removeRoute(id);
            }
        }
    }
}
