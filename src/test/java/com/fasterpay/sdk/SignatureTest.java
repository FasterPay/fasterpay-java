package com.fasterpay.sdk;

import com.fasterpay.sdk.Form.SignVersion;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static com.fasterpay.sdk.FasterPayTestFixture.PRIVATE_KEY;
import static com.fasterpay.sdk.FasterPayTestFixture.PUBLIC_KEY;
import static com.fasterpay.sdk.Form.FORM_AMOUNT_FIELD;
import static com.fasterpay.sdk.Form.FORM_API_KEY_FIELD;
import static com.fasterpay.sdk.Form.FORM_CURRENCY_FIELD;
import static com.fasterpay.sdk.Form.FORM_DESCRIPTION_FIELD;
import static com.fasterpay.sdk.Form.FORM_MERCHANT_ORDER_ID_FIELD;
import static com.fasterpay.sdk.Form.FORM_SIGN_VERSION;
import static com.fasterpay.sdk.Form.FORM_SUCCESS_URL_FIELD;
import static com.google.common.truth.Truth.assertThat;

public class SignatureTest {

    private Map<String, String> parameters;

    private Signature signature;

    @Before
    public void setUp() {
        parameters = new HashMap<>();
        signature = new Signature(PRIVATE_KEY);
    }

    @Test
    public void calculateHashSuccess() {
        parameters.put(FORM_AMOUNT_FIELD, "0.01");
        parameters.put(FORM_CURRENCY_FIELD, "EUR");
        parameters.put(FORM_DESCRIPTION_FIELD, "Golden Ticket");
        parameters.put(FORM_MERCHANT_ORDER_ID_FIELD, "7267");
        parameters.put(FORM_SUCCESS_URL_FIELD, "http://localhost:12345/success.php");
        parameters.put(FORM_API_KEY_FIELD, PUBLIC_KEY);

        Form checkoutBuilder = new Form(PRIVATE_KEY, parameters, "api");

        assertThat(signature.calculateHash(checkoutBuilder.parameters, SignVersion.VERSION_1))
            .isEqualTo("265c1d8b134a31a95f7773225525b49df2421f6a8b08df54250d719f6045c55f");
    }

    @Test
    public void calculateHashError() {
        parameters.put(FORM_AMOUNT_FIELD, "0.04");
        parameters.put(FORM_CURRENCY_FIELD, "EUR");
        parameters.put(FORM_DESCRIPTION_FIELD, "Golden Ticket");
        parameters.put(FORM_MERCHANT_ORDER_ID_FIELD, "7267");
        parameters.put(FORM_SUCCESS_URL_FIELD, "http://localhost:12345/success.php");
        parameters.put(FORM_API_KEY_FIELD, PUBLIC_KEY);

        Form checkoutBuilder = new Form(PRIVATE_KEY, parameters, "api");

        assertThat(signature.calculateHash(checkoutBuilder.parameters, SignVersion.VERSION_1))
            .isNotEqualTo("265c1d8b134a31a95f7773225525b49df2421f6a8b08df54250d719f6045c55f");
    }

    @Test
    public void calculateHashWithPlusSymbolInField() {
        parameters.put(FORM_AMOUNT_FIELD, "0.01");
        parameters.put(FORM_CURRENCY_FIELD, "EUR");
        parameters.put(FORM_DESCRIPTION_FIELD, "Golden+Ticket");
        parameters.put(FORM_MERCHANT_ORDER_ID_FIELD, "7267");
        parameters.put(FORM_SUCCESS_URL_FIELD, "http://localhost:12345/success.php");
        parameters.put(FORM_API_KEY_FIELD, PUBLIC_KEY);

        Form checkoutBuilder = new Form(PRIVATE_KEY, parameters, "api");

        assertThat(signature.calculateHash(checkoutBuilder.parameters, SignVersion.VERSION_1))
            .isEqualTo("265c1d8b134a31a95f7773225525b49df2421f6a8b08df54250d719f6045c55f");
    }

    @Test
    public void calculateHashWithCustomSuccessUrlHaveQueryParameter() {
        parameters.put(FORM_AMOUNT_FIELD, "5");
        parameters.put(FORM_CURRENCY_FIELD, "EUR");
        parameters.put(FORM_DESCRIPTION_FIELD, "Golden Ticket");
        parameters.put(FORM_MERCHANT_ORDER_ID_FIELD, "5d00c568c5b2b");
        parameters.put(FORM_SUCCESS_URL_FIELD, "darren://fasterpay.datph?actionId=test");
        parameters.put(FORM_API_KEY_FIELD, "e416c4de8ffd2b198d83713b8d232fab");

        String privateKey = "4a6608170097835d0fbb856662e99da3";
        Form checkoutBuilder = new Form(PRIVATE_KEY, parameters, "api");
        signature = new Signature(privateKey);

        assertThat(signature.calculateHash(checkoutBuilder.parameters, SignVersion.VERSION_1))
            .isEqualTo("80c324e371613a4bc50419962ef09e0aecac78e13011c75a134a9a64491c5c2e");
    }

    @Test
    public void calculateHashWithCustomSuccessUrlWithoutQueryParameter() {
        parameters.put(FORM_AMOUNT_FIELD, "5");
        parameters.put(FORM_CURRENCY_FIELD, "EUR");
        parameters.put(FORM_DESCRIPTION_FIELD, "Golden Ticket");
        parameters.put(FORM_MERCHANT_ORDER_ID_FIELD, "5d00c568c5b2b");
        parameters.put(FORM_SUCCESS_URL_FIELD, "darren://localhost:12345/success.php");
        parameters.put(FORM_API_KEY_FIELD, "e416c4de8ffd2b198d83713b8d232fab");

        String privateKey = "4a6608170097835d0fbb856662e99da3";
        Form checkoutBuilder = new Form(PRIVATE_KEY, parameters, "api");
        signature = new Signature(privateKey);

        assertThat(signature.calculateHash(checkoutBuilder.parameters, SignVersion.VERSION_1))
            .isEqualTo("5bd6465bb122a18a2a0171709af51b37238bf36208893cf0738a2885bb646548");
    }

    @Test
    public void calculateHashWithSuccessUrlWithoutQueryParameter() {
        parameters.put(FORM_AMOUNT_FIELD, "5");
        parameters.put(FORM_CURRENCY_FIELD, "EUR");
        parameters.put(FORM_DESCRIPTION_FIELD, "Golden Ticket");
        parameters.put(FORM_MERCHANT_ORDER_ID_FIELD, "5d00c568c5b2b");
        parameters.put(FORM_SUCCESS_URL_FIELD, "http://localhost:12345/success.php");
        parameters.put(FORM_API_KEY_FIELD, "e416c4de8ffd2b198d83713b8d232fab");

        String privateKey = "4a6608170097835d0fbb856662e99da3";
        Form checkoutBuilder = new Form(PRIVATE_KEY, parameters, "api");
        signature = new Signature(privateKey);

        assertThat(signature.calculateHash(checkoutBuilder.parameters, SignVersion.VERSION_1))
            .isEqualTo("051655481accbbf2565cbf9dd6b16adde02984b9ac5adb230f14041e764b92a6");
    }

    @Test
    public void calculateHashSuccessV2() {
        parameters.put(FORM_AMOUNT_FIELD, "5");
        parameters.put(FORM_CURRENCY_FIELD, "EUR");
        parameters.put(FORM_DESCRIPTION_FIELD, "Golden Ticket");
        parameters.put(FORM_MERCHANT_ORDER_ID_FIELD, "5d00c568c5b2b");
        parameters.put(FORM_SUCCESS_URL_FIELD, "http://localhost:12345/success.php");
        parameters.put(FORM_SIGN_VERSION, "v2");
        parameters.put(FORM_API_KEY_FIELD, PUBLIC_KEY);

        Form checkoutBuilder = new Form(PRIVATE_KEY, parameters, "api");

        assertThat(signature.calculateHash(checkoutBuilder.parameters, SignVersion.VERSION_2))
                .isEqualTo("075145659f666080b19094fed91959549ac8b724a2ee3750404e8d69831f132b");
    }

    @Test
    public void calculateHashErrorV2() {
        parameters.put(FORM_AMOUNT_FIELD, "0.04");
        parameters.put(FORM_CURRENCY_FIELD, "EUR");
        parameters.put(FORM_DESCRIPTION_FIELD, "Golden Ticket");
        parameters.put(FORM_MERCHANT_ORDER_ID_FIELD, "7267");
        parameters.put(FORM_SUCCESS_URL_FIELD, "http://localhost:12345/success.php");
        parameters.put(FORM_SIGN_VERSION, "v2");
        parameters.put(FORM_API_KEY_FIELD, PUBLIC_KEY);

        Form checkoutBuilder = new Form(PRIVATE_KEY, parameters, "api");

        assertThat(signature.calculateHash(checkoutBuilder.parameters, SignVersion.VERSION_2))
                .isNotEqualTo("265c1d8b134a31a95f7773225525b49df2421f6a8b08df54250d719f6045c55f");
    }

    @Test
    public void calculateHashWithPlusSymbolInFieldV2() {
        parameters.put(FORM_AMOUNT_FIELD, "5");
        parameters.put(FORM_CURRENCY_FIELD, "EUR");
        parameters.put(FORM_DESCRIPTION_FIELD, "Golden+Ticket");
        parameters.put(FORM_MERCHANT_ORDER_ID_FIELD, "7267");
        parameters.put(FORM_SUCCESS_URL_FIELD, "http://localhost:12345/success.php");
        parameters.put(FORM_SIGN_VERSION, "v2");
        parameters.put(FORM_API_KEY_FIELD, PUBLIC_KEY);

        Form checkoutBuilder = new Form(PRIVATE_KEY, parameters, "api");

        assertThat(signature.calculateHash(checkoutBuilder.parameters, SignVersion.VERSION_2))
                .isEqualTo("978eae9ed5f735de77313fdcccedae2815a2f23dbe35e6360b5d2f66cd3d1d16");
    }

    @Test
    public void calculateHashWithCustomSuccessUrlHaveQueryParameterV2() {
        parameters.put(FORM_AMOUNT_FIELD, "5");
        parameters.put(FORM_CURRENCY_FIELD, "EUR");
        parameters.put(FORM_DESCRIPTION_FIELD, "Golden Ticket");
        parameters.put(FORM_MERCHANT_ORDER_ID_FIELD, "5d00c568c5b2b");
        parameters.put(FORM_SUCCESS_URL_FIELD, "darren://fasterpay.datph?actionId=test");
        parameters.put(FORM_SIGN_VERSION, "v2");
        parameters.put(FORM_API_KEY_FIELD, PUBLIC_KEY);

        Form checkoutBuilder = new Form(PRIVATE_KEY, parameters, "api");

        assertThat(signature.calculateHash(checkoutBuilder.parameters, SignVersion.VERSION_2))
                .isEqualTo("9cd918f5c619e40309c1ac7a06e0fbdf9830c91ad17070f21e6148ff3692a0dd");
    }

    @Test
    public void calculateHashWithCustomSuccessUrlWithoutQueryParameterV2() {
        parameters.put(FORM_AMOUNT_FIELD, "5");
        parameters.put(FORM_CURRENCY_FIELD, "EUR");
        parameters.put(FORM_DESCRIPTION_FIELD, "Golden Ticket");
        parameters.put(FORM_MERCHANT_ORDER_ID_FIELD, "5d00c568c5b2b");
        parameters.put(FORM_SUCCESS_URL_FIELD, "darren://localhost:12345/success.php");
        parameters.put(FORM_SIGN_VERSION, "v2");
        parameters.put(FORM_API_KEY_FIELD, PUBLIC_KEY);

        Form checkoutBuilder = new Form(PRIVATE_KEY, parameters, "api");

        assertThat(signature.calculateHash(checkoutBuilder.parameters, SignVersion.VERSION_2))
                .isEqualTo("d4f1e7fa8e07a60037775af8c1132bbfabf0279af4af05c472fe78dcc67c2a0a");
    }
}
