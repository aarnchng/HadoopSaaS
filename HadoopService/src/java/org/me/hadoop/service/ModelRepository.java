/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.me.hadoop.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.me.hadoop.service.internal.JobModel;
import org.me.hadoop.utils.CommonsIO;
import org.me.hadoop.utils.MiscUtils;
import org.me.hadoop.utils.TrueZIP;

/**
 *
 * @author Aaron
 */
public class ModelRepository {

    private static final SimpleDateFormat sdf =
            new SimpleDateFormat("'model_'yyyyMMddHHmmssSSS");
    private static Logger logger = Logger.getLogger(ModelRepository.class.getName());
    private String path;
    private HashMap<String, JobModel> models;

    public ModelRepository(String path) {
        this.path = MiscUtils.appendFileSeparator(path);
        CommonsIO.createDirectory(path);
        models = new HashMap<String, JobModel>();
        initialise();
    }

    private boolean initialise() {
        File dir = new File(path);
        if (dir.exists() && dir.isDirectory()) {
            load(dir.listFiles());
            return true;
        }
        return false;
    }

    private void load(File[] dirs) {
        if (dirs != null) {
            for (File dir : dirs) {
                if (dir.exists() && dir.isDirectory()) {
                    try {
                        addModel(JobModel.getInstance(dir.getAbsolutePath()));
                    } catch (FileNotFoundException ex) {
                        logger.log(Level.WARNING, null, ex);
                    } catch (IOException ex) {
                        logger.log(Level.WARNING, null, ex);
                    }
                }
            }
        }
    }

    public boolean refresh() {
        if (!models.isEmpty()) {
            models.clear();
        }
        return initialise();
    }

    public Collection<JobModel> getModels() {
        return models.values();
    }

    public JobModel getModel(String key) {
        return models.get(key);
    }

    public JobModel addModel(String filename) {
        File file = new File(filename);
        if (file.exists() && file.isFile()) {
            String dirname = path + sdf.format(new Date());
            dirname = TrueZIP.uncompress(file.getAbsolutePath(), dirname);
            if (dirname != null) {
                File dir = new File(dirname);
                if (dir.exists() && dir.isDirectory()) {
                    try {
                        return addModel(JobModel.getInstance(dir.getAbsolutePath()));
                    } catch (FileNotFoundException ex) {
                        logger.log(Level.WARNING, null, ex);
                    } catch (IOException ex) {
                        logger.log(Level.WARNING, null, ex);
                    }
                }
            }
        }
        return null;
    }

    public JobModel addModel(JobModel model) {
        if (model != null) {
            String key;
            for (int i = 0;; i++) {
                key = model.getKey() + (i == 0 ? "" : String.valueOf(i));
                if (!models.containsKey(key)) {
                    break;
                }
            }
            model.setKey(key);
            models.put(key, model);
            return model;
        }
        return null;
    }

    public boolean removeModel(String key) {
        if (models.containsKey(key)) {
            return removeModel(models.get(key));
        }
        return false;
    }

    public boolean removeModel(JobModel model) {
        if (model != null) {
            File dir = new File(model.getPath());
            if (dir.exists() && dir.isDirectory()) {
                if (model.remove()) {
                    models.remove(model.getKey());
                    return true;
                }
            }
        }
        return false;
    }
}
