/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.me.hadoop.wsclient;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.ws.soap.SOAPFaultException;
import org.me.hadoop.service.HadoopService;
import org.me.hadoop.service.HadoopService_Service;
import org.me.hadoop.service.TJobInput;
import org.me.hadoop.service.TJobModelHeader;
import org.me.hadoop.service.TJobOutput;
import org.me.hadoop.service.TJobRun;
import org.me.hadoop.utils.MiscUtils;
import org.me.hadoop.wsclient.internal.ClientProperties;
import org.me.hadoop.wsclient.internal.FileStreamer;
import php.java.bridge.JavaBridgeRunner;

/**
 *
 * @author Aaron
 */
public class WSClientRunner {

    private static Logger logger = Logger.getLogger(WSClientRunner.class.getName());
    private static JavaBridgeRunner runner;
    private SOAPFaultException exception = null;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        try {
            ClientProperties client = ClientProperties.getInstance();
            runner = JavaBridgeRunner.getInstance(client.getRunnerPort());
            runner.waitFor();
            System.exit(0);
        } catch (IOException ex) {
            logger.log(Level.WARNING, null, ex);
        } catch (InterruptedException ex) {
            logger.log(Level.WARNING, null, ex);
        }
    }

    public Collection<TJobModelHeader> listModels() {
        try { // Call Web Service Operation
            HadoopService_Service service = MiscUtils.getHadoopService();
            HadoopService port = service.getHadoopServicePort();
            // TODO process result here
            return port.listModels();
        } catch (Exception ex) {
            logger.log(Level.WARNING, null, ex);
        }
        return null;
    }

    public String putInData(String filename) {
        File file = new File(filename);
        if (file.exists() && file.isFile()) {
            return FileStreamer.putFile(file.getParent(), file.getName());
        }
        return null;
    }

    public TJobInput addInData(String filename) throws SOAPFaultException {
        try { // Call Web Service Operation
            HadoopService_Service service = MiscUtils.getHadoopService();
            HadoopService port = service.getHadoopServicePort();
            // TODO process result here
            return port.addInput((new File(filename)).getName());
        } catch (SOAPFaultException ex) {
            exception = ex;
            throw ex;
        } catch (Exception ex) {
            logger.log(Level.WARNING, null, ex);
        }
        return null;
    }

    public TJobRun submitJob(String modelname, String inputname, String description,
            String arguments) throws SOAPFaultException {
        try { // Call Web Service Operation
            HadoopService_Service service = MiscUtils.getHadoopService();
            HadoopService port = service.getHadoopServicePort();
            // TODO process result here
            return port.runJob(modelname, inputname, description, arguments);
        } catch (SOAPFaultException ex) {
            exception = ex;
            throw ex;
        } catch (Exception ex) {
            logger.log(Level.WARNING, null, ex);
        }
        return null;
    }

    public boolean killJob(String inputname, String outputname) {
        try { // Call Web Service Operation
            HadoopService_Service service = MiscUtils.getHadoopService();
            HadoopService port = service.getHadoopServicePort();
            // TODO process result here
            return port.killJob(inputname, outputname);
        } catch (Exception ex) {
            logger.log(Level.WARNING, null, ex);
        }
        return false;
    }

    public TJobOutput getOutput(String inputname, String outputname) throws SOAPFaultException {
        try { // Call Web Service Operation
            HadoopService_Service service = MiscUtils.getHadoopService();
            HadoopService port = service.getHadoopServicePort();
            // TODO process result here
            return port.getOutput(inputname, outputname);
        } catch (SOAPFaultException ex) {
            exception = ex;
            throw ex;
        } catch (Exception ex) {
            logger.log(Level.WARNING, null, ex);
        }
        return null;
    }

    public TJobOutput getCompressedWCOutput(String inputname, String outputname,
            String wildcard) throws SOAPFaultException {
        try { // Call Web Service Operation
            HadoopService_Service service = MiscUtils.getHadoopService();
            HadoopService port = service.getHadoopServicePort();
            // TODO process result here
            return port.getCompressedWCOutput(inputname, outputname, wildcard);
        } catch (SOAPFaultException ex) {
            exception = ex;
            throw ex;
        } catch (Exception ex) {
            logger.log(Level.WARNING, null, ex);
        }
        return null;
    }

    public String getOutData(String path, String filename) {
        path = MiscUtils.appendFileSeparator(path);
        filename = FileStreamer.getFile(path, new File(filename).getName());
        if (filename != null) {
            return path + filename;
        }
        return null;
    }

    public boolean removeInput(String inputname) {
        try { // Call Web Service Operation
            HadoopService_Service service = MiscUtils.getHadoopService();
            HadoopService port = service.getHadoopServicePort();
            // TODO process result here
            return port.removeInput(inputname);
        } catch (Exception ex) {
            logger.log(Level.WARNING, null, ex);
        }
        return false;
    }

    public boolean removeOutput(String inputname, String outputname) {
        try { // Call Web Service Operation
            HadoopService_Service service = MiscUtils.getHadoopService();
            HadoopService port = service.getHadoopServicePort();
            // TODO process result here
            return port.removeOutput(inputname, outputname);
        } catch (Exception ex) {
            logger.log(Level.WARNING, null, ex);
        }
        return false;
    }

    public SOAPFaultException getException() {
        SOAPFaultException ex = exception;
        exception = null;
        return ex;
    }
}
