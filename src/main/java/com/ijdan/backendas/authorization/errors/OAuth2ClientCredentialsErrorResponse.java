package com.ijdan.backendas.authorization.errors;

public class OAuth2ClientCredentialsErrorResponse {

    // Fatal errors (not sent to client)
    public static final String UNKNOWN_CLIENT = "unknown.client";
    public static final String UNKNOWN_REDIRECT_URI = "unknown.redirect.uri";

    // Errors that are sent to the client
    public static final String RESPONSE_TYPE_NOT_SUPPORTED = "unsupported_response_type";
    public static final String INVALID_SCOPE = "invalid_scope";
    public static final String UNAUTHORIZED_CLIENT = "unauthorized_client";
    public static final String ACCESS_DENIED = "access_denied";
    public static final String INVALID_CLIENT = "invalid_client";
    public static final String INVALID_GRANT = "invalid_grant";
    public static final String INVALID_REQUEST = "invalid_request";
    public static final String UNSUPPORTED_GRANT_TYPE = "unsupported_grant_type";
}
