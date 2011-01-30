/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.me.hadoop.basic;

import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import org.apache.hadoop.mapred.JobShell;
import org.apache.hadoop.util.ToolRunner;
import org.me.hadoop.service.JobRunList;
import org.me.hadoop.service.internal.JobRun;

/**
 *
 * @author Aaron
 */
class BasicHandler extends Handler {

    private JobRun run;
    private int id;

    public BasicHandler(JobRun run) {
        this.run = run;
        id = -1;
    }

    @Override
    public void publish(LogRecord record) {
        String message = record.getMessage();
        if (message != null) {
            if (id == -1) {
                if (message.equals(run.getInputName() + "_" + run.getOutputName())) {
                    id = record.getThreadID();
                }
            } else if (record.getThreadID() == id) {
                if (message.contains("INFO")) {
                    run.writeStdOut(message);
                } else {
                    run.writeStdErr(message);
                }

                if (message.contains("Running job:")) {
                    String jobname = message.substring(message.indexOf("Running job:") +
                            13, message.indexOf("Running job:") + 34);
                    if (!run.getJobName().equals(jobname)) {
                        run.save(jobname);
                    }
                }
            }
        }
    }

    @Override
    public void flush() {
    }

    @Override
    public void close() {
    }
}

public class BasicThread extends Thread {

    private static Logger logger = Logger.getLogger(BasicThread.class.getName());
    private BasicJobTracker jobtracker;
    private JobRun run;
    private String[] args;
    private int exitCode;

    public BasicThread(BasicJobTracker jobtracker, JobRun run, String[] args) {
        this.jobtracker = jobtracker;
        this.run = run;
        this.args = args;
        exitCode = -1;
    }

    protected void setExitCode(int exitCode) {
        if (this.exitCode != 0) {
            this.exitCode = exitCode;
        }
    }

    @Override
    public void run() {
        try {
            run.save(JobRun.STATUS_RUNNING);
            JobRunList.add(run);
            JobShell job = new JobShell(jobtracker.getConf());

            run.open();
            BasicHandler handler = new BasicHandler(run);
            Logger.getLogger("").addHandler(handler);
            logger.log(Level.INFO, run.getInputName() + "_" + run.getOutputName());
            ToolRunner.run(job, args);
            Logger.getLogger("").removeHandler(handler);

            if (exitCode == 0) {
                jobtracker.getNameNode().get(run.getOutputName(),
                        run.getOutputDataPath());
                run.save(JobRun.STATUS_SUCCESS);
            } else if (run.getStatus() != JobRun.STATUS_KILLED) {
                run.save(JobRun.STATUS_FAILURE);
            }
        } catch (Exception ex) {
            logger.log(Level.WARNING, null, ex);

            run.save(JobRun.STATUS_ERROR);
            run.writeStdErr(ex.getMessage());
        } finally {
            run.close();
            JobRunList.remove(run);
        }
    }
}
