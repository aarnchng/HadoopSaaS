/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.me.hadoop;

import org.me.hadoop.service.internal.JobRun;

/**
 *
 * @author Aaron
 */
public abstract class JobTracker {

    private NameNode namenode;

    public JobTracker(NameNode namenode) {
        this.namenode = namenode;
    }

    public NameNode getNameNode() {
        return namenode;
    }

    public abstract boolean isAvailable();

    public abstract boolean kill(String name);

    public abstract void run(JobRun run);
}
