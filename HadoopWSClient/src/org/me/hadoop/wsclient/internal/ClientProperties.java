/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.me.hadoop.wsclient.internal;

import java.io.IOException;
import java.util.Properties;

/**
 *
 * @author Aaron
 */
public class ClientProperties {

    private static ClientProperties app = null;
    private Properties properties;

    private ClientProperties() throws IOException {
        ClassLoader cl = ClientProperties.class.getClassLoader();
        properties = new Properties();
        properties.load(cl.getResourceAsStream("hadoop-client.properties"));
    }

    public static ClientProperties getInstance() throws IOException {
        if (app == null) {
            app = new ClientProperties();
        }
        return app;
    }

    protected String getProperty(String name) {
        return properties.getProperty(name);
    }

    public String getProperty(String name, String value) {
        return properties.getProperty(name, value);
    }

    public String getWSDLUrl() {
        return properties.getProperty("server.wsdl.url");
    }

    public boolean isStreamable() {
        return properties.getProperty("server.streaming").equalsIgnoreCase("yes");
    }

    public boolean useFTP() {
        return properties.getProperty("server.ftp").equalsIgnoreCase("yes");
    }

    public String getFTPPath() {
        return properties.getProperty("server.ftp.path");
    }

    public String getFTPHostname() {
        return properties.getProperty("server.ftp.hostname");
    }

    public int getFTPPort() {
        int port = 21;
        String portnumber = properties.getProperty("server.ftp.port");
        if ((portnumber != null) && (portnumber.trim().length() != 0)) {
            port = Integer.parseInt(portnumber);
        }
        return port;
    }

    public String getFTPUsername() {
        return properties.getProperty("server.ftp.username");
    }

    public String getFTPPassword() {
        return properties.getProperty("server.ftp.password");
    }

    public String getTmpDir() {
        return properties.getProperty("tmp.dir");
    }

    public String getRunnerPort() {
        return properties.getProperty("runner.port");
    }
}
