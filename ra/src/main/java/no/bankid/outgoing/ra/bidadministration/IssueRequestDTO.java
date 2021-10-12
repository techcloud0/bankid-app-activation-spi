package no.bankid.outgoing.ra.bidadministration;

import io.swagger.v3.oas.annotations.media.Schema;
import no.bankid.outgoing.ra.AuthenticationBodyDTO;

import java.util.UUID;

@Schema(description = "Request to reissue a BankID")
public class IssueRequestDTO extends AuthenticationBodyDTO {

    @Schema(description = "An activation id used to track this activation throughout the flow.", required = true, example = "123e4567-e89b-12d3-a456-426614174000")
    public UUID activation_id;

    @Schema(description = "The handover ID used to track the ID-check session.", required = true, example = "123e4567-e89b-12d3-a456-426614174000")
    public UUID identity_check_handover_id;

    @Schema(description = "The order ID of the user's certificate.", required = true, nullable = true, example = "<orderId>")
    public String order_id;

    @Schema(description = "The reference to an msisdn previously received through a call to /selfservice/check_user", required = true, example = "<password>")
    public String msisdn_reference_id;

    @Schema(description = "The timeout for the reissue request, measured in seconds. If the operation cannot be completed " +
            "before the timeout, the RA is free to drop the request, and should return the TIMEOUT status to BankID-app.",
            required = true,
    example = "60")
    public int timeout;
}