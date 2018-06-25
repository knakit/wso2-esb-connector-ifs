package org.wso2.carbon.esb.connector.util;

import ifs.fnd.ap.DataType;
import ifs.fnd.ap.RecordAttributeCollection;
import ifs.fnd.ap.RecordCollection;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.soap.SOAPBody;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.synapse.MessageContext;

import javax.xml.namespace.QName;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class xmlUtil {

    public static OMElement generateResultXML(RecordCollection result)
    {

        //define root element of XML
        OMElement resultsXML = constants.factory.createOMElement(constants.RESULTSET,constants.omNs);


        if(result!=null){

            for(int i = 0; i < result.size(); i++){
                RecordAttributeCollection rec;
                rec = result.get(i).getAttributes();

                //define element for one result set
                OMElement resultXML = constants.factory.createOMElement(constants.RECORD,constants.omNs);

                for(int j = 0; j <rec.size(); j++)
                {
                    //attribure name
                    String attribute_name = rec.get(j).getNameOf();
                    //System.out.print(attribute_name + " ");

                    //type
                    DataType data_type = rec.get(j).getDataType();
                    //System.out.print(data_type.toString() + " ");

                    //value
                    String value;
                    //define element for one attribute
                    OMElement attributeXML = constants.factory.createOMElement(attribute_name, constants.omNs);

                    if (rec.get(j).getValue().toString() != null) {

                        value = StringEscapeUtils.escapeXml(rec.get(j).getValue().toString());
                        constants.factory.createOMText(attributeXML, value);
                    }
                    else{
                        constants.factory.createOMText(attributeXML, constants.NULL);
                    }
                    resultXML.addChild(attributeXML);

                    //log.info(resultXML.size());
                }
                resultsXML.addChild(resultXML);
            }
        }
        return resultsXML;

    }

    public static OMElement generateResultXML(HashMap result)
    {
        //define root element of XML
        OMElement resultsXML = constants.factory.createOMElement(constants.RESPONSE,constants.omNs);
        OMElement bindVariablesXML = constants.factory.createOMElement(constants.BINDVARIABLES,constants.omNs);
        Set set = result.entrySet();
        Iterator response_itr = set.iterator();
        while(response_itr.hasNext()) {
            Map.Entry bind = (Map.Entry)response_itr.next();
            String attribute_name = (String)bind.getKey();
            OMElement attributeXML = constants.factory.createOMElement(attribute_name, constants.omNs);
            String value;
            value = StringEscapeUtils.escapeXml((String)bind.getValue());
            constants.factory.createOMText(attributeXML, value);
            bindVariablesXML.addChild(attributeXML);
        }

        resultsXML.addChild(bindVariablesXML);
        return resultsXML;
    }
    public static OMElement generateExecuteSuccessXML(){
        //define root element of XML
        OMElement resultsXML = constants.factory.createOMElement(constants.RESPONSE,constants.omNs);
        //define element for one result set
        OMElement resultXML = constants.factory.createOMElement(constants.RESULT,constants.omNs);

        constants.factory.createOMText(resultXML, "PL/SQL script successfully executed");

        resultsXML.addChild(resultXML);
        return resultsXML;
    }
}
