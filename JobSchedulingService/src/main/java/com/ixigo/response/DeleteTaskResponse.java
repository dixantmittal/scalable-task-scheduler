package com.ixigo.response;

import com.ixigo.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by dixant on 30/03/17.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeleteTaskResponse {
    private Status status;
}
