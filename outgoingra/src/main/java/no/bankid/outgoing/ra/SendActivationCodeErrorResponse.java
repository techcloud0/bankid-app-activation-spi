package no.bankid.outgoing.ra;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "The result of asking an ra to send an activation code, used in case of some error")
public class SendActivationCodeErrorResponse {
    public enum Status {
        unknown,
        inactive,
        rejected,
        internal_error,
    }

    public Status status;

    public String error;
}
