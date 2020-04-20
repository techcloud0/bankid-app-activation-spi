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

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static no.bankid.outgoing.ra.HttpSignatureHeaders.CONTENT_LENGTH;
import static no.bankid.outgoing.ra.HttpSignatureHeaders.DATE;
import static no.bankid.outgoing.ra.HttpSignatureHeaders.DIGEST;
import static no.bankid.outgoing.ra.HttpSignatureHeaders.SIGNATURE;
import static no.bankid.outgoing.ra.HttpSignatureHeaders.X_CLIENT_CLIENTNAME;
import static no.bankid.outgoing.ra.HttpSignatureHeaders.X_CLIENT_REQUESTID;
import static no.bankid.outgoing.ra.HttpSignatureHeaders.X_CUSTOMERID;
import static no.bankid.outgoing.ra.HttpSignatureHeaders.X_DATAOWNERORGID;

@OpenAPIDefinition(
        info = @Info(
                title = "Integration of BankID App with bank's BankID RA service",
                version = "1.2",
                description = "Defines methods needed to be implemented by Registration Authority service in banks " +
                        "wanting to add BankID App as an OTP mechanism for their BankID Netcentric users.\n\n" +
                        "If an RA service implements these methods, than the activation of an BankID App will use " +
                        "them to connect the BankID App with the endUser's BankID."
        ),
        tags = {
                @Tag(name = "Basic RA Requirements",
                        description = "Adds or deletes BankID App from an endUser's BankID"),
                @Tag(name = "Activation without Code Device",
                        description = "Activation of BankID App with no other OTP merchanism")

        },
        servers = {
                @Server(description = "Preprod Ra-lite",
                        url = "https://ra-preprod.bankidnorge.no/api/enduser/bankid/netcentric/vipps/bapp")
        }

)


@Path("/")
@Produces({MediaType.APPLICATION_JSON})

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

    String DESCRIPTION_CLIENT = "Client calling this method, constant value";
    String EXAMPLE_CLIENT = "vipps-bapp-client";

    String DESCRIPTION_ORIGINATOR = "Originator/bank-number for endUser's bankID, valid BankID ODS number";
    String EXAMPLE_ORIGINATOR = "3625";

    String DESCRIPTION_END_USER = "EndUser's Norwegian national identification number, string of 11 digits";
    String EXAMPLE_END_USER = "11111111016";

    String DESCRIPTION_REQUEST_ID = "A unique identifier pr. request for logcorrelation";
    String EXAMPLE_REQUEST_ID = "d0ef2e2f-2e07-45b1-b2ff-e39fb894adc8";

    String DESCRIPTION_DIGEST = "hash of body";
    String EXAMPLE_DIGEST = "SHA-256=X48E9qOokqqrvdts8nOJRJN3OWDUoyWxBf7kbu9DBPE=";

    String DESCRIPTION_CONTENT_LENGTH = "Length of body";
    String EXAMPLE_CONTENT_LENGTH = "18";


    @Operation(summary = "Adds BankID App to an endUser"
            , description = "Adds BankID App to an endUser's BankID OTP mechanisms in a given bank"
            , tags = {"Basic RA Requirements"}
    )

    @ApiResponse(responseCode = "200", description = "If status returned is valid",
            content = @Content(schema = @Schema(implementation = OTPAddResponse.class))
    )
    @ApiResponse(responseCode = "400", description = "In case of error")
    @ApiResponse(responseCode = "500", description = "In case of error")
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
            @Parameter(description = DESCRIPTION_CLIENT,
                    example = EXAMPLE_CLIENT,
                    required = true)
            @HeaderParam(X_CLIENT_CLIENTNAME) String clientName,
            @Parameter(description = DESCRIPTION_ORIGINATOR,
                    example = EXAMPLE_ORIGINATOR,
                    required = true)
            @HeaderParam(X_DATAOWNERORGID) String odsBankNo,
            @Parameter(description = DESCRIPTION_END_USER,
                    example = EXAMPLE_END_USER,
                    required = true)
            @HeaderParam(X_CUSTOMERID) String nnin,
            @Parameter(description = DESCRIPTION_REQUEST_ID,
                    example = EXAMPLE_REQUEST_ID,
                    required = true)
            @HeaderParam(X_CLIENT_REQUESTID) String requestId
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
    @ApiResponse(responseCode = "500", description = "In case of error")
    @GET
    Response getBappOtpStatus(
            @Parameter(description = DESCRIPTION_SIGNATURE,
                    example = EXAMPLE_SIGNATURE,
                    required = true)
            @HeaderParam(SIGNATURE) String httpSignature,
            @Parameter(description = DESCRIPTION_DATE,
                    example = EXAMPLE_DATE,
                    required = true)
            @HeaderParam(DATE) String date,
            @Parameter(description = DESCRIPTION_CLIENT,
                    example = EXAMPLE_CLIENT,
                    required = true)
            @HeaderParam(X_CLIENT_CLIENTNAME) String clientName,
            @Parameter(description = DESCRIPTION_ORIGINATOR,
                    example = EXAMPLE_ORIGINATOR,
                    required = true)
            @HeaderParam(X_DATAOWNERORGID) String odsBankNo,
            @Parameter(description = DESCRIPTION_END_USER,
                    example = EXAMPLE_END_USER,
                    required = true)
            @HeaderParam(X_CUSTOMERID) String nnin,
            @Parameter(description = DESCRIPTION_REQUEST_ID,
                    example = EXAMPLE_REQUEST_ID,
                    required = true)
            @HeaderParam(X_CLIENT_REQUESTID) String requestId
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
    @ApiResponse(responseCode = "500", description = "In case of error")
    @DELETE
    Response removeBappOtp(
            @Parameter(description = DESCRIPTION_SIGNATURE,
                    example = EXAMPLE_SIGNATURE,
                    required = true)
            @HeaderParam(SIGNATURE) String httpSignature,
            @Parameter(description = DESCRIPTION_DATE,
                    example = EXAMPLE_DATE,
                    required = true)
            @HeaderParam(DATE) String date,
            @Parameter(description = DESCRIPTION_CLIENT,
                    example = EXAMPLE_CLIENT,
                    required = true)
            @HeaderParam(X_CLIENT_CLIENTNAME) String clientName,
            @Parameter(description = DESCRIPTION_ORIGINATOR,
                    example = EXAMPLE_ORIGINATOR,
                    required = true)
            @HeaderParam(X_DATAOWNERORGID) String odsBankNo,
            @Parameter(description = DESCRIPTION_END_USER,
                    example = EXAMPLE_END_USER,
                    required = true)
            @HeaderParam(X_CUSTOMERID) String nnin,
            @Parameter(description = DESCRIPTION_REQUEST_ID,
                    example = EXAMPLE_REQUEST_ID,
                    required = true)
            @HeaderParam(X_CLIENT_REQUESTID) String requestId
    );

    /////////////////////////////// TODO: reservenøkkel

    @Path("healthcheck")
    @Produces(MediaType.APPLICATION_JSON)
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
    @GET
    Response healthCheck(@Parameter(description = DESCRIPTION_SIGNATURE,
            example = EXAMPLE_SIGNATURE,
            required = true)
                         @HeaderParam(SIGNATURE) String httpSignature,
                         @Parameter(description = DESCRIPTION_DATE,
                                 example = EXAMPLE_DATE,
                                 required = true)
                         @HeaderParam(DATE) String date,
                         @Parameter(description = DESCRIPTION_CLIENT,
                                 example = EXAMPLE_CLIENT,
                                 required = true)
                         @HeaderParam(X_CLIENT_CLIENTNAME) String clientName,
                         @Parameter(description = DESCRIPTION_REQUEST_ID,
                                 example = EXAMPLE_REQUEST_ID,
                                 required = true)
                         @HeaderParam(DESCRIPTION_REQUEST_ID) String requestId);

    @Path("check_user_selfservice")
    @Operation(summary = "Check two-channel options for endUser"
            , description = "Check if a specific endUser is eligible for two-channel activation. "
            , tags = {"Activation without Code Device"})
    @ApiResponse(responseCode = "200", description = "If status returned is valid",
            content = @Content(schema = @Schema(implementation = CheckUserSelfServiceResponse.class))
    )
    @ApiResponse(responseCode = "400", description = "In case of error")
    @ApiResponse(responseCode = "500", description = "In case of error")
    @GET
    Response checkUserSelfService(
            @Parameter(description = DESCRIPTION_SIGNATURE,
                    example = EXAMPLE_SIGNATURE,
                    required = true)
            @HeaderParam(SIGNATURE) String httpSignature,
            @Parameter(description = DESCRIPTION_DATE,
                    example = EXAMPLE_DATE,
                    required = true)
            @HeaderParam(DATE) String date,
            @Parameter(description = DESCRIPTION_CLIENT,
                    example = EXAMPLE_CLIENT,
                    required = true)
            @HeaderParam(X_CLIENT_CLIENTNAME) String clientName,
            @Parameter(description = DESCRIPTION_ORIGINATOR,
                    example = EXAMPLE_ORIGINATOR,
                    required = true)
            @HeaderParam(X_DATAOWNERORGID) String odsBankNo,
            @Parameter(description = DESCRIPTION_END_USER,
                    example = EXAMPLE_END_USER,
                    required = true)
            @HeaderParam(X_CUSTOMERID) String nnin,
            @Parameter(description = DESCRIPTION_REQUEST_ID,
                    example = EXAMPLE_REQUEST_ID,
                    required = true)
            @HeaderParam(X_CLIENT_REQUESTID) String requestId
    );


    @Path("send_activation_code")
    @Operation(summary = "Sends activation code(s) to an endUser"
            , description = "Request distribution of an activation code to be sent to a endUser."
            , tags = {"Activation without Code Device"})
    @ApiResponse(responseCode = "200", description = "Empty body if ok, else a status",
            content = @Content(schema = @Schema(implementation = SendActivationCodeErrorResponse.class))
    )
    @ApiResponse(responseCode = "400", description = "In case of error")
    @ApiResponse(responseCode = "500", description = "In case of error")
    @POST
    Response sendActivationCode(
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
            @Parameter(description = DESCRIPTION_CONTENT_LENGTH,
                    example = EXAMPLE_CONTENT_LENGTH,
                    required = true)
            @HeaderParam(CONTENT_LENGTH) String contentLength,
            @Parameter(description = DESCRIPTION_CLIENT,
                    example = EXAMPLE_CLIENT,
                    required = true)
            @HeaderParam(X_CLIENT_CLIENTNAME) String clientName,
            @Parameter(description = DESCRIPTION_ORIGINATOR,
                    example = EXAMPLE_ORIGINATOR,
                    required = true)
            @HeaderParam(X_DATAOWNERORGID) String odsBankNo,
            @Parameter(description = DESCRIPTION_END_USER,
                    example = EXAMPLE_END_USER,
                    required = true)
            @HeaderParam(X_CUSTOMERID) String nnin,
            @Parameter(description = DESCRIPTION_REQUEST_ID,
                    example = EXAMPLE_REQUEST_ID,
                    required = true)
            @HeaderParam(X_CLIENT_REQUESTID) String requestId,
            @RequestBody(description = "Activation code and how to distribute",
                    content = @Content(schema = @Schema(type = "object", implementation = SendActivationCodeRequestBody.class)),
                    required = true)
                    SendActivationCodeRequestBody sendActivationCodeRequestBody
    );

    @Path("notify_user_of_activation")
    @Operation(summary = "Tell endUser that BankID App is activated"
            , description = "Request to tell the endUser that his BankID App instance is activated"
            , tags = {"Activation without Code Device"}
    )
    @ApiResponse(responseCode = "200", description = "Empty body if ok, else a status",
            content = @Content(schema = @Schema(implementation = NotifyUserOfActivationErrorResponse.class))
    )
    @ApiResponse(responseCode = "400", description = "In case of error")
    @ApiResponse(responseCode = "500", description = "In case of error")
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
            @Parameter(description = DESCRIPTION_CONTENT_LENGTH,
                    example = EXAMPLE_CONTENT_LENGTH,
                    required = true)
            @HeaderParam(CONTENT_LENGTH) String contentLength,
            @Parameter(description = DESCRIPTION_CLIENT,
                    example = EXAMPLE_CLIENT,
                    required = true)
            @HeaderParam(X_CLIENT_CLIENTNAME) String clientName,
            @Parameter(description = DESCRIPTION_ORIGINATOR,
                    example = EXAMPLE_ORIGINATOR,
                    required = true)
            @HeaderParam(X_DATAOWNERORGID) String odsBankNo,
            @Parameter(description = DESCRIPTION_END_USER,
                    example = EXAMPLE_END_USER,
                    required = true)
            @HeaderParam(X_CUSTOMERID) String nnin,
            @Parameter(description = DESCRIPTION_REQUEST_ID,
                    example = EXAMPLE_REQUEST_ID,
                    required = true)
            @HeaderParam(X_CLIENT_REQUESTID) String requestId,
            @RequestBody(description = "Activation code metadata",
                    content = @Content(schema = @Schema(type = "object",
                            implementation = NotifyUserOfActivationRequestBody.class)),
                    required = true)
                    NotifyUserOfActivationRequestBody notifyUserOfActivationRequestBody
    );
}
