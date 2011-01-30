/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.me.hadoop.wsclient;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.ws.soap.SOAPFaultException;
import org.me.hadoop.service.HadoopService;
import org.me.hadoop.service.HadoopService_Service;
import org.me.hadoop.service.JobMessage;
import org.me.hadoop.service.TJobOutput;
import org.me.hadoop.service.TJobRun;
import org.me.hadoop.uploader.UploaderClient;
import org.me.hadoop.utils.CommonsIO;
import org.me.hadoop.utils.MiscUtils;
import org.me.hadoop.utils.TrueZIP;
import org.me.hadoop.utils.edtFTPj;
import org.me.hadoop.wsclient.internal.ClientProperties;
import org.me.hadoop.wsclient.internal.FileStreamer;

/**
 *
 * @author Aaron
 */
public class JobRunner extends Progressable {

    public static final int POLL_INTERVAL = 30000;
    public static final int STATUS_UNAVAILABLE = 0;
    public static final int STATUS_RUNNING = 1;
    public static final int STATUS_SUCCESS = 2;
    public static final int STATUS_FAILURE = 3;
    public static final int STATUS_KILLED = 4;
    private static Logger logger = Logger.getLogger(JobRunner.class.getName());
    private String path;
    private String modelname;
    private String inputname;
    private boolean useftp;
    private boolean streaming;
    private String arguments = "";
    private String description = "";

    public JobRunner(String path, String modelname, String inputname, boolean useftp,
            boolean streaming, String arguments, String description) {
        super();
        this.path = MiscUtils.appendFileSeparator(path);
        CommonsIO.createDirectory(this.path);
        this.modelname = modelname;
        this.inputname = inputname;
        this.useftp = useftp;
        this.streaming = streaming;
        setArguments(arguments);
        setDescription(description);
    }

    public JobRunner(String path, String modelname, String inputname, boolean useftp,
            String arguments, String description) {
        this(path, modelname, inputname, useftp, false, arguments, description);
    }

    public JobRunner(String path, String modelname, String inputname, String arguments,
            String description) {
        this(path, modelname, inputname, false, false, arguments, description);
    }

    private void setArguments(String arguments) {
        if (arguments != null) {
            this.arguments = arguments;
        }
    }

    private void setDescription(String description) {
        if (description != null) {
            this.description = description;
        }
    }

    private TJobRun submitJob() throws SOAPFaultException {
        try { // Call Web Service Operation
            HadoopService_Service service = MiscUtils.getHadoopService();
            HadoopService port = service.getHadoopServicePort();
            // TODO process result here
            return port.runJob(modelname, inputname, description, arguments);
        } catch (SOAPFaultException ex) {
            throw ex;
        } catch (Exception ex) {
            logger.log(Level.WARNING, null, ex);
        }
        return null;
    }

    private TJobOutput getOutput(String outputname) throws SOAPFaultException {
        try { // Call Web Service Operation
            HadoopService_Service service = MiscUtils.getHadoopService();
            HadoopService port = service.getHadoopServicePort();
            // TODO process result here
            return port.getOutput(inputname, outputname);
        } catch (SOAPFaultException ex) {
            throw ex;
        } catch (Exception ex) {
            logger.log(Level.WARNING, null, ex);
        }
        return null;

    }

    private int getJobStatus(String outputname) throws SOAPFaultException {
        TJobOutput output = getOutput(outputname);
        if (output != null) {
            String status = output.getStatus();
            if (status.equals(JobMessage.MSG_SUCCESS)) {
                return STATUS_SUCCESS;
            } else if (status.equals(JobMessage.MSG_FAILURE) ||
                    status.equals(JobMessage.MSG_ERROR)) {
                return STATUS_FAILURE;
            } else if (status.equals(JobMessage.MSG_KILLED)) {
                return STATUS_KILLED;
            }
            return STATUS_RUNNING;
        }
        return STATUS_UNAVAILABLE;
    }

    private TJobOutput getCompressedOutput(String outputname) throws SOAPFaultException {
        try { // Call Web Service Operation
            HadoopService_Service service = MiscUtils.getHadoopService();
            HadoopService port = service.getHadoopServicePort();
            // TODO process result here
            return port.getCompressedOutput(inputname, outputname);
        } catch (SOAPFaultException ex) {
            throw ex;
        } catch (Exception ex) {
            logger.log(Level.WARNING, null, ex);
        }
        return null;
    }

    private String getOutData(String filename) {
        if (useftp) {
            return getOutDataFTP(filename);
        }
        if (streaming) {
            return getOutDataXOP(filename);
        }
        return getOutDataSOAP(filename);
    }

    private String getOutDataFTP(String filename) {
        edtFTPj ftp = edtFTPj.getInstance();
        if (ftp != null) {
            return ftp.download(path + filename);
        }
        return null;
    }

    private String getOutDataSOAP(String filename) {
        UploaderClient uploader = new UploaderClient(path);
        filename = uploader.downloadFile((new File(filename).getName()));
        if (filename != null) {
            return path + filename;
        }
        return null;
    }

    private String getOutDataXOP(String filename) {
        filename = FileStreamer.getFile(path, new File(filename).getName());
        if (filename != null) {
            return path + filename;
        }
        return null;
    }

    private String uncompressOutData(String filename) {
        File file = new File(filename);
        if (file.exists() && file.isFile()) {
            String name = file.getName().substring(0, file.getName().indexOf("."));
            return TrueZIP.uncompress(filename, path + name);
        }
        return null;
    }

    public TJobOutput runJob() {
        setProgress("Submitting job...", 0);
        try {
            TJobRun run = submitJob();
            if (run != null) {
                String outputname = run.getOutput().getName();
                setProperty("job.name", outputname);
                setProgress("Running job...", 1, true);
                int status = STATUS_UNAVAILABLE;
                while (true) {
                    if (getProperty("job.forget")) {
                        MiscUtils.waitFor(1000);
                        return null;
                    }
                    status = getJobStatus(outputname);
                    if ((status == STATUS_SUCCESS) || (status == STATUS_FAILURE) ||
                            (status == STATUS_KILLED)) {
                        break;
                    }
                    if (status == STATUS_UNAVAILABLE) {
                        failed("Service is unavailable!", false);
                        return null;
                    }
                    MiscUtils.waitFor(POLL_INTERVAL);
                }

                setProgress("Finalizing...", 2);
                if (status == STATUS_SUCCESS) {
                    TJobOutput output = getCompressedOutput(outputname);
                    if (output != null) {
                        setProgress("Receiving compressed output data...", 3);
                        String filename = getOutData(output.getFileName());
                        if (filename != null) {
                            setProgress("Uncompressing output data...", 4);
                            filename = uncompressOutData(filename);
                            if (filename != null) {
                                output.setFileName(filename);
                                finished(filename, null, output);
                                return output;
                            } else {
                                failed("Output data is not compressed!", false);
                            }
                        } else {
                            failed("Compressed output data is not received!", false);
                        }
                    } else {
                        failed("Service is unavailable!", false);
                    }
                } else {
                    TJobOutput output = getOutput(outputname);
                    if (output != null) {
                        if (status == STATUS_KILLED) {
                            failed("Job is being killed by the user!", false);
                        } else if (output.getStdErr().trim().length() != 0) {
                            failed(output.getStdErr(), false);
                        } else {
                            failed("Unknown error! Please consult your administrator!",
                                    false);
                        }
                    } else {
                        failed("Service is unavailable!", false);
                    }
                }
            } else {
                failed("Service is unavailable!", false);
            }
        } catch (SOAPFaultException ex) {
            logger.log(Level.WARNING, null, ex);
            failed(ex.getFault().getFaultString(), false);
        }
        return null;
    }

    public static TJobOutput runJob(ProgressableUI progress, String modelname,
            String inputname, String arguments, String description) {
        try {
            ClientProperties client = ClientProperties.getInstance();
            JobRunner runner = new JobRunner(client.getTmpDir(), modelname, inputname,
                    client.useFTP(), client.isStreamable(), arguments, description);
            runner.setProgressableUI(progress, 5);
            return runner.runJob();
        } catch (IOException ex) {
            logger.log(Level.WARNING, null, ex);
            progress.failed(ex.getMessage());
        }
        return null;
    }

    /**
     * @param args the command numDots arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        if ((args.length >= 2) && (args.length <= 4)) {
            args = Arrays.copyOf(args, 4);
            try {
                ClientProperties client = ClientProperties.getInstance();
                JobRunner runner = new JobRunner(client.getTmpDir(), args[0], args[1],
                        client.useFTP(), client.isStreamable(), args[2], args[3]);
                TJobOutput output = runner.runJob();
                if ((output != null) && output.getStatus().equals("success")) {
                    System.out.println("RESULT: 0");
                    return;
                }
            } catch (IOException ex) {
                logger.log(Level.SEVERE, null, ex);
                System.err.println("FAILURE: " + ex.getMessage());
            }
            System.out.println("RESULT: -1");
        } else {
            System.out.println("Please provide 2 arguments to this command!");
            System.out.println("2 arguments are listed in order as below:");
            System.out.println("1) <model name> - name of the model");
            System.out.println("2) <input name> - name of the input");
            System.out.println("Please provide 2 optional arguments to this command!");
            System.out.println("2 optional arguments are listed in order as below");
            System.out.println("1) <arguments> - arguments for the job");
            System.out.println("2) <description> - description for the job");
            System.out.println("RESULT: 1");
        }
    }
}
