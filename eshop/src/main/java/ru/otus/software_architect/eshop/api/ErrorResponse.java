package ru.otus.software_architect.eshop.api;

public class ErrorResponse {
    //private String endPoint;
    private int code;
    private String message;
    //private Throwable throwable;

    public ErrorResponse() {
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "ErrorResponse {" +
                " code = " + code +
                ", message = '" + message + '\'' +
                '}';
    }
}
