package org.wso2.carbon.esb.connector.util;

import ifs.fnd.ap.DataType;
import ifs.fnd.ap.RecordAttributeCollection;
import ifs.fnd.ap.RecordCollection;
import org.apache.axiom.om.OMAttribute;
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

    public static OMElement generateResultXML(RecordCollection result)    {

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
                    //element name
                    String element_name = rec.get(j).getNameOf().replace("$", "__");
                    //System.out.print(element_name + " ");

                    //data type
                    DataType data_type = rec.get(j).getDataType();
                    //System.out.print(data_type.toString() + " ");

                    //value
                    String value;
                    //define element for one record
                    OMElement dataElementXML = constants.factory.createOMElement(element_name, constants.omNs);

                    //define the attributes
                    OMAttribute dataTypeAttribute = constants.factory.createOMAttribute(constants.DATATYPEATTRIBUTE, constants.omNs, data_type.toString());

                    if (rec.get(j).getValue().toString() != null) {

                        value = StringEscapeUtils.escapeXml(rec.get(j).getValue().toString());
                        constants.factory.createOMText(dataElementXML, value);
                    }
                    else{
                        constants.factory.createOMText(dataElementXML, constants.NULL);
                    }

                    //pack attributes to data element
                    dataElementXML.addAttribute(dataTypeAttribute);

                    //add data element to results
                    resultXML.addChild(dataElementXML);

                    //log.info(resultXML.size());
                }
                resultsXML.addChild(resultXML);
            }
        }
        return resultsXML;

    }
    public static OMElement generateResultXML(HashMap result)    {
        //define root element of XML
        OMElement resultsXML = constants.factory.createOMElement(constants.RESPONSE,constants.omNs);
        OMElement bindVariablesXML = constants.factory.createOMElement(constants.BINDVARIABLES,constants.omNs);
        Set set = result.entrySet();
        Iterator response_itr = set.iterator();
        while(response_itr.hasNext()) {
            Map.Entry bind = (Map.Entry)response_itr.next();
            String attribute_name = (String)bind.getKey();
            OMElement recordXML = constants.factory.createOMElement(attribute_name, constants.omNs);
            String value;
            value = StringEscapeUtils.escapeXml((String)bind.getValue());
            constants.factory.createOMText(recordXML, value);
            bindVariablesXML.addChild(recordXML);
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

    public static String getElementFromXmlRequest(MessageContext messageContext, String ELEMENT) {
        SOAPBody soapBody = messageContext.getEnvelope().getBody();
        String elementValue = constants.NULL;

        for (Iterator itr = soapBody.getChildElements(); itr.hasNext();)
        {
            OMElement child = (OMElement)itr.next();
            if (child.getLocalName() == constants.REQUEST) // <request> element
            {
                for (Iterator ItrRequest = child.getChildElements(); ItrRequest.hasNext();)
                {
                    OMElement child_lv1 = (OMElement)ItrRequest.next();
                    if (child_lv1.getLocalName() == ELEMENT) { // get the element
                        elementValue = child_lv1.getText();
                        System.out.println("------------------------" + ELEMENT + "---------------------------------");
                        System.out.println(elementValue);
                    }
                }
            }
        }
        return elementValue;
    }

    public static HashMap getElementCollectionFromXmlRequest(MessageContext messageContext, String ELEMENT){
        SOAPBody soapBody = messageContext.getEnvelope().getBody();
        //OMElement bindVariables = soapBody.getFirstChildWithName(new QName(constants.omNs.getNamespaceURI(), constants.BINDVARIABLES));

        HashMap<String, String> bindsMap = new HashMap<>();

        for (Iterator itr = soapBody.getChildElements(); itr.hasNext();)
        {
            OMElement child = (OMElement)itr.next();
            if (child.getLocalName() == constants.REQUEST) // <request> element
            {
                for (Iterator ItrRequest = child.getChildElements(); ItrRequest.hasNext();)
                {
                    OMElement child_lv1 = (OMElement)ItrRequest.next();
                    if (child_lv1.getLocalName() == ELEMENT) { //  element
                        System.out.println("----------------------" + ELEMENT + "--------------------------");
                        for (Iterator bindsItr = child_lv1.getChildElements(); bindsItr.hasNext(); ) {
                            OMElement bind = (OMElement) bindsItr.next();
                            bindsMap.put(bind.getLocalName(), bind.getText());
                            System.out.println(bind.getLocalName() + " - " + bind.getText());
                        }
                    }
                }
            }
        }
        return bindsMap;
    }
}
