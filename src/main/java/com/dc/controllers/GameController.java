package com.dc.controllers;

import com.dc.pojo.Device;
import com.dc.pojo.GameManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.core.MessageSendingOperations;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

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
    public ResponseEntity<Object> startRound(){

        return ResponseEntity.ok(null);
    }

    @PutMapping("/applyPlayOrder")
    public ResponseEntity applyPlayOrder(@RequestBody List<Device> order){
        logger.info("APPLY ORDER WOORHHOOOOOO" );
        gameManager.setPlayingOrder(order);

        gameManager.getPlayingOrder().forEach(e->logger.info(e.getIp()));

        return ResponseEntity.ok(null);
    }

}
