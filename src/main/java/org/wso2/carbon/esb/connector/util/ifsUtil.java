package org.wso2.carbon.esb.connector.util;

import org.apache.axiom.om.OMElement;

import ifs.fnd.ap.RecordCollection;
import ifs.fnd.ap.RecordAttributeCollection;
import ifs.fnd.ap.DataType;

import org.apache.commons.lang3.StringEscapeUtils;


public class ifsUtil {
    public static OMElement generateResultXML(RecordCollection result)
    {

        //define root element of XML
        OMElement resultsXML = constants.factory.createOMElement("response",constants.omNs);


        if(result!=null){

            for(int i = 0; i < result.size(); i++){
                RecordAttributeCollection rec;
                rec = result.get(i).getAttributes();

                //define element for one result set
                OMElement resultXML = constants.factory.createOMElement("record",constants.omNs);

                for(int j = 0; j <rec.size(); j++)
                {
                    //attribure name
                    String attribute_name = rec.get(j).getNameOf();
                    System.out.print(attribute_name + " ");

                    //type
                    DataType data_type = rec.get(j).getDataType();
                    System.out.print(data_type.toString() + " ");

                    //value
                    String value;
                    //define element for one attribute
                    OMElement attributeXML = constants.factory.createOMElement(attribute_name, constants.omNs);

                    if (rec.get(j).getValue().toString() != null) {

                        value = StringEscapeUtils.escapeXml(rec.get(j).getValue().toString());


                        constants.factory.createOMText(attributeXML, value);
                    }
                    //else{
                    //    constants.factory.createOMText(attributeXML);
                    //}
                    resultXML.addChild(attributeXML);

                    //log.info(resultXML.size());
                }
                resultsXML.addChild(resultXML);
            }
        }
        return resultsXML;

    }

}
