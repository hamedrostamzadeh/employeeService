package com.nevo.employeeservice.util;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

/**
 * @author Hamed Rostamzadeh
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest
@ContextConfiguration(loader = AnnotationConfigContextLoader.class, classes = TestConfiguration.class)
public abstract class BasicTest {

}
