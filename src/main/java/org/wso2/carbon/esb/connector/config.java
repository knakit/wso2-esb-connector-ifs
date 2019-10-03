package org.wso2.carbon.esb.connector;

import org.wso2.carbon.connector.core.AbstractConnector;
import org.wso2.carbon.connector.core.AbstractConnector;
import org.wso2.carbon.connector.core.ConnectException;
import org.apache.synapse.MessageContext;

import org.wso2.carbon.connector.core.util.ConnectorUtils;
import org.wso2.carbon.esb.connector.util.constants;

public class config extends AbstractConnector {
    public void connect(MessageContext messageContext) throws ConnectException {
        try {
            String IfsConnURL = (String) getParameter(messageContext, constants.IFSCONNURL);
            String IfsVersion = (String) getParameter(messageContext, constants.IFSVERSION);
            String IfsUserID = (String) getParameter(messageContext, constants.IFSUSERID);
            String IfsPassword = (String) getParameter(messageContext, constants.IFSPASSWORD);

            messageContext.setProperty(constants.IFSCONNURL, IfsConnURL);
            messageContext.setProperty(constants.IFSVERSION, IfsVersion);
            messageContext.setProperty(constants.IFSUSERID, IfsUserID);
            messageContext.setProperty(constants.IFSPASSWORD, IfsPassword);
        }
        catch (Exception e) {
            log.error("IFS Connector: Error Initializing the IFS settings." + " Error:" + e.getMessage());
            handleException("IFS Connector: Error Initializing the IFS settings", e, messageContext);
        }
    }
}
