package no.bankid.outgoing.ra;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

@Schema(description = "Request body content for asking an RA about endUser distribution methods")
public class SelfServiceCheckuserRequestBody {

    //The id of this activation attempt, will be the same for both codes, an uuid actually
    public UUID activation_id;
    public Msisdn msisdn;
}
