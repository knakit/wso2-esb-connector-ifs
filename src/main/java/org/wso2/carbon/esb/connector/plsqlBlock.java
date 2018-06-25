package org.wso2.carbon.esb.connector;

import ifs.fnd.ap.*;
import org.apache.axiom.om.OMElement;
import org.apache.synapse.MessageContext;
import org.wso2.carbon.connector.core.AbstractConnector;
import org.wso2.carbon.connector.core.ConnectException;
import org.wso2.carbon.connector.core.util.ConnectorUtils;
import org.wso2.carbon.esb.connector.util.constants;
import org.wso2.carbon.esb.connector.util.ifsUtil;
import org.wso2.carbon.esb.connector.util.resultPayloadCreate;
import org.wso2.carbon.esb.connector.util.xmlUtil;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.Map;

public class plsqlBlock extends AbstractConnector {

    @Override
    public void connect(MessageContext messageContext) throws ConnectException {

        //IFS connection details
        String IfsConnURL = (String)ConnectorUtils.lookupTemplateParamater(messageContext, constants.IFSCONNURL);
        String IfsVersion = (String)ConnectorUtils.lookupTemplateParamater(messageContext, constants.IFSVERSION);
        String IfsUserID = (String)ConnectorUtils.lookupTemplateParamater(messageContext, constants.IFSUSERID);
        String IfsPassword = (String)ConnectorUtils.lookupTemplateParamater(messageContext, constants.IFSPASSWORD);

        String plsqlBlock = ifsUtil.getPlsqlBlock(messageContext);

        try {
            Server srv = ifsUtil.connect(IfsConnURL, IfsVersion, IfsUserID, IfsPassword);
            PlsqlCommand cmd = new PlsqlCommand(srv, plsqlBlock);

            //add bind variables
            HashMap bindVariables = ifsUtil.getBindVariables(messageContext);
            Set set = bindVariables.entrySet();
            Iterator request_itr = set.iterator();

            while(request_itr.hasNext()) {
                Map.Entry bind = (Map.Entry)request_itr.next();
                cmd.getBindVariables().add((String)bind.getKey(), (String)bind.getValue()).setBindVariableDirection(BindVariableDirection.IN_OUT);
            }

            //start invoke
            cmd.execute();

            //pack outputs of bind variables
            Iterator response_itr = set.iterator();
            while(response_itr.hasNext()) {
                Map.Entry bind = (Map.Entry)response_itr.next();
                bindVariables.put(bind.getKey(), cmd.getBindVariables().findValue((String) bind.getKey()));
            }


            OMElement resultOM = xmlUtil.generateResultXML(bindVariables);
            resultPayloadCreate.preparePayload(messageContext, resultOM);

        } catch (APException e) {
            log.error("error while executing PL/SQL block in " + IfsConnURL + " Error:" + e.getMessage());
            handleException(e.getMessage(), e, messageContext);
        }


    }
}
