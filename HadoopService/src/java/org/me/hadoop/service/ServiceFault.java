/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.me.hadoop.service;

/**
 *
 * @author Aaron
 */
public class ServiceFault extends Throwable {

    public ServiceFault(String msg) {
        super(msg);
    }
}
