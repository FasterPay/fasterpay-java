package com.fasterpay.sdk.utils;

/**
 * Created by hoahieu on 03/01/17.
 */
public class HashUtils {
    public static String sha256String(String input) {
        return Hasher.getDefaultHasher().sha256Hex(input);
    }

    public static String sha1String(String input) {
        return Hasher.getDefaultHasher().sha1Hex(input);
    }

    public static String md5String(String input) {
        return Hasher.getDefaultHasher().md5Hex(input);
    }

    public static String hmacSha256(String privateKey, String input) {
        return Hasher.getMacHasher(privateKey).sha256Hex(input);
    }
}

