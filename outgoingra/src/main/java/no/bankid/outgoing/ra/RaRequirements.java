package no.bankid.outgoing.ra;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.annotations.tags.Tag;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static no.bankid.outgoing.ra.HttpSignatureHeaders.DATE;
import static no.bankid.outgoing.ra.HttpSignatureHeaders.DIGEST;
import static no.bankid.outgoing.ra.HttpSignatureHeaders.SIGNATURE;

@OpenAPIDefinition(
        info = @Info(
                title = "Integration of BankID App with bank's BankID RA service",
                version = "1.2",
                description = "Defines methods needed to be implemented by a Registration Authority service in banks " +
                        "wanting to add BankID App as an OTP mechanism for their BankID Netcentric users.<p>" +
                        "If an RA service implements these methods, than the activation of an BankID App will use " +
                        "them to connect the BankID App with the endUser's BankID.</p>"
        ),
        tags = {
                @Tag(name = "Basic RA Requirements",
                        description = "Adds or deletes BankID App from an endUser's BankID"),
                @Tag(name = "Activation without Code Device",
                        description = "Activation of BankID App without no other Code Device")

        },
        servers = {
                @Server(description = "Preprod Ra-lite",
                        url = "https://ra-preprod.bankidnorge.no/api/enduser/bankid/netcentric/vipps/bapp")
        }

)


@Path("/")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface RaRequirements {

    String DESCRIPTION_SIGNATURE = "The Signature element, as described " +
            "in <a href=\"https://tools.ietf.org/html/draft-cavage-http-signatures-12\">Internet-Draft - Signing HTTP Messages</a>.";
    String EXAMPLE_SIGNATURE = "keyId=\"fa998090\",algorithm=\"rsa-sha256\"," +
            "headers=\"(request-target) date x-client-clientname x-dataownerorgid " +
            "x-client-requestid x-customerid\",signature=\"o7zK892....\"";

    String DESCRIPTION_DATE = "The date element, the preferred format specified in " +
            "<a href=\"https://tools.ietf.org/html/rfc7231#section-7.1.1.1\">RFC 7231 - Hypertext Transfer Protocol (HTTP/1.1): Semantics and Content</a>." +
            "<p><b>Implementation tips:</b> Format in Java is 'EEE, dd MMM yyyy HH:mm:ss zzz', locale english.</p>";
    String EXAMPLE_DATE = "Mon, 16 Sep 2019 12:12:21 GMT";

    String DESCRIPTION_DIGEST = "hash of body, SHA-256 used" +
            "<p><b>Implementation tips:</b>MessageDigest.getIinstance(\"SHA-256\") is not thread safe in Java</p>";
    String EXAMPLE_DIGEST = "SHA-256=X48E9qOokqqrvdts8nOJRJN3OWDUoyWxBf7kbu9DBPE=";

    @Operation(summary = "Adds BankID App to an endUser"
            , description = "Adds BankID App to an endUser's BankID OTP mechanisms in a given bank"
            , tags = {"Basic RA Requirements"}
    )

    @ApiResponse(responseCode = "200", description = "If status returned is valid",
            content = @Content(schema = @Schema(implementation = OTPAddResponse.class))
    )
    @ApiResponse(responseCode = "400", description = "In case of error")
    @ApiResponse(responseCode = "500", description = "In case of error",
            content = @Content(schema = @Schema(implementation = SimpleErrorResponse.class))
    )
    @Path("add")
    @PUT
    Response addBappOtp(
            @Parameter(description = DESCRIPTION_SIGNATURE,
                    example = EXAMPLE_SIGNATURE,
                    required = true)
            @HeaderParam(SIGNATURE) String httpSignature,
            @Parameter(description = DESCRIPTION_DATE,
                    example = EXAMPLE_DATE,
                    required = true)
            @HeaderParam(DATE) String date,
            @Parameter(description = DESCRIPTION_DIGEST,
                    example = EXAMPLE_DIGEST,
                    required = true)
            @HeaderParam(DIGEST) String digest,
            AuthenticationBody authenticationBody
    );

    @Operation(summary = "Gets the BankID App OTP status for an endUser"
            , description = "Checks whether an endUser has BankID App enabled as an OTP mechanism " +
            "for at least one of his BankIDs in a given bank"
            , tags = {"Basic RA Requirements"}
    )
    @ApiResponse(responseCode = "200", description = "If status returned is valid",
            content = @Content(schema = @Schema(implementation = OTPStatusResponse.class))
    )
    @ApiResponse(responseCode = "400", description = "In case of error")
    @ApiResponse(responseCode = "500", description = "In case of error",
            content = @Content(schema = @Schema(implementation = SimpleErrorResponse.class))
    )
    @Path("status")
    @PUT
    Response getBappOtpStatus(
            @Parameter(description = DESCRIPTION_SIGNATURE,
                    example = EXAMPLE_SIGNATURE,
                    required = true)
            @HeaderParam(SIGNATURE) String httpSignature,
            @Parameter(description = DESCRIPTION_DATE,
                    example = EXAMPLE_DATE,
                    required = true)
            @HeaderParam(DATE) String date,
            @Parameter(description = DESCRIPTION_DIGEST,
                    example = EXAMPLE_DIGEST,
                    required = true)
            @HeaderParam(DIGEST) String digest,
            AuthenticationBody authenticationBody
    );

    @Operation(summary = "Removes BankID App from an endUser"
            , description = "Removes BankID App as an endUser's OTP mechanism for at least one of " +
            "his BankIDs in a given bank"
            , tags = {"Basic RA Requirements"}
    )
    @ApiResponse(responseCode = "200", description = "If status returned is valid",
            content = @Content(schema = @Schema(implementation = OTPDeleteResponse.class))
    )
    @ApiResponse(responseCode = "400", description = "In case of error")
    @ApiResponse(responseCode = "500", description = "In case of error",
            content = @Content(schema = @Schema(implementation = SimpleErrorResponse.class))
    )
    @PUT
    @Path("remove")
    Response removeBappOtp(
            @Parameter(description = DESCRIPTION_SIGNATURE,
                    example = EXAMPLE_SIGNATURE,
                    required = true)
            @HeaderParam(SIGNATURE) String httpSignature,
            @Parameter(description = DESCRIPTION_DATE,
                    example = EXAMPLE_DATE,
                    required = true)
            @HeaderParam(DATE) String date,
            @Parameter(description = DESCRIPTION_DIGEST,
                    example = EXAMPLE_DIGEST,
                    required = true)
            @HeaderParam(DIGEST) String digest,
            AuthenticationBody authenticationBody
    );

    /////////////////////////////// TODO: reservenøkkel

    @Operation(
            summary = "Health check of the RA application",
            description = "Checks that the RA is capable of handling endpoints declared here",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "RA is healthy"),
                    @ApiResponse(
                            responseCode = "500",
                            description = "RA is not healthy")}
            , tags = {"Activation without Code Device"})
    @Path("healthcheck")
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    Response healthCheck();

    @Operation(summary = "Check two-channel options for endUser"
            , description = "<p>Endpoint to check if a specific user is eligible for two-channel activation. A user is eligible if:</p><ul><li>They have an active BankID with the current RA.</li><li>They have at least two independent verified points of contact, not violating the following disallowed combinations.<ul><li>sms + phone call</li><li>digipost + post</li><li>the same method used twice, even with different hints.</li></ul></li><li>They have not <span style=\"color: rgb(32,31,30);\">reissued the BankID (password reset)</span> in the last X days. (Subject to regulation by Bits).</li></ul><p>Which method types are supported is up to the individual RA to decide, but at least two independent (i.e. not terminating in the same point) types must be supported. i.e. sms + email is ok, phone call and sms to the same number is not. </p>"
            , tags = {"Activation without Code Device"})
    @ApiResponse(responseCode = "200", description = "If status returned is valid",
            content = @Content(schema = @Schema(implementation = SelfServiceCheckUserResponse.class))
    )
    @ApiResponse(responseCode = "400", description = "In case of error")
    @ApiResponse(responseCode = "500", description = "In case of error",
            content = @Content(schema = @Schema(implementation = SimpleErrorResponse.class))
    )
    @Path("selfservice/check_user")
    @POST
    Response selfServiceCheckUser(
            @Parameter(description = DESCRIPTION_SIGNATURE,
                    example = EXAMPLE_SIGNATURE,
                    required = true)
            @HeaderParam(SIGNATURE) String httpSignature,
            @Parameter(description = DESCRIPTION_DATE,
                    example = EXAMPLE_DATE,
                    required = true)
            @HeaderParam(DATE) String date,
            @Parameter(description = DESCRIPTION_DIGEST,
                    example = EXAMPLE_DIGEST,
                    required = true)
            @HeaderParam(DIGEST) String digest,
            @RequestBody(description = "Activation code and how to distribute", required = true)
                    SelfServiceCheckuserRequestBody selfserviceCheckuserRequestBody
    );

    @Operation(summary = "Request distribution of an a verification code to be sent to a user."
            , description = "Endpoint to request distribution of an a verification code to be sent to a user." +
            " Upon receiving a request on this end-point, the RA should distribute the provided code over " +
            "sms or return an error-code."
            , tags = {"Activation without Code Device"})
    @ApiResponse(responseCode = "200", description = "If all ok, no data is returned") // TODO: bruke 204 NO_CONTENT ???
    @ApiResponse(responseCode = "400", description = "In case of error")
    @ApiResponse(responseCode = "500", description = "In case of error",
            content = @Content(schema = @Schema(implementation = SimpleErrorResponse.class))
    )
    @Path("selfservice/send_verification_code")
    @POST
    Response selfServiceSendVerificationCode(
            @Parameter(description = DESCRIPTION_SIGNATURE,
                    example = EXAMPLE_SIGNATURE,
                    required = true)
            @HeaderParam(SIGNATURE) String httpSignature,
            @Parameter(description = DESCRIPTION_DATE,
                    example = EXAMPLE_DATE,
                    required = true)
            @HeaderParam(DATE) String date,
            @Parameter(description = DESCRIPTION_DIGEST,
                    example = EXAMPLE_DIGEST,
                    required = true)
            @HeaderParam(DIGEST) String digest,
            @RequestBody(description = "Verification code and msisdn", required = true)
                    SendVerificationCodeRequestBody selfserviceSendVerificationCodeRequestBody
    );

    @Operation(summary = "Send codewords to an endUser"
            , description = "request distribution of code words to be sent to a user." +
            " Upon receiving a request on this end-point, the RA should distribute the provided " +
            "code through the channel indicated, or return an error-code."
            , tags = {"Activation without Code Device"})
    @ApiResponse(responseCode = "200", description = "If all ok, no data is returned") // TODO: bruke 204 NO_CONTENT ???
    @ApiResponse(responseCode = "400", description = "In case of error")
    @ApiResponse(responseCode = "500", description = "In case of error",
            content = @Content(schema = @Schema(implementation = SimpleErrorResponse.class))
    )
    @Path("selfservice/send_code_words")
    @POST
    Response selfServiceSendCodeWords(
            @Parameter(description = DESCRIPTION_SIGNATURE,
                    example = EXAMPLE_SIGNATURE,
                    required = true)
            @HeaderParam(SIGNATURE) String httpSignature,
            @Parameter(description = DESCRIPTION_DATE,
                    example = EXAMPLE_DATE,
                    required = true)
            @HeaderParam(DATE) String date,
            @Parameter(description = DESCRIPTION_DIGEST,
                    example = EXAMPLE_DIGEST,
                    required = true)
            @HeaderParam(DIGEST) String digest,
            @RequestBody(description = "Activation codes and how to distribute", required = true)
                    SendCodeWordsRequestBody sendCodeWordsRequestBody
    );

    @Operation(summary = "Prohibit change of endUser password"
            , description = "signal to the RA that self-service activation has reached the point where password " +
            "change (automated or manual) MUST be prohibited until the provided timestamp, effective immediately."
            , tags = {"Activation without Code Device"})
    @ApiResponse(responseCode = "200", description = "Time when password was last reset",
            content = @Content(schema = @Schema(implementation = PasswordQuarantineResponse.class))
    )
    @ApiResponse(responseCode = "400", description = "In case of error")
    @ApiResponse(responseCode = "500", description = "In case of error",
            content = @Content(schema = @Schema(implementation = SimpleErrorResponse.class))
    )
    @Path("selfservice/password_quarantine")
    @POST
    Response selfServicePasswordQuarantine(
            @Parameter(description = DESCRIPTION_SIGNATURE,
                    example = EXAMPLE_SIGNATURE,
                    required = true)
            @HeaderParam(SIGNATURE) String httpSignature,
            @Parameter(description = DESCRIPTION_DATE,
                    example = EXAMPLE_DATE,
                    required = true)
            @HeaderParam(DATE) String date,
            @Parameter(description = DESCRIPTION_DIGEST,
                    example = EXAMPLE_DIGEST,
                    required = true)
            @HeaderParam(DIGEST) String digest,
            @RequestBody(description = "How long to quarantine the password", required = true)
                    PasswordQuarantineRequestBody passwordQuarantineRequestBody
    );

    @Operation(summary = "Tell endUser that BankID App is activated"
            , description = "Request to tell the endUser that his BankID App instance is activated"
            , tags = {"Activation without Code Device"}
    )
    @ApiResponse(responseCode = "200", description = "If all ok, no data is returned") // TODO: bruke 204 NO_CONTENT ???
    @ApiResponse(responseCode = "400", description = "In case of error")
    @ApiResponse(responseCode = "500", description = "In case of error",
            content = @Content(schema = @Schema(implementation = NotifyUserOfActivationErrorResponse.class))
    )
    @Path("notify_user_of_activation")
    @POST
    Response notifyUserOfActivation(
            @Parameter(description = DESCRIPTION_SIGNATURE,
                    example = EXAMPLE_SIGNATURE,
                    required = true)
            @HeaderParam(SIGNATURE) String httpSignature,
            @Parameter(description = DESCRIPTION_DATE,
                    example = EXAMPLE_DATE,
                    required = true)
            @HeaderParam(DATE) String date,
            @Parameter(description = DESCRIPTION_DIGEST,
                    example = EXAMPLE_DIGEST,
                    required = true)
            @HeaderParam(DIGEST) String digest,
            @RequestBody(description = "Activation code metadata", required = true)
                    NotifyUserOfActivationRequestBody notifyUserOfActivationRequestBody
    );
}
