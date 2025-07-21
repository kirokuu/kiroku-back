package com.example.kiroku.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter @Setter
public class ResponseResult<T> {
    private int code;
    private String message;
    private T result;

    public static <T> ResponseResult<T> success(){
        return ResponseResult.<T>builder()
                .code(200)
                .message("성공적으로 데이터를 불어왔습니다")
                .result(null)
                .build();
    }
    public static <T> ResponseResult<T> success(T data){
       return ResponseResult.<T>builder()
                .code(200)
                .message("성공적으로 데이터를 불어왔습니다")
                .result(data)
                .build();
    }

    public static <T> ResponseResult<T> success(int code, String message, T data){
        return ResponseResult.<T>builder()
                .code(code)
                .message(message)
                .result(data)
                .build();
    }

    public static <T> ResponseResult<T> fail(){
        return ResponseResult.<T>builder()
                .code(400)
                .message("데이터를 불러오지 못하였습니다.")
                .result(null)
                .build();
    }

    public static <T> ResponseResult<T> fail(T data){
        return ResponseResult.<T>builder()
                .code(400)
                .message("데이터를 불러오지 못하였습니다.")
                .result(data)
                .build();
    }

    public static <T> ResponseResult<T> fail(int code, String message, T data){
        return ResponseResult.<T>builder()
                .code(code)
                .message(message)
                .result(data)
                .build();
    }

    public static <T> ResponseResult<T> error(int code, String message){
        return ResponseResult.<T>builder()
                .code(code)
                .message(message)
                .result(null)
                .build();
    }

}
