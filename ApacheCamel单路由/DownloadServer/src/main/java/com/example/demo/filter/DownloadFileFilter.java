package com.example.demo.filter;

import com.example.demo.config.FileMonitorConfig;
import com.example.demo.model.FileMonitorOption;
import com.example.demo.utils.MyDateUtils;
import com.jcraft.jsch.HASH;
import org.apache.camel.component.file.GenericFile;
import org.apache.camel.component.file.GenericFileFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by pengwan on 2017/7/12.
 */
@Component
public class DownloadFileFilter<T> implements GenericFileFilter<T> {
    @Autowired
    private FileMonitorConfig fileMonitorConfig;
    private Map<String, String[]> filterNames = new HashMap<>();

    public boolean accept(GenericFile<T> file) {
        //System.out.println("DownloadFileFilter is checking...");
        //
        String folderPath = this.fileMonitorConfig.getLocalDir();
        File targetFile = new File(folderPath+"/"+file.getAbsoluteFilePath());
        System.out.println(targetFile);
        if(targetFile.exists() && targetFile.length()==file.getFileLength()){
            //System.out.println("file already exists");
            return false;
        }
        File targetFolder = new File(folderPath+"/"+file.getEndpointPath());
        boolean a = targetFolder.exists();
        System.out.println(a);
        if(!targetFolder.exists()){
            if (targetFolder.mkdirs()){
                System.out.println("创建成功");
            } else {
                System.out.println("创建失败");
            }

        }
        //System.out.println("new file found: "+file.getAbsoluteFilePath());
        String[] nameFilterList = this.getFilterNames(file.getEndpointPath());
        if( nameFilterList==null || nameFilterList.length==0) {
            System.out.println("没有文件");
            return false;
        }
        for(int i=0; i<nameFilterList.length; i++){
            if(file.getFileName().contains(nameFilterList[i].trim())){
                System.out.println("matched file found: "+file.getAbsoluteFilePath());
                return true;
            }
        }
        return false;
        //return file.getFileName().contains(fileName);
    }
    private String[] getFilterNames(String endpointPath){
        if(this.filterNames.isEmpty()){
            FileMonitorOption[] monitorList = this.fileMonitorConfig.getMonitorList();
            for(int i=0; i<monitorList.length; i++){
                this.filterNames.put("CMSFTPServer/"+monitorList[i].getFieldId()+"/"+ MyDateUtils.generateDayStr(-1*monitorList[i].getDaysBefore())+"/"+monitorList[i].getDeviceId(),monitorList[i].getKeyWordsList());
            }
        }
        return this.filterNames.get(endpointPath);
    }

}
