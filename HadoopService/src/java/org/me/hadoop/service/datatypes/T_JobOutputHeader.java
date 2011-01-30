/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.me.hadoop.service.datatypes;

import org.me.hadoop.service.internal.JobOutput;

/**
 *
 * @author Aaron
 */
public class T_JobOutputHeader {

    private String name;

    public T_JobOutputHeader() {
        name = "";
    }

    public T_JobOutputHeader(JobOutput output) {
        name = output.getName();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
