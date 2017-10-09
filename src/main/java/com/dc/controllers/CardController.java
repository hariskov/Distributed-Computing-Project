package com.dc.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by xumepa on 10/9/17.
 */

@Controller
@RequestMapping(value="card")
public class CardController {

    @RequestMapping(value="/playCard",method = RequestMethod.POST)
    public void playCard(@RequestBody String card){
        System.out.println(card);
    }

}
