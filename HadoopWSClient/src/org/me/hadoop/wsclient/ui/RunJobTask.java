/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.me.hadoop.wsclient.ui;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import org.jdesktop.application.Application;
import org.jdesktop.application.Task;
import org.me.hadoop.service.JobMessage;
import org.me.hadoop.service.TJobOutput;
import org.me.hadoop.wsclient.JobRunner;
import org.me.hadoop.wsclient.ProgressableUI;

/**
 *
 * @author Aaron
 */
public class RunJobTask extends Task<TJobOutput, Void> {

    private static Logger logger = Logger.getLogger(RunJobTask.class.getName());
    private ProgressableUI progress;
    private String modelname;
    private String inputname;
    private String description;
    private String arguments;
    private Thread thread;

    RunJobTask(Application app, ProgressableUI progress, String modelname,
            String inputname, String description, String arguments) {
        // Runs on the EDT.  Copy GUI state that
        // doInBackground() depends on from parameters
        // to RunJobTask fields, here.
        super(app);
        this.progress = progress;
        this.modelname = modelname;
        this.inputname = inputname;
        this.description = description;
        this.arguments = arguments;
        this.thread = null;
        setUserCanCancel(false);
    }

    RunJobTask(Application app, String modelname, String inputname, String description,
            String arguments) {
        this(app, null, modelname, inputname, description, arguments);
    }

    void setProgressableUI(ProgressableUI progress) {
        this.progress = progress;
    }

    void interrupt() {
        if (thread != null) {
            thread.interrupt();
        }
    }

    @Override
    protected TJobOutput doInBackground() {
        // Your Task's code here.  This method runs
        // on a background thread, so don't reference
        // the Swing GUI from here.
        if ((modelname == null) || (inputname == null)) {
            return null;
        }
        thread = Thread.currentThread();
        return JobRunner.runJob(progress, modelname, inputname, arguments, description);
    }

    @Override
    protected void succeeded(TJobOutput result) {
        // Runs on the EDT.  Update the GUI based on
        // the result computed by doInBackground().
        if (result != null) {
            if (result.getStatus().equals(JobMessage.MSG_SUCCESS)) {
                try {
                    Desktop.getDesktop().open(new File(result.getFileName()));
                } catch (IOException ex) {
                    logger.log(Level.WARNING, null, ex);
                }
                String message = "'" + result.getName() + "' has been retrieved!";
                JOptionPane.showMessageDialog(null, message,
                        getResourceMap().getString("Application.title", ""),
                        JOptionPane.INFORMATION_MESSAGE);
            }
        } else {
            if ((modelname == null) || (inputname == null)) {
                JOptionPane.showMessageDialog(null, "Required argument is missing!",
                        getResourceMap().getString("Application.title", ""),
                        JOptionPane.INFORMATION_MESSAGE);
            } else if ((progress == null)) {
                String message = "Unknown error! Please consult your administrator!";
                JOptionPane.showMessageDialog(null, message,
                        getResourceMap().getString("Application.title", ""),
                        JOptionPane.INFORMATION_MESSAGE);
            } else if (!progress.getProperty("job.forget")) {
                String message = "Job has failed due to:\n";
                if (progress.getProperty("job.failure", "").trim().length() != 0) {
                    message += progress.getProperty("job.failure", "");
                } else {
                    message += "Unknown error! Please consult your administrator!";
                }
                JOptionPane.showMessageDialog(null, message,
                        getResourceMap().getString("Application.title", ""),
                        JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }
}
