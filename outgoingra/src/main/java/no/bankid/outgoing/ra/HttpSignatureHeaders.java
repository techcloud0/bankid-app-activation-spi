package no.bankid.outgoing.ra;

import javax.ws.rs.core.HttpHeaders;

public interface HttpSignatureHeaders {
    String SIGNATURE = "Signature";
    String DATE = HttpHeaders.DATE;
    String DIGEST = "digest";

    String X_CLIENT_CLIENTNAME = "X-CLIENT-CLIENTNAME";
    String X_CLIENT_REQUESTID = "X-CLIENT-REQUESTID";

    String X_DATAOWNERORGID = "X-DATAOWNERORGID";
    String X_CUSTOMERID = "X-CUSTOMERID";
}
