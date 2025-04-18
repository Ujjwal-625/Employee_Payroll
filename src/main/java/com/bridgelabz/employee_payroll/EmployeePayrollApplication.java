package com.bridgelabz.employee_payroll;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@Slf4j
@SpringBootApplication
public class EmployeePayrollApplication {

	public static void main(String[] args) {

		ApplicationContext context;
		context = SpringApplication.run(EmployeePayrollApplication.class,args);

		log.info("Employee payroll application started",context.getEnvironment().getProperty("environment"));
		log.info("Employee payroll application started",context.getEnvironment().getProperty("spring.datasource.username"));

	}

}
