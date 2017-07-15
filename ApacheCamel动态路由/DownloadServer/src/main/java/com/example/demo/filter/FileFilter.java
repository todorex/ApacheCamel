package com.example.demo.filter;

import com.example.demo.config.FileMonitorConfig;
import com.example.demo.config.RouteManager;
import org.apache.camel.component.file.GenericFile;
import org.apache.camel.component.file.GenericFileFilter;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Created by pengwan on 2017/7/13.
 */
@Component
public class FileFilter<T> implements GenericFileFilter<T> {
    private static Logger logger = Logger.getLogger(FileFilter.class);
    @Autowired
    private RouteManager routeManager;
    @Autowired
    private FileMonitorConfig fileMonitorConfig;
    @Override
    public boolean accept(GenericFile<T> genericFile) {
        //long time1 = System.currentTimeMillis();
        //long time2;
        logger.info("===================== Checking: "+genericFile.getFileName()+" ======================");
        String folderPath = this.fileMonitorConfig.getLocalDir();
        File targetFile = new File(folderPath+"/"+genericFile.getAbsoluteFilePath());

        if(targetFile.exists() && targetFile.length()==genericFile.getFileLength()){
            logger.info("===================== Already Exits ======================");
            return false;
        }
        Map<String, Set<String>> fileNameFilters = this.routeManager.getFileNameFilters();
        logger.info("EndpointPath: "+genericFile.getEndpointPath());
        for (Map.Entry<String, Set<String>> entry : fileNameFilters.entrySet()) {
            if(entry.getKey().endsWith(genericFile.getEndpointPath())){
                Set<String> filteredNames = entry.getValue();
                if(filteredNames!=null && fileNameFilters.size()>0) {
                    logger.info("FileName filter: " + filteredNames.toString());
                    Iterator iterator = filteredNames.iterator();
                    while (iterator.hasNext()) {
                        if (genericFile.getFileName().contains(iterator.next().toString())) {
                            logger.info("Get it! "+genericFile.getAbsoluteFilePath());
                            logger.info("===================== Found ======================");
                            //logger.info("过滤时间："+(System.currentTimeMillis()-time1));
                            return true;

                        }
                    }
                    logger.info("===================== Found None ======================");
                    return false;
                }
                logger.info("===================== Found None ======================");
                return false;
            }
        }
        logger.info("===================== Found None ======================");
        return false;
    }
}
