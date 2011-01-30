
package org.me.hadoop.service;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for getModel complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="getModel">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="mname" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "getModel", propOrder = {
    "mname"
})
public class GetModel {

    protected String mname;

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

}
