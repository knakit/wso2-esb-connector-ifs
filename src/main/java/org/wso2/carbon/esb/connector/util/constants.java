package org.wso2.carbon.esb.connector.util;

import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.OMNamespace;

public class constants {

    //Create XML factory
    public static final OMFactory factory = OMAbstractFactory.getOMFactory();
    public static final OMNamespace omNs = factory.createOMNamespace("http://wso2.org/knak/ifs/connector", "ifs");

    //XML element definitions
    public static final String REQUEST = "request";
    public static final String RESPONSE = "response";
    public static final String RESULT = "result";
    public static final String RESULTSET = "resultSet";
    public static final String RECORD = "record";
    public static final String BINDVARIABLES = "bindVariables";
    public static final String SQLSTATEMENT = "sqlStatement";
    public static final String PLSQLBLOCK = "plsqlBlock";
    public static final String PLSQLPACKAGE = "plsqlPackage";
    public static final String METHODTYPE = "methodType"; // NEW, MODIFY, REMOVE
    public static final String METHODNAME = "methodName";
    public static final String METHODACTION = "methodAction"; //CHECK, DO, PREPARE


    //XML Attribute  definitions
    public static final String DATATYPEATTRIBUTE = "Type";

    //Common
    public static final String IFSCONNURL = "IfsConnURL";
    public static final String IFSVERSION = "IfsVersion"; //APPS75, APPS8, APPS9, APPS10
    public static final String IFSUSERID = "IfsUserID";
    public static final String IFSPASSWORD = "IfsPassword";

    public static final String NULL = "";



}
