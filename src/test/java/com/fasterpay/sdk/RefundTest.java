package com.fasterpay.sdk;

import com.fasterpay.sdk.FasterPayErrorDecoder.FasterPayException;
import com.fasterpay.sdk.Refund.RefundApi;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.UndeclaredThrowableException;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class RefundTest {

    private static final Response INVALID_AMOUNT = new Response(false, "Invalid requested amount", 400, null);
    private static final Response ALREADY_REFUNDED = new Response(false, "This transaction was already refunded", 400, null);

    private Refund refund;

    private RefundApi refundApi = mock(RefundApi.class);

    @Before
    public void setUp() {
        refund = new Refund("privateKey", refundApi);
    }

    @Test
    public void refundShouldThrowExceptionWithNullOrderId() {
        assertThrows(NullPointerException.class, () -> refund.refund(null, 1));
    }

    @Test
    public void refundShouldThrowExceptionWithAmountLessThanZero() {
        assertThrows(IllegalArgumentException.class, () -> refund.refund("123", -1));
    }

    @Test
    public void refundShouldReturnErrorResponseWithWrongPrivateKey() {
        when(refundApi.refund("123", "privateKey", 1))
                .thenThrow(new UndeclaredThrowableException(new FasterPayException(INVALID_AMOUNT)));

        assertThat(refund.refund("123", 1))
                .isEqualTo(INVALID_AMOUNT);
    }

    @Test
    public void refundShouldReturnErrorResponseWithRefundedOrderId() {
        when(refundApi.refund("123", "privateKey", 1))
                .thenThrow(new UndeclaredThrowableException(new FasterPayException(ALREADY_REFUNDED)));

        assertThat(refund.refund("123", 1))
                .isEqualTo(ALREADY_REFUNDED);
    }
}
