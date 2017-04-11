package com.ixigo.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by dixant on 30/03/17.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RemoveConsumersRequest {
    private String topic;
    private int count;
}
