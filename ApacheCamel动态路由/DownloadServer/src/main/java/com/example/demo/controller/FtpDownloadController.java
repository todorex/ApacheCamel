package com.example.demo.controller;

import com.example.demo.config.FileMonitorConfig;
import com.example.demo.config.FtpClientConfig;
import com.example.demo.config.RouteManager;
import com.example.demo.filter.FileFilter;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

/**
 * Created by pengwan on 2017/7/13.
 */
@RestController
public class FtpDownloadController {
    private static Logger logger = Logger.getLogger(FtpDownloadController.class);
    @Autowired
    private RouteManager routeManager;
    @Autowired
    private FileMonitorConfig fileMonitorConfig;
    @Autowired
    private FtpClientConfig ftpClientConfig;
    @Autowired
    FileFilter fileFilter;
    private String fileFilterBeanName = "fileFilter";
    @RequestMapping(value = "ftp/{fieldId}/{dateStr}/{deviceId}", method = RequestMethod.POST)
    public void ftpDownload(@PathVariable String fieldId, @PathVariable String dateStr, @PathVariable String deviceId,
                            @RequestParam(value = "fileName") String fileName) throws Exception {
        //logger.info(fieldId+"/"+dateStr+"/"+deviceId+"/"+fileName);
        String downloadLocation = this.fileMonitorConfig.getLocalDir();
        int checkInterval = this.fileMonitorConfig.getCheckInterval();
        String ftpHost = this.ftpClientConfig.getHost();
        String ftpPort = this.ftpClientConfig.getPort();
        String ftpUserName = this.ftpClientConfig.getUsername();
        String ftpPassword = this.ftpClientConfig.getPassword();

        String fromUri = this.generateFromUri(this.fileMonitorConfig.getRemoteDir(),fieldId, dateStr, deviceId,checkInterval,ftpHost, ftpPort, ftpUserName, ftpPassword, this.ftpClientConfig.getEncoding(), this.fileFilterBeanName);
        String toUri = this.generateToUri(downloadLocation,fieldId,dateStr,deviceId);
        logger.info("from ["+fromUri+"] to ["+toUri+"]");
        this.routeManager.insertUpdateRoute(fromUri, toUri, fileName);
/*        BaseRoute newRoute = new BaseRoute(fromUrl, toUrl);
        Map<String, RestConfigurationDefinition> restConfigurations = newRoute.getRestConfigurations();
        System.out.println(restConfigurations);
        this.camelContext.addRoutes(newRoute);
        this.camelContext.setTracing(true);
        this.camelContext.start();
        List<Route> routes = this.camelContext.getRoutes();
        if(routes.size()>0){
            String id = routes.get(routes.size() - 1).getId();
            //this.camelContext.stopRoute(id, 10, TimeUnit.MINUTES); // 10 minuts to stop
            //this.camelContext.removeRoute(id);
            ServiceStatus routeStatus = this.camelContext.getRouteStatus(id);
            System.out.println(routeStatus);
            System.out.println(routes.get(0).getUptimeMillis());
            Map<String, Object> estConfigurations1 = this.camelContext.getRoute(id).getProperties();
            System.out.println(estConfigurations1);
            System.out.println(this.camelContext.getRoute(id).getEndpoint());
        }*/

    }
    public static String generateFromUri(String baseFolderPath, String fieldId, String dateStr, String deviceId, int checkInterval, String ftpHost, String ftpPort, String ftpUserName, String ftpPassword, String encoding, String fileFilterBeanName){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("ftp://").append(ftpHost)
                .append(":").append(ftpPort);
        if(StringUtils.isEmpty(baseFolderPath) || !baseFolderPath.startsWith("/")){
            stringBuilder.append("/");
        }else{
            stringBuilder.append(baseFolderPath);
            if(!baseFolderPath.endsWith("/")){
                stringBuilder.append("/");
            }
        }
        stringBuilder.append(fieldId).append("/").append(dateStr).append("/").append(deviceId)
                .append("?username=").append(ftpUserName)
                .append("&password=").append(ftpPassword)
                .append("&delay=").append(checkInterval).append("s")
                //.append("&ftpClient.controlEncoding=UTF8")
                //.append("&ftpClient.controlEncoding=gb2312")
                .append("&ftpClient.controlEncoding=").append(encoding)
                .append("&filter=#").append(fileFilterBeanName).append("&passiveMode=true")
                .append("&binary=true");
        return stringBuilder.toString();
    }
    private String generateToUri(String baseFolderPath, String fieldId, String dateStr, String deviceId){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("file:").append(baseFolderPath);
        if(!baseFolderPath.endsWith("/")){
            stringBuilder.append("/");
        }
        stringBuilder.append(fieldId).append("/").append(dateStr).append("/").append(deviceId);
        return stringBuilder.toString();
    }
}
