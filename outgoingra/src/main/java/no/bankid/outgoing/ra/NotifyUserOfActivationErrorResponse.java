package no.bankid.outgoing.ra;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "The result of asking an ra to notify a user of activation, used in case of some error")
public class NotifyUserOfActivationErrorResponse {
    public enum Status {
        unknown,
        internal_error,
    }

    public Status status;

    public String error;
}
