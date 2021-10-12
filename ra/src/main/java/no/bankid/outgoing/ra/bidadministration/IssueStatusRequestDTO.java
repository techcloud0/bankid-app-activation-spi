package no.bankid.outgoing.ra.bidadministration;

import io.swagger.v3.oas.annotations.media.Schema;
import no.bankid.outgoing.ra.AuthenticationBodyDTO;

import java.util.UUID;

@Schema(description = "")
public class IssueStatusRequestDTO extends AuthenticationBodyDTO {
    @Schema(description = "The activation id used to track this activation throughout the flow.", required = true)
    public UUID activation_id;
}