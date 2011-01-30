/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.me.hadoop.wsclient;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.ws.soap.SOAPFaultException;
import org.me.hadoop.service.HadoopService;
import org.me.hadoop.service.HadoopService_Service;
import org.me.hadoop.service.TJobInput;
import org.me.hadoop.uploader.UploaderClient;
import org.me.hadoop.utils.CommonsIO;
import org.me.hadoop.utils.MiscUtils;
import org.me.hadoop.utils.TrueZIP;
import org.me.hadoop.utils.edtFTPj;
import org.me.hadoop.wsclient.internal.ClientProperties;
import org.me.hadoop.wsclient.internal.FileStreamer;
import org.me.hadoop.wsclient.internal.RandomGenerator;
import org.netbeans.spi.wizard.ResultProgressHandle;

/**
 *
 * @author Aaron
 */
public class DataCreator extends Progressable {

    private static Logger logger = Logger.getLogger(DataCreator.class.getName());
    private String path;
    private String dirname;
    private boolean useftp;
    private boolean streaming;

    public DataCreator(String path, String dirname, boolean useftp, boolean streaming) {
        super();
        this.path = MiscUtils.appendFileSeparator(path);
        CommonsIO.createDirectory(this.path);
        this.dirname = dirname;
        this.useftp = useftp;
        this.streaming = streaming;
    }

    public DataCreator(String path, String dirname, boolean useftp) {
        this(path, dirname, useftp, false);
    }

    public DataCreator(String path, String dirname) {
        this(path, dirname, false, false);
    }

    private String compressInData() {
        File dir = new File(dirname);
        if (dir.exists() && dir.isDirectory()) {
            String filename = path + RandomGenerator.getRandomString();
            return TrueZIP.zip(dir.getAbsolutePath(), filename);
        }
        return null;
    }

    private String putInData(String filename) {
        if (useftp) {
            return putInDataFTP(filename);
        }
        if (streaming) {
            return putInDataXOP(filename);
        }
        return putInDataSOAP(filename);
    }

    private String putInDataFTP(String filename) {
        File file = new File(filename);
        if (file.exists() && file.isFile()) {
            edtFTPj ftp = edtFTPj.getInstance();
            if (ftp != null) {
                return ftp.upload(file.getAbsolutePath());
            }
        }
        return null;
    }

    private String putInDataSOAP(String filename) {
        File file = new File(filename);
        if (file.exists() && file.isFile()) {
            UploaderClient uploader = new UploaderClient(path);
            return uploader.uploadFile(file.getName());
        }
        return null;
    }

    private String putInDataXOP(String filename) {
        File file = new File(filename);
        if (file.exists() && file.isFile()) {
            return FileStreamer.putFile(path, file.getName());
        }
        return null;
    }

    private TJobInput addInData(String filename) throws SOAPFaultException {
        try { // Call Web Service Operation
            HadoopService_Service service = MiscUtils.getHadoopService();
            HadoopService port = service.getHadoopServicePort();
            // TODO process result here
            return port.addInput((new File(filename)).getName());
        } catch (SOAPFaultException ex) {
            throw ex;
        } catch (Exception ex) {
            logger.log(Level.WARNING, null, ex);
        }
        return null;
    }

    public TJobInput createInData() {
        String message = "\nUser Entries\n";
        message += "Directory: " + dirname + "\n";
        setProgress("Compressing input data...", 0);
        String filename = compressInData();
        if (filename != null) {
            setProgress("Sending compressed input data...", 1);
            filename = putInData(filename);
            if (filename != null) {
                setProgress("Finalizing...", 2);
                try {
                    TJobInput input = addInData(filename);
                    if (input != null) {
                        message += "\nResult\n";
                        message += "Input Name: " + input.getName() + "\n";
                        finished(input.getName(), message, input);
                        return input;
                    } else {
                        failed("Service is unavailable!", false);
                    }
                } catch (SOAPFaultException ex) {
                    logger.log(Level.WARNING, null, ex);
                    failed(ex.getFault().getFaultString(), false);
                }
            } else {
                failed("Compressed input data is not sent!", false);
            }
        } else {
            failed("Input data is not compressed!", false);
        }
        return null;
    }

    public static void createInData(Map entries, ResultProgressHandle progress) {
        try {
            ClientProperties client = ClientProperties.getInstance();
            String dirname = (String) entries.get("jInputDirChooser");
            DataCreator creator = new DataCreator(client.getTmpDir(), dirname,
                    client.useFTP(), client.isStreamable());
            creator.setProgressHandle(progress, 3);
            creator.createInData();
        } catch (IOException ex) {
            logger.log(Level.WARNING, null, ex);
            progress.failed(ex.getMessage(), false);
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        if (args.length == 1) {
            try {
                ClientProperties client = ClientProperties.getInstance();
                DataCreator creator = new DataCreator(client.getTmpDir(), args[0],
                        client.useFTP(), client.isStreamable());
                TJobInput input = creator.createInData();
                if (input != null) {
                    System.out.println("RESULT: 0");
                    return;
                }
            } catch (IOException ex) {
                logger.log(Level.SEVERE, null, ex);
                System.err.println("FAILURE: " + ex.getMessage());
            }
            System.out.println("RESULT: -1");
        } else {
            System.out.println("Please provide 1 arguments to this command!");
            System.out.println("1 arguments are listed in order as below:");
            System.out.println("1) <directory path> - path of the directory " +
                    "that contains input files");
            System.out.println("RESULT: 1");
        }
    }
}
