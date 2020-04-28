package no.bankid.outgoing.ra;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "The result of querying RA about endUser distribution methods")
public class SelfServiceCheckUserResponse {

    public static class DistributionMethod {
        // "email/digipost/post/xxx...",        # Type of distribution method
        public String type;
        // An id for this distribution method, must be unique within this response.
        public String id;
        // Hint to user, ex. "XXX XX X42", or "que****@hotmail.com"
        public String hint;
        // When was the contact information last verified by the user, epoch milliseconds
        public long last_verified;

    }
    @Schema(description = "Time when the user's password was last reset, ms since epoch, UTC")
    public long pw_reset_timestamp;
    // True if the given phone number matches the what is registered for the given user.
    public boolean correct_msisdn;
    // List of distribution methods
    public List<DistributionMethod> distribution_methods;
}
