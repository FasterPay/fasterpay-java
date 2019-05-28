package com.fasterpay.sdk;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class Response {
    private final String message;
    private final boolean success;
    private int code;
    private final String type;

    Response(@JsonProperty("success") boolean success,
             @JsonProperty("message") String message,
             @JsonProperty("code") int code,
             @JsonProperty("type") String type) {
        this.success = success;
        this.message = message;
        this.code = code;
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public boolean isSuccess() {
        return success;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getType() {
        return type;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Response) {
            Response that = (Response) obj;

            return Objects.equals(this.success, that.success)
                    && Objects.equals(this.message, that.message)
                    && Objects.equals(this.code, that.code)
                    && Objects.equals(this.type, that.type);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(success, message, code, type);
    }
}
