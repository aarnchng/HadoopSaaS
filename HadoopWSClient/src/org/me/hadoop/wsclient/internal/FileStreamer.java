/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.me.hadoop.wsclient.internal;

import com.sun.xml.ws.developer.JAXWSProperties;
import com.sun.xml.ws.developer.StreamingAttachmentFeature;
import com.sun.xml.ws.developer.StreamingDataHandler;
import java.io.File;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.soap.MTOMFeature;
import org.me.hadoop.service.HadoopService;
import org.me.hadoop.service.HadoopService_Service;
import org.me.hadoop.utils.MiscUtils;

/**
 *
 * @author Aaron
 */
public class FileStreamer {

    private static Logger logger = Logger.getLogger(FileStreamer.class.getName());

    public static String putFile(String path, String filename) {
        File file = new File(path, filename);
        if (file.exists() && file.isFile()) {
            filename = file.getName();
            try { // Call Web Service Operation
                HadoopService_Service service = MiscUtils.getHadoopService();
                HadoopService port = service.getHadoopServicePort(new MTOMFeature(),
                        new StreamingAttachmentFeature(path, true, 4000000L));
                Map<String, Object> ctxt = ((BindingProvider) port).getRequestContext();
                ctxt.put(JAXWSProperties.HTTP_CLIENT_STREAMING_CHUNK_SIZE, 8192);
                DataHandler handler = new DataHandler(new FileDataSource(file));
                // TODO process result here
                if (port.putFile(filename, handler)) {
                    return filename;
                }
            } catch (Exception ex) {
                logger.log(Level.WARNING, null, ex);
            }
        }
        return null;
    }

    public static String getFile(String path, String filename) {
        File file = new File(path, filename);
        filename = file.getName();
        try { // Call Web Service Operation
            HadoopService_Service service = MiscUtils.getHadoopService();
            HadoopService port = service.getHadoopServicePort(new MTOMFeature(),
                    new StreamingAttachmentFeature(path, true, 4000000L));
            Map<String, Object> ctxt = ((BindingProvider) port).getRequestContext();
            ctxt.put(JAXWSProperties.HTTP_CLIENT_STREAMING_CHUNK_SIZE, 8192);
            DataHandler handler = port.getFile(filename);
            // TODO process result here
            StreamingDataHandler streamhandler = (StreamingDataHandler) handler;
            streamhandler.moveTo(file);
            streamhandler.close();
            return filename;
        } catch (Exception ex) {
            logger.log(Level.WARNING, null, ex);
        }
        return null;
    }
}
