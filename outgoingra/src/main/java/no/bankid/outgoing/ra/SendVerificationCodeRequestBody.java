package no.bankid.outgoing.ra;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

@Schema(description = "Request body content for asking an RA to send verification code to enduser")
public class SendVerificationCodeRequestBody {

    //The id of this activation attempt, will be the same for both codes, an uuid actually
    public UUID activation_id;
    public Msisdn msisdn;
    public ActivationCodeLocale locale;
    // four digit code
    public String verification_code;
    // Time when code expire, ms since epoch
    public long exp;
}
