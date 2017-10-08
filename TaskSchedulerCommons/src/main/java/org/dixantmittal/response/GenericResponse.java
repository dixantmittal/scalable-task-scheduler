package org.dixantmittal.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.dixantmittal.entity.Status;

/**
 * Created by dixant on 29/03/17.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GenericResponse {
    protected Status status;
}
