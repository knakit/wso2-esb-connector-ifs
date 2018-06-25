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


public class selectStatement extends AbstractConnector {

    @Override
    public void connect(MessageContext messageContext) throws ConnectException {

        //IFS connection details

        String IfsConnURL = (String)ConnectorUtils.lookupTemplateParamater(messageContext, constants.IFSCONNURL);
        String IfsVersion = (String)ConnectorUtils.lookupTemplateParamater(messageContext, constants.IFSVERSION);
        String IfsUserID = (String)ConnectorUtils.lookupTemplateParamater(messageContext, constants.IFSUSERID);
        String IfsPassword = (String)ConnectorUtils.lookupTemplateParamater(messageContext, constants.IFSPASSWORD);

        String SqlStatement = (String)ConnectorUtils.lookupTemplateParamater(messageContext, constants.SQLSTATEMENT);

        try {

            Server srv = ifsUtil.connect(IfsConnURL, IfsVersion, IfsUserID, IfsPassword);

            //setup PLSQL command to execute
            PlsqlSelectCommand cmd = new PlsqlSelectCommand(srv, SqlStatement);

            //start invoke
            RecordCollection result = cmd.executeQuery();

            if(result!=null){
                OMElement resultOM = xmlUtil.generateResultXML(result);
                resultPayloadCreate.preparePayload(messageContext, resultOM);
            }
        } catch (APException e) {
            log.error("error while executing select statement in " + IfsConnURL + " Error:" + e.getMessage());
            handleException(e.getMessage(), e, messageContext);
        }


    }

}