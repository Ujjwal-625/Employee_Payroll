package com.bridgelabz.employee_payroll.exception;


import com.bridgelabz.employee_payroll.model.Employee;

public class EmployeePayrollException extends RuntimeException {
    public EmployeePayrollException(String message) {
        super(message);
    }
}
