package com.fasterpay.sdk;

import com.fasterpay.sdk.Form.SignVersion;
import com.fasterpay.sdk.utils.HashUtils;
import com.github.fge.lambdas.Throwing;

import java.net.URLEncoder;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.stream.Collectors;

public class Signature {

    private String privateKey;

    public Signature(String privateKey) {
        this.privateKey = privateKey;
    }

    public String http_build_query(Map<String, String> parameters) {
        return parameters.entrySet()
            .stream()
            .sorted(Map.Entry.comparingByKey())
            .map(this::convertToEncodedEntry)
            .collect(Collectors.joining("&"));
    }

    public String calculateHash(Map<String, String> params, SignVersion version) {
        return version == SignVersion.VERSION_2
            ? signatureVersionTwo(params)
            : signatureVersionOne(params);
    }

    private String signatureVersionOne(Map<String, String> params) {
        String queries = http_build_query(params)
            .concat(privateKey);

        String hash = HashUtils.sha256String(queries);
        return hash;
    }

    private String signatureVersionTwo(Map<String, String> params) {
        String baseString = params.entrySet()
            .stream()
            .sorted(Map.Entry.comparingByKey())
            .map(this::convertEntry)
            .collect(Collectors.joining(";", "", ";"));
        return HashUtils.hmacSha256(privateKey, baseString);
    }

    private String convertToEncodedEntry(Entry<String, String> entry) {
        return Optional.of(entry)
            .filter(e -> e.getKey().contains("url"))
            .map(Throwing.function(e -> e.getKey() + "=" + URLEncoder.encode(e.getValue(), "utf-8")))
            .orElseGet(Throwing
                .supplier(() -> entry.getKey() + "=" + URLEncoder.encode(entry.getValue(), "utf-8")
                    .replaceAll("\\%2B", "+")
                    .replaceAll("\\%20", "+")
                    .replaceAll("\\%21", "!")
                    .replaceAll("\\%27", "'")
                    .replaceAll("\\%28", "(")
                    .replaceAll("\\%29", ")")
                    .replaceAll("\\%7E", "~")));
    }

    private String convertEntry(Entry<String, String> entry) {
        return Optional.of(entry)
            .map(e -> e.getKey() + "=" + e.getValue())
            .orElse("");
    }
}
