package org.wso2.carbon.esb.connector.util;

import ifs.fnd.ap.*;

import org.apache.axiom.om.OMElement;
import org.apache.axiom.soap.SOAPBody;
import org.apache.synapse.MessageContext;

import java.util.Iterator;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.lang.String;


public class ifsUtil {

    public static Server connect(String IfsConnURL, String IfsVersion, String IfsUserID, String IfsPassword){
        // Create a server and invoke server
        Server srv = new Server();

        srv.setConnectionString(IfsConnURL);
        srv.setCredentials(IfsUserID, IfsPassword);

        switch(IfsVersion) {
            case "APPS75":
            case "APPS8":
            case "APPS9":
                srv.setLegacyUrl(true);
                break;
            case "APPS10":
                srv.setLegacyUrl(false);
                break;
            default:
                break;
        }
        return srv;

        //setup PLSQL command to execute
    }

    public static PlsqlBaseMethodType getPlsqlBaseMethodType(MessageContext messageContext){

        String methodType_ = xmlUtil.getElementFromXmlRequest(messageContext,constants.METHODTYPE);
        PlsqlBaseMethodType baseMethodType = PlsqlBaseMethodType.NEW;//how valid is this?
        switch(methodType_) {
            case "NEW":
                baseMethodType =  PlsqlBaseMethodType.NEW;
                break;
            case "MODIFY":
                baseMethodType =  PlsqlBaseMethodType.MODIFY;
                break;
            case "REMOVE":
                baseMethodType =  PlsqlBaseMethodType.REMOVE;
            break;
            default:
                //handle exception
        }
        return baseMethodType;
    }
    public static PlsqlBaseMethodAction getPlsqlBaseMethodAction(MessageContext messageContext){
        String methodAction_ = xmlUtil.getElementFromXmlRequest(messageContext,constants.METHODACTION);
        PlsqlBaseMethodAction baseMethodAction = PlsqlBaseMethodAction.DO;

        switch(methodAction_) {
            case "CHECK":
                baseMethodAction =  PlsqlBaseMethodAction.CHECK;
                break;
            case "PREPARE":
                baseMethodAction =  PlsqlBaseMethodAction.PREPARE;
                break;
            case "DO":
                baseMethodAction =  PlsqlBaseMethodAction.DO;
                break;
            default:
                //handle exception
        }
        return baseMethodAction;
    }
    public static HashMap getBindVariables(MessageContext messageContext){
        //rewrite to return cmd
        return xmlUtil.getElementCollectionFromXmlRequest(messageContext, constants.BINDVARIABLES);
    }
    public static Record getRecordAttr(MessageContext messageContext){
        //rewrite to return cmd
        HashMap<String, String> recordAttrMap = new HashMap<>();
        Record rec_ = new Record();

        recordAttrMap =  xmlUtil.getElementCollectionFromXmlRequest(messageContext, constants.RECORD);

        Set set = recordAttrMap.entrySet();
        Iterator request_itr = set.iterator();

        while(request_itr.hasNext()) {
            Map.Entry bind = (Map.Entry)request_itr.next();
            //temp fix to make custom field update
            if (bind.getKey().toString().startsWith("CF_")) rec_.add(bind.getKey().toString().replace("CF_", "CF$_"), (String)bind.getValue());
            else rec_.add((String)bind.getKey(), (String)bind.getValue());


        }
        return rec_;
    }
}
