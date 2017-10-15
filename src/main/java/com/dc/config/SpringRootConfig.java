package com.dc.config;

import com.dc.pojo.DeviceManager;
import com.dc.pojo.VotingManager;
import org.springframework.context.annotation.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@EnableWebMvc
//@ComponentScan({"com.dc"})
@ComponentScan(basePackages = { "com.dc" },
        excludeFilters = @ComponentScan.Filter({Controller.class, Configuration.class}))
public class SpringRootConfig {

}
