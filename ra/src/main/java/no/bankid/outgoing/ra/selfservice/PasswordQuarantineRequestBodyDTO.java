package no.bankid.outgoing.ra.selfservice;

import io.swagger.v3.oas.annotations.media.Schema;
import no.bankid.outgoing.ra.AuthenticationBodyDTO;

import java.util.UUID;

@Schema(description = "Request body content for quarantining the user password")
public class PasswordQuarantineRequestBodyDTO extends AuthenticationBodyDTO {

    @Schema(description = "The id of this activation attempt, used for for logging")
    public UUID activation_id;
    @Schema(description = "Time when quarantine expire, ms since epoch, UTC")
    public long quarantine_until;
}
