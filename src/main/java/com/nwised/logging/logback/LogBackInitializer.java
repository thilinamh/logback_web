package com.nwised.logging.logback;

import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.core.joran.spi.JoranException;
import ch.qos.logback.core.util.StatusPrinter;
import jdk.nashorn.internal.runtime.linker.Bootstrap;
import org.slf4j.LoggerFactory;

import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.servlet.*;
import javax.servlet.annotation.WebListener;
import java.io.File;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by thilina_h on 2/20/2018.
 */
@WebListener
public class LogBackInitializer implements ServletContextListener {


    @Inject
    LogBackConfigProvider logConfigProvider;

    public void init(ServletContext serv_context) {

        String loggingFolder = logConfigProvider.getLogRoot();
        Logger.getLogger(LogBackInitializer.class.getCanonicalName()).log(Level.INFO, "###### Setting Log Home:{0}######", loggingFolder);
        System.setProperty("log.home", loggingFolder);
        System.setProperty("logback.ContextSelector", "JNDI");
        // assume SLF4J is bound to logback in the current environment
        ch.qos.logback.classic.LoggerContext context = (ch.qos.logback.classic.LoggerContext) LoggerFactory.getILoggerFactory();

        JoranConfigurator configurator = new JoranConfigurator();
        configurator.setContext(context);
        // Call context.reset() to clear any previous configuration, e.g. default
        // configuration. For multi-step configuration, omit calling context.reset().
        context.reset();
        try {
            configurator.doConfigure(serv_context.getResourceAsStream("WEB-INF/classes/" + logConfigProvider.getLogbackConfigFileName()));
        } catch (JoranException e) {
            e.printStackTrace();
        }
        StatusPrinter.print(context);
    }


//    public static void setLogHome(String root_dir, String appname) {
//        String loggingFolder = getLogHome(root_dir, appname);
//        Logger.getLogger(LogBackInitializer.class.getCanonicalName()).log(Level.INFO, "###### Setting Log Home:{0}######", loggingFolder);
//        System.setProperty("log.home", loggingFolder);
//    }
//
//    public static String getLogHome(String root_dir, String appname) {
//        String currentUsersHomeDir = System.getProperty("user.home");
//        return currentUsersHomeDir + File.separator + root_dir + File.separator + appname;
//    }


    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        init(servletContextEvent.getServletContext());
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }
}
