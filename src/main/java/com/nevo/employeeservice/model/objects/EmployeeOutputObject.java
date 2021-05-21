package com.nevo.employeeservice.model.objects;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.nevo.employeeservice.model.to.EmployeeEntity;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

/**
 * @author Hamed Rostamzadeh
 */
@Builder
@Data
public class EmployeeOutputObject {
    private Integer id;
    private String name;
    private String position;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startDate;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endDate;
    private EmployeeOutputObject superior;

    public static EmployeeOutputObject buildFromEmployeeEntity(EmployeeEntity employeeEntity) {
        if (employeeEntity == null)
            return null;
        EmployeeOutputObject superior = null;
        if (employeeEntity.getSuperior() != null)
            superior = EmployeeOutputObject.builder()
                    .id(employeeEntity.getSuperior().getId())
                    .name(employeeEntity.getSuperior().getName())
                    .position(employeeEntity.getSuperior().getPosition())
                    .startDate(employeeEntity.getSuperior().getStartDate())
                    .endDate(employeeEntity.getSuperior().getEndDate())
                    .superior(null)
                    .build();
        return EmployeeOutputObject.builder()
                .id(employeeEntity.getId())
                .name(employeeEntity.getName())
                .position(employeeEntity.getPosition())
                .startDate(employeeEntity.getStartDate())
                .endDate(employeeEntity.getEndDate())
                .superior(superior)
                .build();
    }
}
