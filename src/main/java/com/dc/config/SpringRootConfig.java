package com.dc.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@EnableWebMvc
//@ComponentScan({"com.dc"})
@ComponentScan(basePackages = { "com.dc" },
        excludeFilters = @ComponentScan.Filter({Controller.class, Configuration.class, Component.class}))
public class SpringRootConfig {

}

