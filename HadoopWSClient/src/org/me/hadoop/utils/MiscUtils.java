/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.me.hadoop.utils;

import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.namespace.QName;
import org.me.hadoop.service.HadoopService_Service;
import org.me.hadoop.wsclient.internal.ClientProperties;

/**
 *
 * @author Aaron
 */
public class MiscUtils {

    public static final String HDFS_SEPARATOR = "/";
    public static final String FTP_SEPARATOR = "/";
    private static Logger logger = Logger.getLogger(MiscUtils.class.getName());

    public static String appendFileSeparator(String path) {
        if ((!path.endsWith(System.getProperty("file.separator"))) &&
                (path.trim().length() != 0)) {
            path += System.getProperty("file.separator");
        }
        return path;
    }

    public static String appendHdfsSeparator(String path) {
        if (!path.endsWith(HDFS_SEPARATOR)) {
            path += HDFS_SEPARATOR;
        }
        return path;
    }

    public static String appendFtpSeparator(String path) {
        if (!path.endsWith(FTP_SEPARATOR)) {
            path += FTP_SEPARATOR;
        }
        return path;
    }

    public static void waitFor(int duration) {
        try {
            Thread.sleep(duration);
        } catch (InterruptedException ex) {
            logger.log(Level.WARNING, null, ex);
        }
    }

    public static HadoopService_Service getHadoopService() throws Exception {
        ClientProperties client = ClientProperties.getInstance();
        QName name = new QName("http://service.hadoop.me.org/", "HadoopService");
        return new HadoopService_Service(new URL(client.getWSDLUrl()), name);
    }
}
