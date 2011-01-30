/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.me.hadoop.service;

import com.sun.xml.ws.developer.StreamingAttachment;
import com.sun.xml.ws.developer.StreamingDataHandler;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.jws.Oneway;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlMimeType;
import org.me.hadoop.HadoopFactory;
import org.me.hadoop.JobTracker;
import org.me.hadoop.NameNode;
import org.me.hadoop.service.datatypes.T_FileSegment;
import org.me.hadoop.service.datatypes.T_JobInput;
import org.me.hadoop.service.datatypes.T_JobInputHeader;
import org.me.hadoop.service.datatypes.T_JobModel;
import org.me.hadoop.service.datatypes.T_JobModelHeader;
import org.me.hadoop.service.datatypes.T_JobOutputHeader;
import org.me.hadoop.service.datatypes.T_JobRun;
import org.me.hadoop.service.datatypes.T_JobOutput;
import org.me.hadoop.service.internal.JobInput;
import org.me.hadoop.service.internal.JobModel;
import org.me.hadoop.service.internal.JobOutput;
import org.me.hadoop.service.internal.JobRun;
import org.me.hadoop.uploader.FileHandler;
import org.me.hadoop.uploader.Uploader;
import org.me.hadoop.utils.CommonsIO;
import org.me.hadoop.utils.MiscUtils;

/**
 *
 * @author Aaron
 */
@StreamingAttachment(parseEagerly = true, memoryThreshold = 4000000L)
@javax.xml.ws.soap.MTOM
@WebService(serviceName = "HadoopService")
public class HadoopService {

    private static Logger logger = Logger.getLogger(HadoopService.class.getName());
    private ServiceProperties service = null;
    private String tmppath;
    private ModelRepository mrepository = null;
    private DataRepository drepository = null;
    private NameNode namenode = null;
    private JobTracker jobtracker = null;
    private Uploader uploader = null;

    public HadoopService() {
        try {
            service = ServiceProperties.getInstance();
            tmppath = MiscUtils.appendFileSeparator(service.getTmpPath());
            CommonsIO.createDirectory(tmppath);

            mrepository = new ModelRepository(service.getModelPath());
            drepository = new DataRepository(service.getDataPath(), tmppath);
            namenode = HadoopFactory.createNameNode();
            jobtracker = HadoopFactory.createJobTracker(namenode);
            uploader = new Uploader(tmppath);
        } catch (IOException ex) {
            logger.log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "getVersion")
    public String getVersion() {
        //TODO write your implementation code here:
        if (service != null) {
            return service.getServiceVersion();
        }
        return null;
    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "isClusterAvailable")
    public boolean isClusterAvailable() {
        //TODO write your implementation code here:
        if ((namenode != null) && (jobtracker != null)) {
            return (namenode.isAvailable() && jobtracker.isAvailable());
        }
        return false;
    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "listModels")
    public T_JobModelHeader[] listModels() {
        //TODO write your implementation code here:
        if (mrepository != null) {
            Collection<JobModel> models = mrepository.getModels();
            T_JobModelHeader[] tmodels = new T_JobModelHeader[models.size()];
            int index = 0;
            for (JobModel model : models) {
                tmodels[index] = new T_JobModelHeader(model);
                index++;
            }
            return tmodels;
        }
        return null;
    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "getModel")
    public T_JobModel getModel(@WebParam(name = "mname") String mname)
            throws ServiceFault {
        //TODO write your implementation code here:
        if (mrepository != null) {
            JobModel model = mrepository.getModel(mname);
            if (model != null) {
                return (new T_JobModel(model));
            } else {
                throw new ServiceFault("Requested model is unavailable!");
            }
        }
        return null;
    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "addModel")
    public T_JobModel addModel(@WebParam(name = "filename") String filename)
            throws ServiceFault {
        //TODO write your implementation code here:
        if (mrepository != null) {
            JobModel model = mrepository.addModel(tmppath + filename);
            if (model != null) {
                return (new T_JobModel(model));
            } else {
                throw new ServiceFault("Added model is invalid!");
            }
        }
        return null;
    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "removeModel")
    public boolean removeModel(@WebParam(name = "mname") String mname) {
        //TODO write your implementation code here:
        if (mrepository != null) {
            return mrepository.removeModel(mname);
        }
        return false;
    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "refreshModelRepository")
    public boolean refreshModelRepository() {
        //TODO write your implementation code here:
        if (mrepository != null) {
            return mrepository.refresh();
        }
        return false;
    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "listInputs")
    public T_JobInputHeader[] listInputs() {
        //TODO write your implementation code here:
        if (drepository != null) {
            Collection<JobInput> inputs = drepository.getInputs();
            T_JobInputHeader[] tinputs = new T_JobInputHeader[inputs.size()];
            int index = 0;
            for (JobInput input : inputs) {
                tinputs[index] = new T_JobInputHeader(input);
                index++;
            }
            return tinputs;
        }
        return null;
    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "getInput")
    public T_JobInput getInput(@WebParam(name = "inname") String inname)
            throws ServiceFault {
        //TODO write your implementation code here:
        if (drepository != null) {
            JobInput input = drepository.getInput(inname);
            if (input != null) {
                return (new T_JobInput(input));
            } else {
                throw new ServiceFault("Requested input data is unavailable!");
            }
        }
        return null;
    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "addInput")
    public T_JobInput addInput(@WebParam(name = "filename") String filename)
            throws ServiceFault {
        //TODO write your implementation code here:
        if ((drepository != null) && (namenode != null)) {
            JobInput input = drepository.addInput(tmppath + filename);
            if (input != null) {
                if (namenode.mkdir(input.getName()) && namenode.put(input.getName(),
                        input.getDataPath())) {
                    return (new T_JobInput(input));
                } else {
                    throw new ServiceFault("Requested input data cannot be " +
                            "added due to errors in namenode!");
                }
            } else {
                throw new ServiceFault("Added input data is invalid!");
            }
        }
        return null;
    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "removeInput")
    public boolean removeInput(@WebParam(name = "inname") String inname) {
        //TODO write your implementation code here:
        if (drepository != null) {
            return drepository.removeInput(inname);
        }
        return false;
    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "listOutputs")
    public T_JobOutputHeader[] listOutputs(@WebParam(name = "inname") String inname) {
        //TODO write your implementation code here:
        if (drepository != null) {
            Collection<JobOutput> outputs = drepository.getOutputs(inname);
            T_JobOutputHeader[] toutputs = new T_JobOutputHeader[outputs.size()];
            int index = 0;
            for (JobOutput output : outputs) {
                toutputs[index] = new T_JobOutputHeader(output);
                index++;
            }
            return toutputs;
        }
        return null;
    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "removeOutput")
    public boolean removeOutput(@WebParam(name = "inname") String inname,
            @WebParam(name = "outname") String outname) {
        //TODO write your implementation code here:
        if (drepository != null) {
            JobOutput output = drepository.getOutput(inname, outname);
            if (output != null) {
                return drepository.removeOutput(inname, output.getName());
            }
        }
        return false;
    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "prepareJob")
    public T_JobRun prepareJob(@WebParam(name = "mname") String mname,
            @WebParam(name = "inname") String inname) throws ServiceFault {
        //TODO write your implementation code here:
        if ((mrepository != null) && (drepository != null) && (namenode != null) &&
                (jobtracker != null)) {
            JobModel model = mrepository.getModel(mname);
            JobInput input = drepository.getInput(inname);
            if ((model != null) && (input != null)) {
                JobOutput output = drepository.createOutput(inname);
                if (output != null) {
                    return (new T_JobRun(model, input, output));
                } else {
                    throw new ServiceFault("Requested output data cannot be " +
                            "created!");
                }
            } else {
                if (model == null) {
                    throw new ServiceFault("Requested model is unavailable!");
                } else if (input == null) {
                    throw new ServiceFault("Requested input data is unavailable!");
                }
            }
        }
        return null;
    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "runPreparedJob")
    @Oneway
    public void runPreparedJob(@WebParam(name = "mname") String mname,
            @WebParam(name = "inname") String inname, @WebParam(name = "outname") String outname,
            @WebParam(name = "desc") String desc, @WebParam(name = "args") String args) {
        //TODO write your implementation code here:
        if ((mrepository != null) && (drepository != null) && (namenode != null) &&
                (jobtracker != null)) {
            JobModel model = mrepository.getModel(mname);
            JobInput input = drepository.getInput(inname);
            JobOutput output = drepository.getOutput(inname, outname);
            JobRun run = JobRun.getInstance(model, input, output, desc, args);
            if ((model != null) && (input != null) && (output != null) &&
                    (run != null)) {
                jobtracker.run(run);
            }
        }
    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "runJob")
    public T_JobRun runJob(@WebParam(name = "mname") String mname,
            @WebParam(name = "inname") String inname, @WebParam(name = "desc") String desc,
            @WebParam(name = "args") String args) throws ServiceFault {
        //TODO write your implementation code here:
        if ((mrepository != null) && (drepository != null) && (namenode != null) &&
                (jobtracker != null)) {
            JobModel model = mrepository.getModel(mname);
            JobInput input = drepository.getInput(inname);
            if ((model != null) && (input != null)) {
                JobOutput output = drepository.createOutput(inname);
                if (output != null) {
                    JobRun run = JobRun.getInstance(model, input, output, desc,
                            args);
                    jobtracker.run(run);
                    return (new T_JobRun(run));
                } else {
                    throw new ServiceFault("Requested output data cannot be " +
                            "created!");
                }
            } else {
                if (model == null) {
                    throw new ServiceFault("Requested model is unavailable!");
                } else if (input == null) {
                    throw new ServiceFault("Requested input data is unavailable!");
                }
            }
        }
        return null;
    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "killJob")
    public boolean killJob(@WebParam(name = "inname") String inname,
            @WebParam(name = "outname") String outname) {
        //TODO write your implementation code here:
        if ((drepository != null) && (jobtracker != null)) {
            while (JobRunList.exists(inname, outname)) {
                JobRun run = JobRunList.get(inname, outname);
                if ((run != null) && jobtracker.kill(run.getJobName())) {
                    run.save(JobRun.STATUS_KILLED);
                    MiscUtils.waitFor(5000);
                }
            }
            return true;
        }
        return false;
    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "getOutput")
    public T_JobOutput getOutput(@WebParam(name = "inname") String inname,
            @WebParam(name = "outname") String outname) throws ServiceFault {
        //TODO write your implementation code here:
        if (drepository != null) {
            JobOutput output = JobRunList.getOutput(inname, outname);
            if (output == null) {
                output = drepository.getOutput(inname, outname);
            }
            if (output != null) {
                return (new T_JobOutput(output));
            } else {
                throw new ServiceFault("Requested output data is unavailable!");
            }
        }
        return null;
    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "getCompressedOutput")
    public T_JobOutput getCompressedOutput(@WebParam(name = "inname") String inname,
            @WebParam(name = "outname") String outname) throws ServiceFault {
        //TODO write your implementation code here:
        if (drepository != null) {
            JobOutput output = drepository.getOutput(inname, outname);
            if (output != null) {
                String msg = output.getStatus();
                if (msg.equals(JobMessage.MSG_SUCCESS)) {
                    String filename = drepository.getCompressedOutput(output);
                    if (filename != null) {
                        return (new T_JobOutput(output, filename));
                    } else {
                        throw new ServiceFault("Requested output data cannot be " +
                                "retrieved due to errors in compression!");
                    }
                } else if (msg.equals(JobMessage.MSG_STARTING) || msg.equals(
                        JobMessage.MSG_RUNNING)) {
                    throw new ServiceFault("Requested output data is not ready!");
                } else {
                    throw new ServiceFault("Requested output data cannot be " +
                            "retrieved due to errors in simulation!");
                }
            } else {
                throw new ServiceFault("Requested output data is unavailable!");
            }
        }
        return null;
    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "getCompressedWCOutput")
    public T_JobOutput getCompressedWCOutput(@WebParam(name = "inname") String inname,
            @WebParam(name = "outname") String outname, @WebParam(name = "wildcard") String wildcard)
            throws ServiceFault {
        //TODO write your implementation code here:
        if (drepository != null) {
            JobOutput output = drepository.getOutput(inname, outname);
            if (output != null) {
                String msg = output.getStatus();
                if (msg.equals(JobMessage.MSG_SUCCESS)) {
                    String filename = drepository.getCompressedOutput(output, wildcard);
                    if (filename != null) {
                        return (new T_JobOutput(output, filename));
                    } else {
                        throw new ServiceFault("Requested output data cannot be " +
                                "retrieved due to errors in compression!");
                    }
                } else if (msg.equals(JobMessage.MSG_STARTING) || msg.equals(
                        JobMessage.MSG_RUNNING)) {
                    throw new ServiceFault("Requested output data is not ready!");
                } else {
                    throw new ServiceFault("Requested output data cannot be " +
                            "retrieved due to errors in simulation!");
                }
            } else {
                throw new ServiceFault("Requested output data is unavailable!");
            }
        }
        return null;
    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "uploadFile")
    public boolean uploadFile(@WebParam(name = "filename") String filename,
            @WebParam(name = "index") int index, @WebParam(name = "buffer") byte[] buffer,
            @WebParam(name = "merge") boolean merge) {
        //TODO write your implementation code here:
        if (uploader != null) {
            if (uploader.putFile(filename, index, buffer, merge) != null) {
                return true;
            }
        }
        return false;
    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "downloadFile")
    public T_FileSegment downloadFile(@WebParam(name = "filename") String filename)
            throws ServiceFault {
        //TODO write your implementation code here:
        if (uploader != null) {
            FileHandler handler = uploader.getFile(filename);
            if (handler != null) {
                return (new T_FileSegment(handler));
            } else {
                throw new ServiceFault("Requested file is unavailable!");
            }
        }
        return null;
    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "putFile")
    public boolean putFile(@WebParam(name = "filename") String filename,
            @XmlMimeType("application/octet-stream")
            @WebParam(name = "handler") DataHandler handler) {
        //TODO write your implementation code here:
        StreamingDataHandler streamhandler = (StreamingDataHandler) handler;
        try {
            streamhandler.moveTo(new File(tmppath, filename));
            streamhandler.close();
            return true;
        } catch (IOException ex) {
            logger.log(Level.WARNING, null, ex);
        }
        return false;
    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "getFile")
    public
    @XmlMimeType("application/octet-stream")
    DataHandler getFile(@WebParam(name = "filename") String filename) {
        //TODO write your implementation code here:
        FileDataSource source = new FileDataSource(new File(tmppath, filename));
        return new DataHandler(source);
    }
}
