package com.nevo.employeeservice.model.objects;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * @author Hamed Rostamzadeh
 */
@Builder
@Data
public class PaginationModel<T> {
    @JsonProperty
    private final int totalPage;
    @JsonProperty
    private final long totalElements;
    @JsonProperty
    private final int number;
    @JsonProperty
    private final List<T> items;
}
