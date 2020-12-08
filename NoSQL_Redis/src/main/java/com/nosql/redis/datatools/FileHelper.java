package com.nosql.redis.datatools;

import com.nosql.redis.Main;
import org.apache.commons.io.monitor.FileAlterationListenerAdaptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

public class FileHelper extends FileAlterationListenerAdaptor {
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void onFileChange(File file) {
        Main.access.compareAndSet(false, true);
        Main.loadDataFromJson();
        Main.access.set(false);
    }
}
