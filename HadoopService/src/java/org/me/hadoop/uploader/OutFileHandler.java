/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.me.hadoop.uploader;

import java.util.ArrayList;
import org.me.hadoop.utils.CommonsIO;

/**
 *
 * @author Aaron
 */
public class OutFileHandler extends FileHandler {

    private ArrayList<String> segments;

    public OutFileHandler(String filename) {
        super(filename, -1);
        segments = null;
    }

    public OutFileHandler(String filename, ArrayList<String> segments) {
        super(filename, 0);
        this.segments = segments;
    }

    @Override
    protected ArrayList<String> getSegments() {
        return segments;
    }

    @Override
    public boolean hasMoreSegments() {
        if (segments != null) {
            if ((getIndex() != -1) && (getIndex() < segments.size() - 1)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public byte[] getSegment() {
        if (segments != null) {
            return getSegment(getIndex());
        }
        return CommonsIO.toByteArray(getFileName());
    }

    @Override
    public byte[] getSegment(int index) {
        if (segments != null) {
            if ((index != -1) && (index < segments.size())) {
                return CommonsIO.toByteArray(segments.get(index));
            }
        }
        return null;
    }

    @Override
    protected boolean putSegment(byte[] buffer) {
        return false;
    }

    @Override
    protected boolean putSegment(byte[] buffer, int index, String segment) {
        return false;
    }
}
