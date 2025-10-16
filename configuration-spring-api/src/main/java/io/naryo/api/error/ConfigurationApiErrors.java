package io.naryo.api.error;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@ApiResponses({
    @ApiResponse(
            responseCode = "400",
            description = "Validation error / malformed JSON",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    @ApiResponse(
    responseCode = "404",
    description = "Not found",
    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    @ApiResponse(
            responseCode = "409",
            description = "Revision conflict",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    @ApiResponse(
            responseCode = "429",
            description = "Queue overflow",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    @ApiResponse(
            responseCode = "503",
            description = "Queue closed",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
})
public @interface ConfigurationApiErrors {}
