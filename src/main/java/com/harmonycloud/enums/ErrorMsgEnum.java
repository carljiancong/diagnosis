package com.harmonycloud.enums;


public enum ErrorMsgEnum {

    SERVICE_ERROR("Internal service error"),
    QUERY_DATA_ERROR("query data error"),
    SAVE_ERROR("Save error"),
    UPDATE_ERROR("Update error"),
    PARAM_ERROR("param error"),
    ROCKETMQ_ERROR("Rocketmq send message error");

    private String message;

    ErrorMsgEnum(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
