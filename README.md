# IFS-WSO2 ESB Connector
The IFS-WSO2 Connector (project code name knakit) allows you to access Core database objects of IFS ERP, which provides a simple yet powerful web service interface that can be used to perform all possible DML operations using simple XML/JSON syntax.

Backend of the connector was built using IFS JAVA Access Provider making fully compatibility with the IFS framework and security. It opens endless possibilities of integrating IFS with other applications and business scenarios using WSO2 products and connectors.

## Compatibility
IFS-WSO2 Connector is tested with IFS versions APP8, APP9, APP10 and with WSO2 EI 6.X.X

## Download and install the connector
If you are new to [WSO2 ]( https://wso2.com/)and haven’t install it before, first download and install [WSO2 EI]( https://wso2.com/integration/)
1. Download the connector from [build](build/knakitifs-connector-1.0.0.zip)
2. Open the Management Console of WSO2 EI
3. On the **Connectors** node, click **Add** to open the Add Connector page
4. Click **Choose File**, select the required connector ZIP file, and click **Upload**.  The connector should now appear in the Connectors list and is by default disabled.
5. Click on the **Disabled** status icon to enable connector.

## Configuring the connector operations
IFS-WSO2 Connector supports following IFS operations 
1.	Execute SQL statement and retrieve records from IFS
2.	Execute PL/SQL base method for NEW, MODIFY, REMOVE
3.	Execute PL/SQL block and retrieves the values assigned for bind variables

To get started with **IFS-WSO2 Connector** see [Configure]( doc/config.md)



