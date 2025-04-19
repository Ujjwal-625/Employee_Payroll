package com.bridgelabz.employee_payroll.dto;

import lombok.extern.slf4j.Slf4j;


public class ResponseDTO {
    private String message;
    private Object data;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public ResponseDTO(String message, Object data) {
        this.message = message;
        this.data = data;
    }
}
