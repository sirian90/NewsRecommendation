/**
 * Calais.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package org.rm3umf.net.openCalais.clearforest;

public interface Calais extends javax.xml.rpc.Service {
    public java.lang.String getcalaisSoapAddress();

    public com.clearforest.CalaisSoap getcalaisSoap() throws javax.xml.rpc.ServiceException;

    public com.clearforest.CalaisSoap getcalaisSoap(java.net.URL portAddress) throws javax.xml.rpc.ServiceException;
}
