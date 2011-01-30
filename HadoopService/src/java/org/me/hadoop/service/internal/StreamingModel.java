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
public class StreamingModel extends JobModel {

    public static final String[] LANGUAGES = {"python"};

    public StreamingModel(String path) throws FileNotFoundException,
            IOException {
        super(path);
    }

    @Override
    public boolean validate() {
        boolean result = super.validate();
        if (result) {
            result = false;
            for (String language : LANGUAGES) {
                if (getLanguage().equals(language)) {
                    result = true;
                    break;
                }
            }
        }
        result = (result && (getMapper() != null));
        result = (result && (new File(getMapperPath())).exists());
        if (result && (!getReducer().equals(""))) {
            result = (result && (new File(getReducerPath())).exists());
        }
        return result;
    }

    public String getLanguage() {
        return super.getProperty("model.language", "none").toLowerCase();
    }

    public String getMapperPath() {
        return super.getPath() + getMapper();
    }

    public String getMapper() {
        return super.getProperty("model.mapper");
    }

    public String getReducerPath() {
        if (getReducer().equals("")) {
            return "none";
        }
        return super.getPath() + getReducer();
    }

    public String getReducer() {
        return super.getProperty("model.reducer", "");
    }
}
