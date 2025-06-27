package com.techlab.proyecto_final_caruso_luciano.response;

public class ApiResponseWithData<T>{
    private String message;
    private T data;

    public ApiResponseWithData(String message, T data)
    {
        this.message = message;
        this.data    = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
