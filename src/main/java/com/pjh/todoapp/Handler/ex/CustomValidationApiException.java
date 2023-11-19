package com.pjh.todoapp.Handler.ex;

import java.util.Map;

public class CustomValidationApiException extends RuntimeException{
    // RuntimeException은 return으로 String을 받기에
    // 어떤 객체에서 데이터 하나를 받을때 생기는 에러

    private static final long serialVersionUID = 1L;
    // 객체를 구분할 때

    private Map<String, String> errorMap;

    public CustomValidationApiException(String message) {
        super(message);
    }

    public CustomValidationApiException(String message, Map<String, String> errorMap) {
        super(message);
        this.errorMap = errorMap;
    }

    public Map<String, String> getErrorMap(){
        return errorMap;
    }
}
