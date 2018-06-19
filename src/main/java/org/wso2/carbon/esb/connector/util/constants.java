package org.wso2.carbon.esb.connector.util;

import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.OMNamespace;

public class constants {

    //Create XML factory
    public static final OMFactory factory = OMAbstractFactory.getOMFactory();
    public static final OMNamespace omNs = factory.createOMNamespace("http://wso2.org/knak/ifs/connector", "ifs");

}
