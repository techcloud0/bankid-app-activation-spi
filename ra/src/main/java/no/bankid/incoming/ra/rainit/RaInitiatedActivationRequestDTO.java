package no.bankid.incoming.ra.rainit;

import io.swagger.v3.oas.annotations.media.Schema;
import no.bankid.outgoing.ra.AuthenticationBodyDTO;

public class RaInitiatedActivationRequestDTO extends AuthenticationBodyDTO {

    @Schema(description = "" +
            "The netcentric BankID which BankID-app should be activated against. " +
            "If the order id of a netcentric BankID is provided, the returned QR-code will activate BankID-app for that BankID without further validation. " +
            "If the order id is null, this is a request for first time issuance and BankID-app will guide the user through an identity proofing procedure, before activating the app.",
            required = true, nullable = true)
    public String order_id;

    @Schema(description = "Requested expiration for the returned QR code. Provided in seconds from from \"now\"", example = "1209600", required = true)
    public int expiry;
}
