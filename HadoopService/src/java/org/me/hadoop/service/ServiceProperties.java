/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.me.hadoop.service;

import java.io.IOException;
import java.util.Properties;

/**
 *
 * @author Aaron
 */
public class ServiceProperties {

    private static ServiceProperties service = null;
    private Properties properties;

    private ServiceProperties() throws IOException {
        ClassLoader loader = ServiceProperties.class.getClassLoader();
        properties = new Properties();
        properties.load(loader.getResourceAsStream("hadoop-service.properties"));
    }

    public static ServiceProperties getInstance() throws IOException {
        if (service == null) {
            service = new ServiceProperties();
        }
        return service;
    }

    public String getProperty(String name) {
        return properties.getProperty(name);
    }

    public String getProperty(String name, String value) {
        return properties.getProperty(name, value);
    }

    public String getServiceVersion() {
        return properties.getProperty("service.version");
    }

    public boolean isWin32() {
        return properties.getProperty("win32").equalsIgnoreCase("yes");
    }

    public boolean hasCygwin() {
        return properties.getProperty("cygwin").equalsIgnoreCase("yes");
    }

    public String getModelPath() {
        if (isWin32()) {
            return properties.getProperty("win32.model.path");
        }
        return properties.getProperty("model.path");
    }

    public String getDataPath() {
        if (isWin32()) {
            return properties.getProperty("win32.data.path");
        }
        return properties.getProperty("data.path");
    }

    public String getTmpPath() {
        if (isWin32()) {
            return properties.getProperty("win32.tmp.path");
        }
        return properties.getProperty("tmp.path");
    }

    public String getScriptPath() {
        if (isWin32()) {
            return properties.getProperty("win32.script.path");
        }
        return properties.getProperty("script.path");
    }

    public String getHadoopPath() {
        if (isWin32()) {
            if (hasCygwin()) {
                return properties.getProperty("cygwin.hadoop.path");
            } else {
                return properties.getProperty("win32.hadoop.path");
            }
        }
        return properties.getProperty("hadoop.path");
    }

    public String getHdfsPath() {
        return properties.getProperty("hdfs.path");
    }

    public String getHadoopType() {
        return properties.getProperty("hadoop.type").toLowerCase();
    }

    public String getScriptType() {
        return properties.getProperty("script.type").toLowerCase();
    }
}
