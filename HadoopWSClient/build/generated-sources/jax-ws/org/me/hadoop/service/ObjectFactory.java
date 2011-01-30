
package org.me.hadoop.service;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the org.me.hadoop.service package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _RemoveOutputResponse_QNAME = new QName("http://service.hadoop.me.org/", "removeOutputResponse");
    private final static QName _GetInputResponse_QNAME = new QName("http://service.hadoop.me.org/", "getInputResponse");
    private final static QName _PutFile_QNAME = new QName("http://service.hadoop.me.org/", "putFile");
    private final static QName _GetModelResponse_QNAME = new QName("http://service.hadoop.me.org/", "getModelResponse");
    private final static QName _GetFileResponse_QNAME = new QName("http://service.hadoop.me.org/", "getFileResponse");
    private final static QName _ListInputsResponse_QNAME = new QName("http://service.hadoop.me.org/", "listInputsResponse");
    private final static QName _PutFileResponse_QNAME = new QName("http://service.hadoop.me.org/", "putFileResponse");
    private final static QName _RemoveInput_QNAME = new QName("http://service.hadoop.me.org/", "removeInput");
    private final static QName _GetInput_QNAME = new QName("http://service.hadoop.me.org/", "getInput");
    private final static QName _DownloadFileResponse_QNAME = new QName("http://service.hadoop.me.org/", "downloadFileResponse");
    private final static QName _GetFile_QNAME = new QName("http://service.hadoop.me.org/", "getFile");
    private final static QName _RunPreparedJob_QNAME = new QName("http://service.hadoop.me.org/", "runPreparedJob");
    private final static QName _PrepareJobResponse_QNAME = new QName("http://service.hadoop.me.org/", "prepareJobResponse");
    private final static QName _GetCompressedOutput_QNAME = new QName("http://service.hadoop.me.org/", "getCompressedOutput");
    private final static QName _RemoveModelResponse_QNAME = new QName("http://service.hadoop.me.org/", "removeModelResponse");
    private final static QName _AddInput_QNAME = new QName("http://service.hadoop.me.org/", "addInput");
    private final static QName _AddModelResponse_QNAME = new QName("http://service.hadoop.me.org/", "addModelResponse");
    private final static QName _ListInputs_QNAME = new QName("http://service.hadoop.me.org/", "listInputs");
    private final static QName _ServiceFault_QNAME = new QName("http://service.hadoop.me.org/", "ServiceFault");
    private final static QName _RemoveOutput_QNAME = new QName("http://service.hadoop.me.org/", "removeOutput");
    private final static QName _DownloadFile_QNAME = new QName("http://service.hadoop.me.org/", "downloadFile");
    private final static QName _RemoveInputResponse_QNAME = new QName("http://service.hadoop.me.org/", "removeInputResponse");
    private final static QName _ListModelsResponse_QNAME = new QName("http://service.hadoop.me.org/", "listModelsResponse");
    private final static QName _GetVersion_QNAME = new QName("http://service.hadoop.me.org/", "getVersion");
    private final static QName _UploadFileResponse_QNAME = new QName("http://service.hadoop.me.org/", "uploadFileResponse");
    private final static QName _UploadFile_QNAME = new QName("http://service.hadoop.me.org/", "uploadFile");
    private final static QName _IsClusterAvailable_QNAME = new QName("http://service.hadoop.me.org/", "isClusterAvailable");
    private final static QName _GetCompressedWCOutputResponse_QNAME = new QName("http://service.hadoop.me.org/", "getCompressedWCOutputResponse");
    private final static QName _RefreshModelRepository_QNAME = new QName("http://service.hadoop.me.org/", "refreshModelRepository");
    private final static QName _KillJob_QNAME = new QName("http://service.hadoop.me.org/", "killJob");
    private final static QName _ListOutputsResponse_QNAME = new QName("http://service.hadoop.me.org/", "listOutputsResponse");
    private final static QName _RunJob_QNAME = new QName("http://service.hadoop.me.org/", "runJob");
    private final static QName _GetOutputResponse_QNAME = new QName("http://service.hadoop.me.org/", "getOutputResponse");
    private final static QName _ListOutputs_QNAME = new QName("http://service.hadoop.me.org/", "listOutputs");
    private final static QName _ListModels_QNAME = new QName("http://service.hadoop.me.org/", "listModels");
    private final static QName _IsClusterAvailableResponse_QNAME = new QName("http://service.hadoop.me.org/", "isClusterAvailableResponse");
    private final static QName _GetModel_QNAME = new QName("http://service.hadoop.me.org/", "getModel");
    private final static QName _AddModel_QNAME = new QName("http://service.hadoop.me.org/", "addModel");
    private final static QName _RunJobResponse_QNAME = new QName("http://service.hadoop.me.org/", "runJobResponse");
    private final static QName _RefreshModelRepositoryResponse_QNAME = new QName("http://service.hadoop.me.org/", "refreshModelRepositoryResponse");
    private final static QName _KillJobResponse_QNAME = new QName("http://service.hadoop.me.org/", "killJobResponse");
    private final static QName _RemoveModel_QNAME = new QName("http://service.hadoop.me.org/", "removeModel");
    private final static QName _GetCompressedOutputResponse_QNAME = new QName("http://service.hadoop.me.org/", "getCompressedOutputResponse");
    private final static QName _GetCompressedWCOutput_QNAME = new QName("http://service.hadoop.me.org/", "getCompressedWCOutput");
    private final static QName _PrepareJob_QNAME = new QName("http://service.hadoop.me.org/", "prepareJob");
    private final static QName _GetOutput_QNAME = new QName("http://service.hadoop.me.org/", "getOutput");
    private final static QName _AddInputResponse_QNAME = new QName("http://service.hadoop.me.org/", "addInputResponse");
    private final static QName _GetVersionResponse_QNAME = new QName("http://service.hadoop.me.org/", "getVersionResponse");
    private final static QName _UploadFileBuffer_QNAME = new QName("", "buffer");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: org.me.hadoop.service
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link ServiceFault }
     * 
     */
    public ServiceFault createServiceFault() {
        return new ServiceFault();
    }

    /**
     * Create an instance of {@link PutFileResponse }
     * 
     */
    public PutFileResponse createPutFileResponse() {
        return new PutFileResponse();
    }

    /**
     * Create an instance of {@link GetOutputResponse }
     * 
     */
    public GetOutputResponse createGetOutputResponse() {
        return new GetOutputResponse();
    }

    /**
     * Create an instance of {@link RunJobResponse }
     * 
     */
    public RunJobResponse createRunJobResponse() {
        return new RunJobResponse();
    }

    /**
     * Create an instance of {@link ListModels }
     * 
     */
    public ListModels createListModels() {
        return new ListModels();
    }

    /**
     * Create an instance of {@link RemoveModelResponse }
     * 
     */
    public RemoveModelResponse createRemoveModelResponse() {
        return new RemoveModelResponse();
    }

    /**
     * Create an instance of {@link AddModel }
     * 
     */
    public AddModel createAddModel() {
        return new AddModel();
    }

    /**
     * Create an instance of {@link KillJob }
     * 
     */
    public KillJob createKillJob() {
        return new KillJob();
    }

    /**
     * Create an instance of {@link TJobModel }
     * 
     */
    public TJobModel createTJobModel() {
        return new TJobModel();
    }

    /**
     * Create an instance of {@link AddModelResponse }
     * 
     */
    public AddModelResponse createAddModelResponse() {
        return new AddModelResponse();
    }

    /**
     * Create an instance of {@link RemoveInput }
     * 
     */
    public RemoveInput createRemoveInput() {
        return new RemoveInput();
    }

    /**
     * Create an instance of {@link RemoveInputResponse }
     * 
     */
    public RemoveInputResponse createRemoveInputResponse() {
        return new RemoveInputResponse();
    }

    /**
     * Create an instance of {@link GetInputResponse }
     * 
     */
    public GetInputResponse createGetInputResponse() {
        return new GetInputResponse();
    }

    /**
     * Create an instance of {@link GetOutput }
     * 
     */
    public GetOutput createGetOutput() {
        return new GetOutput();
    }

    /**
     * Create an instance of {@link GetCompressedOutput }
     * 
     */
    public GetCompressedOutput createGetCompressedOutput() {
        return new GetCompressedOutput();
    }

    /**
     * Create an instance of {@link UploadFileResponse }
     * 
     */
    public UploadFileResponse createUploadFileResponse() {
        return new UploadFileResponse();
    }

    /**
     * Create an instance of {@link RemoveOutput }
     * 
     */
    public RemoveOutput createRemoveOutput() {
        return new RemoveOutput();
    }

    /**
     * Create an instance of {@link RemoveModel }
     * 
     */
    public RemoveModel createRemoveModel() {
        return new RemoveModel();
    }

    /**
     * Create an instance of {@link AddInputResponse }
     * 
     */
    public AddInputResponse createAddInputResponse() {
        return new AddInputResponse();
    }

    /**
     * Create an instance of {@link RefreshModelRepository }
     * 
     */
    public RefreshModelRepository createRefreshModelRepository() {
        return new RefreshModelRepository();
    }

    /**
     * Create an instance of {@link DownloadFile }
     * 
     */
    public DownloadFile createDownloadFile() {
        return new DownloadFile();
    }

    /**
     * Create an instance of {@link TJobOutputHeader }
     * 
     */
    public TJobOutputHeader createTJobOutputHeader() {
        return new TJobOutputHeader();
    }

    /**
     * Create an instance of {@link GetCompressedWCOutputResponse }
     * 
     */
    public GetCompressedWCOutputResponse createGetCompressedWCOutputResponse() {
        return new GetCompressedWCOutputResponse();
    }

    /**
     * Create an instance of {@link RemoveOutputResponse }
     * 
     */
    public RemoveOutputResponse createRemoveOutputResponse() {
        return new RemoveOutputResponse();
    }

    /**
     * Create an instance of {@link TJobRun }
     * 
     */
    public TJobRun createTJobRun() {
        return new TJobRun();
    }

    /**
     * Create an instance of {@link ListInputs }
     * 
     */
    public ListInputs createListInputs() {
        return new ListInputs();
    }

    /**
     * Create an instance of {@link TJobInput }
     * 
     */
    public TJobInput createTJobInput() {
        return new TJobInput();
    }

    /**
     * Create an instance of {@link GetVersion }
     * 
     */
    public GetVersion createGetVersion() {
        return new GetVersion();
    }

    /**
     * Create an instance of {@link ListInputsResponse }
     * 
     */
    public ListInputsResponse createListInputsResponse() {
        return new ListInputsResponse();
    }

    /**
     * Create an instance of {@link GetCompressedOutputResponse }
     * 
     */
    public GetCompressedOutputResponse createGetCompressedOutputResponse() {
        return new GetCompressedOutputResponse();
    }

    /**
     * Create an instance of {@link GetFile }
     * 
     */
    public GetFile createGetFile() {
        return new GetFile();
    }

    /**
     * Create an instance of {@link GetFileResponse }
     * 
     */
    public GetFileResponse createGetFileResponse() {
        return new GetFileResponse();
    }

    /**
     * Create an instance of {@link PrepareJobResponse }
     * 
     */
    public PrepareJobResponse createPrepareJobResponse() {
        return new PrepareJobResponse();
    }

    /**
     * Create an instance of {@link GetInput }
     * 
     */
    public GetInput createGetInput() {
        return new GetInput();
    }

    /**
     * Create an instance of {@link IsClusterAvailable }
     * 
     */
    public IsClusterAvailable createIsClusterAvailable() {
        return new IsClusterAvailable();
    }

    /**
     * Create an instance of {@link GetCompressedWCOutput }
     * 
     */
    public GetCompressedWCOutput createGetCompressedWCOutput() {
        return new GetCompressedWCOutput();
    }

    /**
     * Create an instance of {@link KillJobResponse }
     * 
     */
    public KillJobResponse createKillJobResponse() {
        return new KillJobResponse();
    }

    /**
     * Create an instance of {@link ListOutputs }
     * 
     */
    public ListOutputs createListOutputs() {
        return new ListOutputs();
    }

    /**
     * Create an instance of {@link GetVersionResponse }
     * 
     */
    public GetVersionResponse createGetVersionResponse() {
        return new GetVersionResponse();
    }

    /**
     * Create an instance of {@link TFileSegment }
     * 
     */
    public TFileSegment createTFileSegment() {
        return new TFileSegment();
    }

    /**
     * Create an instance of {@link RefreshModelRepositoryResponse }
     * 
     */
    public RefreshModelRepositoryResponse createRefreshModelRepositoryResponse() {
        return new RefreshModelRepositoryResponse();
    }

    /**
     * Create an instance of {@link DownloadFileResponse }
     * 
     */
    public DownloadFileResponse createDownloadFileResponse() {
        return new DownloadFileResponse();
    }

    /**
     * Create an instance of {@link TJobModelHeader }
     * 
     */
    public TJobModelHeader createTJobModelHeader() {
        return new TJobModelHeader();
    }

    /**
     * Create an instance of {@link PutFile }
     * 
     */
    public PutFile createPutFile() {
        return new PutFile();
    }

    /**
     * Create an instance of {@link ListOutputsResponse }
     * 
     */
    public ListOutputsResponse createListOutputsResponse() {
        return new ListOutputsResponse();
    }

    /**
     * Create an instance of {@link PrepareJob }
     * 
     */
    public PrepareJob createPrepareJob() {
        return new PrepareJob();
    }

    /**
     * Create an instance of {@link GetModelResponse }
     * 
     */
    public GetModelResponse createGetModelResponse() {
        return new GetModelResponse();
    }

    /**
     * Create an instance of {@link ListModelsResponse }
     * 
     */
    public ListModelsResponse createListModelsResponse() {
        return new ListModelsResponse();
    }

    /**
     * Create an instance of {@link IsClusterAvailableResponse }
     * 
     */
    public IsClusterAvailableResponse createIsClusterAvailableResponse() {
        return new IsClusterAvailableResponse();
    }

    /**
     * Create an instance of {@link AddInput }
     * 
     */
    public AddInput createAddInput() {
        return new AddInput();
    }

    /**
     * Create an instance of {@link RunJob }
     * 
     */
    public RunJob createRunJob() {
        return new RunJob();
    }

    /**
     * Create an instance of {@link TJobOutput }
     * 
     */
    public TJobOutput createTJobOutput() {
        return new TJobOutput();
    }

    /**
     * Create an instance of {@link RunPreparedJob }
     * 
     */
    public RunPreparedJob createRunPreparedJob() {
        return new RunPreparedJob();
    }

    /**
     * Create an instance of {@link UploadFile }
     * 
     */
    public UploadFile createUploadFile() {
        return new UploadFile();
    }

    /**
     * Create an instance of {@link TJobInputHeader }
     * 
     */
    public TJobInputHeader createTJobInputHeader() {
        return new TJobInputHeader();
    }

    /**
     * Create an instance of {@link GetModel }
     * 
     */
    public GetModel createGetModel() {
        return new GetModel();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RemoveOutputResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.hadoop.me.org/", name = "removeOutputResponse")
    public JAXBElement<RemoveOutputResponse> createRemoveOutputResponse(RemoveOutputResponse value) {
        return new JAXBElement<RemoveOutputResponse>(_RemoveOutputResponse_QNAME, RemoveOutputResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetInputResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.hadoop.me.org/", name = "getInputResponse")
    public JAXBElement<GetInputResponse> createGetInputResponse(GetInputResponse value) {
        return new JAXBElement<GetInputResponse>(_GetInputResponse_QNAME, GetInputResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link PutFile }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.hadoop.me.org/", name = "putFile")
    public JAXBElement<PutFile> createPutFile(PutFile value) {
        return new JAXBElement<PutFile>(_PutFile_QNAME, PutFile.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetModelResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.hadoop.me.org/", name = "getModelResponse")
    public JAXBElement<GetModelResponse> createGetModelResponse(GetModelResponse value) {
        return new JAXBElement<GetModelResponse>(_GetModelResponse_QNAME, GetModelResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetFileResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.hadoop.me.org/", name = "getFileResponse")
    public JAXBElement<GetFileResponse> createGetFileResponse(GetFileResponse value) {
        return new JAXBElement<GetFileResponse>(_GetFileResponse_QNAME, GetFileResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ListInputsResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.hadoop.me.org/", name = "listInputsResponse")
    public JAXBElement<ListInputsResponse> createListInputsResponse(ListInputsResponse value) {
        return new JAXBElement<ListInputsResponse>(_ListInputsResponse_QNAME, ListInputsResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link PutFileResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.hadoop.me.org/", name = "putFileResponse")
    public JAXBElement<PutFileResponse> createPutFileResponse(PutFileResponse value) {
        return new JAXBElement<PutFileResponse>(_PutFileResponse_QNAME, PutFileResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RemoveInput }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.hadoop.me.org/", name = "removeInput")
    public JAXBElement<RemoveInput> createRemoveInput(RemoveInput value) {
        return new JAXBElement<RemoveInput>(_RemoveInput_QNAME, RemoveInput.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetInput }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.hadoop.me.org/", name = "getInput")
    public JAXBElement<GetInput> createGetInput(GetInput value) {
        return new JAXBElement<GetInput>(_GetInput_QNAME, GetInput.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DownloadFileResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.hadoop.me.org/", name = "downloadFileResponse")
    public JAXBElement<DownloadFileResponse> createDownloadFileResponse(DownloadFileResponse value) {
        return new JAXBElement<DownloadFileResponse>(_DownloadFileResponse_QNAME, DownloadFileResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetFile }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.hadoop.me.org/", name = "getFile")
    public JAXBElement<GetFile> createGetFile(GetFile value) {
        return new JAXBElement<GetFile>(_GetFile_QNAME, GetFile.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RunPreparedJob }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.hadoop.me.org/", name = "runPreparedJob")
    public JAXBElement<RunPreparedJob> createRunPreparedJob(RunPreparedJob value) {
        return new JAXBElement<RunPreparedJob>(_RunPreparedJob_QNAME, RunPreparedJob.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link PrepareJobResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.hadoop.me.org/", name = "prepareJobResponse")
    public JAXBElement<PrepareJobResponse> createPrepareJobResponse(PrepareJobResponse value) {
        return new JAXBElement<PrepareJobResponse>(_PrepareJobResponse_QNAME, PrepareJobResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetCompressedOutput }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.hadoop.me.org/", name = "getCompressedOutput")
    public JAXBElement<GetCompressedOutput> createGetCompressedOutput(GetCompressedOutput value) {
        return new JAXBElement<GetCompressedOutput>(_GetCompressedOutput_QNAME, GetCompressedOutput.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RemoveModelResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.hadoop.me.org/", name = "removeModelResponse")
    public JAXBElement<RemoveModelResponse> createRemoveModelResponse(RemoveModelResponse value) {
        return new JAXBElement<RemoveModelResponse>(_RemoveModelResponse_QNAME, RemoveModelResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AddInput }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.hadoop.me.org/", name = "addInput")
    public JAXBElement<AddInput> createAddInput(AddInput value) {
        return new JAXBElement<AddInput>(_AddInput_QNAME, AddInput.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AddModelResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.hadoop.me.org/", name = "addModelResponse")
    public JAXBElement<AddModelResponse> createAddModelResponse(AddModelResponse value) {
        return new JAXBElement<AddModelResponse>(_AddModelResponse_QNAME, AddModelResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ListInputs }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.hadoop.me.org/", name = "listInputs")
    public JAXBElement<ListInputs> createListInputs(ListInputs value) {
        return new JAXBElement<ListInputs>(_ListInputs_QNAME, ListInputs.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ServiceFault }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.hadoop.me.org/", name = "ServiceFault")
    public JAXBElement<ServiceFault> createServiceFault(ServiceFault value) {
        return new JAXBElement<ServiceFault>(_ServiceFault_QNAME, ServiceFault.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RemoveOutput }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.hadoop.me.org/", name = "removeOutput")
    public JAXBElement<RemoveOutput> createRemoveOutput(RemoveOutput value) {
        return new JAXBElement<RemoveOutput>(_RemoveOutput_QNAME, RemoveOutput.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DownloadFile }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.hadoop.me.org/", name = "downloadFile")
    public JAXBElement<DownloadFile> createDownloadFile(DownloadFile value) {
        return new JAXBElement<DownloadFile>(_DownloadFile_QNAME, DownloadFile.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RemoveInputResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.hadoop.me.org/", name = "removeInputResponse")
    public JAXBElement<RemoveInputResponse> createRemoveInputResponse(RemoveInputResponse value) {
        return new JAXBElement<RemoveInputResponse>(_RemoveInputResponse_QNAME, RemoveInputResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ListModelsResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.hadoop.me.org/", name = "listModelsResponse")
    public JAXBElement<ListModelsResponse> createListModelsResponse(ListModelsResponse value) {
        return new JAXBElement<ListModelsResponse>(_ListModelsResponse_QNAME, ListModelsResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetVersion }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.hadoop.me.org/", name = "getVersion")
    public JAXBElement<GetVersion> createGetVersion(GetVersion value) {
        return new JAXBElement<GetVersion>(_GetVersion_QNAME, GetVersion.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link UploadFileResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.hadoop.me.org/", name = "uploadFileResponse")
    public JAXBElement<UploadFileResponse> createUploadFileResponse(UploadFileResponse value) {
        return new JAXBElement<UploadFileResponse>(_UploadFileResponse_QNAME, UploadFileResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link UploadFile }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.hadoop.me.org/", name = "uploadFile")
    public JAXBElement<UploadFile> createUploadFile(UploadFile value) {
        return new JAXBElement<UploadFile>(_UploadFile_QNAME, UploadFile.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link IsClusterAvailable }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.hadoop.me.org/", name = "isClusterAvailable")
    public JAXBElement<IsClusterAvailable> createIsClusterAvailable(IsClusterAvailable value) {
        return new JAXBElement<IsClusterAvailable>(_IsClusterAvailable_QNAME, IsClusterAvailable.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetCompressedWCOutputResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.hadoop.me.org/", name = "getCompressedWCOutputResponse")
    public JAXBElement<GetCompressedWCOutputResponse> createGetCompressedWCOutputResponse(GetCompressedWCOutputResponse value) {
        return new JAXBElement<GetCompressedWCOutputResponse>(_GetCompressedWCOutputResponse_QNAME, GetCompressedWCOutputResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RefreshModelRepository }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.hadoop.me.org/", name = "refreshModelRepository")
    public JAXBElement<RefreshModelRepository> createRefreshModelRepository(RefreshModelRepository value) {
        return new JAXBElement<RefreshModelRepository>(_RefreshModelRepository_QNAME, RefreshModelRepository.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link KillJob }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.hadoop.me.org/", name = "killJob")
    public JAXBElement<KillJob> createKillJob(KillJob value) {
        return new JAXBElement<KillJob>(_KillJob_QNAME, KillJob.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ListOutputsResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.hadoop.me.org/", name = "listOutputsResponse")
    public JAXBElement<ListOutputsResponse> createListOutputsResponse(ListOutputsResponse value) {
        return new JAXBElement<ListOutputsResponse>(_ListOutputsResponse_QNAME, ListOutputsResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RunJob }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.hadoop.me.org/", name = "runJob")
    public JAXBElement<RunJob> createRunJob(RunJob value) {
        return new JAXBElement<RunJob>(_RunJob_QNAME, RunJob.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetOutputResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.hadoop.me.org/", name = "getOutputResponse")
    public JAXBElement<GetOutputResponse> createGetOutputResponse(GetOutputResponse value) {
        return new JAXBElement<GetOutputResponse>(_GetOutputResponse_QNAME, GetOutputResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ListOutputs }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.hadoop.me.org/", name = "listOutputs")
    public JAXBElement<ListOutputs> createListOutputs(ListOutputs value) {
        return new JAXBElement<ListOutputs>(_ListOutputs_QNAME, ListOutputs.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ListModels }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.hadoop.me.org/", name = "listModels")
    public JAXBElement<ListModels> createListModels(ListModels value) {
        return new JAXBElement<ListModels>(_ListModels_QNAME, ListModels.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link IsClusterAvailableResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.hadoop.me.org/", name = "isClusterAvailableResponse")
    public JAXBElement<IsClusterAvailableResponse> createIsClusterAvailableResponse(IsClusterAvailableResponse value) {
        return new JAXBElement<IsClusterAvailableResponse>(_IsClusterAvailableResponse_QNAME, IsClusterAvailableResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetModel }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.hadoop.me.org/", name = "getModel")
    public JAXBElement<GetModel> createGetModel(GetModel value) {
        return new JAXBElement<GetModel>(_GetModel_QNAME, GetModel.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AddModel }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.hadoop.me.org/", name = "addModel")
    public JAXBElement<AddModel> createAddModel(AddModel value) {
        return new JAXBElement<AddModel>(_AddModel_QNAME, AddModel.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RunJobResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.hadoop.me.org/", name = "runJobResponse")
    public JAXBElement<RunJobResponse> createRunJobResponse(RunJobResponse value) {
        return new JAXBElement<RunJobResponse>(_RunJobResponse_QNAME, RunJobResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RefreshModelRepositoryResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.hadoop.me.org/", name = "refreshModelRepositoryResponse")
    public JAXBElement<RefreshModelRepositoryResponse> createRefreshModelRepositoryResponse(RefreshModelRepositoryResponse value) {
        return new JAXBElement<RefreshModelRepositoryResponse>(_RefreshModelRepositoryResponse_QNAME, RefreshModelRepositoryResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link KillJobResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.hadoop.me.org/", name = "killJobResponse")
    public JAXBElement<KillJobResponse> createKillJobResponse(KillJobResponse value) {
        return new JAXBElement<KillJobResponse>(_KillJobResponse_QNAME, KillJobResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RemoveModel }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.hadoop.me.org/", name = "removeModel")
    public JAXBElement<RemoveModel> createRemoveModel(RemoveModel value) {
        return new JAXBElement<RemoveModel>(_RemoveModel_QNAME, RemoveModel.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetCompressedOutputResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.hadoop.me.org/", name = "getCompressedOutputResponse")
    public JAXBElement<GetCompressedOutputResponse> createGetCompressedOutputResponse(GetCompressedOutputResponse value) {
        return new JAXBElement<GetCompressedOutputResponse>(_GetCompressedOutputResponse_QNAME, GetCompressedOutputResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetCompressedWCOutput }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.hadoop.me.org/", name = "getCompressedWCOutput")
    public JAXBElement<GetCompressedWCOutput> createGetCompressedWCOutput(GetCompressedWCOutput value) {
        return new JAXBElement<GetCompressedWCOutput>(_GetCompressedWCOutput_QNAME, GetCompressedWCOutput.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link PrepareJob }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.hadoop.me.org/", name = "prepareJob")
    public JAXBElement<PrepareJob> createPrepareJob(PrepareJob value) {
        return new JAXBElement<PrepareJob>(_PrepareJob_QNAME, PrepareJob.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetOutput }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.hadoop.me.org/", name = "getOutput")
    public JAXBElement<GetOutput> createGetOutput(GetOutput value) {
        return new JAXBElement<GetOutput>(_GetOutput_QNAME, GetOutput.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AddInputResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.hadoop.me.org/", name = "addInputResponse")
    public JAXBElement<AddInputResponse> createAddInputResponse(AddInputResponse value) {
        return new JAXBElement<AddInputResponse>(_AddInputResponse_QNAME, AddInputResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetVersionResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.hadoop.me.org/", name = "getVersionResponse")
    public JAXBElement<GetVersionResponse> createGetVersionResponse(GetVersionResponse value) {
        return new JAXBElement<GetVersionResponse>(_GetVersionResponse_QNAME, GetVersionResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link byte[]}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "buffer", scope = UploadFile.class)
    public JAXBElement<byte[]> createUploadFileBuffer(byte[] value) {
        return new JAXBElement<byte[]>(_UploadFileBuffer_QNAME, byte[].class, UploadFile.class, ((byte[]) value));
    }

}
