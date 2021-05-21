package com.nevo.employeeservice.util;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * @author Hamed Rostamzadeh
 */
@Configuration
@EnableJpaRepositories("com.nevo.employeeservice.model.dao")
@EntityScan("com.nevo.employeeservice.model.to")
@ComponentScan("com.nevo.employeeservice")
public class TestConfiguration {
}
