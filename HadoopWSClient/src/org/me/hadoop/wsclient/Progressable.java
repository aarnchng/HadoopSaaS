/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.me.hadoop.wsclient;

import org.netbeans.spi.wizard.ResultProgressHandle;
import org.netbeans.spi.wizard.Summary;

/**
 *
 * @author Aaron
 */
public class Progressable {

    private ResultProgressHandle handle;
    private ProgressableUI progress;
    private int totalsteps;

    public Progressable() {
        handle = null;
        progress = null;
        totalsteps = 0;
    }

    public void setProgressHandle(ResultProgressHandle handle, int totalsteps) {
        this.handle = handle;
        this.totalsteps = totalsteps;
    }

    public void setProgressableUI(ProgressableUI progress, int totalsteps) {
        this.progress = progress;
        this.totalsteps = totalsteps;
    }

    protected boolean getProperty(String key) {
        if (progress != null) {
            return progress.getProperty(key);
        }
        return false;
    }

    protected void setProperty(String key, String value) {
        if (progress != null) {
            progress.setProperty(key, value);
        }
    }

    protected void setProgress(String message, int currentstep) {
        setProgress(message, currentstep, false);
    }

    protected void setProgress(String message, int currentstep, boolean indeterminate) {
        System.out.println("PROCEDURE: " + message);
        if (handle != null) {
            handle.setProgress(message, currentstep, totalsteps);
        }
        if (progress != null) {
            progress.setProgress(message, indeterminate, currentstep, totalsteps);
        }
    }

    protected void finished(String value, String message, Object result) {
        System.out.println("SUCCESS: " + value);
        if (handle != null) {
            handle.finished(Summary.create(message, result));
        }
    }

    protected void failed(String message, boolean canback) {
        System.err.println("FAILURE: " + message);
        if (handle != null) {
            handle.failed(message, canback);
        }
        if (progress != null) {
            progress.failed(message);
        }
    }
}
