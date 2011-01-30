/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.me.hadoop.service.internal;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 *
 * @author Aaron
 */
public class JarModel extends JobModel {

    public JarModel(String path) throws FileNotFoundException, IOException {
        super(path);
    }

    @Override
    public boolean validate() {
        boolean result = super.validate();
        result = (result && (getJar() != null));
        result = (result && (new File(getJarPath())).exists());
        result = (result && (getMainClass() != null));
        return result;
    }

    public String getJarPath() {
        return super.getPath() + getJar();
    }

    public String getJar() {
        return super.getProperty("model.jar");
    }

    public String getMainClass() {
        return super.getProperty("model.mainclass");
    }
}
