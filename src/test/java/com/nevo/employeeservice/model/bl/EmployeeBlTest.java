package com.nevo.employeeservice.model.bl;

import com.nevo.employeeservice.model.dao.EmployeeDao;
import com.nevo.employeeservice.model.enums.ErrorCodeEnum;
import com.nevo.employeeservice.model.objects.EmployeeInputObject;
import com.nevo.employeeservice.model.objects.EmployeeOutputObject;
import com.nevo.employeeservice.model.objects.PaginationModel;
import com.nevo.employeeservice.model.objects.ResponseModel;
import com.nevo.employeeservice.model.to.EmployeeEntity;
import com.nevo.employeeservice.util.BasicTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Hamed Rostamzadeh
 */
class EmployeeBlTest extends BasicTest {
    @Autowired
    private EmployeeBl employeeBl;
    @Autowired
    private EmployeeDao employeeDao;

    @BeforeEach
    public void clearDb() {
        employeeDao.deleteAll();
    }

    /**
     * Scenario: for valid employee input it is expected to that the employee be saved on the database
     */
    @Test
    void testAddEmployee_validInput_expectedValidOutput() {
        EmployeeInputObject employeeInputObject = EmployeeInputObject.builder()
                .name("employee1")
                .position("developer")
                .startDate(new Date())
                .endDate(new Date())
                .build();
        ResponseModel<EmployeeOutputObject> result = employeeBl.addEmployee(employeeInputObject);
        assertEquals(ErrorCodeEnum.OK, result.getErrorCode());
        assertNull(result.getMessage());
        assertNotNull(result.getContent());
        assertEquals(employeeInputObject.getName(), result.getContent().getName());
        assertEquals(employeeInputObject.getPosition(), result.getContent().getPosition());
        assertEquals(employeeInputObject.getStartDate(), result.getContent().getStartDate());
        assertEquals(employeeInputObject.getEndDate(), result.getContent().getEndDate());
        assertNull(result.getContent().getSuperior());
    }

    /**
     * Scenario:for the provided employee, there is not superior employee with the id in input.
     * So, error will returned.
     */
    @Test
    void testAddEmployee_invalidSuperiorEmployeeId_expectedValidOutput() {
        EmployeeInputObject employeeInputObject = EmployeeInputObject.builder()
                .name("employee1")
                .position("developer")
                .startDate(new Date())
                .endDate(new Date())
                .superior_id(9999)
                .build();
        ResponseModel<EmployeeOutputObject> result = employeeBl.addEmployee(employeeInputObject);
        assertEquals(ErrorCodeEnum.ERROR, result.getErrorCode());
        assertNull(result.getContent());
        assertEquals("superior employee not found", result.getMessage());
    }

    /**
     * Scenario: for the valid input employee with a superior, it is expected to that the employee
     * be saved on the database.
     */
    @Test
    void testAddEmployee_validInputWithSuperior_expectedValidOutput() {
        EmployeeInputObject employeeInputObject = EmployeeInputObject.builder()
                .name("employee1")
                .position("management")
                .startDate(new Date())
                .endDate(new Date())
                .build();
        ResponseModel<EmployeeOutputObject> result = employeeBl.addEmployee(employeeInputObject);
        assertEquals(ErrorCodeEnum.OK, result.getErrorCode());
        assertNull(result.getMessage());
        assertNotNull(result.getContent());
        assertEquals(employeeInputObject.getName(), result.getContent().getName());
        assertEquals(employeeInputObject.getPosition(), result.getContent().getPosition());
        assertEquals(employeeInputObject.getStartDate(), result.getContent().getStartDate());
        assertEquals(employeeInputObject.getEndDate(), result.getContent().getEndDate());
        assertNull(result.getContent().getSuperior());

        EmployeeInputObject employeeInputObject2 = EmployeeInputObject.builder()
                .name("employee1")
                .position("developer")
                .startDate(new Date())
                .endDate(new Date())
                .superior_id(result.getContent().getId())
                .build();
        ResponseModel<EmployeeOutputObject> result2 = employeeBl.addEmployee(employeeInputObject2);
        assertEquals(ErrorCodeEnum.OK, result2.getErrorCode());
        assertNull(result2.getMessage());
        assertNotNull(result2.getContent());

        assertEquals(employeeInputObject2.getName(), result2.getContent().getName());
        assertEquals(employeeInputObject2.getPosition(), result2.getContent().getPosition());
        assertEquals(employeeInputObject2.getStartDate(), result2.getContent().getStartDate());
        assertEquals(employeeInputObject2.getEndDate(), result2.getContent().getEndDate());
        assertNotNull(result2.getContent().getSuperior());

        assertEquals(employeeInputObject.getName(), result2.getContent().getSuperior().getName());
        assertEquals(employeeInputObject.getPosition(), result2.getContent().getSuperior().getPosition());
        assertEquals(employeeInputObject.getStartDate(), result2.getContent().getSuperior().getStartDate());
        assertEquals(employeeInputObject.getEndDate(), result2.getContent().getSuperior().getEndDate());
        assertNull(result2.getContent().getSuperior().getSuperior());

    }

    /**
     * Scenario: for the provided employee, there superior employee not found. So error will
     * returned.
     */
    @Test
    void testAddEmployee_superiorEmployeeNotManagement_expectedError() {
        EmployeeInputObject employeeInputObject = EmployeeInputObject.builder()
                .name("employee1")
                .position("developer")
                .startDate(new Date())
                .endDate(new Date())
                .build();
        ResponseModel<EmployeeOutputObject> result = employeeBl.addEmployee(employeeInputObject);
        assertEquals(ErrorCodeEnum.OK, result.getErrorCode());
        assertNull(result.getMessage());
        assertNotNull(result.getContent());

        assertEquals(employeeInputObject.getName(), result.getContent().getName());
        assertEquals(employeeInputObject.getPosition(), result.getContent().getPosition());
        assertEquals(employeeInputObject.getStartDate(), result.getContent().getStartDate());
        assertEquals(employeeInputObject.getEndDate(), result.getContent().getEndDate());
        assertNull(result.getContent().getSuperior());

        EmployeeInputObject employeeInputObject2 = EmployeeInputObject.builder()
                .name("employee1")
                .position("developer")
                .startDate(new Date())
                .endDate(new Date())
                .superior_id(result.getContent().getId())
                .build();
        ResponseModel<EmployeeOutputObject> result2 = employeeBl.addEmployee(employeeInputObject2);
        assertEquals(ErrorCodeEnum.ERROR, result2.getErrorCode());
        assertNull(result2.getContent());
        assertEquals("superior employee is not management", result2.getMessage());
    }

    /**
     * Scenario: with invalid employee Id, it is expected to error returned.
     */
    @Test
    void testGetEmployee_invalidEmployeeId_expectedError() {
        ResponseModel<EmployeeOutputObject> result = employeeBl.getEmployee(99999);
        assertEquals(ErrorCodeEnum.ERROR, result.getErrorCode());
        assertEquals("employee not found", result.getMessage());
        assertNull(result.getContent());
    }

    /**
     * Scenario: with the valid employee Id, it is expected to corresponding employee
     * be returned from the database.
     */
    @Test
    void testGetEmployee_validEmployeeId_expectedValidOutput() {
        EmployeeEntity employeeEntity = EmployeeEntity.builder()
                .name("employee1")
                .position("developer")
                .startDate(new Date())
                .endDate(new Date())
                .build();
        employeeDao.save(employeeEntity);

        ResponseModel<EmployeeOutputObject> result = employeeBl.getEmployee(employeeEntity.getId());
        assertEquals(ErrorCodeEnum.OK, result.getErrorCode());
        assertNull(result.getMessage());
        assertNotNull(result.getContent());

        assertEquals(employeeEntity.getId(), result.getContent().getId());
        assertEquals(employeeEntity.getName(), result.getContent().getName());
        assertEquals(employeeEntity.getPosition(), result.getContent().getPosition());
        assertEquals(employeeEntity.getStartDate(), result.getContent().getStartDate());
        assertEquals(employeeEntity.getEndDate(), result.getContent().getEndDate());
        assertNull(result.getContent().getSuperior());
    }

    /**
     * Scenario: by providing valid input for editing the employee, it is expected to the employee
     * updated on the database.
     */
    @Test
    void testEditEmployee_validInput_expectedValidOutput() {
        EmployeeEntity employeeEntity = EmployeeEntity.builder()
                .name("employee1")
                .position("developer")
                .startDate(new Date())
                .endDate(new Date())
                .build();
        employeeDao.save(employeeEntity);

        EmployeeInputObject employeeInputObject = EmployeeInputObject.builder()
                .name("employee2")
                .position("management")
                .startDate(new Date())
                .endDate(new Date())
                .build();
        ResponseModel<EmployeeOutputObject> result = employeeBl.editEmployee(employeeEntity.getId(), employeeInputObject);
        assertEquals(ErrorCodeEnum.OK, result.getErrorCode());
        assertNull(result.getMessage());
        assertNotNull(result.getContent());
        assertEquals(employeeInputObject.getName(), result.getContent().getName());
        assertEquals(employeeInputObject.getPosition(), result.getContent().getPosition());
        assertEquals(employeeInputObject.getStartDate(), result.getContent().getStartDate());
        assertEquals(employeeInputObject.getEndDate(), result.getContent().getEndDate());
        assertNull(result.getContent().getSuperior());
    }

    /**
     * Scenario: by providing invalid employee Id to edit, it is expected to error returned.
     */
    @Test
    void testEditEmployee_invalidEmployeeId_expectedError() {
        EmployeeInputObject employeeInputObject = EmployeeInputObject.builder()
                .name("employee2")
                .position("management")
                .startDate(new Date())
                .endDate(new Date())
                .build();
        ResponseModel<EmployeeOutputObject> result = employeeBl.editEmployee(88888, employeeInputObject);
        assertEquals(ErrorCodeEnum.ERROR, result.getErrorCode());
        assertNull(result.getContent());
        assertEquals("employee not found", result.getMessage());
    }

    /**
     * Scenario: by providing invalid superior Id, it is expected to error returned by employee edit method.
     */
    @Test
    void testEditEmployee_invalidSuperiorEmployeeId_expectedError() {
        EmployeeEntity employeeEntity = EmployeeEntity.builder()
                .name("employee1")
                .position("developer")
                .startDate(new Date())
                .endDate(new Date())
                .build();
        employeeDao.save(employeeEntity);

        EmployeeInputObject employeeInputObject = EmployeeInputObject.builder()
                .name("employee2")
                .position("management")
                .startDate(new Date())
                .endDate(new Date())
                .superior_id(9999)
                .build();
        ResponseModel<EmployeeOutputObject> result = employeeBl.editEmployee(employeeEntity.getId(), employeeInputObject);
        assertEquals(ErrorCodeEnum.ERROR, result.getErrorCode());
        assertNull(result.getContent());
        assertEquals("superior employee not found", result.getMessage());
    }

    /**
     * Scenario: by providing valid superior Id, the superior's position does not match management position.
     * so it is not possible to edit the employee
     */
    @Test
    void testEditEmployee_invalidSuperiorPosition_expectedError() {
        EmployeeEntity employeeEntity = EmployeeEntity.builder()
                .name("employee1")
                .position("developer")
                .startDate(new Date())
                .endDate(new Date())
                .build();
        employeeDao.save(employeeEntity);

        EmployeeEntity employeeEntity2 = EmployeeEntity.builder()
                .name("employee2")
                .position("developer")
                .startDate(new Date())
                .endDate(new Date())
                .build();
        employeeDao.save(employeeEntity2);

        EmployeeInputObject employeeInputObject2 = EmployeeInputObject.builder()
                .name("111")
                .position("tester")
                .startDate(new Date())
                .endDate(new Date())
                .superior_id(employeeEntity.getId())
                .build();
        ResponseModel<EmployeeOutputObject> result = employeeBl.editEmployee(employeeEntity2.getId(), employeeInputObject2);
        assertEquals(ErrorCodeEnum.ERROR, result.getErrorCode());
        assertNull(result.getContent());
        assertEquals("superior employee is not management", result.getMessage());
    }

    /**
     * Scenario: by providing valid input for editing the employee, it is expected to modification
     * be applied on the database.
     */
    @Test
    void testEditEmployee_validInputWithSuperior_expectedValidOutput() {
        EmployeeEntity employeeEntity = EmployeeEntity.builder()
                .name("employee1")
                .position("management")
                .startDate(new Date())
                .endDate(new Date())
                .build();
        employeeDao.save(employeeEntity);

        EmployeeEntity employeeEntity2 = EmployeeEntity.builder()
                .name("employee2")
                .position("developer")
                .startDate(new Date())
                .endDate(new Date())
                .build();
        employeeDao.save(employeeEntity2);

        EmployeeInputObject employeeInputObject2 = EmployeeInputObject.builder()
                .name("111")
                .position("tester")
                .startDate(new Date())
                .endDate(new Date())
                .superior_id(employeeEntity.getId())
                .build();
        ResponseModel<EmployeeOutputObject> result2 = employeeBl.editEmployee(employeeEntity2.getId(), employeeInputObject2);
        assertEquals(ErrorCodeEnum.OK, result2.getErrorCode());
        assertNull(result2.getMessage());
        assertNotNull(result2.getContent());

        assertEquals(employeeInputObject2.getName(), result2.getContent().getName());
        assertEquals(employeeInputObject2.getPosition(), result2.getContent().getPosition());
        assertEquals(employeeInputObject2.getStartDate(), result2.getContent().getStartDate());
        assertEquals(employeeInputObject2.getEndDate(), result2.getContent().getEndDate());
        assertNotNull(result2.getContent().getSuperior());

        assertEquals(employeeEntity.getId(), result2.getContent().getSuperior().getId());
        assertEquals(employeeEntity.getName(), result2.getContent().getSuperior().getName());
        assertEquals(employeeEntity.getPosition(), result2.getContent().getSuperior().getPosition());
        assertEquals(employeeEntity.getStartDate(), result2.getContent().getSuperior().getStartDate());
        assertEquals(employeeEntity.getEndDate(), result2.getContent().getSuperior().getEndDate());
        assertNull(result2.getContent().getSuperior().getSuperior());

    }

    /**
     * Scenario: in this scenario, the employee that it's id is provided for the deletion, the employee
     * be deleted from database.
     */
    @Test
    void testDeleteEmployee_expectedEmployeeToBeDeleted() {
        EmployeeEntity employeeEntity = EmployeeEntity.builder()
                .name("employee1")
                .position("developer")
                .startDate(new Date())
                .endDate(new Date())
                .build();
        employeeDao.save(employeeEntity);

        ResponseModel<Object> result = employeeBl.deleteEmployee(employeeEntity.getId());
        assertEquals(ErrorCodeEnum.OK, result.getErrorCode());
        assertEquals(0, employeeDao.count());
    }

    /**
     * Scenario: by invalid employee Id, it's not possible to get it's children. so error will be returned.
     */
    @Test
    void testGetEmployeeChildren_invalidEmployeeId_expectedError() {
        ResponseModel<PaginationModel<EmployeeOutputObject>> result = employeeBl.getEmployeeChildren(88888, 0, 10);
        assertEquals(ErrorCodeEnum.ERROR, result.getErrorCode());
        assertNull(result.getContent());
        assertEquals("employee not found", result.getMessage());
    }

    /**
     * Scenario: by providing valid employee Id, the employee's position does not match management,
     * So error will be returned.
     */
    @Test
    void testGetEmployeeChildren_invalidEmployeePosition_expectedError() {
        List<EmployeeEntity> employeeEntities = this.fillDatabase();
        ResponseModel<PaginationModel<EmployeeOutputObject>> result = employeeBl.getEmployeeChildren(employeeEntities.get(3).getId(), 0, 10);
        assertEquals(ErrorCodeEnum.ERROR, result.getErrorCode());
        assertNull(result.getContent());
        assertEquals("employee has not management position", result.getMessage());
    }

    /**
     * Scenario: by providing valid employee that has multiple children, it is expected to valid output returned.
     */
    @Test
    void testGetEmployeeChildren_validInput_expectedValidOutput() {
        List<EmployeeEntity> employeeEntities = this.fillDatabase();
        ResponseModel<PaginationModel<EmployeeOutputObject>> result = employeeBl.getEmployeeChildren(employeeEntities.get(0).getId(), 0, 10);
        assertEquals(ErrorCodeEnum.OK, result.getErrorCode());
        assertNull(result.getMessage());
        assertNotNull(result.getContent());
        assertEquals(2, result.getContent().getTotalElements());
        assertEquals(1, result.getContent().getTotalPage());
        assertEquals(2, result.getContent().getNumber());

        assertEquals(employeeEntities.get(2).getId(), result.getContent().getItems().get(0).getId());
        assertEquals(employeeEntities.get(2).getName(), result.getContent().getItems().get(0).getName());
        assertEquals(employeeEntities.get(2).getPosition(), result.getContent().getItems().get(0).getPosition());
        assertEquals(employeeEntities.get(2).getStartDate(), result.getContent().getItems().get(0).getStartDate());
        assertEquals(employeeEntities.get(2).getEndDate(), result.getContent().getItems().get(0).getEndDate());

        assertEquals(employeeEntities.get(4).getId(), result.getContent().getItems().get(1).getId());
        assertEquals(employeeEntities.get(4).getName(), result.getContent().getItems().get(1).getName());
        assertEquals(employeeEntities.get(4).getPosition(), result.getContent().getItems().get(1).getPosition());
        assertEquals(employeeEntities.get(4).getStartDate(), result.getContent().getItems().get(1).getStartDate());
        assertEquals(employeeEntities.get(4).getEndDate(), result.getContent().getItems().get(1).getEndDate());
    }

    /**
     * Scenario: in this scenario, all the employees with the input position must be returned.
     */
    @Test
    void testGetEmployeeByPosition_validInput_expectedValidOutput() {
        List<EmployeeEntity> employeeEntities = this.fillDatabase();
        ResponseModel<PaginationModel<EmployeeOutputObject>> result =
                employeeBl.getEmployeeByPosition("management", 0, 10);
        assertEquals(ErrorCodeEnum.OK, result.getErrorCode());
        assertNull(result.getMessage());
        assertNotNull(result.getContent());
        assertEquals(2, result.getContent().getTotalElements());
        assertEquals(1, result.getContent().getTotalPage());
        assertEquals(2, result.getContent().getNumber());

        assertEquals(employeeEntities.get(0).getId(), result.getContent().getItems().get(0).getId());
        assertEquals(employeeEntities.get(0).getName(), result.getContent().getItems().get(0).getName());
        assertEquals(employeeEntities.get(0).getPosition(), result.getContent().getItems().get(0).getPosition());
        assertEquals(employeeEntities.get(0).getStartDate(), result.getContent().getItems().get(0).getStartDate());
        assertEquals(employeeEntities.get(0).getEndDate(), result.getContent().getItems().get(0).getEndDate());
        assertNull(result.getContent().getItems().get(0).getSuperior());

        assertEquals(employeeEntities.get(1).getId(), result.getContent().getItems().get(1).getId());
        assertEquals(employeeEntities.get(1).getName(), result.getContent().getItems().get(1).getName());
        assertEquals(employeeEntities.get(1).getPosition(), result.getContent().getItems().get(1).getPosition());
        assertEquals(employeeEntities.get(1).getStartDate(), result.getContent().getItems().get(1).getStartDate());
        assertEquals(employeeEntities.get(1).getEndDate(), result.getContent().getItems().get(1).getEndDate());
        assertNull(result.getContent().getItems().get(1).getSuperior());
    }


    /**
     * this method creates multiple employees on the database for tests.
     *
     * @return list of emplyees
     */
    public List<EmployeeEntity> fillDatabase() {
        EmployeeEntity employeeEntity1 = EmployeeEntity.builder().name("name1").position("management")
                .startDate(new Date()).endDate(new Date()).superior(null).build();

        EmployeeEntity employeeEntity2 = EmployeeEntity.builder().name("name2").position("management")
                .startDate(new Date()).endDate(new Date()).superior(null).build();

        EmployeeEntity employeeEntity3 = EmployeeEntity.builder().name("name3").position("developer")
                .startDate(new Date()).endDate(new Date()).superior(employeeEntity1).build();

        EmployeeEntity employeeEntity4 = EmployeeEntity.builder().name("name4").position("developer")
                .startDate(new Date()).endDate(new Date()).superior(employeeEntity2).build();

        EmployeeEntity employeeEntity5 = EmployeeEntity.builder().name("name5").position("developer")
                .startDate(new Date()).endDate(new Date()).superior(employeeEntity1).build();
        List<EmployeeEntity> employeeEntityList = new ArrayList<>();
        employeeEntityList.add(employeeEntity1);
        employeeEntityList.add(employeeEntity2);
        employeeEntityList.add(employeeEntity3);
        employeeEntityList.add(employeeEntity4);
        employeeEntityList.add(employeeEntity5);

        employeeDao.saveAll(employeeEntityList);
        return employeeEntityList;
    }
}