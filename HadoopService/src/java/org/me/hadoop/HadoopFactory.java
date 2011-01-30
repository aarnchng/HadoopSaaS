/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.me.hadoop;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.me.hadoop.basic.BasicJobTracker;
import org.me.hadoop.basic.BasicNameNode;
import org.me.hadoop.service.ServiceProperties;
import org.me.hadoop.shell.JobTrackerShell;
import org.me.hadoop.shell.NameNodeShell;

/**
 *
 * @author Aaron
 */
public class HadoopFactory {

    public static final String HADOOP_SHELL = "shell";
    public static final String HADOOP_BASIC = "basic";
    private static Logger logger = Logger.getLogger(HadoopFactory.class.getName());

    public static NameNode createNameNode() {
        try {
            ServiceProperties service = ServiceProperties.getInstance();
            if (service.getHadoopType().equals(HADOOP_SHELL)) {
                return new NameNodeShell(service.getHdfsPath(),
                        service.getScriptPath(), service.getScriptType(),
                        service.getHadoopPath());
            } else if (service.getHadoopType().equals(HADOOP_BASIC)) {
                return new BasicNameNode(service.getHdfsPath());
            }
        } catch (IOException ex) {
            logger.log(Level.WARNING, null, ex);
        }
        return null;
    }

    public static JobTracker createJobTracker(NameNode namenode) {
        if (namenode != null) {
            try {
                ServiceProperties service = ServiceProperties.getInstance();
                if (service.getHadoopType().equals(HADOOP_SHELL) &&
                        (namenode instanceof NameNodeShell)) {
                    return new JobTrackerShell((NameNodeShell) namenode,
                            service.getScriptType(), service.getHadoopPath());
                } else if (service.getHadoopType().equals(HADOOP_BASIC) &&
                        (namenode instanceof BasicNameNode)) {
                    return new BasicJobTracker((BasicNameNode) namenode,
                            service.getHadoopPath());
                }
            } catch (IOException ex) {
                logger.log(Level.WARNING, null, ex);
            }
        }
        return null;
    }
}
