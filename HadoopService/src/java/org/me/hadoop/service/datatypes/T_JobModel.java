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
public class T_JobModel {

    private String key;
    private String name;
    private String version;
    private String author;
    private String method;
    private String description;

    public T_JobModel() {
        key = "";
        name = "";
        version = "";
        author = "";
        method = "";
        description = "";
    }

    public T_JobModel(JobModel model) {
        key = model.getKey();
        name = model.getName();
        version = model.getVersion();
        author = model.getAuthor();
        method = model.getMethod();
        description = model.getDescription();
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

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
