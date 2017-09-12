package com.i2n.imCms.config;

import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

/**
 * Created by petarh on 30/06/2016.
 */
public class WebInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

    @Override
    protected Class<?>[] getRootConfigClasses(){
        return new Class[]{
                SpringRootConfig.class
        };
    }

    @Override
    protected Class<?>[] getServletConfigClasses(){
        return null;
    }

    @Override
    protected String[] getServletMappings(){
        return new String[]{
                "/"
        };
    }

}
