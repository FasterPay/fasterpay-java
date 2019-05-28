package com.fasterpay.sdk;

import com.fasterpay.sdk.FasterPayErrorDecoder.FasterPayException;
import com.google.common.base.Preconditions;
import feign.Body;
import feign.Feign;
import feign.Headers;
import feign.Logger.Level;
import feign.Param;
import feign.RequestLine;
import feign.jackson.JacksonDecoder;
import feign.okhttp.OkHttpClient;
import feign.slf4j.Slf4jLogger;

import java.lang.reflect.UndeclaredThrowableException;
import java.util.Optional;

public class Refund {

    private final String privateKey;
    private final RefundApi refundApi;

    public Refund(String privateKey, RefundApi refundApi) {
        Preconditions.checkNotNull(privateKey);
        this.privateKey = privateKey;
        this.refundApi = refundApi;
    }

    public interface RefundApi {
        static RefundApi from(String refundUrl) {
            return Feign.builder()
                .logger(new Slf4jLogger(Refund.class))
                .errorDecoder(new FasterPayErrorDecoder())
                .logLevel(Level.FULL)
                .client(new OkHttpClient())
                .decoder(new JacksonDecoder())
                .target(RefundApi.class, refundUrl);
        }

        @RequestLine("POST /payment/{orderId}/refund")
        @Headers({ FasterPayApi.Headers.X_API_KEY + ": {apiKey}",
                FasterPayApi.Headers.CONTENT_TYPE_APPLICATION_URL_ENCODE })
        @Body("amount={value}")
        Response refund(@Param("orderId") String orderId,
                        @Param("apiKey") String apiKey,
                        @Param("value") float amount);
    }

    public Response refund(String orderId, float amount) {
        Preconditions.checkNotNull(orderId, "Invalid orderId");
        Preconditions.checkArgument(amount > 0, "Amount less than or equal 0 is not accepted");
        try {
            return refundApi.refund(orderId, privateKey, amount);
        } catch (Exception e) {
            return Optional.of(e)
                .filter(exception -> exception instanceof UndeclaredThrowableException)
                .map(exception -> (UndeclaredThrowableException) exception)
                .filter(undeclaredException -> undeclaredException.getUndeclaredThrowable() != null &&
                        undeclaredException.getUndeclaredThrowable() instanceof FasterPayException)
                .map(undeclaredException -> (FasterPayException) undeclaredException.getUndeclaredThrowable())
                .map(fasterPayException -> fasterPayException.getOriginalResponse())
                .orElse(new Response(false, e.getMessage(), -1, e.getMessage()));
        }
    }
}
