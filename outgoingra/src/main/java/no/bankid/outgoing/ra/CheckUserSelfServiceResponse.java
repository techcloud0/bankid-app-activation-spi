package no.bankid.outgoing.ra;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "The result of asking if an end user is eligible for two-channel activation")
public class CheckUserSelfServiceResponse {
    public enum Status {
        unknown,
        eligibible,
        not_eligibible,
        grace_period,
    }

    public static class DistributionMethod {
        // "sms/email/digipost/phonecall/post/xxx...",        # Type of distribution method
        public String type;
        // An id for this distribution method, must be unique within this response.
        public String id;
        // Hint to user, ex. "XXX XX X42", or "que****@hotmail.com"
        public String hint;
        // When was the contact information last verified by the user.
        public String last_verified;

    }
    // What is the status of the given user
    public Status status;
    // List of distribution methods
    public List<DistributionMethod> distribution_methods;
}
