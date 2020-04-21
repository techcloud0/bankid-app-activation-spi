package no.bankid.outgoing.ra;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

@Schema(description = "Request body content for asking an RA to send code words to an endUser")
public class SendCodeWordsRequestBody {

    //The id of this activation attempt, will be the same for both codes, an uuid actually
    public UUID activation_id;
    // One of the method identifiers returned from selfsercive/check_user call
    public String distribution_method;
    // Locale of initiating app, either "no" for Norwegian and Norwegian dialects, otherwise "en".
    public ActivationCodeLocale locale;
    // The twe code words to distribute
    public String code_words;
    // Time when code expire, ms since epoch
    public long exp;
}
