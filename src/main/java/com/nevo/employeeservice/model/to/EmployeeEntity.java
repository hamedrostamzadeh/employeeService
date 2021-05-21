package com.nevo.employeeservice.model.to;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

/**
 * @author Hamed Rostamzadeh
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "employee")
public class EmployeeEntity {
    @Id
    @GeneratedValue
    @Column(name = "id")
    private Integer id;
    @Column(name = "name")
    private String name;
    @Column(name = "position")
    private String position;
    @Column(name = "startDate")
    private Date startDate;
    @Column(name = "endDate")
    private Date endDate;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToOne(optional = true)
    @JoinColumn(name = "superior_id", referencedColumnName = "id")
    private EmployeeEntity superior;
}
