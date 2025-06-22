package com.bnppf.kata.online_book_store.dto;

import java.time.LocalDateTime;

/**
 * Standard error response structure for REST APIs.
 * This record is used to send detailed error information to the client
 * when exceptions occur in the application. It's useful for debugging,
 * logging, and client-side handling of errors.
 * Example usage:
 * - 400 Bad Request
 * - 404 Not Found
 * - 500 Internal Server Error
 */
public record ErrorResponse(

        /*
         * The exact date and time when the error occurred.
         * Helps in tracing logs and identifying issues.
         */
        LocalDateTime timestamp,

        /*
         * The HTTP status code (e.g., 400, 404, 500).
         */
        int status,

        /*
         * A short phrase describing the HTTP error (e.g., "Not Found", "Bad Request").
         */
        String error,

        /*
         * A more detailed message describing what went wrong.
         */
        String message,

        /*
         * The request path that triggered the error.
         */
        String path
) {}
