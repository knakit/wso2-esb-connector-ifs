package org.wso2.carbon.esb.connector;

import org.apache.axiom.om.OMElement;
import org.apache.synapse.MessageContext;
import org.wso2.carbon.connector.core.AbstractConnector;
import org.wso2.carbon.connector.core.ConnectException;
import org.wso2.carbon.connector.core.util.ConnectorUtils;

import org.wso2.carbon.esb.connector.util.constants;
import org.wso2.carbon.esb.connector.util.ifsUtil;
import org.wso2.carbon.esb.connector.util.xmlUtil;
import org.wso2.carbon.esb.connector.util.resultPayloadCreate;

import ifs.fnd.ap.*;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;


public class selectStatement extends AbstractConnector {

    @Override
    public void connect(MessageContext messageContext) throws ConnectException {


        String SqlStatement = xmlUtil.getElementFromXmlRequest(messageContext, constants.SQLSTATEMENT);

        try {

            Server srv = ifsUtil.connect(messageContext);

            //setup PLSQL command to execute
            PlsqlSelectCommand cmd = new PlsqlSelectCommand(srv, SqlStatement);

            //add bind variables
            HashMap bindVariables = ifsUtil.getBindVariables(messageContext);
            Set set = bindVariables.entrySet();
            Iterator request_itr = set.iterator();

            while(request_itr.hasNext()) {
                Map.Entry bind = (Map.Entry)request_itr.next();
                cmd.getBindVariables().add((String)bind.getKey(), (String)bind.getValue());
            }

            //start invoke
            RecordCollection result = cmd.executeQuery();

            if(result!=null){
                OMElement resultOM = xmlUtil.generateResultXML(result);
                resultPayloadCreate.preparePayload(messageContext, resultOM);
            }
        } catch (APException e) {
            log.error("error while executing select statement. Error:" + e.getMessage());
            handleException(e.getMessage(), e, messageContext);
        }


    }

}