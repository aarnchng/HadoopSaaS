/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.me.hadoop.service.internal;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashSet;
import org.me.hadoop.utils.CommonsIO;
import org.me.hadoop.utils.MiscUtils;

/**
 *
 * @author Aaron
 */
public class JobInput {

    private String path;

    public JobInput(String path) {
        this.path = path = MiscUtils.appendFileSeparator(path);
    }

    public boolean validate() {
        File[] dirs = new File(path).listFiles();
        if (dirs != null) {
            for (File dir : dirs) {
                if (dir.exists() && dir.isDirectory()) {
                    if (dir.getName().equals("DATA")) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public boolean remove() {
        return CommonsIO.deleteDirectory(path);
    }

    public HashSet<JobOutput> getOutputs() throws FileNotFoundException,
            IOException {
        File[] dirs = new File(path).listFiles();
        if (dirs != null) {
            HashSet<JobOutput> outputs = new HashSet<JobOutput>();
            JobOutput output;
            for (File dir : dirs) {
                if (dir.exists() && dir.isDirectory()) {
                    if (!dir.getName().equals("DATA")) {
                        output = new JobOutput(dir.getAbsolutePath());
                        outputs.add(output);
                    }
                }
            }
            return outputs;
        }
        return null;
    }

    public JobOutput createOutput(String name) throws FileNotFoundException,
            IOException {
        String dirname = path + name;
        File dir = new File(dirname);
        if (!dir.exists()) {
            JobOutput output = new JobOutput(dir.getAbsolutePath());
            return output;
        }
        return null;
    }

    public JobOutput getOutput(String name) throws FileNotFoundException,
            IOException {
        String dirname = path + name;
        File dir = new File(dirname);
        if (dir.exists() && dir.isDirectory()) {
            JobOutput output = new JobOutput(dir.getAbsolutePath());
            return output;
        }
        return null;
    }

    public boolean removeOutput(String name) throws FileNotFoundException,
            IOException {
        String dirname = path + name;
        File dir = new File(dirname);
        if (dir.exists() && dir.isDirectory()) {
            JobOutput output = new JobOutput(dir.getAbsolutePath());
            return output.remove();
        }
        return false;
    }

    public String getPath() {
        return path;
    }

    public String getName() {
        return (new File(path)).getName();
    }

    public String getDataPath() {
        return path + "DATA";
    }
}
