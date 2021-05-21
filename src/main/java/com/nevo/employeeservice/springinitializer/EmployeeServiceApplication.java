package com.nevo.employeeservice.springinitializer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories("com.nevo.employeeservice.model.dao")
@EntityScan("com.nevo.employeeservice.model.to")
@SpringBootApplication(scanBasePackages = "com.nevo.employeeservice")
public class EmployeeServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(EmployeeServiceApplication.class, args);
    }

}
