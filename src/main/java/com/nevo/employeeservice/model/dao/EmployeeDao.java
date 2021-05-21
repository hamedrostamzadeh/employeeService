package com.nevo.employeeservice.model.dao;

import com.nevo.employeeservice.model.to.EmployeeEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Hamed Rostamzadeh
 */
@Repository
public interface EmployeeDao extends CrudRepository<EmployeeEntity, Integer> {
    Page<EmployeeEntity> findBySuperior(EmployeeEntity superior, Pageable peaPageable);

    Page<EmployeeEntity> findByPositionEquals(String position, Pageable peaPageable);
}
