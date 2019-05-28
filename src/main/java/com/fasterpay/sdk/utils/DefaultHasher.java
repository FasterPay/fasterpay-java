package com.fasterpay.sdk.utils;

import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class DefaultHasher extends Hasher {
    private final String MD5 = "MD5";
    private final String SHA1 = "SHA-1";
    private final String SHA256 = "SHA-256";
    private final String UTF8 = "UTF-8";

    public byte[] md5(byte[] input) {
        return hash(MD5, input);
    }

    public byte[] sha1(byte[] input) {
        return hash(SHA1, input);
    }

    public byte[] sha256(byte[] input) {
        return hash(SHA256, input);
    }

    public byte[] md5(String input) {
        return hash(MD5, input.getBytes(Charset.forName(UTF8)));
    }

    public byte[] sha1(String input) {
        return hash(SHA1, input.getBytes(Charset.forName(UTF8)));
    }

    public byte[] sha256(String input) {
        return hash(SHA256, input.getBytes(Charset.forName(UTF8)));
    }

    public String md5Hex(byte[] input) {
        return bytesToString(md5(input));
    }

    public String sha1Hex(byte[] input) {
        return bytesToString(sha1(input));
    }

    public String sha256Hex(byte[] input) {
        return bytesToString(sha256(input));
    }

    public String md5Hex(String input) {
        return bytesToString(md5(input));
    }

    public String sha1Hex(String input) {
        return bytesToString(sha1(input));
    }

    public String sha256Hex(String input) {
        return bytesToString(sha256(input));
    }

    private byte[] hash(String messageDigestMethod, byte[] input) {
        try {
            MessageDigest md = MessageDigest.getInstance(messageDigestMethod);
            return md.digest(input);
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
    }
}
