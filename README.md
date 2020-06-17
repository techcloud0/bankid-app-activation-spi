# BankID app integration
This guide describes how to integrate the BankID App as an HA2 mechanism for a BankID issuing bank.

To enable BankID App the bank's BankID RA system must handle at least the first of these requirements 
* Enable and disable the OTP service _bapp_ manually 
* Enable and disable OTP service _bapp_ automatically by calls from Vipps
* Hold verified end user contact information and send messages to user by SMS and email or post as a response to calls from Vipss. 

The bank needs to open it's RA for calls from Vipps to integrate BankID APP such that end users may activate BankID APP themselves
with no manual interaction from the bank.
 
Support for the basic OTP administration operations is sufficient to add BankID App to end users already having a Netcentric BankID. 
  
For information about API keys, product activation, etc see [Getting Started](https://github.com/vippsas/bankid-app-api/blob/master/bankid-app-getting-started.md).

For API documentation and development guidelines please see our [BankID app API guide](https://github.com/vippsas/bankid-app-api/blob/master/bankid-app-api.md).

You can peruse the SPI reference documentation as [Swagger](https://vippsas.github.io/bankid-app-api/) or [ReDoc](https://vippsas.github.io/bankid-app-api/redoc.html).

Building this documentation, run 

`mvn clean compile -f outgoingra`

the result will be placed in docs catalogue 
