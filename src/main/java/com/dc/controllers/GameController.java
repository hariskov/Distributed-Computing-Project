package com.dc.controllers;

import com.dc.pojo.Device;
import com.dc.pojo.GameManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

/**
 * Created by xumepa on 10/25/17.
 */

@Controller
@RequestMapping("/game")
public class GameController {

    private final Logger logger = LoggerFactory.getLogger(GameController.class);


    @Autowired
    GameManager gameManager;

    @PutMapping("/startNewRound")
    public void startRound(){

    }

    @PutMapping("/applyPlayOrder")
    public void applyPlayOrder(@RequestBody Map<Device,Integer> order){
        logger.info("APPLY ORDER WOORHHOOOOOO" );
        gameManager.setPlayingOrder(order);
    }

}
