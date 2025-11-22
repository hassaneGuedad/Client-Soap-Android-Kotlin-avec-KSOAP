Server module for local SOAP testing

How to run (Windows PowerShell):

1. From project root:

```powershell
cd C:\Users\youbitech\AndroidStudioProjects\SOAPCompteApp
.\gradlew.bat :server:run
```

2. After startup, open in your browser:

http://localhost:8082/services/ws?wsdl

If the WSDL is visible the service is running.

