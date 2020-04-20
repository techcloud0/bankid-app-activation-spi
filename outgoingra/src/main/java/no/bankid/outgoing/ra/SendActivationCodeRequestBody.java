package no.bankid.outgoing.ra;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Request body content for asking an RA to send an activation code to an endUser")
public class SendActivationCodeRequestBody {

    public enum ActivationCodeType { digits, words }

    //The id of this activation attempt, will be the same for both codes, an uuid actually
    public String correlation_id;
    // One of the method identifiers returned from check_user_selfservice call
    public String distribution_method;
    // Locale of initiating app, either "no" for Norwegian and Norwegian dialects, otherwise "en".
    public ActivationCodeLocale locale;
    // Whether the activation code is a number or two code words
    public ActivationCodeType activation_code_type;
    // The code to distribute
    public String activation_code;
    // Time when code expire, ms since epoch
    public String exp;
}
