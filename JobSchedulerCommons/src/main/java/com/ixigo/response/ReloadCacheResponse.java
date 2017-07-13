package com.ixigo.response;

import com.ixigo.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by dixant on 04/04/17.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReloadCacheResponse {
    private Status status;
}
