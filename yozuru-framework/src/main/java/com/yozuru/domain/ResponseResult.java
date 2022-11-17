package com.yozuru.domain;

import com.yozuru.domain.enums.HttpCodeEnum;
import lombok.Data;

import java.util.List;

/**
 * @author Yozuru
 */
@Data
public class ResponseResult<T> {
    private Integer code;
    private String message;
    private T data;

    public ResponseResult() {
        this.setCode(HttpCodeEnum.SUCCESS.getCode());
        this.setMessage(HttpCodeEnum.SUCCESS.getMessage());
    }

    public ResponseResult(HttpCodeEnum httpCodeEnum, T data) {
        this.code = httpCodeEnum.getCode();
        this.message = httpCodeEnum.getMessage();
        this.data = data;
    }

    public static <T> ResponseResult<T> success(T data) {
        ResponseResult<T> responseResult = new ResponseResult<>();
        responseResult.setData(data);
        return responseResult;
    }
}
