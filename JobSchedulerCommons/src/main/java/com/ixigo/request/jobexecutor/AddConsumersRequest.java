package com.ixigo.request.jobexecutor;

import com.ixigo.validation.jobexecutor.AddConsumersRequestValidation;
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
