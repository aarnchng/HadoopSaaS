
package org.me.hadoop.service;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for tJobOutput complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="tJobOutput">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="arguments" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="description" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="endDate" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="fileName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="jobCount" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="jobName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="modelAuthor" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="modelDescription" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="modelName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="modelVersion" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="name" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="startDate" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="status" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="stdErr" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="stdOut" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "tJobOutput", propOrder = {
    "arguments",
    "description",
    "endDate",
    "fileName",
    "jobCount",
    "jobName",
    "modelAuthor",
    "modelDescription",
    "modelName",
    "modelVersion",
    "name",
    "startDate",
    "status",
    "stdErr",
    "stdOut"
})
public class TJobOutput {

    protected String arguments;
    protected String description;
    protected String endDate;
    protected String fileName;
    protected String jobCount;
    protected String jobName;
    protected String modelAuthor;
    protected String modelDescription;
    protected String modelName;
    protected String modelVersion;
    protected String name;
    protected String startDate;
    protected String status;
    protected String stdErr;
    protected String stdOut;

    /**
     * Gets the value of the arguments property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getArguments() {
        return arguments;
    }

    /**
     * Sets the value of the arguments property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setArguments(String value) {
        this.arguments = value;
    }

    /**
     * Gets the value of the description property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the value of the description property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescription(String value) {
        this.description = value;
    }

    /**
     * Gets the value of the endDate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEndDate() {
        return endDate;
    }

    /**
     * Sets the value of the endDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEndDate(String value) {
        this.endDate = value;
    }

    /**
     * Gets the value of the fileName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * Sets the value of the fileName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFileName(String value) {
        this.fileName = value;
    }

    /**
     * Gets the value of the jobCount property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getJobCount() {
        return jobCount;
    }

    /**
     * Sets the value of the jobCount property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setJobCount(String value) {
        this.jobCount = value;
    }

    /**
     * Gets the value of the jobName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getJobName() {
        return jobName;
    }

    /**
     * Sets the value of the jobName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setJobName(String value) {
        this.jobName = value;
    }

    /**
     * Gets the value of the modelAuthor property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getModelAuthor() {
        return modelAuthor;
    }

    /**
     * Sets the value of the modelAuthor property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setModelAuthor(String value) {
        this.modelAuthor = value;
    }

    /**
     * Gets the value of the modelDescription property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getModelDescription() {
        return modelDescription;
    }

    /**
     * Sets the value of the modelDescription property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setModelDescription(String value) {
        this.modelDescription = value;
    }

    /**
     * Gets the value of the modelName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getModelName() {
        return modelName;
    }

    /**
     * Sets the value of the modelName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setModelName(String value) {
        this.modelName = value;
    }

    /**
     * Gets the value of the modelVersion property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getModelVersion() {
        return modelVersion;
    }

    /**
     * Sets the value of the modelVersion property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setModelVersion(String value) {
        this.modelVersion = value;
    }

    /**
     * Gets the value of the name property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setName(String value) {
        this.name = value;
    }

    /**
     * Gets the value of the startDate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStartDate() {
        return startDate;
    }

    /**
     * Sets the value of the startDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStartDate(String value) {
        this.startDate = value;
    }

    /**
     * Gets the value of the status property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStatus() {
        return status;
    }

    /**
     * Sets the value of the status property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStatus(String value) {
        this.status = value;
    }

    /**
     * Gets the value of the stdErr property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStdErr() {
        return stdErr;
    }

    /**
     * Sets the value of the stdErr property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStdErr(String value) {
        this.stdErr = value;
    }

    /**
     * Gets the value of the stdOut property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStdOut() {
        return stdOut;
    }

    /**
     * Sets the value of the stdOut property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStdOut(String value) {
        this.stdOut = value;
    }

}
