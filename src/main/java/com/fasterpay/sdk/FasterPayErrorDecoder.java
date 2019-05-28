package com.fasterpay.sdk;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.lambdas.Throwing;
import com.google.common.io.CharStreams;
import feign.Response;
import feign.Response.Body;
import feign.codec.ErrorDecoder;

import java.io.IOException;
import java.util.Optional;

public class FasterPayErrorDecoder implements ErrorDecoder {
    @Override
    public Exception decode(String methodKey, Response response) {
        com.fasterpay.sdk.Response originalResponse = Optional.of(response.body())
            .map(Throwing.function(Body::asReader))
            .map(Throwing.function(CharStreams::toString))
            .map(Throwing.function(result -> readOriginalMessage(result, response.status())))
            .orElse(new com.fasterpay.sdk.Response(false, "error", response.status(), null));

        return new FasterPayException(originalResponse);
    }

    private com.fasterpay.sdk.Response readOriginalMessage(String result, int code) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        com.fasterpay.sdk.Response response = mapper.readValue(result, com.fasterpay.sdk.Response.class);
        if (response.getCode() == 0) {
            response.setCode(code);
        }
        return response;
    }

    static class FasterPayException extends Exception {
        private com.fasterpay.sdk.Response response;

        FasterPayException(com.fasterpay.sdk.Response response) {
            super(response.getMessage());
            this.response = response;
        }

        public com.fasterpay.sdk.Response getOriginalResponse() {
            return response;
        }
    }
}
