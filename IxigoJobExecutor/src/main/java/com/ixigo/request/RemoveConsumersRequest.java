package com.ixigo.request;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Created by dixant on 30/03/17.
 */
@Data
@AllArgsConstructor
public class RemoveConsumersRequest {
    private String topic;
    private int count;
}
