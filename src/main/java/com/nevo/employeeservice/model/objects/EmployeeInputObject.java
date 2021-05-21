package com.nevo.employeeservice.model.objects;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

/**
 * @author Hamed Rostamzadeh
 */
@Data
@Builder
public class EmployeeInputObject {
    private String name;
    private String position;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startDate;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endDate;
    private Integer superior_id;
}
