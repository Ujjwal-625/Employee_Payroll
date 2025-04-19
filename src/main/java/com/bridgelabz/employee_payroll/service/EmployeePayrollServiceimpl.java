package com.bridgelabz.employee_payroll.service;


import com.bridgelabz.employee_payroll.dto.EmployeeDTO;
import com.bridgelabz.employee_payroll.dto.ResponseDTO;
import com.bridgelabz.employee_payroll.exception.EmployeePayrollException;
import com.bridgelabz.employee_payroll.model.Employee;
import com.bridgelabz.employee_payroll.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class EmployeePayrollServiceimpl implements IEmployeePayrollService {

    private List<Employee> employees = new ArrayList<>();

    @Autowired
    private EmployeeRepository employeeRepository;

    @Override
    public List<Employee> getEmployeePayrollData() {
        return employeeRepository.findAll();
    }

    public Employee getEmployeePayrollDataById(int empId) {
        try{
        return employeeRepository.findById(empId).get();
        }catch (Exception e){
            System.out.println("Exception caught "+e.getMessage());
            throw new EmployeePayrollException("Provided Id is not present");
        }
    }

    public Employee createEmployeePayrollData(EmployeeDTO empPayrollDTO) {
        try{
            Employee employee = null;
            employee = new Employee(empPayrollDTO);
            employeeRepository.save(employee);
            return employee;
        } catch (Exception e) {
            throw new EmployeePayrollException(e.getMessage());
        }
    }

    public Employee updateEmployeePayrollData(int empId, EmployeeDTO empPayrollDTO) {
        try{
            Employee empData = this.getEmployeePayrollDataById(empId);
            empData.setName(empPayrollDTO.getName());
            empData.setSalary(empPayrollDTO.getSalary());
            employeeRepository.save(empData);
            return empData;
        } catch (Exception e) {
            throw new EmployeePayrollException(e.getMessage());
        }
    }

    public void deleteEmployeePayrollData(int empId) {
        if(!employeeRepository.existsById(empId)){
            throw new EmployeePayrollException("No Employee found with given id");
        }
        employeeRepository.deleteById(empId);
    }
}