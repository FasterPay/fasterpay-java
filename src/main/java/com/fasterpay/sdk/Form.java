package com.fasterpay.sdk;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class Form {

    public enum SignVersion {
        VERSION_1("v1"),
        VERSION_2("v2");

        private String value;

        SignVersion(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        public static SignVersion fromString(String value) {
            for (SignVersion signVersion : SignVersion.values()) {
                if (signVersion.value.equals(value)) {
                    return signVersion;
                }
            }
            return VERSION_1;
        }
    }

    public static final String FORM_AMOUNT_FIELD = "amount";
    public static final String FORM_DESCRIPTION_FIELD = "description";
    public static final String FORM_CURRENCY_FIELD = "currency";
    public static final String FORM_API_KEY_FIELD = "api_key";
    public static final String FORM_MERCHANT_ORDER_ID_FIELD = "merchant_order_id";
    public static final String FORM_SUCCESS_URL_FIELD = "success_url";
    public static final String FORM_HASH_FIELD = "hash";
    public static final String FORM_EMAIL_FIELD = "email";
    public static final String FORM_FIRST_NAME_FIELD = "first_name";
    public static final String FORM_LAST_NAME_FIELD = "last_name";
    public static final String FORM_CITY_FIELD = "city";
    public static final String FORM_ZIP_FIELD = "zip";
    public static final String FORM_PINGBACK_URL = "pingback_url";
    public static final String FORM_SIGN_VERSION = "sign_version";
    public static final String FORM_RECURRING_NAME_FIELD = "recurring_name";
    public static final String FORM_RECURRING_SKU_ID_FIELD = "recurring_sku_id";
    public static final String FORM_RECURRING_PERIOD_FIELD = "recurring_period";
    public static final String FORM_RECURRING_TRIAL_AMOUNT_FIELD = "recurring_trial_amount";
    public static final String FORM_RECURRING_TRIAL_PERIOD_FIELD = "recurring_trial_period";
    public static final String FORM_RECURRING_DURATION_FIELD = "recurring_duration";

    protected Map<String, String> parameters;
    private String apiUrl;
    private Signature signature;
    private boolean isAutoSubmit = true;

    protected Form(String privateKey, Map<String, String> parameters, String apiUrl) {
        this.parameters = parameters;
        this.apiUrl = apiUrl;
        this.signature = new Signature(privateKey);
    }

    public Form successUrl(String successUrl) {
        parameters.put(FORM_SUCCESS_URL_FIELD, successUrl);
        return this;
    }

    public Form email(String email) {
        parameters.put(FORM_EMAIL_FIELD, email);
        return this;
    }

    public Form firstName(String firstName) {
        parameters.put(FORM_FIRST_NAME_FIELD, firstName);
        return this;
    }

    public Form lastName(String lastName) {
        parameters.put(FORM_LAST_NAME_FIELD, lastName);
        return this;
    }

    public Form city(String city) {
        parameters.put(FORM_CITY_FIELD, city);
        return this;
    }

    public Form zip(String zip) {
        parameters.put(FORM_ZIP_FIELD, zip);
        return this;
    }

    public Form pingback_url(String pingback_url) {
        parameters.put(FORM_PINGBACK_URL, pingback_url);
        return this;
    }

    public Form sign_version(SignVersion signVersion) {
        String version;
        switch (signVersion) {
            case VERSION_2:
                version = "v2";
                break;
            default:
                version = "v1";
                break;
        }
        parameters.put(FORM_SIGN_VERSION, version);
        return this;
    }

    public Form isAutoSubmit(boolean isAutoSubmit) {
        this.isAutoSubmit = isAutoSubmit;
        return this;
    }

    public String build() {
        SignVersion signVersion = Optional.ofNullable(parameters.get(FORM_SIGN_VERSION))
            .map(SignVersion::fromString)
            .orElse(SignVersion.VERSION_1);
        parameters.put(FORM_HASH_FIELD, signature.calculateHash(parameters, signVersion));
        return buildForm();
    }

    private String buildForm() {
        StringBuilder htmlForm = new StringBuilder();

        htmlForm.append("<form align=\"center\" method=\"post\" action=\"" + apiUrl + "/payment/form\">");

        htmlForm.append(parameters.entrySet().stream()
            .map(entry -> String.format("<input type=\"hidden\" name=\"%s\" value=\"%s\"/>", entry.getKey(), entry.getValue()))
            .collect(Collectors.joining("")));

        htmlForm.append("<input type=\"Submit\" value=\"Pay Now\" id=\"fasterpay-submit\"")
                .append(isAutoSubmit ? " style=\"display:none\"/>" : "/>")
                .append("</form>");

        if (isAutoSubmit) {
            htmlForm.append("<script type=\"text/javascript\">document.getElementById(\"fasterpay-submit\").click(); </script>");
        }
        return htmlForm.toString();
    }

    @FunctionalInterface
    public interface RequireAmount<T> {
        RequireCurrency<T> amount(String amount);
    }

    @FunctionalInterface
    public interface RequireCurrency<T> {
        RequireDescription<T> currency(String currency);
    }

    @FunctionalInterface
    public interface RequireDescription<T> {
        RequireMerchantOrderId<T> description(String description);
    }

    @FunctionalInterface
    public interface RequireMerchantOrderId<T> {
        T merchantOrderId(String merchant_order_id);
    }
}
