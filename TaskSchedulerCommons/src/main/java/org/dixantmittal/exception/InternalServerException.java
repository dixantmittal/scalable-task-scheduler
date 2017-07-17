package org.dixantmittal.exception;

import org.dixantmittal.exception.codes.CommonExceptionCodes;

public class InternalServerException extends GenericException {

    public InternalServerException(String errCode, String errMsg) {
        super(errCode, errMsg);
    }

    public InternalServerException(Throwable cause) {
        super(CommonExceptionCodes.INTERNAL_SERVER_EXCEPTION.code(),
                CommonExceptionCodes.INTERNAL_SERVER_EXCEPTION.message(),
                cause);
    }

    public InternalServerException() {
        super(CommonExceptionCodes.INTERNAL_SERVER_EXCEPTION.code(),
                CommonExceptionCodes.INTERNAL_SERVER_EXCEPTION.message());
    }

    public InternalServerException(String code, String message, Throwable cause) {
        super(code, message, cause);
    }
}
