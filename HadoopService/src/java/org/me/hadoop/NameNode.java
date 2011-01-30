/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.me.hadoop;

import org.me.hadoop.utils.MiscUtils;

/**
 *
 * @author Aaron
 */
public abstract class NameNode {

    private String path;

    public NameNode(String path) {
        this.path = MiscUtils.appendHdfsSeparator(path);
    }

    public String getPath() {
        return path;
    }

    public abstract boolean isAvailable();

    public abstract boolean mkdir(String name);

    public abstract boolean rmdir(String name);

    public abstract boolean put(String name, String source);

    public abstract boolean get(String name, String destination);
}
