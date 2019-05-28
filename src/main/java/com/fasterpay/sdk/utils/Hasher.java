package com.fasterpay.sdk.utils;

/**
 * Created by hoahieu on 04/01/17.
 */
public abstract class Hasher {
    public abstract byte[] md5(byte[] input);
    public abstract byte[] sha1(byte[] input);
    public abstract byte[] sha256(byte[] input);
    public abstract byte[] md5(String input);
    public abstract byte[] sha1(String input);
    public abstract byte[] sha256(String input);
    public abstract String md5Hex(byte[] input);
    public abstract String sha1Hex(byte[] input);
    public abstract String sha256Hex(byte[] input);
    public abstract String md5Hex(String input);
    public abstract String sha1Hex(String input);
    public abstract String sha256Hex(String input);

    final char[] hexArray = "0123456789abcdef".toCharArray();

    protected String bytesToString(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

    static Hasher getDefaultHasher() {
        return new DefaultHasher();
    }

    static MacHasher getMacHasher(String privateKey) {
        return new MacHasher(privateKey);
    }
}

