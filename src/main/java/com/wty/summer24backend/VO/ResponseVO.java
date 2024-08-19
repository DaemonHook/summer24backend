package com.wty.summer24backend.VO;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseVO<T> {
    private final Integer status;
    private final String message;
    private final T data;

    public ResponseVO(Integer status, String message, T data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public static <T> ResponseVO<T> success() {
        return new ResponseVO<>(200, "Success", null);
    }

    public static <T> ResponseVO<T> error() {
        return new ResponseVO<>(400, "error", null);
    }

    public static <T> ResponseVO<T> success(T data) {
        return new ResponseVO<>(200, "Success", null);
    }
}
