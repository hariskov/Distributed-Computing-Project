package com.dc.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by xumepa on 9/17/17.
 */

@Controller
public class EchoController {

    @RequestMapping(value="/echo/{ip}", method = RequestMethod.GET)
    public boolean exists(@PathVariable String ip){
        System.out.println(ip);
        return false;
    }
}
