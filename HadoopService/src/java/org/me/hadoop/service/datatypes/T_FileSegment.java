/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.me.hadoop.service.datatypes;

import org.me.hadoop.uploader.FileHandler;

/**
 *
 * @author Aaron
 */
public class T_FileSegment {

    private String filename;
    private int index;
    private byte[] buffer;
    private boolean next;

    public T_FileSegment() {
        filename = "";
        index = -1;
        buffer = null;
        next = false;
    }

    public T_FileSegment(FileHandler handler) {
        filename = handler.getName();
        index = handler.getIndex();
        buffer = handler.getSegment();
        next = handler.hasMoreSegments();
    }

    public String getFileName() {
        return filename;
    }

    public void setFileName(String filename) {
        this.filename = filename;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public byte[] getBuffer() {
        return buffer;
    }

    public void setBuffer(byte[] buffer) {
        this.buffer = buffer;
    }

    public boolean getNext() {
        return next;
    }

    public void setNext(boolean next) {
        this.next = next;
    }
}
