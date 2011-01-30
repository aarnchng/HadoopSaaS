/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.me.hadoop.utils;

import com.enterprisedt.net.ftp.FTPException;
import com.enterprisedt.net.ftp.FileTransferClient;
import com.enterprisedt.net.ftp.WriteMode;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.me.hadoop.wsclient.internal.ClientProperties;

/**
 *
 * @author Aaron
 */
public class edtFTPj {

    private static Logger logger = Logger.getLogger(edtFTPj.class.getName());
    private static edtFTPj ftp = null;
    private String path;
    private FileTransferClient client;

    private edtFTPj(String path) throws FTPException {
        this.path = MiscUtils.appendFtpSeparator(path);
        client = new FileTransferClient();
    }

    private void setHostname(String hostname) throws FTPException {
        client.setRemoteHost(hostname);
    }

    private void setPort(int port) throws FTPException {
        client.setRemotePort(port);
    }

    private void setUsername(String username) throws FTPException {
        client.setUserName(username);
    }

    private void setPassword(String password) throws FTPException {
        client.setPassword(password);
    }

    public static edtFTPj getInstance() {
        if (ftp == null) {
            try {
                ClientProperties app = ClientProperties.getInstance();
                ftp = new edtFTPj(app.getFTPPath());
                ftp.setHostname(app.getFTPHostname());
                ftp.setPort(app.getFTPPort());
                ftp.setUsername(app.getFTPUsername());
                ftp.setPassword(app.getFTPPassword());
                return ftp;
            } catch (IOException ex) {
                logger.log(Level.WARNING, null, ex);
            } catch (FTPException ex) {
                logger.log(Level.WARNING, null, ex);
            }
            ftp = null;
        }
        return ftp;
    }

    private void connect() throws FTPException, IOException {
        if (!client.isConnected()) {
            client.connect();
        }
    }

    private void disconnect() throws FTPException, IOException {
        if (client.isConnected()) {
            client.disconnect();
        }
    }

    public String upload(String filename) {
        File file = new File(filename);
        if (file.exists() && file.isFile()) {
            try {
                connect();
                filename = file.getName();
                client.uploadFile(file.getAbsolutePath(), path + filename,
                        WriteMode.OVERWRITE);
                disconnect();
                return filename;
            } catch (FTPException ex) {
                logger.log(Level.WARNING, null, ex);
            } catch (IOException ex) {
                logger.log(Level.WARNING, null, ex);
            }
        }
        return null;
    }

    public String download(String filename) {
        File file = new File(filename);
        try {
            connect();
            if (client.exists(path + file.getName())) {
                filename = file.getAbsolutePath();
                client.downloadFile(filename, path + file.getName(),
                        WriteMode.OVERWRITE);
            } else {
                filename = null;
            }
            disconnect();
            return filename;
        } catch (FTPException ex) {
            logger.log(Level.WARNING, null, ex);
        } catch (IOException ex) {
            logger.log(Level.WARNING, null, ex);
        }
        return null;
    }
}
