/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.me.hadoop.basic;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FsShell;
import org.apache.hadoop.util.ToolRunner;
import org.me.hadoop.NameNode;
import org.me.hadoop.utils.MiscUtils;

/**
 *
 * @author Aaron
 */
public class BasicNameNode extends NameNode {

    private static Logger logger = Logger.getLogger(BasicNameNode.class.getName());
    private Configuration conf;

    public BasicNameNode(String path) {
        super(path);
        ClassLoader loader = BasicNameNode.class.getClassLoader();
        conf = new Configuration();
        conf.addResource(loader.getResourceAsStream("/hadoop-default.xml"));
        conf.addResource(loader.getResourceAsStream("/hadoop-site.xml"));
    }

    protected Configuration getConf() {
        return conf;
    }

    @Override
    public boolean isAvailable() {
        String[] args = {"-ls", "/"};
        return runCommand(args);
    }

    @Override
    public boolean get(String name, String destination) {
        destination = MiscUtils.appendFileSeparator(destination);
        String[] args = new String[3];
        args[0] = "-get";
        args[1] = getPath() + name + MiscUtils.HDFS_SEPARATOR + "*";
        args[2] = destination;
        return runCommand(args);
    }

    @Override
    public boolean put(String name, String source) {
        source = MiscUtils.appendFileSeparator(source);
        String[] args = new String[3];
        args[0] = "-put";
        args[1] = source;
        args[2] = getPath() + name;
        return (rmdir(name) && runCommand(args));
    }

    @Override
    public boolean mkdir(String name) {
        String[] args = new String[2];
        args[0] = "-mkdir";
        args[1] = getPath() + name;
        return runCommand(args);
    }

    @Override
    public boolean rmdir(String name) {
        String[] args = new String[2];
        args[0] = "-rmr";
        args[1] = getPath() + name;
        return runCommand(args);
    }

    private boolean runCommand(String[] args) {
        boolean result = false;
        FsShell fs = new FsShell(conf);
        try {
            if (ToolRunner.run(fs, args) == 0) {
                result = true;
            }
        } catch (Exception ex) {
            logger.log(Level.WARNING, null, ex);
        }
        try {
            fs.close();
        } catch (IOException ex) {
            logger.log(Level.WARNING, null, ex);
        }
        return result;
    }
}
