package com.nwised.logging.logback;

import java.io.File;

/**
 * Created by thilina_h on 2/7/2018.
 */

public interface LogBackConfigProvider {

 String getAppName();

 String getLogbackConfigFileName();

 String getRootFolder();

    default String getLogRoot() {
        String currentUsersHomeDir = System.getProperty("user.home");
        return currentUsersHomeDir + File.separator + getRootFolder()+ File.separator +getAppName();
    }

}
