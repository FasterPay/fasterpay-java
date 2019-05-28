package com.fasterpay.sdk;

import com.fasterpay.sdk.Refund.RefundApi;
import com.fasterpay.sdk.SubscriptionsForm.RequireRecurringName;
import com.google.common.base.Preconditions;

import java.util.HashMap;
import java.util.Map;

import static com.fasterpay.sdk.Form.FORM_AMOUNT_FIELD;
import static com.fasterpay.sdk.Form.FORM_API_KEY_FIELD;
import static com.fasterpay.sdk.Form.FORM_CURRENCY_FIELD;
import static com.fasterpay.sdk.Form.FORM_DESCRIPTION_FIELD;
import static com.fasterpay.sdk.Form.FORM_MERCHANT_ORDER_ID_FIELD;
import static com.fasterpay.sdk.Form.FORM_RECURRING_NAME_FIELD;
import static com.fasterpay.sdk.Form.FORM_RECURRING_PERIOD_FIELD;
import static com.fasterpay.sdk.Form.FORM_RECURRING_SKU_ID_FIELD;
import static com.fasterpay.sdk.Form.RequireAmount;

public class Gateway {
    private final static String API_BASE_URL = "https://pay.fasterpay.com";
    private final static String API_SANDBOX_BASE_URL = "https://pay.sandbox.fasterpay.com";

    public static class Builder {
        @FunctionalInterface
        public interface RequirePublicApi {
            RequirePrivateApi publicApi(String publicApi);
        }

        @FunctionalInterface
        public interface RequirePrivateApi {
            FinalStage privateApi(String privateApi);
        }

        public static class FinalStage {
            private String publicApi;
            private String privateApi;
            private String testUrl = API_SANDBOX_BASE_URL;
            private boolean isTest = false;

            FinalStage(String publicApi, String privateApi) {
                this.publicApi = publicApi;
                this.privateApi = privateApi;
            }

            public FinalStage isTest(boolean isTest) {
                this.isTest = isTest;
                return this;
            }

            public FinalStage testUrl(String testUrl) {
                Preconditions.checkNotNull(testUrl, "testUrl null");
                this.testUrl = testUrl;
                this.isTest = true;
                return this;
            }

            public Gateway build() {
                return new Gateway(publicApi, privateApi, isTest, testUrl);
            }
        }
    }

    public static Builder.RequirePublicApi builder() {
        return publicKey -> privateKey -> new Builder.FinalStage(publicKey, privateKey);
    }

    private final String publicApi;
    private final String privateApi;
    private final String testUrl;
    private final boolean isTest;

    private Gateway(String publicApi, String privateApi, boolean isTest, String testUrl) {
        this.publicApi = publicApi;
        this.privateApi = privateApi;
        this.isTest = isTest;
        this.testUrl = testUrl;
    }

    public Form.RequireAmount<Form> paymentForm() {
        return amount -> currency -> description -> merchant_order_id -> {
            Map<String, String> parameters = new HashMap<>();
            parameters.put(FORM_AMOUNT_FIELD, amount);
            parameters.put(FORM_CURRENCY_FIELD, currency);
            parameters.put(FORM_DESCRIPTION_FIELD, description);
            parameters.put(FORM_MERCHANT_ORDER_ID_FIELD, merchant_order_id);
            parameters.put(FORM_API_KEY_FIELD, publicApi);
            return new Form(privateApi, parameters, getApiUrl());
        };
    }

    public RequireAmount<RequireRecurringName<SubscriptionsForm>> subscriptionForm() {
        return amount -> currency -> description -> merchant_order_id
                -> recurring_name -> recurring_sku_id -> recurring_period -> {
            Map<String, String> parameters = new HashMap<>();
            parameters.put(FORM_AMOUNT_FIELD, amount);
            parameters.put(FORM_CURRENCY_FIELD, currency);
            parameters.put(FORM_DESCRIPTION_FIELD, description);
            parameters.put(FORM_MERCHANT_ORDER_ID_FIELD, merchant_order_id);
            parameters.put(FORM_API_KEY_FIELD, publicApi);
            parameters.put(FORM_RECURRING_NAME_FIELD, recurring_name);
            parameters.put(FORM_RECURRING_SKU_ID_FIELD, recurring_sku_id);
            parameters.put(FORM_RECURRING_PERIOD_FIELD, recurring_period);
            return new SubscriptionsForm(privateApi, parameters, getApiUrl());
        };
    }

    public PingBack getPingBack() {
        return new PingBack(privateApi);
    }

    public Response cancelSubscription(String orderId) {
        Preconditions.checkNotNull(orderId, "Invalid orderId");
        return Subscriptions.from(getApiUrl())
            .cancelSubscription(orderId, privateApi);
    }

    public Response refund(String orderId, float amount) {
        return new Refund(privateApi, RefundApi.from(getApiUrl()))
            .refund(orderId, amount);
    }

    public String getApiUrl() {
        return isTest ? testUrl : API_BASE_URL;
    }
}
