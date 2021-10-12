package no.bankid.incoming.ra.callback;

import io.swagger.v3.oas.annotations.media.Schema;
import no.bankid.outgoing.ra.AuthenticationBodyDTO;

import java.util.UUID;

@Schema(description = "")

public class ActivationValidationCallbackRequestDTO extends AuthenticationBodyDTO {

    public enum Result {
        SUCCESS,
        NO_ID_DOCUMENT,
        WRONG_ID_DOCUMENT,
        TIMEOUT,
        BANK_ID_NOT_AVAILABLE,
        BLOCKED_BY_BANK,
        BLOCKED_IN_ODS,
        INTERNAL_ERROR
    }

    @Schema(description = "The activation id used to track this activation throughout the flow.", required = true)
    public UUID activation_id;

    @Schema(description= "The final status of the validation request." +
            "<ul>" +
            "<li>SUCCESS - The  request has been successfully processed. </li>" +
            "<li>NO_ID_DOCUMENT - The activation request timed out before receiving a ID-document. This error takes precedence over TIMEOUT.</li>" +
            "<li>WRONG_ID_DOCUMENT - The ID document did not match the activation request.</li>" +
            "<li>TIMEOUT - The request did not complete before the timeout.</li>" +
            "<li>BANK_ID_NOT_AVAILABLE - end user doesn't have active BankID</li>" +
            "<li>BLOCKED_BY_BANK - end user is not allowed to perform the requested operation</li>" +
            "<li>BLOCKED_IN_ODS - the request was blocked by ODS.</li>" +
            "<li>INTERNAL_ERROR - the request failed for another unspecified reason.</li>" +
            "</ul>", required = true)
    public Result result;

    @Schema(description = "The order ID of the certificate for which the Validation was performed. Required when result=COMPLETED", example = "<orderId>", required = true, nullable = true)
    public String certificate_order_id;
}