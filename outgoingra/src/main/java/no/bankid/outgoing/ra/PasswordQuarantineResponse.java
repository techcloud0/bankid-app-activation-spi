package no.bankid.outgoing.ra;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

@Schema(description = "Resporse when quarantining an endUser password")
public class PasswordQuarantineResponse {
    // Timestamp when user's password was last reset, ms since epoch.
    public long pw_reset_timestamp;
}
