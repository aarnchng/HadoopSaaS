/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.me.hadoop.service.internal;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import org.me.hadoop.utils.CommonsIO;
import org.me.hadoop.utils.MiscUtils;

/**
 *
 * @author Aaron
 */
public class RunProperties {

    private static final SimpleDateFormat sdf =
            new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z");
    private String path;
    private Properties properties;

    public RunProperties(String path) throws FileNotFoundException,
            IOException {
        this.path = path = MiscUtils.appendFileSeparator(path);
        CommonsIO.createFile(path + "run.properties");
        synchronized (RunProperties.class) {
            FileInputStream fis = new FileInputStream(path + "run.properties");
            properties = new Properties();
            properties.load(fis);
            fis.close();
        }
    }

    public synchronized void save() throws FileNotFoundException, IOException {
        FileOutputStream fos = new FileOutputStream(path + "run.properties");
        properties.store(fos, "Modified On " + sdf.format(new Date()));
        fos.close();
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

    public String getModelName() {
        return properties.getProperty("run.model.name", "");
    }

    public void setModelName(String modelname) {
        if ((modelname != null) && (modelname.trim().length() != 0)) {
            properties.setProperty("run.model.name", modelname);
        }
    }

    public String getModelMethod() {
        return properties.getProperty("run.model.method", "");
    }

    public void setModelMethod(String modelmethod) {
        if ((modelmethod != null) && (modelmethod.trim().length() != 0)) {
            properties.setProperty("run.model.method", modelmethod);
        }
    }

    public String getModelVersion() {
        return properties.getProperty("run.model.version", "");
    }

    public void setModelVersion(String modelversion) {
        if ((modelversion != null) && (modelversion.trim().length() != 0)) {
            properties.setProperty("run.model.version", modelversion);
        }
    }

    public String getModelAuthor() {
        return properties.getProperty("run.model.author", "");
    }

    public void setModelAuthor(String modelauthor) {
        if ((modelauthor != null) && (modelauthor.trim().length() != 0)) {
            properties.setProperty("run.model.author", modelauthor);
        }
    }

    public String getModelDescription() {
        return properties.getProperty("run.model.description", "");
    }

    public void setModelDescription(String modeldesc) {
        if ((modeldesc != null) && (modeldesc.trim().length() != 0)) {
            properties.setProperty("run.model.description", modeldesc);
        }
    }

    public String getDescription() {
        return properties.getProperty("run.description", "");
    }

    public void setDescription(String description) {
        if ((description != null) && (description.trim().length() != 0)) {
            properties.setProperty("run.description", description);
        }
    }

    public String getArguments() {
        return properties.getProperty("run.arguments", "");
    }

    public void setArguments(String arguments) {
        if ((arguments != null) && (arguments.trim().length() != 0)) {
            properties.setProperty("run.arguments", arguments);
        }
    }

    public String getStatus() {
        return properties.getProperty("run.status", "unknown").toLowerCase();
    }

    public void setStatus(String status) {
        if ((status != null) && (status.trim().length() != 0)) {
            properties.setProperty("run.status", status);
        }
    }

    public void createStartDate() {
        properties.setProperty("run.startdate", sdf.format(new Date()));
    }

    public String getStartDate() {
        return properties.getProperty("run.startdate", "");
    }

    public void createEndDate() {
        properties.setProperty("run.enddate", sdf.format(new Date()));
    }

    public String getEndDate() {
        return properties.getProperty("run.enddate", "");
    }

    public String getJobCount() {
        return properties.getProperty("run.job.count", "0");
    }

    public String getJobName() {
        return properties.getProperty("run.job.name", "");
    }

    public void setJobName(String jobname) {
        if ((jobname != null) && (jobname.trim().length() != 0)) {
            properties.setProperty("run.job.name", jobname);

            int jobcount = Integer.parseInt(getJobCount()) + 1;
            properties.setProperty("run.job.count", Integer.toString(jobcount));
        }
    }
}
