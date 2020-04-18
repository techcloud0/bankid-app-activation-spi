package no.bankid.outgoing.ra;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Map;

@Schema(description = "Request body content for asking an RA to send an activation code to an endUser")
public class NotifyUserOfActivationRequestBody {

    public static class ActivationMetadata {
        /**
         *  The flow used for activation, valid values as of writing:
         *  <ul>
         *  <li> - single_activation_code: A single activation code provided over a secure channel. </li>
         *  <li> - activation_codes: Activation codes provided over two insecure channels. </li>
         *  <li> - bankid_auth: Activation through a BankID authentication. </li>
         *  </ul>
         */
        public enum FlowType { single_activation_code, activation_codes, bankid_auth }

        public FlowType flow;
        public String[] via_distribution_methods;
        public ActivationCodeLocale locale;
        // Map of localized strings explaining how the device was activated. e.g:
        //  {
        //     "no": "med aktiveringskoder gitt på email til que***@xyz.no og på sms til XXX XX X42",
        //     "en": "using activation codes by email to que***@xyz.no and sms to XXX XX X42"
        //  }
        public Map<ActivationCodeLocale, String> human_readable;


    }
    // A string identifying the device activated to the user. e.g: "Galaxy S10" or "My Phone"
    public String deviceName;
    // Data about the activation
    public ActivationMetadata activationMetadata;
}
