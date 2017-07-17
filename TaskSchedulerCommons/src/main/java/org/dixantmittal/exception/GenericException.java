package org.dixantmittal.exception;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class GenericException extends RuntimeException {

    private static final long serialVersionUID = 1L;
    private String errCode;
    private String errMessage;

    private Class<? extends GenericException> exceptionCause;
    {
        this.setExceptionCause(this.getClass());
    }

    public GenericException(String errCode, String errMessage) {
        super(errMessage);
        this.errCode = errCode;
        this.errMessage = errMessage;
    }

    public GenericException(String errCode, String message, Throwable cause) {
        super(message, cause);
        this.errCode = errCode;
        this.errMessage = message;
    }

}
