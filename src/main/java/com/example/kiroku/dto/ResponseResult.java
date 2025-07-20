package com.example.kiroku.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter @Setter
public abstract class ResponseResult {
    private int code;
    private String message;
    private Object result;

    public ResponseResult(int code, String message, Object result){
        this.code = code;
        this.message = message;
        this.result = result;
    }

    public ResponseResult getResult(){
        setResult();
        return this;
    };

    public ResponseResult getCustomResult(String message, int code){
        setResult();
        this.message = message;
        this.code = code;
        return this;
    };

    protected abstract void setResult();

    protected void basicSuccessSet(){
        this.code = 200;
        this.message = "성공적으로 데이터를 불어왔습니다";
    }

    protected void customSuccessSet(int code, String message){
        this.code = code;
        this.message = message;
    }
    protected void basicFailSet(){
        this.code = 400;
        this.message = "데이터를 불러오지 못하였습니다.";
    }

    protected void customFailSet(int code, String message){
        this.code = code;
        this.message = message;
    }

}
