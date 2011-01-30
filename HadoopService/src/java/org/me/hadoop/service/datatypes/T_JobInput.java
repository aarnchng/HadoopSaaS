/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.me.hadoop.service.datatypes;

import org.me.hadoop.service.internal.JobInput;

/**
 *
 * @author Aaron
 */
public class T_JobInput {

    private String name;

    public T_JobInput() {
        name = "";
    }

    public T_JobInput(JobInput input) {
        name = input.getName();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
