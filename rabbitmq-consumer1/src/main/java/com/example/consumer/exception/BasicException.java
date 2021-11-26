package com.example.consumer.exception;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

/**
 * @Author songbo
 * @Date 2020/8/12 12:27
 * @Version 1.0
 */
public class BasicException extends RuntimeException {
    private Integer code;
    private String message;
    private Object data;
    private Throwable originException;

    public BasicException() {
    }

    public static BasicException newInstance() {
        return new BasicException();
    }

    public BasicException error(String message, Integer code) {
        this.code = code;
        this.message = message;
        return this;
    }

    public BasicException originEx(Throwable originException) {
        this.originException = originException;
        return this;
    }

    public BasicException data(Object data) {
        this.data = data;
        return this;
    }

    public Integer getCode() {
        return this.code;
    }

    public String getMessage() {
        return this.message;
    }

    public Object getData() {
        return this.data;
    }

    public Throwable getOriginException() {
        return this.originException;
    }

    public String fetchExceptionTrace() {
        return this.originException != null ? exceptionTrace(this.originException) : exceptionTrace(this);
    }

    public static String exceptionTrace(Throwable e) {
        if (e == null) {
            return null;
        } else {
            StringBuilder stringBuilder = new StringBuilder();

            try {
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                Throwable var3 = null;

                try {
                    PrintStream writer = new PrintStream(out);
                    Throwable var5 = null;

                    try {
                        if (e != null) {
                            e.printStackTrace(writer);
                            writer.flush();
                            stringBuilder.append(new String(out.toByteArray()));
                        }
                    } catch (Throwable var30) {
                        var5 = var30;
                        throw var30;
                    } finally {
                        if (writer != null) {
                            if (var5 != null) {
                                try {
                                    writer.close();
                                } catch (Throwable var29) {
                                    var5.addSuppressed(var29);
                                }
                            } else {
                                writer.close();
                            }
                        }

                    }
                } catch (Throwable var32) {
                    var3 = var32;
                    throw var32;
                } finally {
                    if (out != null) {
                        if (var3 != null) {
                            try {
                                out.close();
                            } catch (Throwable var28) {
                                var3.addSuppressed(var28);
                            }
                        } else {
                            out.close();
                        }
                    }

                }
            } catch (Exception var34) {
            }

            return stringBuilder.toString();
        }
    }
}
