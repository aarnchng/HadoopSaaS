/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.me.hadoop.service.datatypes;

import org.me.hadoop.service.internal.JobInput;
import org.me.hadoop.service.internal.JobModel;
import org.me.hadoop.service.internal.JobOutput;
import org.me.hadoop.service.internal.JobRun;

/**
 *
 * @author Aaron
 */
public class T_JobRun {

    private T_JobModel model;
    private T_JobInput input;
    private T_JobOutputHeader output;

    public T_JobRun() {
        model = new T_JobModel();
        input = new T_JobInput();
        output = new T_JobOutputHeader();
    }

    public T_JobRun(JobRun run) {
        model = new T_JobModel(run.getModel());
        input = new T_JobInput(run.getInput());
        output = new T_JobOutputHeader(run.getOutput());
    }

    public T_JobRun(JobModel model, JobInput input, JobOutput output) {
        this.model = new T_JobModel(model);
        this.input = new T_JobInput(input);
        this.output = new T_JobOutputHeader(output);
    }

    public T_JobModel getModel() {
        return model;
    }

    public void setModel(T_JobModel model) {
        this.model = model;
    }

    public T_JobInput getInput() {
        return input;
    }

    public void setInput(T_JobInput input) {
        this.input = input;
    }

    public T_JobOutputHeader getOutput() {
        return output;
    }

    public void setOutput(T_JobOutputHeader output) {
        this.output = output;
    }
}
