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
import no.bankid.incoming.ra.callback.ActivationValidationCallbackRequestDTO;
import no.bankid.incoming.ra.rainit.RaInitiatedActivationRequestDTO;
import no.bankid.incoming.ra.rainit.RaInitiatedActivationResponseDTO;
import no.bankid.outgoing.ra.bidadministration.IssueRequestDTO;
import no.bankid.outgoing.ra.bidadministration.IssueStatusRequestDTO;
import no.bankid.outgoing.ra.bidadministration.IssueStatusResponseDTO;
import no.bankid.outgoing.ra.otpadministration.AddBappResponseDTO;
import no.bankid.outgoing.ra.otpadministration.DeleteBappResponseDTO;
import no.bankid.outgoing.ra.otpadministration.StatusBappResponseDTO;
import no.bankid.outgoing.ra.selfservice.*;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static no.bankid.outgoing.ra.HttpSignatureHeaders.*;

@OpenAPIDefinition(
        info = @Info(
                title = "BankID RA Service Provider Interface (SPI) for activation of BankID App",
                version = "1.0.0",
                description = "Defines the interface to be provided by a Registration Authority service to " +
                        "support activation of BankID App as a HA2 element for an end user's Netcentric BankID." +
                        "<p>" +
                        "An RA implementing the <i>OTP administration</i> group of operations may offer BankID App " +
                        "to all it's end users having a functioning Netcentric BankID with a code device." +
                        "</p>" +
                        "<p>" +
                        "If in addition, the RA implements the <i>Activation without Code Device</i> " +
                        "groups of operations then end users may activate the Bankid App without having " +
                        "another Code Device at all !" +
                        "</p>" +
                        "<h6>This SPI version corresponds to the document " +
                        "'Specification of Solutions for Activation of BankID App as HA2-elements' version v.63</h6>"
        ),
        tags = {
                @Tag(name = RaRequirements.SERVICE_AVAILABILITY,
                        description = "Checks that the service is available"),
                @Tag(name = RaRequirements.OTP_ADMINISTRATION,
                        description = "Adds or deletes BankID App from an end user's BankID"),
                @Tag(name = RaRequirements.ACTIVATION_WITHOUT_CODE_DEVICE),
                @Tag(name = RaRequirements.ACTIVATION_WITHOUT_CODE_DEVICE_SELF_SERVICE),
                @Tag(name = RaRequirements.BANK_ID_PROVISIONING, description = "Ra actions involved in provisioning flows."),
                @Tag(name = RaRequirements.CALLBACKS, description = "These are callbacks which BankID-app implement and must be called by the RA."),
                @Tag(name = RaRequirements.RA_INIT, description = "API which BankID-app implements and may be called by the RA.")
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
            "headers=\"(request-target) date digest\",signature=\"o7zK892....\"";

    String DESCRIPTION_DATE = "The date element, required format is the format named 'preferred' specified in " +
            "<a href=\"https://tools.ietf.org/html/rfc7231#section-7.1.1.1\">RFC 7231 - Hypertext Transfer Protocol (HTTP/1.1): Semantics and Content</a>." +
            "<p><b>Implementation tips:</b> Format in Java is 'EEE, dd MMM yyyy HH:mm:ss zzz', locale english.</p>";
    String EXAMPLE_DATE = "Mon, 16 Sep 2019 12:12:21 GMT";

    String DESCRIPTION_DIGEST = "SHA-256 hash of body" +
            "<p><b>Implementation tips:</b>MessageDigest.getIinstance(\"SHA-256\") in Java returns an object which is not thread safe</p>";
    String EXAMPLE_DIGEST = "SHA-256=X48E9qOokqqrvdts8nOJRJN3OWDUoyWxBf7kbu9DBPE=";


    String ACTIVATION_WITHOUT_CODE_DEVICE = "Activation without Code Device";
    String ACTIVATION_WITHOUT_CODE_DEVICE_SELF_SERVICE = "Activation without Code Device - Self Service";
    String BANK_ID_PROVISIONING = "BankID Provisioning";
    String OTP_ADMINISTRATION = "OTP administration";
    String SERVICE_AVAILABILITY = "Service availability";
    String CALLBACKS = "BankID Provisioning Callbacks";
    String RA_INIT = "BankID-app Provisioning API";

    @Operation(summary = "Adds BankID App to an end user"
            , description = "Adds BankID App to an end user's BankID OTP mechanisms in a given bank"
            , tags = {OTP_ADMINISTRATION}
    )

    @ApiResponse(responseCode = "200", description = "If status returned is valid",
            content = @Content(schema = @Schema(implementation = AddBappResponseDTO.class))
    )
    @ApiResponse(responseCode = "400", description = "In case of error")
    @ApiResponse(responseCode = "500", description = "In case of error",
            content = @Content(schema = @Schema(implementation = SimpleErrorResponseDTO.class))
    )
    @Path("add")
    @POST
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
            AuthenticationBodyDTO authenticationBody
    );

    @Operation(summary = "Gets the BankID App OTP status for an end user"
            , description = "Checks whether an end user has BankID App enabled as an OTP mechanism " +
            "for at least one of his BankIDs in a given bank"
            , tags = {OTP_ADMINISTRATION}
    )
    @ApiResponse(responseCode = "200", description = "If status returned is valid",
            content = @Content(schema = @Schema(implementation = StatusBappResponseDTO.class))
    )
    @ApiResponse(responseCode = "400", description = "In case of error")
    @ApiResponse(responseCode = "500", description = "In case of error",
            content = @Content(schema = @Schema(implementation = SimpleErrorResponseDTO.class))
    )
    @Path("status")
    @POST
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
            AuthenticationBodyDTO authenticationBody
    );

    @Operation(summary = "Removes BankID App from an end user"
            , description = "Removes BankID App as an end user's OTP mechanism for at least one of " +
            "his BankIDs in a given bank"
            , tags = {OTP_ADMINISTRATION}
    )
    @ApiResponse(responseCode = "200", description = "If status returned is valid",
            content = @Content(schema = @Schema(implementation = DeleteBappResponseDTO.class))
    )
    @ApiResponse(responseCode = "400", description = "In case of error")
    @ApiResponse(responseCode = "500", description = "In case of error",
            content = @Content(schema = @Schema(implementation = SimpleErrorResponseDTO.class))
    )
    @POST
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
            AuthenticationBodyDTO authenticationBody
    );

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
            , tags = {SERVICE_AVAILABILITY})
    @Path("healthcheck")
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    Response healthCheck();

    @Operation(summary = "Check two-channel options for end user"
            , description =
            "<p>Endpoint to check if a specific user is eligible from single originator for self-service activation.</p>" +
                    "<p>The RA should check if the provided phone number is registered for the user, " +
                    "but return the other information regardless."
            , tags = {ACTIVATION_WITHOUT_CODE_DEVICE_SELF_SERVICE})
    @ApiResponse(responseCode = "200", description = "If status returned is valid",
            content = @Content(schema = @Schema(implementation = SelfServiceCheckUserResponseDTO.class))
    )
    @ApiResponse(responseCode = "400", description = "In case of error")
    @ApiResponse(responseCode = "500", description = "In case of error",
            content = @Content(schema = @Schema(implementation = SimpleErrorResponseDTO.class))
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
                    SelfServiceCheckUserRequestBodyDTO selfserviceCheckuserRequestBody
    );

    @Operation(summary = "Request distribution of a verification code to be sent to an end user."
            , description = "<p>Endpoint to request distribution of an a verification code to be sent to an end user. " +
            "Upon receiving a request on this end-point, the RA should distribute the provided code over " +
            "sms or return an error-code.</p>" +
            "<p>The RA should reject requests if they do not recognize the combination of nnin + msisdn</p>"
            , tags = {ACTIVATION_WITHOUT_CODE_DEVICE_SELF_SERVICE})
    @ApiResponse(responseCode = "200", description = "If all ok, no data is returned")
    @ApiResponse(responseCode = "400", description = "In case of error")
    @ApiResponse(responseCode = "500", description = "In case of error",
            content = @Content(schema = @Schema(implementation = SimpleErrorResponseDTO.class))
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
                    SendVerificationCodeRequestBodyDTO selfserviceSendVerificationCodeRequestBody
    );

    @Operation(summary = "Send codewords to an end user"
            , description = "request distribution of code words to be sent to a user." +
            " Upon receiving a request on this end-point, the RA should distribute the provided " +
            "code through the channel indicated, or return an error-code."
            , tags = {ACTIVATION_WITHOUT_CODE_DEVICE_SELF_SERVICE})
    @ApiResponse(responseCode = "200", description = "If all ok, no data is returned")
    @ApiResponse(responseCode = "400", description = "In case of error")
    @ApiResponse(responseCode = "500", description = "In case of error",
            content = @Content(schema = @Schema(implementation = SimpleErrorResponseDTO.class))
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
                    SendCodeWordsRequestBodyDTO sendCodeWordsRequestBody
    );

    @Operation(summary = "Prohibit change of end user password"
            , description = "signal to the RA that self-service activation has reached the point where password " +
            "change (automated or manual) MUST be prohibited until the provided timestamp, effective immediately."
            , tags = {ACTIVATION_WITHOUT_CODE_DEVICE_SELF_SERVICE})
    @ApiResponse(responseCode = "200", description = "Time when password was last reset",
            content = @Content(schema = @Schema(implementation = PasswordQuarantineResponseDTO.class))
    )
    @ApiResponse(responseCode = "400", description = "In case of error")
    @ApiResponse(responseCode = "500", description = "In case of error",
            content = @Content(schema = @Schema(implementation = SimpleErrorResponseDTO.class))
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
                    PasswordQuarantineRequestBodyDTO passwordQuarantineRequestBody
    );

    @Operation(summary = "Tell end user that BankID App is activated"
            , description = "Request notification of the end user that his BankID App instance is activated"
            , tags = {ACTIVATION_WITHOUT_CODE_DEVICE}
    )
    @ApiResponse(responseCode = "200", description = "If all ok, no data is returned")
    @ApiResponse(responseCode = "400", description = "In case of error")
    @ApiResponse(responseCode = "500", description = "In case of error",
            content = @Content(schema = @Schema(implementation = NotifyUserOfActivationErrorResponseDTO.class))
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
                    NotifyUserOfActivationRequestBodyDTO notifyUserOfActivationRequestBody
    );

    @Operation(summary = "Issue (or reissue) the user's BankID with a new temporary password"
            , description = "Issue (or reissue) the user's BankID with a new temporary password."
            , tags = {BANK_ID_PROVISIONING}
    )
    @ApiResponse(responseCode = "202", description = "If all ok, the request is accepted")
    @ApiResponse(responseCode = "400", description = "In case of error")
    @ApiResponse(responseCode = "500", description = "In case of error",
            content = @Content(schema = @Schema(implementation = SimpleErrorResponseDTO.class))
    )
    @Path("bankid_provisioning/issue")
    @POST
    Response issue(
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
            @RequestBody(description = "Request that the user's BankID is (re-)issued.", required = true)
                    IssueRequestDTO reissueReqest
    );

    @Operation(summary = "Get status of issue request"
            , description = "Poll for the status of a pending issue request."
            , tags = {BANK_ID_PROVISIONING}
    )
    @ApiResponse(responseCode = "200", description = "Returns the status of the (re-)issue request",
            content = @Content(schema = @Schema(implementation = IssueStatusResponseDTO.class))
    )
    @ApiResponse(responseCode = "400", description = "In case of error")
    @ApiResponse(responseCode = "500", description = "In case of error",
            content = @Content(schema = @Schema(implementation = SimpleErrorResponseDTO.class))
    )
    @Path("bankid_provisioning/issue/status")
    @POST
    Response issueStatus(
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
            @RequestBody(description = "Request the current status of a pending (re-)issue request.", required = true)
                    IssueStatusRequestDTO issueStatusRequest
    );

    @Operation(summary = "BankID issuance callback to BankID-app"
            , description = "Callback to BankID-app informing of the status of a (re-)issue request. " +
            "Any subsequent calls from BankID-app to /bankid_provisioning/issue/status, must return the same status as this request."
            , tags = {CALLBACKS}
    )
    @ApiResponse(responseCode = "200", description = "Returns the status of the (re-)issue request")
    @ApiResponse(responseCode = "400", description = "In case of error")
    @ApiResponse(responseCode = "500", description = "In case of error",
            content = @Content(schema = @Schema(implementation = SimpleErrorResponseDTO.class))
    )
    @Path("ra/callbacks/bankid_provisioning/issue")
    @POST
    Response issueCallback(
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
            @RequestBody(description = "Callback informing of the status of a issue request.", required = true)
                    ActivationValidationCallbackRequestDTO issueCallback
    );

    @Operation(summary = "Start a RA-initiated QR-code activation session"
            , description = "The RA may call this endpoint to start a new RA-initiated activation. " +
            "If no order id is provided this will be a request for first time issuance and will initiate remote identity-proofing in BankID-app."
            , tags = {RA_INIT}
    )
    @ApiResponse(responseCode = "200", description = "Success",
            content = @Content(schema = @Schema(implementation = RaInitiatedActivationResponseDTO.class))
    )
    @ApiResponse(responseCode = "400", description = "In case of error")
    @ApiResponse(responseCode = "500", description = "In case of error",
            content = @Content(schema = @Schema(implementation = SimpleErrorResponseDTO.class))
    )
    @Path("ra/bankid_provisioning/qr")
    @POST
    Response rainitiated(
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
            @RequestBody(description = "Start a new QR-code activation session", required = true)
                    RaInitiatedActivationRequestDTO request
    );


}

