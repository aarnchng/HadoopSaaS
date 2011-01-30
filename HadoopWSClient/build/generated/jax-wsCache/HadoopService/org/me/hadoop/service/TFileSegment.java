
package org.me.hadoop.service;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for tFileSegment complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="tFileSegment">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="buffer" type="{http://www.w3.org/2001/XMLSchema}base64Binary" minOccurs="0"/>
 *         &lt;element name="fileName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="index" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="next" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "tFileSegment", propOrder = {
    "buffer",
    "fileName",
    "index",
    "next"
})
public class TFileSegment {

    protected byte[] buffer;
    protected String fileName;
    protected int index;
    protected boolean next;

    /**
     * Gets the value of the buffer property.
     * 
     * @return
     *     possible object is
     *     byte[]
     */
    public byte[] getBuffer() {
        return buffer;
    }

    /**
     * Sets the value of the buffer property.
     * 
     * @param value
     *     allowed object is
     *     byte[]
     */
    public void setBuffer(byte[] value) {
        this.buffer = ((byte[]) value);
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
     * Gets the value of the index property.
     * 
     */
    public int getIndex() {
        return index;
    }

    /**
     * Sets the value of the index property.
     * 
     */
    public void setIndex(int value) {
        this.index = value;
    }

    /**
     * Gets the value of the next property.
     * 
     */
    public boolean isNext() {
        return next;
    }

    /**
     * Sets the value of the next property.
     * 
     */
    public void setNext(boolean value) {
        this.next = value;
    }

}
