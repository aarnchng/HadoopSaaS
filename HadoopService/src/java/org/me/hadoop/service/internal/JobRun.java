/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.me.hadoop.service.internal;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.me.hadoop.service.JobMessage;

/**
 *
 * @author Aaron
 */
public class JobRun {

    public static final int STATUS_STARTING = 0;
    public static final int STATUS_RUNNING = 1;
    public static final int STATUS_SUCCESS = 2;
    public static final int STATUS_FAILURE = 3;
    public static final int STATUS_ERROR = 4;
    public static final int STATUS_KILLED = 5;
    public static final int STATUS_UNKNOWN = 6;
    private static Logger logger = Logger.getLogger(JobRun.class.getName());
    private JobModel model;
    private JobInput input;
    private JobOutput output;
    private RunProperties run;

    private JobRun(JobModel model, JobInput input, JobOutput output) {
        this.model = model;
        this.input = input;
        this.output = output;
        run = output.getRunProperties();
        run.setModelName(model.getName());
        run.setModelMethod(model.getMethod());
        run.setModelVersion(model.getVersion());
        run.setModelDescription(model.getDescription());
        run.setModelAuthor(model.getAuthor());
    }

    public static JobRun getInstance(JobModel model, JobInput input,
            JobOutput output, String description, String arguments) {
        if ((model != null) && (input != null) && (output != null)) {
            JobRun run = new JobRun(model, input, output);
            run.setDescription(description);
            run.setArguments(arguments);
            run.save(STATUS_STARTING);
            return run;
        }
        return null;
    }

    public void save(int status) {
        setStatus(status);
        try {
            run.save();
        } catch (FileNotFoundException ex) {
            logger.log(Level.WARNING, null, ex);
        } catch (IOException ex) {
            logger.log(Level.WARNING, null, ex);
        }
    }

    public void save(String jobname) {
        setJobName(jobname);
        try {
            run.save();
        } catch (FileNotFoundException ex) {
            logger.log(Level.WARNING, null, ex);
        } catch (IOException ex) {
            logger.log(Level.WARNING, null, ex);
        }
    }

    public JobModel getModel() {
        return model;
    }

    public JobInput getInput() {
        return input;
    }

    public String getInputName() {
        return input.getName();
    }

    public JobOutput getOutput() {
        return output;
    }

    public String getOutputName() {
        return output.getName();
    }

    public String getOutputDataPath() {
        return output.getDataPath();
    }

    public void setDescription(String description) {
        run.setDescription(description);
    }

    public String getArguments() {
        return run.getArguments();
    }

    public void setArguments(String arguments) {
        run.setArguments(arguments);
    }

    public String getJobName() {
        return run.getJobName();
    }

    public void setJobName(String jobname) {
        run.setJobName(jobname);
    }

    public int getStatus() {
        int status = STATUS_UNKNOWN;
        String message = run.getStatus();
        if (message.equals(JobMessage.MSG_STARTING)) {
            status = STATUS_STARTING;
        } else if (message.equals(JobMessage.MSG_RUNNING)) {
            status = STATUS_RUNNING;
        } else if (message.equals(JobMessage.MSG_SUCCESS)) {
            status = STATUS_SUCCESS;
        } else if (message.equals(JobMessage.MSG_FAILURE)) {
            status = STATUS_FAILURE;
        } else if (message.equals(JobMessage.MSG_ERROR)) {
            status = STATUS_ERROR;
        } else if (message.equals(JobMessage.MSG_KILLED)) {
            status = STATUS_KILLED;
        }
        return status;
    }

    public void setStatus(int status) {
        switch (status) {
            case STATUS_STARTING:
                run.createStartDate();
                run.setStatus(JobMessage.MSG_STARTING);
                break;
            case STATUS_RUNNING:
                run.setStatus(JobMessage.MSG_RUNNING);
                break;
            case STATUS_SUCCESS:
                run.createEndDate();
                run.setStatus(JobMessage.MSG_SUCCESS);
                break;
            case STATUS_FAILURE:
                run.createEndDate();
                run.setStatus(JobMessage.MSG_FAILURE);
                break;
            case STATUS_ERROR:
                run.createEndDate();
                run.setStatus(JobMessage.MSG_ERROR);
                break;
            case STATUS_KILLED:
                run.createEndDate();
                run.setStatus(JobMessage.MSG_KILLED);
                break;
            default:
                run.setStatus(JobMessage.MSG_UNKNOWN);
        }
    }

    public void open() {
        try {
            output.open();
        } catch (IOException ex) {
            logger.log(Level.WARNING, null, ex);
        }
    }

    public void writeStdOut(String log) {
        try {
            output.writeStdOut(log);
        } catch (IOException ex) {
            logger.log(Level.WARNING, null, ex);
        }
    }

    public void writeStdErr(String log) {
        try {
            output.writeStdErr(log);
        } catch (IOException ex) {
            logger.log(Level.WARNING, null, ex);
        }
    }

    public void close() {
        try {
            output.close();
        } catch (IOException ex) {
            logger.log(Level.WARNING, null, ex);
        }
    }
}
