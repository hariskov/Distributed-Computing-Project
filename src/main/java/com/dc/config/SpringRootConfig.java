package com.i2n.imCms.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportResource;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

/**
 * Created by petarh on 30/06/2016.
 */
@Configuration
@EnableWebMvc
@ComponentScan({"com.i2n.imCms"})
@ImportResource("classpath:imCms-servlet.xml")
@Import(value=WebSecurityConfiguration.class)
public class SpringRootConfig {
}
