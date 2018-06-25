package org.wso2.carbon.esb.connector.util;

import ifs.fnd.ap.Server;

import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMNode;
import org.apache.axiom.soap.SOAPBody;
import org.apache.synapse.MessageContext;

import javax.xml.namespace.QName;
import java.util.Iterator;
import java.util.HashMap;


public class ifsUtil {

    public static Server connect(String IfsConnURL, String IfsVersion, String IfsUserID, String IfsPassword){
        // Create a server and invoke server
        Server srv = new Server();

        srv.setConnectionString(IfsConnURL);
        srv.setCredentials(IfsUserID, IfsPassword);

        return srv;

        //setup PLSQL command to execute
    }

    public static String getPlsqlBlock(MessageContext messageContext){

        SOAPBody soapBody = messageContext.getEnvelope().getBody();
        String plsqlBlock = "";

        for (Iterator itr = soapBody.getChildElements(); itr.hasNext();)
        {
            OMElement child = (OMElement)itr.next();
            if (child.getLocalName() == constants.REQUEST) // <request> element
            {
                for (Iterator ItrRequest = child.getChildElements(); ItrRequest.hasNext();)
                {
                    OMElement child_lv1 = (OMElement)ItrRequest.next();
                    if (child_lv1.getLocalName() == constants.PLSQLBLOCK) { // <plsqlBlock> element
                        plsqlBlock = child_lv1.getText();
                        System.out.println("------------------------PLSQL---------------------------------");
                        System.out.println(plsqlBlock);
                    }
                }
            }
        }
        return plsqlBlock;
    }

    public static HashMap getBindVariables(MessageContext messageContext){
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
                    if (child_lv1.getLocalName() == constants.BINDVARIABLES) { // <bindVariables> element
                        System.out.println("++++++++++++++++++++");
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
