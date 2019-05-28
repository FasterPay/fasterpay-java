package com.fasterpay.sdk.utils;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.util.Optional;

public class MacHasher extends Hasher {
    private static final String HMAC_SHA256 = "HmacSHA256";

    private final String key;

    MacHasher(String key) {
        this.key = key;
    }

    @Override
    public byte[] md5(byte[] input) {
        return new byte[0];
    }

    @Override
    public byte[] sha1(byte[] input) {
        return new byte[0];
    }

    @Override
    public byte[] sha256(byte[] input) {
        return new byte[0];
    }

    @Override
    public byte[] md5(String input) {
        return new byte[0];
    }

    @Override
    public byte[] sha1(String input) {
        return new byte[0];
    }

    @Override
    public byte[] sha256(String input) {
        try {
            Mac hmac = Mac.getInstance(HMAC_SHA256);
            SecretKeySpec secretKey = new SecretKeySpec(key.getBytes("UTF-8"), HMAC_SHA256);
            hmac.init(secretKey);
            return hmac.doFinal(input.getBytes("UTF-8"));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public String md5Hex(byte[] input) {
        return null;
    }

    @Override
    public String sha1Hex(byte[] input) {
        return null;
    }

    @Override
    public String sha256Hex(byte[] input) {
        return null;
    }

    @Override
    public String md5Hex(String input) {
        return null;
    }

    @Override
    public String sha1Hex(String input) {
        return null;
    }

    @Override
    public String sha256Hex(String input) {
        return Optional.of(sha256(input))
            .map(this::bytesToString)
            .orElse(null);
    }
}
