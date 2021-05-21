package com.nevo.employeeservice.controller;

import com.nevo.employeeservice.model.bl.EmployeeBl;
import com.nevo.employeeservice.model.objects.EmployeeInputObject;
import com.nevo.employeeservice.model.objects.EmployeeOutputObject;
import com.nevo.employeeservice.model.objects.PaginationModel;
import com.nevo.employeeservice.model.objects.ResponseModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author Hamed Rostamzadeh
 */
@RestController
@RequestMapping("/employee")
public class EmployeeController {
    @Autowired
    private EmployeeBl employeeBl;

    /**
     * this method receives an employee and saves it on the database.
     *
     * @param employeeInputObject employee object
     * @return saved employee on database
     */
    @PostMapping("/add")
    public ResponseModel<EmployeeOutputObject> addEmployee(@RequestBody EmployeeInputObject employeeInputObject) {
        return employeeBl.addEmployee(employeeInputObject);
    }

    /**
     * this method receives employee id and returns corresponding employee.
     *
     * @param id id
     * @return employee
     */
    @GetMapping("/{id}")
    public ResponseModel<EmployeeOutputObject> getEmployee(@PathVariable("id") int id) {
        return employeeBl.getEmployee(id);
    }

    /**
     * this method receive an employee and updates the employee on database.
     *
     * @param id                  id
     * @param employeeInputObject employee
     * @return saved employee on database
     */
    @PutMapping("/{id}")
    public ResponseModel<EmployeeOutputObject> editEmployee(@PathVariable("id") int id, @RequestBody EmployeeInputObject employeeInputObject) {
        return employeeBl.editEmployee(id, employeeInputObject);
    }

    /**
     * this method removes the corresponding employee to the provided id.
     *
     * @param id id
     * @return status
     */
    @DeleteMapping("/{id}")
    public ResponseModel<Object> deleteEmployee(@PathVariable("id") int id) {
        return employeeBl.deleteEmployee(id);
    }

    /**
     * this method return all the children of the provided employee.
     *
     * @param id   id
     * @param page page number
     * @param size size of each page
     * @return employee children list
     */
    @GetMapping("/getChildren")
    public ResponseModel<PaginationModel<EmployeeOutputObject>> getEmployeeChildren(
            @RequestParam int id, @RequestParam int page, @RequestParam int size) {
        return employeeBl.getEmployeeChildren(id, page, size);
    }

    /**
     * this method returns all the employees with the provided position.
     *
     * @param position employee position
     * @param page     page number
     * @param size     page size
     * @return list of employees
     */
    @GetMapping("/filter_by_position")
    public ResponseModel<PaginationModel<EmployeeOutputObject>> getEmployeeByPosition(
            @RequestParam String position, @RequestParam int page, @RequestParam int size) {
        return employeeBl.getEmployeeByPosition(position, page, size);
    }
}
