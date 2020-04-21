package no.bankid.outgoing.ra;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

@Schema(description = "Request body content for quarantining the user password")
public class PasswordQuarantineRequestBody {

    //The id of this activation attempt, will be the same for both codes, an uuid actually
    public UUID activation_id;
    // Time when quarantine expires, ms since epoch
    public long quarantine_until;
}
