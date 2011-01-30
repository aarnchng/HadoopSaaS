/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.me.hadoop.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.me.hadoop.service.internal.JobInput;
import org.me.hadoop.service.internal.JobOutput;
import org.me.hadoop.utils.CommonsIO;
import org.me.hadoop.utils.MiscUtils;
import org.me.hadoop.utils.TrueZIP;

/**
 *
 * @author Aaron
 */
public class DataRepository {

    private static final SimpleDateFormat sdf =
            new SimpleDateFormat("yyyyMMddHHmmssSSS");
    private static Logger logger = Logger.getLogger(DataRepository.class.getName());
    private String path;
    private String tmppath;

    public DataRepository(String path, String tmppath) {
        this.path = MiscUtils.appendFileSeparator(path);
        CommonsIO.createDirectory(path);
        this.tmppath = tmppath;
    }

    public HashSet<JobInput> getInputs() {
        File file = new File(path);
        if (file.exists() && file.isDirectory()) {
            File[] dirs = file.listFiles();
            if (dirs != null) {
                HashSet<JobInput> inputs = new HashSet<JobInput>();
                JobInput input;
                for (File dir : dirs) {
                    if (dir.exists() && dir.isDirectory()) {
                        input = new JobInput(dir.getAbsolutePath());
                        if (input.validate()) {
                            inputs.add(input);
                        }
                    }
                }
                return inputs;
            }
        }
        return null;
    }

    public JobInput getInput(String name) {
        String dirname = path + name;
        File dir = new File(dirname);
        if (dir.exists() && dir.isDirectory()) {
            JobInput input = new JobInput(dir.getAbsolutePath());
            if (input.validate()) {
                return input;
            }
        }
        return null;
    }

    public JobInput addInput(String filename) {
        File file = new File(filename);
        if (file.exists() && file.isFile()) {
            String dirname = path + "INPUT_" + sdf.format(new Date());
            File dir = new File(dirname);
            if ((!dir.exists()) && dir.mkdirs()) {
                JobInput input = new JobInput(dir.getAbsolutePath());
                dirname = TrueZIP.uncompress(file.getAbsolutePath(),
                        input.getDataPath());
                if (dirname != null) {
                    if (input.validate()) {
                        return input;
                    }
                }
            }
        }
        return null;
    }

    public boolean removeInput(String name) {
        String dirname = path + name;
        File dir = new File(dirname);
        if (dir.exists() && dir.isDirectory()) {
            JobInput input = new JobInput(dir.getAbsolutePath());
            if (input.validate()) {
                return input.remove();
            }
        }
        return false;
    }

    public HashSet<JobOutput> getOutputs(String name) {
        return getOutputs(getInput(name));
    }

    public HashSet<JobOutput> getOutputs(JobInput input) {
        if (input != null) {
            try {
                return input.getOutputs();
            } catch (FileNotFoundException ex) {
                logger.log(Level.WARNING, null, ex);
            } catch (IOException ex) {
                logger.log(Level.WARNING, null, ex);
            }
        }
        return null;
    }

    public JobOutput createOutput(String name) {
        return createOutput(getInput(name));
    }

    public JobOutput createOutput(JobInput input) {
        if (input != null) {
            try {
                return input.createOutput("OUTPUT_" + sdf.format(new Date()));
            } catch (FileNotFoundException ex) {
                logger.log(Level.WARNING, null, ex);
            } catch (IOException ex) {
                logger.log(Level.WARNING, null, ex);
            }
        }
        return null;
    }

    public JobOutput getOutput(String inname, String outname) {
        return getOutput(getInput(inname), outname);
    }

    public JobOutput getOutput(JobInput input, String name) {
        if (input != null) {
            try {
                return input.getOutput(name);
            } catch (FileNotFoundException ex) {
                logger.log(Level.WARNING, null, ex);
            } catch (IOException ex) {
                logger.log(Level.WARNING, null, ex);
            }
        }
        return null;
    }

    public String getCompressedOutput(String inname, String outname) {
        return getCompressedOutput(getOutput(getInput(inname), outname));
    }

    public String getCompressedOutput(JobInput input, String name) {
        return getCompressedOutput(getOutput(input, name));
    }

    public String getCompressedOutput(JobOutput output) {
        if (output != null) {
            String filename = tmppath + "ZIP_" + sdf.format(new Date());
            File file = new File(filename);
            if (!file.exists()) {
                File dir = new File(output.getDataPath());
                if (dir.exists() && dir.isDirectory()) {
                    filename = TrueZIP.zip(dir.getAbsolutePath(),
                            file.getAbsolutePath());
                    if (filename != null) {
                        file = new File(filename);
                        return file.getName();
                    }
                }
            }
        }
        return null;
    }

    public String getCompressedOutput(String inname, String outname, String wildcard) {
        return getCompressedOutput(getOutput(getInput(inname), outname), wildcard);
    }

    public String getCompressedOutput(JobInput input, String name, String wildcard) {
        return getCompressedOutput(getOutput(input, name), wildcard);
    }

    public String getCompressedOutput(JobOutput output, String wildcard) {
        if (output != null) {
            String filename = tmppath + "ZIP_" + sdf.format(new Date());
            File file = new File(filename);
            if (!file.exists()) {
                File dir = new File(output.getDataPath());
                if (dir.exists() && dir.isDirectory()) {
                    filename = TrueZIP.zip(dir.getAbsolutePath(),
                            file.getAbsolutePath(), wildcard);
                    if (filename != null) {
                        file = new File(filename);
                        return file.getName();
                    }
                }
            }
        }
        return null;
    }

    public boolean removeOutput(String inname, String outname) {
        return removeOutput(getInput(inname), outname);
    }

    public boolean removeOutput(JobInput input, String name) {
        if (input != null) {
            try {
                return input.removeOutput(name);
            } catch (FileNotFoundException ex) {
                logger.log(Level.WARNING, null, ex);
            } catch (IOException ex) {
                logger.log(Level.WARNING, null, ex);
            }
        }
        return false;
    }
}
