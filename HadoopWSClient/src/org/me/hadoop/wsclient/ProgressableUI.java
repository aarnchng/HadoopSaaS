/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.me.hadoop.wsclient;

/**
 *
 * @author Aaron
 */
public interface ProgressableUI {

    public void setProgress(String message, boolean indeterminate, int currentstep,
            int totalsteps);

    public boolean getProperty(String key);

    public String getProperty(String key, String value);

    public void setProperty(String key, String value);

    public void failed(String message);
}
