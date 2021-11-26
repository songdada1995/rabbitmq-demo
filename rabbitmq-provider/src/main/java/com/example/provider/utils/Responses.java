package com.example.provider.utils;

import com.example.provider.exception.BasicException;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class Responses {
    private boolean success = true;
    private Object data;
    private Error error;
    @JsonIgnore
    private HttpServletResponse response;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String exceptionStackTrace;

    public Responses() {
    }

    public static Responses newInstance() {
        return new Responses();
    }

    public Responses failed(String message, Integer code, String trace) {
        this.success = false;
        this.error = new Error(code, message, trace);
        return this;
    }

    public Responses failed(String message, Integer code) {
        return this.failed(message, code, (String) null);
    }

    public Responses failed(BasicException e) {
        return this.failed(e.getMessage(), e.getCode(), e.fetchExceptionTrace()).data(e.getData());
    }

    public Responses data(Object data) {
        this.data = data;
        return this;
    }

    public Responses succeed() {
        this.success = true;
        return this;
    }

    public Responses succeed(Object data) {
        this.data = data;
        this.success = true;
        return this;
    }

    public Responses exportExcel07Init(String fileName) {
        this.response.reset();
        this.response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

        try {
            this.response.setHeader("Content-Disposition", "attachment;filename=" + new String((fileName + ".xlsx").getBytes(), "iso-8859-1"));
            return this;
        } catch (UnsupportedEncodingException var3) {
            throw BasicException.newInstance().error(var3.getMessage(), 500).originEx(var3);
        }
    }

    public Responses exportCSVInit(String fileName) {
        this.response.reset();
        this.response.setContentType("application/csv");

        try {
            this.response.setHeader("Content-Disposition", "attachment;filename=" + new String((fileName + ".csv").getBytes(), "iso-8859-1"));
            return this;
        } catch (UnsupportedEncodingException var3) {
            throw BasicException.newInstance().error(var3.getMessage(), 500).originEx(var3);
        }
    }

    public Responses response(HttpServletResponse response) {
        this.response = response;
        return this;
    }

    public void writeAsJson(int httpStatus) throws IOException {
        if (this.response == null) {
            throw BasicException.newInstance().error("HttpServletResponse is Null", 500);
        } else {
            this.response.setCharacterEncoding("UTF-8");
            this.response.setHeader("Content-type", "application/json;charset=UTF-8");
            this.response.setStatus(httpStatus);
            ObjectMapper jsonMapper = new ObjectMapper();
            String result = jsonMapper.writeValueAsString(this);
            this.response.getWriter().write(result);
        }
    }

    public boolean isSuccess() {
        return this.success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Object getData() {
        return this.data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public Error getError() {
        return this.error;
    }

    public void setError(Error error) {
        this.error = error;
    }

    public String getExceptionStackTrace() {
        return exceptionStackTrace;
    }

    public void setExceptionStackTrace(String exceptionStackTrace) {
        this.exceptionStackTrace = exceptionStackTrace;
    }

    public static class Error {
        private Integer code;
        private String message;
        private String trace;

        public Error(Integer code, String message, String trace) {
            this.code = code;
            this.message = message;
            this.trace = trace;
        }

        public Integer getCode() {
            return this.code;
        }

        public void setCode(Integer code) {
            this.code = code;
        }

        public String getMessage() {
            return this.message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getTrace() {
            return this.trace;
        }

        public void setTrace(String trace) {
            this.trace = trace;
        }
    }
}
