package com.dc.mvc.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


/**
 * Created by xumepa on 9/12/17.
 */
@Controller
public class MainController {

    private static final String MAIN = "main";
    private final static Logger log = LoggerFactory.getLogger(MainController.class);

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String getMainScreen() {
        System.out.println("a");
        log.info(Thread.currentThread().getStackTrace()[1].getClassName() + " / " + Thread.currentThread().getStackTrace()[1].getMethodName());
        return MAIN;
    }
    @RequestMapping(value = "/main", method = RequestMethod.GET)
    public String getMainScreen1() {
        System.out.println("b");
        log.info(Thread.currentThread().getStackTrace()[1].getClassName() + " / " + Thread.currentThread().getStackTrace()[1].getMethodName());
        return MAIN;
    }
}