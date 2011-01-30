
package org.me.hadoop.service;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for runJob complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="runJob">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="mname" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="inname" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="desc" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="args" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "runJob", propOrder = {
    "mname",
    "inname",
    "desc",
    "args"
})
public class RunJob {

    protected String mname;
    protected String inname;
    protected String desc;
    protected String args;

    /**
     * Gets the value of the mname property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMname() {
        return mname;
    }

    /**
     * Sets the value of the mname property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMname(String value) {
        this.mname = value;
    }

    /**
     * Gets the value of the inname property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getInname() {
        return inname;
    }

    /**
     * Sets the value of the inname property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setInname(String value) {
        this.inname = value;
    }

    /**
     * Gets the value of the desc property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDesc() {
        return desc;
    }

    /**
     * Sets the value of the desc property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDesc(String value) {
        this.desc = value;
    }

    /**
     * Gets the value of the args property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getArgs() {
        return args;
    }

    /**
     * Sets the value of the args property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setArgs(String value) {
        this.args = value;
    }

}
