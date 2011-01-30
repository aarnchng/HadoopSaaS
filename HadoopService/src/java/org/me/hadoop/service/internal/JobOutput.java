/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.me.hadoop.service.internal;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.me.hadoop.utils.CommonsIO;
import org.me.hadoop.utils.MiscUtils;

/**
 *
 * @author Aaron
 */
public class JobOutput {

    private static Logger logger = Logger.getLogger(JobOutput.class.getName());
    private String path;
    private RunProperties run;
    private FileWriter stdout = null;
    private FileWriter stderr = null;

    public JobOutput(String path) throws FileNotFoundException, IOException {
        this.path = path = MiscUtils.appendFileSeparator(path);
        CommonsIO.createDirectory(getDataPath());
        run = new RunProperties(path);
    }

    public boolean remove() {
        return CommonsIO.deleteDirectory(path);
    }

    public String getPath() {
        return path;
    }

    public String getName() {
        return (new File(path)).getName();
    }

    public RunProperties getRunProperties() {
        return run;
    }

    public String getDataPath() {
        return path + "DATA";
    }

    public String getStatus() {
        return run.getStatus();
    }

    public String getJobName() {
        return run.getJobName();
    }

    public String getPidPath() {
        return path + "pid.log";
    }

    public String getStdOutPath() {
        return path + "stdout.log";
    }

    public String getStdOut() {
        try {
            File file = new File(getPidPath());
            if (!file.exists()) {
                return getLog(getStdOutPath());
            }
        } catch (FileNotFoundException ex) {
            logger.log(Level.WARNING, null, ex);
        } catch (IOException ex) {
            logger.log(Level.WARNING, null, ex);
        }
        return "";
    }

    public String getStdErrPath() {
        return path + "stderr.log";
    }

    public String getStdErr() {
        try {
            File file = new File(getPidPath());
            if (!file.exists()) {
                return getLog(getStdErrPath());
            }
        } catch (FileNotFoundException ex) {
            logger.log(Level.WARNING, null, ex);
        } catch (IOException ex) {
            logger.log(Level.WARNING, null, ex);
        }
        return "";
    }

    private String getLog(String filename) throws FileNotFoundException, IOException {
        String log = "";
        File file = new File(filename);
        if (file.exists()) {
            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);
            String line = null;
            while ((line = br.readLine()) != null) {
                log += line + "\n";
            }
            br.close();
            fr.close();
        }
        return log;
    }

    public void open() throws IOException {
        CommonsIO.createFile(getPidPath());
        if (stdout == null) {
            stdout = new FileWriter(getStdOutPath(), true);
        }
        if (stderr == null) {
            stderr = new FileWriter(getStdErrPath(), true);
        }
    }

    public void writeStdOut(String log) throws IOException {
        if ((stdout != null) && (log != null)) {
            stdout.write(log + "\n");
        }
    }

    public void writeStdErr(String log) throws IOException {
        if ((stderr != null) && (log != null)) {
            stderr.write(log + "\n");
        }
    }

    public void close() throws IOException {
        if (stdout != null) {
            stdout.close();
            stdout = null;
        }
        if (stderr != null) {
            stderr.close();
            stderr = null;
        }
        CommonsIO.deleteFile(getPidPath());
    }
}
