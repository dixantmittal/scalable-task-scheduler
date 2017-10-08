package org.dixantmittal.exception;

import lombok.Data;

import java.io.Serializable;

/**
 * Created by dixant on 03/04/17.
 */
@Data
public class ExceptionResponse implements Serializable {
    private String code;
    private String message;
}
