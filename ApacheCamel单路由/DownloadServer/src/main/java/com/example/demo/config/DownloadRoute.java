package com.example.demo.config;

import com.example.demo.filter.DownloadFileFilter;
import com.example.demo.model.FileMonitorOption;
import com.example.demo.utils.MyDateUtils;
import org.apache.camel.builder.RouteBuilder;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


/**
 * Created by pengwan on 2017/7/11.
 */
//@Component
public class DownloadRoute extends RouteBuilder {

    private static Logger logger = Logger.getLogger(DownloadRoute.class);
    @Autowired
    private FileMonitorConfig fileMonitorConfig;
    @Autowired
    private DownloadFileFilter downloadFileFilter;
    @Autowired
    private FtpClientConfig ftpClientConfig;
//
//    public DownloadRoute(FileMonitorConfig fileMonitorConfig){
//        this.fileMonitorConfig = fileMonitorConfig;
//
//    }
//    public  DownloadRoute(){}
//



    @Override
    public void configure() throws Exception {
        String downloadLocation = this.fileMonitorConfig.getLocalDir(); //获得本地根目录
        int checkInterval = this.fileMonitorConfig.getCheckInterval();  //获得轮训时间
        FileMonitorOption[] monitorList = this.fileMonitorConfig.getMonitorList(); //匹配选项列表
        String ftpHost = this.ftpClientConfig.getHost(); //获得远程ip
        String ftpPort = this.ftpClientConfig.getPort();  //获得远程端口
        String ftpUserName = this.ftpClientConfig.getUsername(); //远程用户名
        String ftpPassword = this.ftpClientConfig.getPassword();   //远程密码
        for(int i=0; i<monitorList.length; i++){
            String filedId = monitorList[i].getFieldId();
            int daysBefore = monitorList[i].getDaysBefore();
            String deviceId = monitorList[i].getDeviceId();
            String fileFilterBeanName = "downloadFileFilter";//String.valueOf(DownloadFileFilter.class);//+Integer.toString(i);
            String fromUrl = this.generateFromUrl(this.fileMonitorConfig.getRemoteDir(),filedId,daysBefore,deviceId,checkInterval,ftpHost, ftpPort, ftpUserName, ftpPassword, fileFilterBeanName);
            String toUrl = this.generateToUrl(downloadLocation,filedId,daysBefore,deviceId);
            logger.info("from ["+fromUrl+"] to ["+toUrl+"]");
            from((fromUrl)).to(toUrl).log("Route"+Integer.toString(i)+" is transforming...");
        }

    }
/*    public static class SomeBean {
        public void someMethod(String body) {
            System.out.println("Received: " + body);
        }
    }*/
    private String generateFromUrl(String baseFolderPath, String fieldId, int daysBefore, String deviceId, int checkInterval, String ftpHost, String ftpPort, String ftpUserName, String ftpPassword, String fileFilterBeanName){
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
        String dateStr = MyDateUtils.generateDayStr(-1*daysBefore);
        stringBuilder.append(fieldId).append("/").append(dateStr).append("/").append(deviceId)
                .append("?username=").append(ftpUserName)
                .append("&password=").append(ftpPassword)
                .append("&delay=").append(checkInterval).append("s")
                .append("&filter=#").append(fileFilterBeanName).append("&ftpClient.controlEncoding=gb2312") ;
        return stringBuilder.toString();
    }
    private String generateToUrl(String baseFolderPath, String fieldId, int daysBefore, String deviceId){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("file:").append(baseFolderPath).append("/CMSFTPServer");
        if(!baseFolderPath.endsWith("/")){
            stringBuilder.append("/");
        }
        Calendar date = Calendar.getInstance();
        date.setTime(new Date());
        date.set(Calendar.DATE, date.get(Calendar.DATE) - daysBefore);
        String dateStr = (new SimpleDateFormat("yyyyMMdd")).format(date.getTime());
        stringBuilder.append(fieldId).append("/").append(dateStr).append("/").append(deviceId);
        return stringBuilder.toString();
    }

/*
    public static FTPClient getFTPClient(String ftpHost, String ftpUserName, String ftpPassword,
                                         int ftpPort) {
        FTPClient ftpClient = null;
        try {
            ftpClient = new FTPClient();
            ftpClient.connect(ftpHost, ftpPort);// 连接FTP服务器
            ftpClient.login(ftpUserName, ftpPassword);// 登陆FTP服务器
            logger.info(ftpClient.getReplyCode());
            if (!FTPReply.isPositiveCompletion(ftpClient.getReplyCode())) {
                logger.warn("未连接到FTP，用户名或密码错误。");
                ftpClient.disconnect();
            } else {
                logger.info("FTP连接成功。");
            }
        } catch (SocketException e) {
            e.printStackTrace();
            logger.warn("FTP的IP地址可能错误，请正确配置。");
        } catch (IOException e) {
            e.printStackTrace();
            logger.warn("FTP的端口错误,请正确配置。");
        }
        return ftpClient;
    }
    private List<String> list(String fieldId, String dateStr, String deviceId, String fileName) throws IOException {

        List<String> result = new ArrayList<>();
        String pattern = ".*"+fileName+".*";
        Pattern r = Pattern.compile(pattern);

        FTPClient ftpClient = DownloadRoute.getFTPClient(ftpHost, ftpUserName, ftpPassword,
                ftpPort);
        ftpClient.setControlEncoding("UTF-8"); // 中文支持
        ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
        ftpClient.enterLocalPassiveMode();

        String folderPath = fieldId+"/"+dateStr+"/"+deviceId;
        FTPFile[] ftpFiles = ftpClient.listFiles(folderPath);
        for(int i=0; i<ftpFiles.length; i++){
            String name = ftpFiles[i].getName();
            if(r.matcher(name).find()){
                System.out.println(name);
                //result.add(folderPath+"/"+name);
                result.add(folderPath);
            }
        }
        return result;
    }*/
}
