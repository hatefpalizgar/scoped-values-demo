package com.example;

/**
 * Represents the context of a request in the application.
 * This record encapsulates the user ID and request ID associated with a
 * request.
 *
 * The RequestContext is immutable and thread-safe, making it suitable for use
 * in concurrent environments. It is typically created at the beginning of a
 * request's lifecycle and passed through various layers of the application.
 *
 * @param userId    A unique identifier for the user making the request.
 * @param requestId A unique identifier for the request itself.
 */
public record RequestContext(String userId, String requestId) {
    @Override
    public String toString() {
        return "RequestContext{userId='" + userId + "', requestId='" + requestId + "'}";
    }
}
