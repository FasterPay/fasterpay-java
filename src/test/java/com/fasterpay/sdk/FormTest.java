package com.fasterpay.sdk;

import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static com.fasterpay.sdk.FasterPayTestFixture.PRIVATE_KEY;
import static com.fasterpay.sdk.FasterPayTestFixture.PUBLIC_KEY;
import static com.fasterpay.sdk.FasterPayTestFixture.URL_API;
import static com.fasterpay.sdk.Form.FORM_AMOUNT_FIELD;
import static com.fasterpay.sdk.Form.FORM_API_KEY_FIELD;
import static com.fasterpay.sdk.Form.FORM_CURRENCY_FIELD;
import static com.fasterpay.sdk.Form.FORM_DESCRIPTION_FIELD;
import static com.fasterpay.sdk.Form.FORM_MERCHANT_ORDER_ID_FIELD;
import static com.fasterpay.sdk.Form.FORM_SIGN_VERSION;
import static com.google.common.truth.Truth.assertThat;

public class FormTest {

    private Form form;

    private Map<String, String> parameters;

    @Before
    public void setUp() {
        parameters = new HashMap<>();
        form = new Form(PRIVATE_KEY, parameters, URL_API);
    }

    @Test
    public void buildShouldSuccessInV1SignatureWithoutSignVersionField() {
        parameters.put(FORM_AMOUNT_FIELD, "5.00");
        parameters.put(FORM_CURRENCY_FIELD, "EUR");
        parameters.put(FORM_DESCRIPTION_FIELD, "Golden Ticket");
        parameters.put(FORM_MERCHANT_ORDER_ID_FIELD, "5d00c568c5b2b");
        parameters.put(FORM_API_KEY_FIELD, PUBLIC_KEY);

        assertThat(form.build())
                .isEqualTo("<form align=\"center\" method=\"post\" action=\"https://domain.com/payment/form\">" +
                                        "<input type=\"hidden\" name=\"amount\" value=\"5.00\"/>" +
                                        "<input type=\"hidden\" name=\"merchant_order_id\" value=\"5d00c568c5b2b\"/>" +
                                        "<input type=\"hidden\" name=\"api_key\" value=\"95b682bafc8837b1e80e5fe940cb5731\"/>" +
                                        "<input type=\"hidden\" name=\"description\" value=\"Golden Ticket\"/>" +
                                        "<input type=\"hidden\" name=\"currency\" value=\"EUR\"/>" +
                                        "<input type=\"hidden\" name=\"hash\" value=\"82d34f95a4c7ea8c38c2d25ae5a126560f369be6944d3113fb55f682ae131314\"/>" +
                                        "<input type=\"Submit\" value=\"Pay Now\" id=\"fasterpay-submit\" style=\"display:none\"/>" +
                                    "</form>" +
                                    "<script type=\"text/javascript\">document.getElementById(\"fasterpay-submit\").click(); </script>");
    }

    @Test
    public void buildShouldSuccessInV1SignatureWithSignVersionField() {
        parameters.put(FORM_AMOUNT_FIELD, "5.00");
        parameters.put(FORM_CURRENCY_FIELD, "EUR");
        parameters.put(FORM_DESCRIPTION_FIELD, "Golden Ticket");
        parameters.put(FORM_MERCHANT_ORDER_ID_FIELD, "5d00c568c5b2b");
        parameters.put(FORM_SIGN_VERSION, "v1");
        parameters.put(FORM_API_KEY_FIELD, PUBLIC_KEY);

        assertThat(form.build())
                .isEqualTo("<form align=\"center\" method=\"post\" action=\"https://domain.com/payment/form\">" +
                                        "<input type=\"hidden\" name=\"amount\" value=\"5.00\"/>" +
                                        "<input type=\"hidden\" name=\"merchant_order_id\" value=\"5d00c568c5b2b\"/>" +
                                        "<input type=\"hidden\" name=\"sign_version\" value=\"v1\"/>" +
                                        "<input type=\"hidden\" name=\"api_key\" value=\"95b682bafc8837b1e80e5fe940cb5731\"/>" +
                                        "<input type=\"hidden\" name=\"description\" value=\"Golden Ticket\"/>" +
                                        "<input type=\"hidden\" name=\"currency\" value=\"EUR\"/>" +
                                        "<input type=\"hidden\" name=\"hash\" value=\"87b3cebde91b94547e80364bad37d2dfd0cb44a0915373bdf500b6f0eddaf59d\"/>" +
                                        "<input type=\"Submit\" value=\"Pay Now\" id=\"fasterpay-submit\" style=\"display:none\"/>" +
                                    "</form>" +
                                    "<script type=\"text/javascript\">document.getElementById(\"fasterpay-submit\").click(); </script>");
    }

    @Test
    public void buildShouldSuccessInV2SignatureWithSignVersionField() {
        parameters.put(FORM_AMOUNT_FIELD, "5.00");
        parameters.put(FORM_CURRENCY_FIELD, "EUR");
        parameters.put(FORM_DESCRIPTION_FIELD, "Golden Ticket");
        parameters.put(FORM_MERCHANT_ORDER_ID_FIELD, "5d00c568c5b2b");
        parameters.put(FORM_SIGN_VERSION, "v2");
        parameters.put(FORM_API_KEY_FIELD, PUBLIC_KEY);

        assertThat(form.build())
                .isEqualTo("<form align=\"center\" method=\"post\" action=\"https://domain.com/payment/form\">" +
                                        "<input type=\"hidden\" name=\"amount\" value=\"5.00\"/>" +
                                        "<input type=\"hidden\" name=\"merchant_order_id\" value=\"5d00c568c5b2b\"/>" +
                                        "<input type=\"hidden\" name=\"sign_version\" value=\"v2\"/>" +
                                        "<input type=\"hidden\" name=\"api_key\" value=\"95b682bafc8837b1e80e5fe940cb5731\"/>" +
                                        "<input type=\"hidden\" name=\"description\" value=\"Golden Ticket\"/>" +
                                        "<input type=\"hidden\" name=\"currency\" value=\"EUR\"/>" +
                                        "<input type=\"hidden\" name=\"hash\" value=\"e77ff5cb9a08d9dea4454ab14b462a43f3fd3f0af6258baa43d260e0c56d79d3\"/>" +
                                        "<input type=\"Submit\" value=\"Pay Now\" id=\"fasterpay-submit\" style=\"display:none\"/>" +
                                    "</form>" +
                                    "<script type=\"text/javascript\">document.getElementById(\"fasterpay-submit\").click(); </script>");
    }
}
