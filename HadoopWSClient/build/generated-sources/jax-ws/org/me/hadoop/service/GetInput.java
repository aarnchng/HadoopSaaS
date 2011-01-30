
package org.me.hadoop.service;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for getInput complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="getInput">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="inname" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "getInput", propOrder = {
    "inname"
})
public class GetInput {

    protected String inname;

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

}
