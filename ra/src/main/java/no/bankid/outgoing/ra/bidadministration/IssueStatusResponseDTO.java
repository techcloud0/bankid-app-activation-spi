package no.bankid.outgoing.ra.bidadministration;

import io.swagger.v3.oas.annotations.media.Schema;
import no.bankid.outgoing.ra.AuthenticationBodyDTO;

import java.util.UUID;

@Schema
public class IssueStatusResponseDTO extends AuthenticationBodyDTO {
    public enum Status {
        PENDING,
        SUCCESS,
        NO_ID_DOCUMENT,
        WRONG_ID_DOCUMENT,
        TIMEOUT,
        BANK_ID_NOT_AVAILABLE,
        BLOCKED_BY_BANK,
        BLOCKED_IN_ODS,
        UNKNOWN,
        INTERNAL_ERROR
    }

    @Schema(description = "The order ID of the certificate which was reissued. Only required when reissueStatus=REISSUED", example = "<orderId>")
    public String order_id;

    @Schema(description= "The status of the reissue request." +
            "<ul>" +
            "<li>PENDING - The reissue request has is still pending. </li>" +
            "<li>SUCCESS - The reissue request has been successfully processed. </li>" +
            "<li>NO_ID_DOCUMENT - The reissue request timed out before receiving a ID-document.</li>" +
            "<li>WRONG_ID_DOCUMENT - The ID document did not match the reissue request.</li>" +
            "<li>TIMEOUT - The reissue request did not complete before the timeout.</li>" +
            "<li>BANK_ID_NOT_AVAILABLE - end user doesn't have active BankID</li>" +
            "<li>BLOCKED_BY_BANK - end user is not allowed to reissue</li>" +
            "<li>BLOCKED_IN_ODS - the reissue request was blocked by ODS.</li>" +
            "<li>UNKNOWN - The activation id is not known.</li>" +
            "<li>INTERNAL_ERROR - the reissue request failed for another unspecified reason.</li>" +
            "</ul>" +
            "If the status is PENDING or the response code is HTTP 500, BankID-app will call the endpoint again after a delay. " +
            "For any other status, the RA must ensure that this call is idempotent and returns the same response when " +
            "called multiple times.")
    public Status status;

    @Schema(description = "If the ra is experiencing increased load, it MAY return a non-zero value for backoffMs in " +
            "which case the poller will wait at least backoffMs before polling again on this session.",
            required = true,
            defaultValue = "0",
            example = "2500",
            maximum = "10000",
            minimum = "0"
    )
    public int backoff_ms;

}