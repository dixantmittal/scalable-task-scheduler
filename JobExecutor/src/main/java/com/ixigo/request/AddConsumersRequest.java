package com.ixigo.request;

import com.ixigo.validation.AddConsumersRequestValidation;
import lombok.Data;

/**
 * Created by dixant on 29/03/17.
 */
@Data
@AddConsumersRequestValidation
public class AddConsumersRequest {
    private String topicName;
    private int count;
}
