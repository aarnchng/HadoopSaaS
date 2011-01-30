/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.me.hadoop.basic;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.mapred.JobClient;
import org.apache.hadoop.net.NetUtils;
import org.apache.hadoop.util.ToolRunner;
import org.me.hadoop.JobTracker;
import org.me.hadoop.service.internal.JarModel;
import org.me.hadoop.service.internal.JobRun;
import org.me.hadoop.service.internal.StreamingModel;

/**
 *
 * @author Aaron
 */
public class BasicJobTracker extends JobTracker {

    private static Logger logger = Logger.getLogger(BasicJobTracker.class.getName());
    private String hadooppath;
    private Configuration conf;

    public BasicJobTracker(BasicNameNode namenode, String hadooppath)
            throws IOException {
        super(namenode);
        this.hadooppath = hadooppath;
        conf = namenode.getConf();
        System.setSecurityManager(new JobSecurityManager());
    }

    protected Configuration getConf() {
        return conf;
    }

    @Override
    public boolean isAvailable() {
        String[] args = {"-list", "all"};
        return runCommand(args);
    }

    @Override
    public boolean kill(String name) {
        if ((name != null) && (name.trim().length() != 0)) {
            String[] args = {"-kill", name};
            return runCommand(args);
        }
        return false;
    }

    private boolean runCommand(String[] args) {
        boolean result = false;
        JobClient jobClient = getJobClient();
        if (jobClient != null) {
            try {
                if (ToolRunner.run(jobClient, args) == 0) {
                    result = true;
                }
            } catch (Exception ex) {
                logger.log(Level.WARNING, null, ex);
            }
            try {
                jobClient.close();
            } catch (IOException ex) {
                logger.log(Level.WARNING, null, ex);
            }
        }
        return result;
    }

    private JobClient getJobClient() {
        JobClient jobClient = null;
        String address = conf.get("mapred.job.tracker");
        if ((address == null) || address.equals("local")) {
            jobClient = new JobClient();
        } else {
            InetSocketAddress inetaddress = NetUtils.createSocketAddr(address);
            try {
                jobClient = new JobClient(inetaddress, conf);
            } catch (IOException ex) {
                logger.log(Level.WARNING, null, ex);
            }
        }
        return jobClient;
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
        String[] args = new String[4];
        JarModel model = (JarModel) run.getModel();
        args[0] = model.getJarPath();
        args[1] = model.getMainClass();
        args[2] = getNameNode().getPath() + run.getInputName();
        args[3] = getNameNode().getPath() + run.getOutputName();
        args = addArgs(args, run.getArguments());

        Thread thread = new BasicThread(this, run, args);
        thread.start();
    }

    private void streaming(JobRun run) {
        String[] args = new String[9];
        StreamingModel model = (StreamingModel) run.getModel();
        args[0] = hadooppath + "hadoop-streaming.jar";
        args[1] = "-mapper";
        args[2] = model.getLanguage() + " " + model.getMapperPath();
        if (!model.getReducerPath().equals("none")) {
            args[3] = "-reducer";
            args[4] = model.getLanguage() + " " + model.getReducerPath();
            args[5] = "-input";
            args[6] = getNameNode().getPath() + run.getInputName();
            args[7] = "-output";
            args[8] = getNameNode().getPath() + run.getOutputName();
        } else {
            args[3] = "-input";
            args[4] = getNameNode().getPath() + run.getInputName();
            args[5] = "-output";
            args[6] = getNameNode().getPath() + run.getOutputName();
            args[7] = "-numReduceTasks";
            args[8] = "0";
        }
        args = addArgs(args, run.getArguments());

        Thread thread = new BasicThread(this, run, args);
        thread.start();
    }

    private String[] addArgs(String[] args, String arguments) {
        if (arguments.trim().length() != 0) {
            String[] tokens = arguments.split("\\s");
            int olength = args.length;
            args = Arrays.copyOf(args, olength + tokens.length);
            for (int i = olength; i < args.length; i++) {
                args[i] = tokens[i - olength];
            }
        }
        return args;
    }
}
