package no.bankid.incoming.ra.rainit;

import io.swagger.v3.oas.annotations.media.Schema;
import no.bankid.outgoing.ra.AuthenticationBodyDTO;

import java.util.UUID;

public class RaInitiatedActivationResponseDTO extends AuthenticationBodyDTO {
    @Schema(description = "An activation id used to track this activation throughout subsequent requests.")
    public UUID activation_id;

    @Schema(description = "Base64 encoded SVG QR image.")
    public String qr_image;

    @Schema(description = "Raw data encoded in qr_image.")
    public String qr_raw;
}
