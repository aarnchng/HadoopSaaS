/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.me.hadoop.uploader;

import java.io.File;
import java.util.ArrayList;

/**
 *
 * @author Aaron
 */
public abstract class FileHandler {

    private String filename;
    private int index;

    public FileHandler(String filename, int index) {
        this.filename = filename;
        this.index = index;
    }

    public String getFileName() {
        return filename;
    }

    public String getName() {
        return (new File(filename)).getName();
    }

    public int getIndex() {
        return index;
    }

    protected void incIndex() {
        index += 1;
    }

    protected abstract ArrayList<String> getSegments();

    public abstract boolean hasMoreSegments();

    public abstract byte[] getSegment();

    public abstract byte[] getSegment(int index);

    protected abstract boolean putSegment(byte[] buffer);

    protected abstract boolean putSegment(byte[] buffer, int index,
            String segment);
}
