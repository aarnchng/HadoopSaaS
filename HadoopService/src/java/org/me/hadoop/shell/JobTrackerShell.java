/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.me.hadoop.shell;

import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.me.hadoop.JobTracker;
import org.me.hadoop.service.internal.JarModel;
import org.me.hadoop.service.internal.JobRun;
import org.me.hadoop.service.internal.StreamingModel;

/**
 *
 * @author Aaron
 */
class ShellRunWorker implements Runnable {

    private static Logger logger = Logger.getLogger(ShellRunWorker.class.getName());
    private JobTrackerShell jobtracker;
    private JobRun run;
    private String script;
    private String args;

    public ShellRunWorker(JobTrackerShell jobtracker, JobRun run, String script,
            String args) {
        this.jobtracker = jobtracker;
        this.run = run;
        this.script = script;
        this.args = args;
    }

    @Override
    public void run() {
        run.save(JobRun.STATUS_RUNNING);
        try {
            String[] output = ShellUtils.runScript(script, args);
            run.open();
            run.writeStdOut(output[ShellUtils.OUTPUT_STDOUT]);
            run.writeStdErr(output[ShellUtils.OUTPUT_STDERR]);
            if (output[ShellUtils.OUTPUT_EXIT].trim().equals("0")) {
                jobtracker.getNameNode().get(run.getOutputName(),
                        run.getOutputDataPath());
                run.save(JobRun.STATUS_SUCCESS);
            } else {
                run.save(JobRun.STATUS_FAILURE);
            }
        } catch (IOException ex) {
            logger.log(Level.WARNING, null, ex);

            run.save(JobRun.STATUS_ERROR);
            run.writeStdErr(ex.getMessage());
        } catch (InterruptedException ex) {
            logger.log(Level.WARNING, null, ex);

            run.save(JobRun.STATUS_ERROR);
            run.writeStdErr(ex.getMessage());
        } finally {
            run.close();
        }
    }
}

public class JobTrackerShell extends JobTracker {

    private static Logger logger = Logger.getLogger(JobTrackerShell.class.getName());
    private String scripttype;
    private String hadooppath;
    private Properties properties;
    private boolean ready;

    public JobTrackerShell(NameNodeShell namenode, String scripttype,
            String hadooppath) {
        super(namenode);
        this.scripttype = scripttype;
        this.hadooppath = hadooppath;
        properties = namenode.getProperties();
        ready = namenode.isReady();
    }

    @Override
    public boolean isAvailable() {
        return (ready && list());
    }

    private boolean list() {
        String script = properties.getProperty("script.list" + scripttype);
        String args = "'" + hadooppath + "'";
        try {
            String[] output = ShellUtils.runScript(script, args);
            if (output[ShellUtils.OUTPUT_STDERR].trim().length() == 0) {
                return true;
            }
        } catch (IOException ex) {
            logger.log(Level.WARNING, null, ex);
        } catch (InterruptedException ex) {
            logger.log(Level.WARNING, null, ex);
        }
        return false;
    }

    @Override
    public boolean kill(String name) {
        return false;
    }

    @Override
    public void run(JobRun run) {
        if (run != null) {
            if (run.getModel() instanceof JarModel) {
                jar(run);
            } else if (run.getModel() instanceof StreamingModel) {
                streaming(run);
            }
        }
    }

    private void jar(JobRun run) {
        String script = properties.getProperty("script.jar" + scripttype);
        JarModel model = (JarModel) run.getModel();
        String args = "'" + hadooppath + "' '" + model.getJarPath() + "'";
        args += " " + model.getMainClass();
        args += " '" + getNameNode().getPath() + run.getInputName() + "'";
        args += " '" + getNameNode().getPath() + run.getOutputName() + "'";
        if (run.getArguments().trim().length() != 0) {
            args += " '" + run.getArguments() + "'";
        }
        args = args.trim();

        Thread thread = new Thread(new ShellRunWorker(this, run, script, args));
        thread.start();
    }

    private void streaming(JobRun run) {
        String script = properties.getProperty("script.streaming" + scripttype);
        StreamingModel model = (StreamingModel) run.getModel();
        String args = "'" + hadooppath + "' " + model.getLanguage();
        args += " '" + model.getMapperPath() + "'";
        args += " '" + model.getReducerPath() + "'";
        args += " '" + getNameNode().getPath() + run.getInputName() + "'";
        args += " '" + getNameNode().getPath() + run.getOutputName() + "'";
        if (run.getArguments().trim().length() != 0) {
            args += " '" + run.getArguments() + "'";
        }
        args = args.trim();

        Thread thread = new Thread(new ShellRunWorker(this, run, script, args));
        thread.start();
    }
}
