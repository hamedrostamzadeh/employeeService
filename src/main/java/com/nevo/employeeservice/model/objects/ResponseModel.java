package com.nevo.employeeservice.model.objects;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.nevo.employeeservice.model.enums.ErrorCodeEnum;
import lombok.Builder;
import lombok.Data;

/**
 * @author Hamed Rostamzadeh
 */
@Builder
@Data
public class ResponseModel<T> {
    @JsonProperty
    private final ErrorCodeEnum errorCode;
    @JsonProperty
    private final String message;
    @JsonProperty
    private final T content;
}
