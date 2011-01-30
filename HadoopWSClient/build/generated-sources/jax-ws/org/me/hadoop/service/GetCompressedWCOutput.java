
package org.me.hadoop.service;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for getCompressedWCOutput complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="getCompressedWCOutput">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="inname" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="outname" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="wildcard" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "getCompressedWCOutput", propOrder = {
    "inname",
    "outname",
    "wildcard"
})
public class GetCompressedWCOutput {

    protected String inname;
    protected String outname;
    protected String wildcard;

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
     * Gets the value of the outname property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOutname() {
        return outname;
    }

    /**
     * Sets the value of the outname property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOutname(String value) {
        this.outname = value;
    }

    /**
     * Gets the value of the wildcard property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getWildcard() {
        return wildcard;
    }

    /**
     * Sets the value of the wildcard property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setWildcard(String value) {
        this.wildcard = value;
    }

}
