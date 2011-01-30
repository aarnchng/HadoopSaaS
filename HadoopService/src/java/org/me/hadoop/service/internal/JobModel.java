/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.me.hadoop.service.internal;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import org.me.hadoop.utils.CommonsIO;
import org.me.hadoop.utils.MiscUtils;

/**
 *
 * @author Aaron
 */
public class JobModel {

    public static final String[] METHODS = {"hadoop.jar", "hadoop.streaming"};
    public static final int METHOD_JAR = 0;
    public static final int METHOD_STREAMING = 1;
    private String path;
    private String key;
    private Properties properties;

    protected JobModel(String path) throws FileNotFoundException, IOException {
        this.path = path = MiscUtils.appendFileSeparator(path);
        key = "";
        FileInputStream fis = new FileInputStream(path + "model.properties");
        properties = new Properties();
        properties.load(fis);
        fis.close();
    }

    public static JobModel getInstance(String path)
            throws FileNotFoundException, IOException {
        JobModel model = new JobModel(path);
        if (model.validate()) {
            if (model.getMethod().equals(METHODS[METHOD_JAR])) {
                return new JarModel(path);
            } else if (model.getMethod().equals(METHODS[METHOD_STREAMING])) {
                return new StreamingModel(path);
            }
        }
        return null;
    }

    public boolean validate() {
        boolean result = (getName() != null);
        if (result) {
            result = false;
            for (String method : METHODS) {
                if (getMethod().equals(method)) {
                    result = true;
                    break;
                }
            }
        }
        return result;
    }

    public boolean remove() {
        return CommonsIO.deleteDirectory(path);
    }

    public String getPath() {
        return path;
    }

    protected String getProperty(String name) {
        return properties.getProperty(name);
    }

    public String getProperty(String name, String value) {
        return properties.getProperty(name, value);
    }

    public String getKey() {
        if (key.trim().length() == 0) {
            return properties.getProperty("model.name");
        }
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return properties.getProperty("model.name");
    }

    @Deprecated
    public void setName(String name) {
        properties.setProperty("model.name", name);
    }

    public String getMethod() {
        return properties.getProperty("model.method", "none").toLowerCase();
    }

    public String getVersion() {
        return properties.getProperty("model.version", "");
    }

    public String getAuthor() {
        return properties.getProperty("model.author", "");
    }

    public String getDescription() {
        return properties.getProperty("model.description", "");
    }
}
