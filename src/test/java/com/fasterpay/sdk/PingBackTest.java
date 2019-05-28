package com.fasterpay.sdk;

import com.fasterpay.sdk.utils.HashUtils;
import org.json.JSONException;
import org.junit.Before;
import org.junit.Test;

import java.util.Optional;

import static com.fasterpay.sdk.FasterPayTestFixture.PRIVATE_KEY;
import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertThrows;

public class PingBackTest {

    private PingBack pingback;

    @Before
    public void setUp() {
        pingback = new PingBack(PRIVATE_KEY);
    }

    @Test
    public void validateFailedWhenAllFieldsAreNull() {
        assertThat(pingback.validation()
            .signVersion(Optional.empty())
            .apiKey(Optional.empty())
            .signature(Optional.empty())
            .pingBackData(Optional.empty())
            .execute()).isFalse();
    }

    @Test
    public void validateFailedWhenAllFieldsAreNullButSignVersionIsV1() {
        assertThat(pingback.validation()
            .signVersion(Optional.of("v1"))
            .apiKey(Optional.empty())
            .signature(Optional.empty())
            .pingBackData(Optional.empty())
            .execute()).isFalse();
    }

    @Test
    public void validateSuccessWhenSignVersionIsEmptyAndPrivateKeyIsCorrect() {
        assertThat(pingback.validation()
            .signVersion(Optional.empty())
            .apiKey(Optional.of(PRIVATE_KEY))
            .signature(Optional.of("abc"))
            .pingBackData(Optional.of("bcd"))
            .execute()).isTrue();
    }

    @Test
    public void validateFailedWhenSignVersionIsEmptyAndPrivateKeyIsWrong() {
        assertThat(pingback.validation()
            .signVersion(Optional.empty())
            .apiKey(Optional.of("WRONG_KEY"))
            .signature(Optional.of("abc"))
            .pingBackData(Optional.of("bcd"))
            .execute()).isFalse();
    }

    @Test
    public void validateSuccessWhenSignVersionIsV1AndPrivateKeyIsCorrect() {
        assertThat(pingback.validation()
            .signVersion(Optional.of("v1"))
            .apiKey(Optional.of(PRIVATE_KEY))
            .signature(Optional.of("abc"))
            .pingBackData(Optional.of("bcd"))
            .execute()).isTrue();
    }

    @Test
    public void validateFailedWhenSignVersionIsV1AndPrivateKeyIsWrong() {
        assertThat(pingback.validation()
            .signVersion(Optional.of("v1"))
            .apiKey(Optional.of("Wrong_key"))
            .signature(Optional.of("abc"))
            .pingBackData(Optional.of("bcd"))
            .execute()).isFalse();
    }

    @Test
    public void validateSuccessWhenSignVersionIsV1AndPrivateKeyIsCorrectAndOtherFieldsEmpty() {
        assertThat(pingback.validation()
            .signVersion(Optional.of("v1"))
            .apiKey(Optional.of(PRIVATE_KEY))
            .signature(Optional.empty())
            .pingBackData(Optional.empty())
            .execute()).isTrue();
    }

    @Test
    public void validateSuccessWhenSignVersionIsV1AndPrivateKeyIsWrongAndOtherFieldsEmpty() {
        assertThat(pingback.validation()
            .signVersion(Optional.of("v1"))
            .apiKey(Optional.of(PRIVATE_KEY))
            .signature(Optional.empty())
            .pingBackData(Optional.empty())
            .execute()).isTrue();
    }

    @Test
    public void validateFailedWhenAllFieldsAreNullButSignVersionIsV2() {
        assertThat(pingback.validation()
            .signVersion(Optional.of("v2"))
            .apiKey(Optional.empty())
            .signature(Optional.empty())
            .pingBackData(Optional.empty())
            .execute()).isFalse();
    }

    @Test
    public void validateFailedWhenAllFieldsAreNullButSignVersionIsV2AndApiKeyIsCorrect() {
        assertThat(pingback.validation()
            .signVersion(Optional.of("v2"))
            .apiKey(Optional.of(PRIVATE_KEY))
            .signature(Optional.empty())
            .pingBackData(Optional.empty())
            .execute()).isFalse();
    }

    @Test
    public void validateFailedWhenAllFieldsAreNullButSignVersionIsV2AndApiKeyIsWrong() {
        assertThat(pingback.validation()
            .signVersion(Optional.of("v2"))
            .apiKey(Optional.of("WRONG_KEY"))
            .signature(Optional.empty())
            .pingBackData(Optional.empty())
            .execute()).isFalse();
    }

    @Test
    public void validateFailedWhenSignVersionIsV2AndApiKeyIsCorrectButSignatureWrong() {
        assertThat(pingback.validation()
            .signVersion(Optional.of("v2"))
            .apiKey(Optional.of(PRIVATE_KEY))
            .signature(Optional.of("wrong"))
            .pingBackData(Optional.empty())
            .execute()).isFalse();
    }

    @Test
    public void validateThrowExceptionWhenSignVersionIsV2AndApiKeyIsCorrectButPingBackNotAJSON() {
        assertThrows(JSONException.class, () -> pingback.validation()
            .signVersion(Optional.of("v2"))
            .apiKey(Optional.of(PRIVATE_KEY))
            .signature(Optional.of("wrong"))
            .pingBackData(Optional.of("abc"))
            .execute());
    }

    @Test
    public void validateFailedWhenTimeStampIsExpired() {
        String pingbackData =
                "{" +
                    "\"event\": \"payment\"," +
                    "\"payment_order\": {" +
                        "\"id\": 13339," +
                        "\"merchant_order_id\": \"8245\"," +
                        "\"payment_system\": 1," +
                        "\"status\": \"successful\"," +
                        "\"paid_amount\": 0.01," +
                        "\"paid_currency\": \"EUR\"," +
                        "\"merchant_net_revenue\": -0.2608," +
                        "\"merchant_rolling_reserve\": 0.0005," +
                        "\"fees\": 0.2703," +
                        "\"date\": {" +
                            "\"date\": \"2019-07-11 10:56:03.000000\"," +
                            "\"timezone_type\": 3," +
                            "\"timezone\": \"UTC\"" +
                        "}" +
                    "}," +
                    "\"user\": {" +
                        "\"firstname\": \"John\"," +
                        "\"lastname\": \"Doe\"," +
                        "\"username\": \"John-Doe-1@my.passport.io\"," +
                        "\"country\": \"IN\"," +
                        "\"email\": \"john+doe@gmail.com\"" +
                    "}," +
                    "\"with_risk_check\": false," +
                    "\"pingback_ts\": 1562842637" +
                "}";

        assertThat(pingback.validation()
            .signVersion(Optional.of("v2"))
            .apiKey(Optional.empty())
            .signature(Optional.of("0e30d646a6f8c01e8bc7ad4a5de65e95a80044c687e45c1fa5ff7a751653dbcf"))
            .pingBackData(Optional.of(pingbackData))
            .execute()).isFalse();
    }

    @Test
    public void validateSuccessWhenSignVersionIsV2AndTimeStampIsValidAndSignatureCorrect() {
        String pingBackData =
                "{" +
                    "\"event\": \"payment\"," +
                    "\"payment_order\": {" +
                        "\"id\": 13339," +
                        "\"merchant_order_id\": \"8245\"," +
                        "\"payment_system\": 1," +
                        "\"status\": \"successful\"," +
                        "\"paid_amount\": 0.01," +
                        "\"paid_currency\": \"EUR\"," +
                        "\"merchant_net_revenue\": -0.2608," +
                        "\"merchant_rolling_reserve\": 0.0005," +
                        "\"fees\": 0.2703," +
                        "\"date\": {" +
                            "\"date\": \"2019-07-11 10:56:03.000000\"," +
                            "\"timezone_type\": 3," +
                            "\"timezone\": \"UTC\"" +
                        "}" +
                    "}," +
                    "\"user\": {" +
                        "\"firstname\": \"John\"," +
                        "\"lastname\": \"Doe\"," +
                        "\"username\": \"John-Doe-1@my.passport.io\"," +
                        "\"country\": \"IN\"," +
                        "\"email\": \"john+doe@gmail.com\"" +
                    "}," +
                    "\"with_risk_check\": false," +
                    "\"pingback_ts\": " + System.currentTimeMillis() / 1000L +
                "}";

        String signature = HashUtils.hmacSha256(PRIVATE_KEY, pingBackData);

        assertThat(pingback.validation()
            .signVersion(Optional.of("v2"))
            .apiKey(Optional.empty())
            .signature(Optional.of(signature))
            .pingBackData(Optional.of(pingBackData))
            .execute()).isTrue();
    }

    @Test
    public void validateFailedWhenSignVersionIsV2AndTimeStampIsValidAndSignatureWrong() {
        String pingBackData =
                "{" +
                    "\"event\": \"payment\"," +
                    "\"payment_order\": {" +
                        "\"id\": 13339," +
                        "\"merchant_order_id\": \"8245\"," +
                        "\"payment_system\": 1," +
                        "\"status\": \"successful\"," +
                        "\"paid_amount\": 0.01," +
                        "\"paid_currency\": \"EUR\"," +
                        "\"merchant_net_revenue\": -0.2608," +
                        "\"merchant_rolling_reserve\": 0.0005," +
                        "\"fees\": 0.2703," +
                        "\"date\": {" +
                            "\"date\": \"2019-07-11 10:56:03.000000\"," +
                            "\"timezone_type\": 3," +
                            "\"timezone\": \"UTC\"" +
                        "}" +
                    "}," +
                    "\"user\": {" +
                        "\"firstname\": \"John\"," +
                        "\"lastname\": \"Doe\"," +
                        "\"username\": \"John-Doe-1@my.passport.io\"," +
                        "\"country\": \"IN\"," +
                        "\"email\": \"john+doe@gmail.com\"" +
                    "}," +
                    "\"with_risk_check\": false," +
                    "\"pingback_ts\": " + System.currentTimeMillis() / 1000L +
                "}";

        assertThat(pingback.validation()
            .signVersion(Optional.of("v2"))
            .apiKey(Optional.empty())
            .signature(Optional.of("wrong"))
            .pingBackData(Optional.of(pingBackData))
            .execute()).isFalse();
    }
}
