# Configuring IFS-WSO2 Operations
>NOTE: If your IFS instance uses HTTPS connection, you may have to add the certificate to WSO2 keystore
```
keytool -importcert -file IFS_HOME\instance\INSTANCE\security\certs\import\INSTANCE.cer -keystore WSO2_EI_HOME\repository\resources\security\client-truststore.jks -alias "IFS"
```

In this example we create a basic proxy in WSO2 to open the connector operations and return the result. But WSO2 can be configure in many ways to mediate the request and response.

## Configuring Proxy

1. Open the Management Console of WSO2 EI
2. On the **Services** node, select Add, **Proxy Service**
3. Select **Custom Proxy** from the templates
4. Press **Switch to source view**
5. You need to create a Proxy services for each operation

**Property description**
- `IfsConnURL` : IFS Connection URL with port
- `IfsVersion` : IFS version (APPS75, APPS8, APPS9, APPS10)
- `IfsUserID` : IFS user ID to execute the request. This user should have FND_CONNECT permission set and corresponding permission to the database objects which are accessed.
- `IfsPassword` : IFS User password 


### Proxy-Execute SQL Statement

```xml
<?xml version="1.0" encoding="UTF-8"?>
<proxy name="Apps10_SQL" startOnLoad="true" transports="http https" xmlns="http://ws.apache.org/ns/synapse">
   <target>
      <inSequence>
         <log description="request" level="full"/>
         <knakitifs.init>
            <IfsConnURL>IFS_URL:PORT</IfsConnURL>
            <IfsVersion>APPS10</IfsVersion>
            <IfsUserID>IFS_USER</IfsUserID>
            <IfsPassword>IFS_USER_PW</IfsPassword>
         </knakitifs.init>
         <knakitifs.selectStatement/>
         <log description="response" level="full"/>
         <respond description="Response"/>
      </inSequence>
      <outSequence/>
      <faultSequence/>
   </target>
</proxy>

```



### Proxy-Execute PLSQL Block

```xml
<?xml version="1.0" encoding="UTF-8"?>
<proxy name="Apps10_PLSQL_Block" startOnLoad="true" transports="http https" xmlns="http://ws.apache.org/ns/synapse">
   <target>
      <inSequence>
         <log description="request" level="full"/>
         <knakitifs.init>
            <IfsConnURL>IFS_URL:PORT</IfsConnURL>
            <IfsVersion>APPS10</IfsVersion>
            <IfsUserID>IFS_USER</IfsUserID>
            <IfsPassword>IFS_USER_PW</IfsPassword>
         </knakitifs.init>
         <knakitifs.plsqlBlock/>
         <log description="response" level="full"/>
         <respond description="Response"/>
      </inSequence>
      <outSequence/>
      <faultSequence/>
   </target>
</proxy>

```

### Proxy-Execute PL/SQL base method for NEW, MODIFY, REMOVE

```xml
<?xml version="1.0" encoding="UTF-8"?>
<proxy name="Apps10_PLSQL_Base" startOnLoad="true" transports="http https" xmlns="http://ws.apache.org/ns/synapse">
   <target>
      <inSequence>
         <log description="request" level="full"/>
         <knakitifs.init>
            <IfsConnURL>IFS_URL:PORT</IfsConnURL>
            <IfsVersion>APPS10</IfsVersion>
            <IfsUserID>IFS_USER</IfsUserID>
            <IfsPassword>IFS_USER_PW</IfsPassword>
         </knakitifs.init>
         <knakitifs.plsqlBaseMethod/>
         <log description="response" level="full"/>
         <respond description="Response"/>
      </inSequence>
      <outSequence/>
      <faultSequence/>
   </target>
</proxy>
```

# Sample XML requests

## Execute SQL statement and retrieve records from IFS

This works just like normal SQL
- `<bindVariables>` : used to input SQL bind variables
- `<sqlStatement>` : SQL statement to be execute

```xml
<request>
	<bindVariables>
		<ORACLE_USER>A%</ORACLE_USER>
		<ACTIVE>FALSE</ACTIVE>
	</bindVariables>
	<sqlStatement>
      SELECT IDENTITY, DESCRIPTION as “User_Name”, WEB_USER FROM FND_USER
    	where ORACLE_USER like :ORACLE_USER
    	and ACTIVE = :ACTIVE
    	order by oracle_user
	</sqlStatement>
</request>

```

>NOTE: SELECT * will not work. Need to define all required columns and column alias can be used.
>Result XML will use the column names defined in the SQL


## Execute PL/SQL base method for NEW, MODIFY, REMOVE
This can be used to execute IFS base methods (NEW, MODIFY, REMOVE)

- `<record>` : works similar to IFS attribute string. Need to provide the exact column names as record elements like defining attribute string.

- `<methodType>` : NEW/MODIFY/REMOVE
- `<plsqlPackage>` : name of the PL SQL package
- `<methodAction>` : CHECK/PREPARE/DO

```xml
<request>
    <record>
        <CUSTOMER_ID>DSJ</CUSTOMER_ID>
        <NAME>DSJ Customer 2</NAME>
        <DEFAULT_LANGUAGE>English</DEFAULT_LANGUAGE>
        <COUNTRY>SRI LANKA</COUNTRY>
        <CUSTOMER_CATEGORY>Customer</CUSTOMER_CATEGORY>
        <CREATION_DATE>2018-06-26-00.00.00</CREATION_DATE>
        <DEFAULT_DOMAIN>TRUE</DEFAULT_DOMAIN>
        <PARTY_TYPE>Customer</PARTY_TYPE>
        <IDENTIFIER_REF_VALIDATION>None</IDENTIFIER_REF_VALIDATION>
        <ONE_TIME_DB>FALSE</ONE_TIME_DB>
    </record>
    <methodType>NEW</methodType>
    <plsqlPackage>CUSTOMER_INFO_API</plsqlPackage>
    <methodAction>DO</methodAction>
</request>

```

## Execute PL/SQL Block

Can used to write PLSQL block and execute in IFS and get results.

- `<bindVariables>` : parameters used in the PLSQL block. Refer note for using IN type parameters.
- `<plsqlBlock>` : PLSQL block to execute

```xml
<request>
	<bindVariables>
		<DESC/>
		<ORACLE_USER/>
		<ACTIVE/>
		<GEN_PASSWORD/>
		<IDENTITY>ALAIN</IDENTITY>
	</bindVariables>
	<plsqlBlock>
BEGIN 
	:DESC := IFSAPP.FND_USER_API.GET_DESCRIPTION(:IDENTITY);
	:ORACLE_USER := IFSAPP.FND_USER_API.Get_Oracle_User(:IDENTITY);
	:ACTIVE := IFSAPP.FND_USER_API.Get_ACTIVE(:IDENTITY);
	:GEN_PASSWORD := IFSAPP.FND_USER_API.Create_Random_Pwd__();	
END;
	</plsqlBlock>
</request>
```


