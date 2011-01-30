/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.me.hadoop.wsclient.internal;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.Set;
import org.me.hadoop.utils.MiscUtils;

/**
 *
 * @author Aaron
 */
public class PropertiesWriter {

    private static SimpleDateFormat sdf = new SimpleDateFormat("EEE, d MMM " +
            "yyyy HH:mm:ss Z");
    private String path;
    private String filename;
    private Properties properties;

    public PropertiesWriter(String path, String filename) {
        this.path = MiscUtils.appendFileSeparator(path);
        this.filename = filename;
        properties = new Properties();
    }

    public Set<String> getProperties() {
        return properties.stringPropertyNames();
    }

    public String getProperty(String name, String value) {
        return properties.getProperty(name, value);
    }

    public void setProperty(String name, String value) {
        properties.setProperty(name, value);
    }

    public void save() throws FileNotFoundException, IOException {
        FileOutputStream fos = new FileOutputStream(path + filename);
        properties.store(fos, "Modified On " + sdf.format(new Date()));
        fos.close();
    }
}
