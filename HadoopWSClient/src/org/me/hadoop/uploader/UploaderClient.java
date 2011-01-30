/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.me.hadoop.uploader;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.ws.soap.MTOMFeature;
import org.me.hadoop.service.HadoopService;
import org.me.hadoop.service.HadoopService_Service;
import org.me.hadoop.service.TFileSegment;
import org.me.hadoop.utils.CommonsIO;
import org.me.hadoop.utils.MiscUtils;

/**
 *
 * @author Aaron
 */
public class UploaderClient {

    public static final int MAX_LENGTH = 20480000;
    private static Logger logger = Logger.getLogger(UploaderClient.class.getName());
    private String path;
    private String tmppath;

    public UploaderClient(String path) {
        this.path = MiscUtils.appendFileSeparator(path);
        tmppath = MiscUtils.appendFileSeparator(this.path + "segments");
        CommonsIO.createDirectory(tmppath);
    }

    public String uploadFile(String filename) {
        File file = new File(path + filename);
        if (file.exists() && file.isFile()) {
            FileHandler handler = putFile(file);
            if (handler != null) {
                filename = file.getName();
                boolean result = false;

                try { // Call Web Service Operation
                    HadoopService_Service service = MiscUtils.getHadoopService();
                    HadoopService port = service.getHadoopServicePort(new MTOMFeature());
                    // TODO process result here
                    do {
                        if (result) {
                            handler.incIndex();
                        }
                        if (handler.hasMoreSegments()) {
                            result = port.uploadFile(filename, handler.getIndex(),
                                    handler.getSegment(), false);
                        } else {
                            result = port.uploadFile(filename, handler.getIndex(),
                                    handler.getSegment(), true);
                        }
                    } while (handler.hasMoreSegments() && result);

                    if (!result) {
                        filename = null;
                    }
                    return filename;
                } catch (Exception ex) {
                    logger.log(Level.WARNING, null, ex);
                }
            }
        }
        return null;
    }

    private OutFileHandler putFile(File file) {
        if (file.exists() && file.isFile()) {
            OutFileHandler handler;
            if (file.length() <= MAX_LENGTH) {
                // OutFileHandler With No Segments
                handler = new OutFileHandler(file.getAbsolutePath());
            } else {
                // OutFileHandler With Segments
                ArrayList<String> segments = splitFile(file);
                if (segments != null) {
                    handler = new OutFileHandler(file.getAbsolutePath(), segments);
                } else {
                    handler = null;
                }
            }
            return handler;
        }
        return null;
    }

    private ArrayList<String> splitFile(File file) {
        if (file.exists() && file.isFile()) {
            ArrayList<String> segments = new ArrayList<String>();
            try {
                FileInputStream fis = new FileInputStream(file);
                BufferedInputStream bis = new BufferedInputStream(fis);
                byte[] buffer;
                while (true) {
                    buffer = new byte[MAX_LENGTH];
                    int length = bis.read(buffer, 0, MAX_LENGTH);
                    if (length == -1) {
                        break;
                    } else if (length < MAX_LENGTH) {
                        buffer = Arrays.copyOf(buffer, length);
                    }

                    String segment = tmppath + file.getName() + "_seg" +
                            segments.size();
                    if (CommonsIO.write(buffer, segment)) {
                        segments.add(segment);
                    } else {
                        segments = null;
                        break;
                    }
                }
                bis.close();
                fis.close();
                return segments;
            } catch (FileNotFoundException ex) {
                logger.log(Level.WARNING, null, ex);
            } catch (IOException ex) {
                logger.log(Level.WARNING, null, ex);
            }
        }
        return null;
    }

    public String downloadFile(String filename) {
        File file = new File(path + filename);
        filename = file.getName();
        InFileHandler handler = null;
        boolean result = false;

        try { // Call Web Service Operation
            HadoopService_Service service = MiscUtils.getHadoopService();
            HadoopService port = service.getHadoopServicePort(new MTOMFeature());
            // TODO process result here
            do {
                TFileSegment segment = null;
                try {
                    segment = port.downloadFile(filename);
                } catch (Exception ex) {
                    logger.log(Level.WARNING, null, ex);
                }
                if (segment != null) {
                    result = segment.isNext();
                    handler = getFile(handler, file, segment.getIndex(),
                            segment.getBuffer(), (!result));
                } else {
                    handler = null;
                }
            } while ((handler != null) && result);

            if (handler == null) {
                filename = null;
            }
            return filename;
        } catch (Exception ex) {
            logger.log(Level.WARNING, null, ex);
        }
        return null;
    }

    private InFileHandler getFile(InFileHandler handler, File file, int index,
            byte[] buffer, boolean merge) {
        if (handler != null) {
            // Existing InFileHandler With Segments
            String segment = tmppath + file.getName() + "_seg" + index;
            if (handler.putSegment(buffer, index, segment)) {
                if (merge) {
                    ArrayList<String> segments = handler.getSegments();
                    if ((segments == null) || (!mergeFile(handler.getFileName(),
                            segments))) {
                        handler = null;
                    }
                }
            } else {
                handler = null;
            }
        } else {
            handler = new InFileHandler(file.getAbsolutePath());
            if (merge) {
                // InFileHandler With No Segments
                if (!handler.putSegment(buffer)) {
                    handler = null;
                }
            } else {
                // InFileHandler With Segments
                String segment = tmppath + file.getName() + "_seg" + index;
                if (!handler.putSegment(buffer, index, segment)) {
                    handler = null;
                }
            }
        }
        return handler;
    }

    private boolean mergeFile(String filename, ArrayList<String> segments) {
        if ((segments != null) && (segments.size() != 0)) {
            boolean result = false;
            for (int i = 0; i < segments.size(); i++) {
                byte[] buffer = CommonsIO.toByteArray(segments.get(i));
                if (buffer != null) {
                    if (i == 0) {
                        result = CommonsIO.write(buffer, filename);
                    } else {
                        result = CommonsIO.append(buffer, filename);
                    }
                } else {
                    result = false;
                }

                if (!result) {
                    break;
                }
            }
            return result;
        }
        return false;
    }
}
