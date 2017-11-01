package com.dc.mvc.controllers;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


/**
 * Created by xumepa on 9/12/17.
 */
@Controller
@RequestMapping(value = "/", method = RequestMethod.GET)
public class MainController {

    private static final String MAIN = "main";
    @JsonIgnore
    private final static Logger log = LoggerFactory.getLogger(MainController.class);

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public void getMainScreen(HttpServletResponse httpServletResponse) throws IOException {
        httpServletResponse.sendRedirect(MAIN);
//        return MAIN;
    }

    @RequestMapping(value = "/main", method = RequestMethod.GET)
    public String getMainScreen1() {
        log.info(Thread.currentThread().getStackTrace()[1].getClassName() + " / " + Thread.currentThread().getStackTrace()[1].getMethodName());
        return MAIN;
    }

}