/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.me.hadoop.service;

import java.util.HashMap;
import org.me.hadoop.service.internal.JobOutput;
import org.me.hadoop.service.internal.JobRun;

/**
 *
 * @author Aaron
 */
public class JobRunList {

    private static HashMap<String, JobRun> runs = new HashMap<String, JobRun>();

    public static boolean add(JobRun run) {
        String name = run.getInputName() + "_" + run.getOutputName();
        if ((run != null) && (!runs.containsKey(name))) {
            runs.put(name, run);
            return true;
        }
        return false;
    }

    public static boolean exists(String inname, String outname) {
        String name = inname + "_" + outname;
        return runs.containsKey(name);
    }

    public static JobRun get(String inname, String outname) {
        String name = inname + "_" + outname;
        if (runs.containsKey(name)) {
            return runs.get(name);
        }
        return null;
    }

    public static JobOutput getOutput(String inname, String outname) {
        String name = inname + "_" + outname;
        if (runs.containsKey(name)) {
            return runs.get(name).getOutput();
        }
        return null;
    }

    public static void remove(JobRun run) {
        String name = run.getInputName() + "_" + run.getOutputName();
        runs.remove(name);
    }
}
