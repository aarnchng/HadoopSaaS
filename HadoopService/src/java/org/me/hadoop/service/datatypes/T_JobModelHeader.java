/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.me.hadoop.service.datatypes;

import org.me.hadoop.service.internal.JobModel;

/**
 *
 * @author Aaron
 */
public class T_JobModelHeader {

    private String key;
    private String name;
    private String version;
    private String author;

    public T_JobModelHeader() {
        key = "";
        name = "";
        version = "";
        author = "";
    }

    public T_JobModelHeader(JobModel model) {
        key = model.getKey();
        name = model.getName();
        version = model.getVersion();
        author = model.getAuthor();
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}
