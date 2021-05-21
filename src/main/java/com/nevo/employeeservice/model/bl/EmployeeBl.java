package com.nevo.employeeservice.model.bl;

import com.nevo.employeeservice.model.dao.EmployeeDao;
import com.nevo.employeeservice.model.enums.ErrorCodeEnum;
import com.nevo.employeeservice.model.objects.EmployeeInputObject;
import com.nevo.employeeservice.model.objects.EmployeeOutputObject;
import com.nevo.employeeservice.model.objects.PaginationModel;
import com.nevo.employeeservice.model.objects.ResponseModel;
import com.nevo.employeeservice.model.to.EmployeeEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

/**
 * @author Hamed Rostamzadeh
 */
@Service
public class EmployeeBl {
    @Autowired
    private EmployeeDao employeeDao;

    /**
     * this method receives an employee and saves it on the database.
     *
     * @param employeeInputObject employee object
     * @return saved employee on database
     */
    public ResponseModel<EmployeeOutputObject> addEmployee(EmployeeInputObject employeeInputObject) {
        EmployeeEntity superior = null;
        if (employeeInputObject.getSuperior_id() != null) {
            superior = employeeDao.findById(employeeInputObject.getSuperior_id()).orElse(null);
            if (superior == null)
                return ResponseModel.<EmployeeOutputObject>builder()
                        .errorCode(ErrorCodeEnum.ERROR)
                        .message("superior employee not found")
                        .build();
            if (!superior.getPosition().equals("management"))
                return ResponseModel.<EmployeeOutputObject>builder()
                        .errorCode(ErrorCodeEnum.ERROR)
                        .message("superior employee is not management")
                        .build();

        }
        EmployeeEntity employeeEntity = employeeDao.save(EmployeeEntity.builder()
                .name(employeeInputObject.getName())
                .position(employeeInputObject.getPosition())
                .startDate(employeeInputObject.getStartDate())
                .endDate(employeeInputObject.getEndDate())
                .superior(superior)
                .build());
        return ResponseModel.<EmployeeOutputObject>builder()
                .errorCode(ErrorCodeEnum.OK)
                .content(EmployeeOutputObject.buildFromEmployeeEntity(employeeEntity))
                .build();
    }

    /**
     * this method receives employee id and returns corresponding employee.
     *
     * @param id id
     * @return employee
     */
    public ResponseModel<EmployeeOutputObject> getEmployee(int id) {
        EmployeeEntity employeeEntity = employeeDao.findById(id).orElse(null);
        if (employeeEntity == null)
            return ResponseModel.<EmployeeOutputObject>builder()
                    .errorCode(ErrorCodeEnum.ERROR)
                    .message("employee not found")
                    .build();

        return ResponseModel.<EmployeeOutputObject>builder()
                .errorCode(ErrorCodeEnum.OK)
                .content(EmployeeOutputObject.buildFromEmployeeEntity(employeeEntity))
                .build();
    }


    /**
     * this method receive an employee and updates the employee on database.
     *
     * @param id                  id
     * @param employeeInputObject employee
     * @return saved employee on database
     */
    public ResponseModel<EmployeeOutputObject> editEmployee(int id, EmployeeInputObject employeeInputObject) {
        EmployeeEntity employeeEntity = employeeDao.findById(id).orElse(null);
        if (employeeEntity == null)
            return ResponseModel.<EmployeeOutputObject>builder()
                    .errorCode(ErrorCodeEnum.ERROR)
                    .message("employee not found")
                    .build();

        EmployeeEntity superior = null;
        if (employeeInputObject.getSuperior_id() != null) {
            superior = employeeDao.findById(employeeInputObject.getSuperior_id()).orElse(null);
            if (superior == null)
                return ResponseModel.<EmployeeOutputObject>builder()
                        .errorCode(ErrorCodeEnum.ERROR)
                        .message("superior employee not found")
                        .build();
            if (!superior.getPosition().equals("management"))
                return ResponseModel.<EmployeeOutputObject>builder()
                        .errorCode(ErrorCodeEnum.ERROR)
                        .message("superior employee is not management")
                        .build();

        }

        employeeEntity.setName(employeeInputObject.getName());
        employeeEntity.setPosition(employeeInputObject.getPosition());
        employeeEntity.setStartDate(employeeInputObject.getStartDate());
        employeeEntity.setEndDate(employeeInputObject.getEndDate());
        employeeEntity.setSuperior(superior);

        return ResponseModel.<EmployeeOutputObject>builder()
                .errorCode(ErrorCodeEnum.OK)
                .content(EmployeeOutputObject.buildFromEmployeeEntity(employeeDao.save(employeeEntity)))
                .build();
    }

    /**
     * this method removes the corresponding employee to the provided id.
     *
     * @param id id
     * @return status
     */
    public ResponseModel<Object> deleteEmployee(int id) {
        employeeDao.deleteById(id);
        return ResponseModel.builder()
                .errorCode(ErrorCodeEnum.OK)
                .build();
    }

    /**
     * this method return all the children of the provided employee.
     *
     * @param id   id
     * @param page page number
     * @param size size of each page
     * @return employee children list
     */
    public ResponseModel<PaginationModel<EmployeeOutputObject>> getEmployeeChildren(int id, int page, int size) {
        EmployeeEntity employeeEntity = employeeDao.findById(id).orElse(null);
        if (employeeEntity == null)
            return ResponseModel.<PaginationModel<EmployeeOutputObject>>builder()
                    .errorCode(ErrorCodeEnum.ERROR)
                    .message("employee not found")
                    .build();
        if (!employeeEntity.getPosition().equals("management"))
            return ResponseModel.<PaginationModel<EmployeeOutputObject>>builder()
                    .errorCode(ErrorCodeEnum.ERROR)
                    .message("employee has not management position")
                    .build();

        Page<EmployeeEntity> employeeEntityPage = employeeDao.findBySuperior(employeeEntity, PageRequest.of(page, size));
        return ResponseModel.<PaginationModel<EmployeeOutputObject>>builder()
                .errorCode(ErrorCodeEnum.OK)
                .content(PaginationModel.<EmployeeOutputObject>builder()
                        .totalElements(employeeEntityPage.getTotalElements())
                        .totalPage(employeeEntityPage.getTotalPages())
                        .number(employeeEntityPage.getNumberOfElements())
                        .items(employeeEntityPage.getContent().stream()
                                .map(EmployeeOutputObject::buildFromEmployeeEntity).collect(Collectors.toList()))
                        .build())
                .build();
    }

    /**
     * this method returns all the employees with the provided position.
     *
     * @param position employee position
     * @param page     page number
     * @param size     page size
     * @return list of employees
     */
    public ResponseModel<PaginationModel<EmployeeOutputObject>> getEmployeeByPosition(String position, int page, int size) {
        Page<EmployeeEntity> employeeEntityPage = employeeDao.findByPositionEquals(position, PageRequest.of(page, size));
        return ResponseModel.<PaginationModel<EmployeeOutputObject>>builder()
                .errorCode(ErrorCodeEnum.OK)
                .content(PaginationModel.<EmployeeOutputObject>builder()
                        .totalElements(employeeEntityPage.getTotalElements())
                        .totalPage(employeeEntityPage.getTotalPages())
                        .number(employeeEntityPage.getNumberOfElements())
                        .items(employeeEntityPage.getContent().stream()
                                .map(EmployeeOutputObject::buildFromEmployeeEntity).collect(Collectors.toList()))
                        .build())
                .build();
    }
}
