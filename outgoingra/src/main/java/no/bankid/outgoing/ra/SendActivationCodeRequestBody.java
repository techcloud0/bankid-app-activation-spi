package no.bankid.outgoing.ra;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

@Schema(description = "Request body content for asking an RA to send an activation code to an endUser")
public class SendActivationCodeRequestBody extends AuthenticationBody {

    public enum ActivationCodeType { digits, words }

    @Schema(description = "The id of this activation attempt, used for for logging, will be the same for both codes")
    public UUID activation_id;
    @Schema(description = "One of the method identifiers returned from check_user_selfservice call")
    public String distribution_method;
    public ActivationCodeLocale locale;
    @Schema(description = "Whether the activation code is a number or two code words")
    public ActivationCodeType activation_code_type;
    @Schema(description = "The code to distribute")
    public String activation_code;
    @Schema(description = "Time when code expire, ms since epoch, UTC")
    public long exp;
}
