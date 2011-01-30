/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.me.hadoop.uploader;

import java.util.ArrayList;
import java.util.HashMap;
import org.me.hadoop.utils.CommonsIO;

/**
 *
 * @author Aaron
 */
public class InFileHandler extends FileHandler {

    private HashMap<Integer, String> buffers;

    public InFileHandler(String filename) {
        super(filename, -1);
        buffers = new HashMap<Integer, String>();
    }

    @Override
    protected ArrayList<String> getSegments() {
        if ((buffers != null) && (!buffers.isEmpty())) {
            ArrayList<String> segments = new ArrayList<String>();
            for (int i = 0; i < buffers.size(); i++) {
                if (buffers.get(i) == null) {
                    segments = null;
                    break;
                }
                segments.add(buffers.get(i));
            }
            return segments;
        }
        return null;
    }

    @Override
    public boolean hasMoreSegments() {
        return false;
    }

    @Override
    public byte[] getSegment() {
        return null;
    }

    @Override
    public byte[] getSegment(int index) {
        return null;
    }

    @Override
    protected boolean putSegment(byte[] buffer) {
        return CommonsIO.write(buffer, getFileName());
    }

    @Override
    protected boolean putSegment(byte[] buffer, int index, String segment) {
        if (CommonsIO.write(buffer, segment)) {
            buffers.put(index, segment);
            return true;
        }
        return false;
    }
}
