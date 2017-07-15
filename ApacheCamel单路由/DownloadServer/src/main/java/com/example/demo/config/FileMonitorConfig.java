package com.example.demo.config;

import com.example.demo.model.FileMonitorOption;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Created by pengwan on 2017/6/29.
 */
@Configuration
@ConfigurationProperties(prefix = "fileMonitor")
public class FileMonitorConfig {

    private String remoteDir;
    private String localDir;
    private int checkInterval;
    private FileMonitorOption[] monitorList;

    public String getRemoteDir() {
        return remoteDir;
    }

    public void setRemoteDir(String remoteDir) {
        this.remoteDir = remoteDir;
    }

    public String getLocalDir() {
        return localDir;
    }

    public void setLocalDir(String localDir) {
        this.localDir = localDir;
    }

    public int getCheckInterval() {
        return checkInterval;
    }

    public void setCheckInterval(int checkInterval) {
        this.checkInterval = checkInterval;
    }

    public FileMonitorOption[] getMonitorList() {
        return monitorList;
    }

    public void setMonitorList(FileMonitorOption[] monitorList) {
        this.monitorList = monitorList;
    }
}
