package org.wso2.carbon.esb.connector;

import org.apache.axiom.om.OMElement;
import org.apache.synapse.MessageContext;
import org.wso2.carbon.connector.core.AbstractConnector;
import org.wso2.carbon.connector.core.ConnectException;
import org.wso2.carbon.connector.core.util.ConnectorUtils;

import org.wso2.carbon.esb.connector.util.ifsUtil;
import org.wso2.carbon.esb.connector.util.resultPayloadCreate;

import ifs.fnd.ap.*;

public class selectStatement extends AbstractConnector {

    @Override
    public void connect(MessageContext messageContext) throws ConnectException {

        //IFS connection details

        String IfsConnURL = (String)ConnectorUtils.lookupTemplateParamater(messageContext, "IfsConnURL");
        String IfsVersion = (String)ConnectorUtils.lookupTemplateParamater(messageContext, "IfsVersion");
        String IfsUserID = (String)ConnectorUtils.lookupTemplateParamater(messageContext, "IfsUserID");
        String IfsPassword = (String)ConnectorUtils.lookupTemplateParamater(messageContext, "IfsPassword");

        String SqlStatement = (String)ConnectorUtils.lookupTemplateParamater(messageContext, "SqlStatement");

        try {

            // Create a server and invoke server
            Server srv = new Server();

            srv.setConnectionString(IfsConnURL);
            srv.setCredentials(IfsUserID, IfsPassword);

            //setup PLSQL command to execute
            PlsqlSelectCommand cmd = new PlsqlSelectCommand(srv, SqlStatement);

            //start invoke
            RecordCollection result = cmd.executeQuery();


            if(result!=null){
                OMElement resultOM = ifsUtil.generateResultXML(result);

                resultPayloadCreate.preparePayload(messageContext, resultOM);

            }
        } catch (Exception e) {
            System.out.print(e.toString());
        }

    }

}