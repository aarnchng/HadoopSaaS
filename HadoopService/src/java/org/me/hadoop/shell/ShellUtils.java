/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.me.hadoop.shell;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.me.hadoop.service.ServiceProperties;

/**
 *
 * @author Aaron
 */
class StreamGobbler extends Thread {

    private static Logger logger = Logger.getLogger(StreamGobbler.class.getName());
    private InputStream is;
    private String output;

    public StreamGobbler(InputStream is) {
        this.is = is;
        output = "";
    }

    public String getOutput() {
        return output;
    }

    @Override
    public void run() {
        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader br = new BufferedReader(isr);
        try {
            String line = null;
            while ((line = br.readLine()) != null) {
                output += line + "\n";
            }
            br.close();
            isr.close();
        } catch (IOException ex) {
            logger.log(Level.WARNING, null, ex);
        }
    }
}

public class ShellUtils {

    public static final int OUTPUT_EXIT = 0;
    public static final int OUTPUT_STDOUT = 1;
    public static final int OUTPUT_STDERR = 2;
    public static final int OUTPUT_ALL = 3;

    public static String[] runScript(String path, String arguments) throws
            IOException, InterruptedException {
        return runScript(path + " " + arguments);
    }

    public static String[] runScript(String path) throws IOException,
            InterruptedException {
        Runtime runtime = Runtime.getRuntime();
        Process process = runtime.exec(getCommand(path));
        StreamGobbler stdout = new StreamGobbler(process.getInputStream());
        StreamGobbler stderr = new StreamGobbler(process.getErrorStream());
        stdout.start();
        stderr.start();
        int exit = process.waitFor();

        String[] output = new String[OUTPUT_ALL];
        output[OUTPUT_EXIT] = String.valueOf(exit);
        output[OUTPUT_STDOUT] = stdout.getOutput();
        output[OUTPUT_STDERR] = stderr.getOutput();
        return output;
    }

    private static String getCommand(String path) throws IOException {
        String cmd = "";
        ServiceProperties service = ServiceProperties.getInstance();
        if (service.isWin32()) {
            if (service.hasCygwin()) {
                cmd = service.getProperty("cygwin.path");
                cmd += service.getProperty("cygwin.bash");
                cmd += " \"" + service.getProperty("cygwin.hadoop.path");
                cmd += path + "\"";
            } else {
                cmd = service.getProperty("win32.hadoop.path") + path;
            }
        } else {
            cmd = service.getProperty("hadoop.path") + path;
        }
        return cmd;
    }
}
