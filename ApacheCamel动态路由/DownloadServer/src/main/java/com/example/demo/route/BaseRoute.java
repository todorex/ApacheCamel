package com.example.demo.route;

import org.apache.camel.builder.RouteBuilder;

import java.util.logging.Logger;

/**
 * Created by pengwan on 2017/7/13.
 */
public class BaseRoute extends RouteBuilder {
    private String fromConfigStr;
    private String toConfigStr;
    public BaseRoute(String fromConfigStr, String toConfigStr){
        this.fromConfigStr = fromConfigStr;
        this.toConfigStr = toConfigStr;
    }
    @Override
    public void configure() throws Exception {
        long time1 = System.currentTimeMillis();
        System.out.println("开始下载");
        System.out.println(fromConfigStr);
        System.out.println(toConfigStr);
        from(this.fromConfigStr).to(this.toConfigStr).log("transfering...");
        System.out.println("下载时间："+(System.currentTimeMillis()-time1));
    }
}
