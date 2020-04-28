package no.bankid.outgoing.ra;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Map;
import java.util.UUID;

@Schema(description = "Request body content for asking an RA to send an activation code to an endUser")
public class NotifyUserOfActivationRequestBody extends AuthenticationBody {
    @Schema(description = "Information about the activation")
    public static class ActivationMetadata {
        @Schema(description = "The flow used for activation, meanings:" +
                "<ul>" +
                "<li> - single_activation_code: A single activation code provided over a secure channel. </li>" +
                "<li> - activation_codes: Activation codes provided over two insecure channels. </li>" +
                "<li> - bankid_auth: Activation through a BankID authentication. </li>" +
                "</ul>", example = "bankid_auth")
        public enum FlowType {
            single_activation_code, activation_codes, bankid_auth
        }

        public FlowType flow;
        @Schema(description = "The distribution methods")
        public String[] via_distribution_methods;

        public ActivationCodeLocale locale;
        @Schema(description = "Map of localized strings explaining how the device was activated",
                example = "{" +
                        "\"no\": \"med aktiveringskoder gitt på email til que***@xyz.no og på sms til XXX XX X42\"," +
                        "\"en\": \"using activation codes by email to que***@xyz.no and sms to XXX XX X42\"" +
                        "}")
        public Map<ActivationCodeLocale, String> human_readable;


    }

    @Schema(description = "The id of this activation attempt, used for for logging")
    public UUID activation_id;
    @Schema(description = "A string identifying the device activated to the user. e.g: \"Galaxy S10\" or \"My Phone\"")
    public String device_name;
    public ActivationMetadata activationMetadata;
}
