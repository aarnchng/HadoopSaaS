/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.me.hadoop.basic;

import java.security.Permission;

/**
 *
 * @author Aaron
 */
public class JobSecurityManager extends SecurityManager {

    public JobSecurityManager() {
        super();
    }

    @Override
    public void checkExit(int status) {
        Thread thread = Thread.currentThread();
        if ((thread != null) && (thread instanceof BasicThread)) {
            BasicThread bthread = (BasicThread) thread;
            bthread.setExitCode(status);
            throw new SecurityException();
        }
    }

    @Override
    public void checkPermission(Permission perm) {
    }
}
