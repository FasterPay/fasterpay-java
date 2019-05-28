package com.fasterpay.sdk;

import com.fasterpay.sdk.Form.SignVersion;
import com.fasterpay.sdk.utils.HashUtils;
import org.json.JSONObject;

import java.util.Optional;

public class PingBack {

    public static final String X_API_KEY = "X-ApiKey";

    public static final String X_FASTERPAY_SIGNATURE = "X-FasterPay-Signature";

    public static final String X_FASTERPAY_SIGNATURE_VERSION = "X-FasterPay-Signature-Version";

    private static final long EXPIRED_TIME_V2 = 300L;

    private String privateKey;

    public PingBack(String privateKey) {
        this.privateKey = privateKey;
    }

    public SignVersionHeader validation() {
        return signVersionHeader -> apiKeyHeader -> signatureHeader -> pingBackData
            -> new ValidateStage(privateKey, signVersionHeader, apiKeyHeader, signatureHeader, pingBackData);
    }

    @FunctionalInterface
    public interface SignVersionHeader {
        ApiKeyHeader signVersion(Optional<String> signVersionHeader);
    }

    @FunctionalInterface
    public interface SignatureHeader {
        PingbackData signature(Optional<String> signatureHeader);
    }

    @FunctionalInterface
    public interface ApiKeyHeader {
        SignatureHeader apiKey(Optional<String> apiKeyHeader);
    }

    @FunctionalInterface
    public interface PingbackData {
        ValidateStage pingBackData(Optional<String> requestBody);
    }

    public static class ValidateStage {
        private final String privateKey;
        private final Optional<String> signVersion;
        private final Optional<String> signature;
        private final Optional<String> apiKey;
        private final Optional<String> pingBackData;

        public ValidateStage(String privateKey,
                             Optional<String> signVersion,
                             Optional<String> apiKey,
                             Optional<String> signature,
                             Optional<String> pingBackData) {
            this.privateKey = privateKey;
            this.signVersion = signVersion;
            this.signature = signature;
            this.apiKey = apiKey;
            this.pingBackData = pingBackData;
        }

        public boolean execute() {
            return signVersion.map(SignVersion::fromString)
                .filter(version -> SignVersion.VERSION_2.equals(version))
                .map(version -> validateWithSignature())
                .orElse(validateWithApiKey());
        }

        private boolean validateWithApiKey() {
            return apiKey.map(api -> privateKey.equals(api))
                .orElse(false);
        }

        private boolean validateWithSignature() {
            boolean valid = pingBackData.map(pingBack -> new JSONObject(pingBack))
                .map(pingBackJson -> pingBackJson.getInt("pingback_ts"))
                .map(timeStamp -> validateTimeStamp(timeStamp))
                .orElse(false);

            if (valid) {
                valid = pingBackData.map(pingBack -> HashUtils.hmacSha256(privateKey, pingBack))
                    .map(pingBack -> signature.map(sign -> pingBack.equals(sign))
                        .orElse(false))
                    .orElse(false);
            }

            return valid;
        }

        private boolean validateTimeStamp(int timeStamp) {
            long now = System.currentTimeMillis() / 1000L;
            return (now - timeStamp) <= EXPIRED_TIME_V2;
        }
    }
}
