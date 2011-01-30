/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.me.hadoop.service.datatypes;

import org.me.hadoop.service.internal.JobOutput;
import org.me.hadoop.service.internal.RunProperties;

/**
 *
 * @author Aaron
 */
public class T_JobOutput {

    private String name;
    private String stdout;
    private String stderr;
    private String modelname;
    private String modelversion;
    private String modelauthor;
    private String modeldesc;
    private String description;
    private String arguments;
    private String status;
    private String startdate;
    private String enddate;
    private String filename;
    private String jobcount;
    private String jobname;

    public T_JobOutput() {
        name = "";
        stdout = "";
        stderr = "";
        modelname = "";
        modelversion = "";
        modelauthor = "";
        modeldesc = "";
        description = "";
        arguments = "";
        status = "";
        startdate = "";
        enddate = "";
        filename = "";
        jobcount = "0";
        jobname = "";
    }

    public T_JobOutput(JobOutput output) {
        name = output.getName();
        stdout = output.getStdOut();
        stderr = output.getStdErr();
        RunProperties run = output.getRunProperties();
        modelname = run.getModelName();
        modelversion = run.getModelVersion();
        modelauthor = run.getModelAuthor();
        modeldesc = run.getModelDescription();
        description = run.getDescription();
        arguments = run.getArguments();
        status = run.getStatus();
        startdate = run.getStartDate();
        enddate = run.getEndDate();
        filename = "";
        jobcount = run.getJobCount();
        jobname = run.getJobName();
    }

    public T_JobOutput(JobOutput output, String filename) {
        name = output.getName();
        stdout = output.getStdOut();
        stderr = output.getStdErr();
        RunProperties run = output.getRunProperties();
        modelname = run.getModelName();
        modelversion = run.getModelVersion();
        modelauthor = run.getModelAuthor();
        modeldesc = run.getModelDescription();
        description = run.getDescription();
        arguments = run.getArguments();
        status = run.getStatus();
        startdate = run.getStartDate();
        enddate = run.getEndDate();
        this.filename = filename;
        jobcount = run.getJobCount();
        jobname = run.getJobName();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStdOut() {
        return stdout;
    }

    public void setStdOut(String stdout) {
        this.stdout = stdout;
    }

    public String getStdErr() {
        return stderr;
    }

    public void setStdErr(String stderr) {
        this.stderr = stderr;
    }

    public String getModelName() {
        return modelname;
    }

    public void setModelName(String modelname) {
        this.modelname = modelname;
    }

    public String getModelVersion() {
        return modelversion;
    }

    public void setModelVersion(String modelversion) {
        this.modelversion = modelversion;
    }

    public String getModelAuthor() {
        return modelauthor;
    }

    public void setModelAuthor(String modelauthor) {
        this.modelauthor = modelauthor;
    }

    public String getModelDescription() {
        return modeldesc;
    }

    public void setModelDescription(String modeldesc) {
        this.modeldesc = modeldesc;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getArguments() {
        return arguments;
    }

    public void setArguments(String arguments) {
        this.arguments = arguments;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStartDate() {
        return startdate;
    }

    public void setStartDate(String startdate) {
        this.startdate = startdate;
    }

    public String getEndDate() {
        return enddate;
    }

    public void setEndDate(String enddate) {
        this.enddate = enddate;
    }

    public String getFileName() {
        return filename;
    }

    public void setFileName(String filename) {
        this.filename = filename;
    }

    public String getJobCount() {
        return jobcount;
    }

    public void setJobCount(String jobcount) {
        this.jobcount = jobcount;
    }

    public String getJobName() {
        return jobname;
    }

    public void setJobName(String jobname) {
        this.jobname = jobname;
    }
}
