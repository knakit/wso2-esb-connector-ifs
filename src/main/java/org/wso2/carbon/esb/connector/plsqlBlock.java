package org.wso2.carbon.esb.connector;

import ifs.fnd.ap.*;
import org.apache.axiom.om.OMElement;
import org.apache.synapse.MessageContext;
import org.apache.synapse.SynapseException;
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

        String plsqlBlock = xmlUtil.getElementFromXmlRequest(messageContext, constants.PLSQLBLOCK);

        try {
            Server srv = ifsUtil.connect(messageContext);
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

            RecordCollection recCollection = new RecordCollection();
            recCollection.add(cmd.getBindVariables());

            //pack outputs of bind variables
            Iterator response_itr = set.iterator();
            while(response_itr.hasNext()) {
                Map.Entry bind = (Map.Entry)response_itr.next();
                bindVariables.put( (String) bind.getKey(), (String) cmd.getBindVariables().findValue((String) bind.getKey()));
            }

            OMElement resultOM = xmlUtil.generateResultXML(bindVariables);

            resultPayloadCreate.preparePayload(messageContext, resultOM);

        } catch (APException e) {
            OMElement errorOM = xmlUtil.generateErrorXML("APException", e.getMessage());
            resultPayloadCreate.preparePayload(messageContext, errorOM);
            //handleException(e.getMessage(), e, messageContext);
            //throw new SynapseException("error while executing PL/SQL block", e);
        }
    }
}
