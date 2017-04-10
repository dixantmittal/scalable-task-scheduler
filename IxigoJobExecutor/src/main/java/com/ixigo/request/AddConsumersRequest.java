package com.ixigo.request;

import com.ixigo.validation.IncreaseConsumerRequestValidation;
import lombok.Data;

/**
 * Created by dixant on 29/03/17.
 */
@Data
@IncreaseConsumerRequestValidation
public class AddConsumersRequest {
    private String topicName;
    private int count;
}
