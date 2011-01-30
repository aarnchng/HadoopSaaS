
package org.me.hadoop.service;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for tJobRun complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="tJobRun">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="input" type="{http://service.hadoop.me.org/}tJobInput" minOccurs="0"/>
 *         &lt;element name="model" type="{http://service.hadoop.me.org/}tJobModel" minOccurs="0"/>
 *         &lt;element name="output" type="{http://service.hadoop.me.org/}tJobOutputHeader" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "tJobRun", propOrder = {
    "input",
    "model",
    "output"
})
public class TJobRun {

    protected TJobInput input;
    protected TJobModel model;
    protected TJobOutputHeader output;

    /**
     * Gets the value of the input property.
     * 
     * @return
     *     possible object is
     *     {@link TJobInput }
     *     
     */
    public TJobInput getInput() {
        return input;
    }

    /**
     * Sets the value of the input property.
     * 
     * @param value
     *     allowed object is
     *     {@link TJobInput }
     *     
     */
    public void setInput(TJobInput value) {
        this.input = value;
    }

    /**
     * Gets the value of the model property.
     * 
     * @return
     *     possible object is
     *     {@link TJobModel }
     *     
     */
    public TJobModel getModel() {
        return model;
    }

    /**
     * Sets the value of the model property.
     * 
     * @param value
     *     allowed object is
     *     {@link TJobModel }
     *     
     */
    public void setModel(TJobModel value) {
        this.model = value;
    }

    /**
     * Gets the value of the output property.
     * 
     * @return
     *     possible object is
     *     {@link TJobOutputHeader }
     *     
     */
    public TJobOutputHeader getOutput() {
        return output;
    }

    /**
     * Sets the value of the output property.
     * 
     * @param value
     *     allowed object is
     *     {@link TJobOutputHeader }
     *     
     */
    public void setOutput(TJobOutputHeader value) {
        this.output = value;
    }

}
