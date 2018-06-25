package org.wso2.carbon.esb.connector.util;

import org.apache.axiom.om.OMElement;
import org.apache.axiom.soap.SOAPBody;

import java.util.Iterator;

public class resultPayloadCreate {
    public static void preparePayload(org.apache.synapse.MessageContext messageContext, OMElement element)
    {
        SOAPBody soapBody = messageContext.getEnvelope().getBody();
        for (Iterator itr = soapBody.getChildElements(); itr.hasNext();)
        {
            OMElement child = (OMElement)itr.next();
            child.detach();
        }
        soapBody.addChild(element);
        //for (Iterator itr = element.getChildElements(); itr.hasNext();)
        //{
        //    OMElement child = (OMElement)itr.next();
        //   soapBody.addChild(child);
        //}
    }
}
