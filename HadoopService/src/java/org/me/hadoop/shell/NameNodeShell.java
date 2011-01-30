/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.me.hadoop.shell;

import java.io.File;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.me.hadoop.NameNode;
import org.me.hadoop.utils.CommonsIO;
import org.me.hadoop.utils.MiscUtils;

/**
 *
 * @author Aaron
 */
public class NameNodeShell extends NameNode {

    private static Logger logger = Logger.getLogger(NameNodeShell.class.getName());
    private String scriptpath;
    private String scripttype;
    private String hadooppath;
    private Properties properties;
    private boolean ready;

    public NameNodeShell(String path, String scriptpath, String scripttype,
            String hadooppath) {
        super(path);
        this.scriptpath = MiscUtils.appendFileSeparator(scriptpath);
        CommonsIO.createDirectory(scriptpath);
        this.scripttype = scripttype;
        this.hadooppath = hadooppath;
        ClassLoader loader = NameNodeShell.class.getClassLoader();
        properties = new Properties();
        try {
            properties.load(loader.getResourceAsStream("script.properties"));
            ready = initialise();
        } catch (IOException ex) {
            logger.log(Level.WARNING, null, ex);
        }
    }

    private boolean initialise() {
        return (copyScript(properties.getProperty("script.ls" + scripttype)) &&
                copyScript(properties.getProperty("script.mkdir" + scripttype)) &&
                copyScript(properties.getProperty("script.rmdir" + scripttype)) &&
                copyScript(properties.getProperty("script.put" + scripttype)) &&
                copyScript(properties.getProperty("script.get" + scripttype)) &&
                copyScript(properties.getProperty("script.list" + scripttype)) &&
                copyScript(properties.getProperty("script.jar" + scripttype)) &&
                copyScript(properties.getProperty("script.streaming" + scripttype)));
    }

    private boolean copyScript(String scriptname) {
        File file = new File(scriptpath, (new File(scriptname)).getName());
        if (!file.exists()) {
            return CommonsIO.copyResource(file.getName(), file.getAbsolutePath());
        }
        return true;
    }

    protected Properties getProperties() {
        return properties;
    }

    protected boolean isReady() {
        return ready;
    }

    @Override
    public boolean isAvailable() {
        return (ready && ls());
    }

    private boolean ls() {
        String script = properties.getProperty("script.ls" + scripttype);
        String args = "'" + hadooppath + "'";
        try {
            String[] output = ShellUtils.runScript(script, args);
            if (output[ShellUtils.OUTPUT_STDERR].trim().length() == 0) {
                return true;
            }
        } catch (IOException ex) {
            logger.log(Level.WARNING, null, ex);
        } catch (InterruptedException ex) {
            logger.log(Level.WARNING, null, ex);
        }
        return false;
    }

    @Override
    public boolean mkdir(String name) {
        String script = properties.getProperty("script.mkdir" + scripttype);
        String args = "'" + hadooppath + "' '" + getPath() + name + "'";
        try {
            String[] output = ShellUtils.runScript(script, args);
            if (output[ShellUtils.OUTPUT_STDERR].trim().length() == 0) {
                return true;
            }
        } catch (IOException ex) {
            logger.log(Level.WARNING, null, ex);
        } catch (InterruptedException ex) {
            logger.log(Level.WARNING, null, ex);
        }
        return false;
    }

    @Override
    public boolean rmdir(String name) {
        String script = properties.getProperty("script.rmdir" + scripttype);
        String args = "'" + hadooppath + "' '" + getPath() + name + "'";
        try {
            String[] output = ShellUtils.runScript(script, args);
            if (output[ShellUtils.OUTPUT_STDERR].trim().length() == 0) {
                return true;
            }
        } catch (IOException ex) {
            logger.log(Level.WARNING, null, ex);
        } catch (InterruptedException ex) {
            logger.log(Level.WARNING, null, ex);
        }
        return false;
    }

    @Override
    public boolean put(String name, String source) {
        source = MiscUtils.appendFileSeparator(source);
        File dir = new File(source);
        if (dir.exists() && dir.isDirectory()) {
            String script = properties.getProperty("script.put" + scripttype);
            String args = "'" + hadooppath + "' '" + source + "*' '" +
                    getPath() + name + "'";
            try {
                String[] output = ShellUtils.runScript(script, args);
                if (output[ShellUtils.OUTPUT_STDERR].trim().length() == 0) {
                    return true;
                }
            } catch (IOException ex) {
                logger.log(Level.WARNING, null, ex);
            } catch (InterruptedException ex) {
                logger.log(Level.WARNING, null, ex);
            }
        }
        return false;
    }

    @Override
    public boolean get(String name, String destination) {
        destination = MiscUtils.appendFileSeparator(destination);
        File dir = new File(destination);
        if (dir.exists() && dir.isDirectory()) {
            String script = properties.getProperty("script.get" + scripttype);
            String args = "'" + hadooppath + "' '" + getPath() + name +
                    MiscUtils.HDFS_SEPARATOR + "*' '" + destination + "'";
            try {
                String[] output = ShellUtils.runScript(script, args);
                if (output[ShellUtils.OUTPUT_STDERR].trim().length() == 0) {
                    return true;
                }
            } catch (IOException ex) {
                logger.log(Level.WARNING, null, ex);
            } catch (InterruptedException ex) {
                logger.log(Level.WARNING, null, ex);
            }
        }
        return false;
    }
}
