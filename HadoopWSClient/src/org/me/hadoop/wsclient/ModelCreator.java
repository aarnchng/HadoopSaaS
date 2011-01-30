/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.me.hadoop.wsclient;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.ws.soap.SOAPFaultException;
import org.me.hadoop.service.HadoopService;
import org.me.hadoop.service.HadoopService_Service;
import org.me.hadoop.service.TJobModel;
import org.me.hadoop.uploader.UploaderClient;
import org.me.hadoop.utils.CommonsIO;
import org.me.hadoop.utils.MiscUtils;
import org.me.hadoop.utils.TrueZIP;
import org.me.hadoop.utils.edtFTPj;
import org.me.hadoop.wsclient.internal.ClientProperties;
import org.me.hadoop.wsclient.internal.FileStreamer;
import org.me.hadoop.wsclient.internal.PropertiesWriter;
import org.me.hadoop.wsclient.internal.RandomGenerator;
import org.netbeans.spi.wizard.ResultProgressHandle;

/**
 *
 * @author Aaron
 */
public class ModelCreator extends Progressable {

    private static Logger logger = Logger.getLogger(ModelCreator.class.getName());
    private String path;
    private boolean useftp;
    private boolean streaming;
    private String dirname;
    private PropertiesWriter writer;

    public ModelCreator(String path, boolean useftp, boolean streaming) {
        super();
        this.path = MiscUtils.appendFileSeparator(path);
        CommonsIO.createDirectory(this.path);
        this.useftp = useftp;
        this.streaming = streaming;
        dirname = MiscUtils.appendFileSeparator(this.path + RandomGenerator.getRandomString());
        writer = new PropertiesWriter(dirname, "model.properties");
    }

    public ModelCreator(String path, boolean useftp) {
        this(path, useftp, false);
    }

    public ModelCreator(String path) {
        this(path, false, false);
    }

    private String getName() {
        return writer.getProperty("model.name", "");
    }

    public void setName(String name) {
        if ((name != null) && (name.trim().length() != 0)) {
            writer.setProperty("model.name", name);
        }
    }

    private String getMethod() {
        return writer.getProperty("model.method", "");
    }

    public void setMethod(String method) {
        if ((method != null) && (method.trim().length() != 0)) {
            writer.setProperty("model.method", method);
        }
    }

    private String getVersion() {
        return writer.getProperty("model.version", "");
    }

    public void setVersion(String version) {
        if ((version != null) && (version.trim().length() != 0)) {
            writer.setProperty("model.version", version);
        }
    }

    private String getAuthor() {
        return writer.getProperty("model.author", "");
    }

    public void setAuthor(String author) {
        if ((author != null) && (author.trim().length() != 0)) {
            writer.setProperty("model.author", author);
        }
    }

    private String getDescription() {
        return writer.getProperty("model.description", "");
    }

    public void setDescription(String description) {
        if ((description != null) && (description.trim().length() != 0)) {
            writer.setProperty("model.description", description);
        }
    }

    private String getJar() {
        return writer.getProperty("model.jar", "");
    }

    public void setJar(String jar) {
        if ((jar != null) && (jar.trim().length() != 0)) {
            writer.setProperty("model.jar", jar);
        }
    }

    private String getMainClass() {
        return writer.getProperty("model.mainclass", "");
    }

    public void setMainClass(String mainclass) {
        if ((mainclass != null) && (mainclass.trim().length() != 0)) {
            writer.setProperty("model.mainclass", mainclass);
        }
    }

    private String getLanguage() {
        return writer.getProperty("model.language", "");
    }

    public void setLanguage(String language) {
        if ((language != null) && (language.trim().length() != 0)) {
            writer.setProperty("model.language", language);
        }
    }

    private String getMapper() {
        return writer.getProperty("model.mapper", "");
    }

    public void setMapper(String mapper) {
        if ((mapper != null) && (mapper.trim().length() != 0)) {
            writer.setProperty("model.mapper", mapper);
        }
    }

    private String getReducer() {
        return writer.getProperty("model.reducer", "");
    }

    public void setReducer(String reducer) {
        if ((reducer != null) && (reducer.trim().length() != 0)) {
            writer.setProperty("model.reducer", reducer);
        }
    }

    private boolean saveModel() {
        try {
            writer.save();
            return true;
        } catch (FileNotFoundException ex) {
            logger.log(Level.WARNING, null, ex);
        } catch (IOException ex) {
            logger.log(Level.WARNING, null, ex);
        }
        return false;
    }

    private String compressModel() {
        File dir = new File(dirname);
        if (dir.exists() && dir.isDirectory()) {
            return TrueZIP.zip(dir.getAbsolutePath(), path + dir.getName());
        }
        return null;
    }

    private String putModel(String filename) {
        if (useftp) {
            return putModelFTP(filename);
        }
        if (streaming) {
            return putModelXOP(filename);
        }
        return putModelSOAP(filename);
    }

    private String putModelFTP(String filename) {
        File file = new File(filename);
        if (file.exists() && file.isFile()) {
            edtFTPj ftp = edtFTPj.getInstance();
            if (ftp != null) {
                return ftp.upload(file.getAbsolutePath());
            }
        }
        return null;
    }

    private String putModelSOAP(String filename) {
        File file = new File(filename);
        if (file.exists() && file.isFile()) {
            UploaderClient uploader = new UploaderClient(path);
            return uploader.uploadFile(file.getName());
        }
        return null;
    }

    private String putModelXOP(String filename) {
        File file = new File(filename);
        if (file.exists() && file.isFile()) {
            return FileStreamer.putFile(path, file.getName());
        }
        return null;
    }

    private TJobModel addModel(String filename) throws SOAPFaultException {
        try { // Call Web Service Operation
            HadoopService_Service service = MiscUtils.getHadoopService();
            HadoopService port = service.getHadoopServicePort();
            // TODO process result here
            return port.addModel((new File(filename)).getName());
        } catch (SOAPFaultException ex) {
            throw ex;
        } catch (Exception ex) {
            logger.log(Level.WARNING, null, ex);
        }
        return null;
    }

    public TJobModel createModel(String odirname) {
        String message = "\nUser Entries\n";
        message += "Model Name: " + getName() + "\n";
        message += "Method Name: " + getMethod() + "\n";
        message += "Version Name: " + getVersion() + "\n";
        message += "Author Name: " + getAuthor() + "\n";
        message += "Description: " + getDescription() + "\n";
        message += "Directory: " + odirname + "\n";
        if (getMethod().equals("hadoop.jar")) {
            message += "Jar File Name: " + getJar() + "\n";
            message += "Main Class: " + getMainClass() + "\n";
        } else if (getMethod().equals("hadoop.streaming")) {
            message += "Language: " + getLanguage() + "\n";
            message += "Mapper File: " + getMapper() + "\n";
            message += "Reducer File: " + getReducer() + "\n";
        } else {
            failed("Method name is invalid!", false);
            return null;
        }

        if (CommonsIO.copyDirectory(odirname, dirname)) {
            setProgress("Saving model...", 0);
            if (saveModel()) {
                setProgress("Compressing model...", 1);
                String filename = compressModel();
                if (filename != null) {
                    setProgress("Sending compressed model...", 2);
                    filename = putModel(filename);
                    if (filename != null) {
                        setProgress("Finalizing...", 3);
                        try {
                            TJobModel model = addModel(filename);
                            if (model != null) {
                                message += "\nResult\n";
                                message += "Model Name: " + model.getName() + "\n";
                                finished(model.getName(), message, model);
                                return model;
                            } else {
                                failed("Service is unavailable!", false);
                            }
                        } catch (SOAPFaultException ex) {
                            logger.log(Level.WARNING, null, ex);
                            failed(ex.getFault().getFaultString(), false);
                        }
                    } else {
                        failed("Compressed model is not sent!", false);
                    }
                } else {
                    failed("Model is not compressed!", false);
                }
            } else {
                failed("Model is not saved!", false);
            }
        } else {
            failed("Unable to copy required files!", false);
        }
        return null;
    }

    public static void createModel(Map entries, ResultProgressHandle progress) {
        try {
            ClientProperties client = ClientProperties.getInstance();
            ModelCreator creator = new ModelCreator(client.getTmpDir(),
                    client.useFTP(), client.isStreamable());
            creator.setName((String) entries.get("jNameTextField"));
            creator.setMethod((String) entries.get("jMethodComboBox"));
            creator.setVersion((String) entries.get("jVersionTextField"));
            creator.setAuthor((String) entries.get("jAuthorTextField"));
            creator.setDescription((String) entries.get("jDescriptionTextField"));
            creator.setJar((String) entries.get("jJarFileComboBox"));
            creator.setMainClass((String) entries.get("jMainClassComboBox"));
            creator.setLanguage((String) entries.get("jLanguageComboBox"));
            creator.setMapper((String) entries.get("jMapperFileComboBox"));
            creator.setReducer((String) entries.get("jReducerFileComboBox"));
            creator.setProgressHandle(progress, 4);
            creator.createModel((String) entries.get("jModelDirChooser"));
        } catch (IOException ex) {
            logger.log(Level.WARNING, null, ex);
            progress.failed(ex.getMessage(), false);
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        if ((args[0].equals("hadoop.jar") && (args.length >= 5) && (args.length <= 8)) ||
                (args[0].equals("hadoop.streaming") && (args.length >= 6) && (args.length <= 9))) {
            args = Arrays.copyOf(args, 9);
            try {
                ClientProperties client = ClientProperties.getInstance();
                ModelCreator creator = new ModelCreator(client.getTmpDir(),
                        client.useFTP(), client.isStreamable());
                creator.setMethod(args[0]);
                creator.setName(args[1]);
                if (args[0].equals("hadoop.jar")) {
                    creator.setJar(args[3]);
                    creator.setMainClass(args[4]);
                    creator.setVersion(args[5]);
                    creator.setAuthor(args[6]);
                    creator.setDescription(args[7]);
                } else if (args[0].equals("hadoop.streaming")) {
                    creator.setLanguage(args[3]);
                    creator.setMapper(args[4]);
                    creator.setReducer(args[5].equals("none") ? "" : args[5]);
                    creator.setVersion(args[6]);
                    creator.setAuthor(args[7]);
                    creator.setDescription(args[8]);
                }
                TJobModel model = creator.createModel(args[2]);
                if (model != null) {
                    System.out.println("RESULT: 0");
                    return;
                }
            } catch (IOException ex) {
                logger.log(Level.SEVERE, null, ex);
                System.err.println("FAILURE: " + ex.getMessage());
            }
            System.out.println("RESULT: -1");
        } else {
            System.out.println("Please provide 3 basic arguments to this command!");
            System.out.println("3 basic arguments are listed in order as below:");
            System.out.println("1) <method name> - 'hadoop.jar' OR 'hadoop.streaming'");
            System.out.println("2) <model name> - name of the model");
            System.out.println("3) <directory path> - path of the directory that " +
                    "contains required files");
            System.out.println("Please provide 2 advanced arguments to this command " +
                    "for the method 'hadoop.jar'!");
            System.out.println("2 advanced arguments are listed in order as below:");
            System.out.println("1) <jar filename> - name of the jar file");
            System.out.println("2) <main class> - name of the main class");
            System.out.println("Please provide 3 advanced arguments to this command " +
                    "for the method 'hadoop.streaming'!");
            System.out.println("3 advanced arguments are listed in order as below:");
            System.out.println("1) <language name> - 'python'");
            System.out.println("2) <mapper filename> - name of the mapper file");
            System.out.println("3) <reducer filename> - name of the reducer file OR 'none'");
            System.out.println("Please provide 3 optional arguments to this command!");
            System.out.println("3 optional arguments are listed in order as below");
            System.out.println("1) <version name> - version of the model");
            System.out.println("2) <author name> - name of the author");
            System.out.println("3) <description> - description for the model");
            System.out.println("RESULT: 1");
        }
    }
}
