package com.fasterpay.sdk;

import feign.Feign;
import feign.Headers;
import feign.Logger.Level;
import feign.Param;
import feign.RequestLine;
import feign.jackson.JacksonDecoder;
import feign.slf4j.Slf4jLogger;

public interface Subscriptions {

    static Subscriptions from(String subscriptionUrl) {
        return Feign.builder()
                .logger(new Slf4jLogger(Subscriptions.class))
                .logLevel(Level.FULL)
                .decoder(new JacksonDecoder())
                .target(Subscriptions.class, subscriptionUrl);
    }

    @RequestLine("POST /api/subscription/{orderId}/cancel")
    @Headers({ FasterPayApi.Headers.X_API_KEY + ": {apiKey}"})
    Response cancelSubscription(@Param("orderId") String orderId,
                                @Param("apiKey") String apiKey);
}
