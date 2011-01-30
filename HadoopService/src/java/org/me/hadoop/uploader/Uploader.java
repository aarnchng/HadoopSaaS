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
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.me.hadoop.utils.CommonsIO;
import org.me.hadoop.utils.MiscUtils;

/**
 *
 * @author Aaron
 */
public class Uploader {

    public static final int MAX_LENGTH = 20480000;
    private static Logger logger = Logger.getLogger(Uploader.class.getName());
    private String path;
    private String tmppath;
    private HashMap<String, FileHandler> handlers;

    public Uploader(String path) {
        this.path = MiscUtils.appendFileSeparator(path);
        tmppath = MiscUtils.appendFileSeparator(path + "segments");
        CommonsIO.createDirectory(tmppath);
        handlers = new HashMap<String, FileHandler>();
    }

    public OutFileHandler getFile(String filename) {
        File file = new File(path + filename);
        if (file.exists() && file.isFile()) {
            OutFileHandler handler;
            if (file.length() <= MAX_LENGTH) {
                // OutFileHandler With No Segments
                handler = new OutFileHandler(file.getAbsolutePath());
            } else {
                if (handlers.containsKey("get_" + file.getName())) {
                    // Existing OutFileHandler With Segments
                    handler = (OutFileHandler) handlers.get("get_" +
                            file.getName());
                    handler.incIndex();
                    if (!handler.hasMoreSegments()) {
                        handlers.remove("get_" + file.getName());
                    }
                } else {
                    // OutFileHandler With Segments
                    ArrayList<String> segments = splitFile(file);
                    if (segments != null) {
                        handler = new OutFileHandler(file.getAbsolutePath(),
                                segments);
                        handlers.put("get_" + file.getName(), handler);
                    } else {
                        handler = null;
                    }
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

    public InFileHandler putFile(String filename, byte[] segment) {
        return putFile(filename, -1, segment, true);
    }

    public InFileHandler putFile(String filename, int index, byte[] segment) {
        return putFile(filename, index, segment, false);
    }

    public InFileHandler putFile(String filename, int index, byte[] buffer,
            boolean merge) {
        InFileHandler handler;
        File file = new File(path + filename);
        if (handlers.containsKey("put_" + file.getName())) {
            // Existing InFileHandler With Segments
            handler = (InFileHandler) handlers.get("put_" + file.getName());
            String segment = tmppath + file.getName() + "_seg" + index;
            if (handler.putSegment(buffer, index, segment)) {
                if (merge) {
                    ArrayList<String> segments = handler.getSegments();
                    if ((segments != null) && mergeFile(file.getAbsolutePath(),
                            segments)) {
                        handlers.remove("put_" + file.getName());
                    } else {
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
                if (handler.putSegment(buffer, index, segment)) {
                    handlers.put("put_" + file.getName(), handler);
                } else {
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
