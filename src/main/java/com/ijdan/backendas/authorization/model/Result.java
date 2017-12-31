package com.ijdan.backendas.authorization.model;

public class Result {
    public enum ResultStatus {
        SUCCESS, FAIL
    }

    private ResultStatus resultStatus;
    private Object body;

    private Result() {
    }

    public static Result success(Object body) {
        Result response = new Result();
        response.body = body;
        response.resultStatus = ResultStatus.SUCCESS;
        return response;
    }

    public static Result success() {
        Result response = new Result();
        response.body = null;
        response.resultStatus = ResultStatus.SUCCESS;
        return response;
    }

    public static Result fail(Object body) {
        Result response = new Result();
        response.body = body;
        response.resultStatus = ResultStatus.FAIL;
        return response;
    }

    public boolean isSuccess() {
        return resultStatus == ResultStatus.SUCCESS;
    }

    public Object getBody() {
        return body;
    }
}
