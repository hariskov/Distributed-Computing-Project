package com.dc.controllers;

import com.dc.pojo.Device;
import com.dc.pojo.DeviceManager;
import com.dc.pojo.GameManager;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.PUT;
import java.util.List;

/**
 * Created by xumepa on 10/25/17.
 */

@Controller
@RequestMapping("/game")
public class GameController {

    @JsonIgnore
    private final Logger logger = LoggerFactory.getLogger(GameController.class);

    @Autowired
    DeviceManager deviceManager;

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

    @GetMapping(value="/checkGameExists")
    public ResponseEntity<Boolean> checkGameExists(){
        boolean gameExists = gameManager.doesGameExist();
        if (!gameExists) {
            deviceManager.callPlayersToNotDiscover();
        }
        return ResponseEntity.ok(gameExists);
    }

    @PostMapping(value="/getCurrentPlayer")
    public ResponseEntity<Device> getCurrentPlayer(){
        Device currentPlayer = gameManager.getCurrentPlayer();
        if(currentPlayer==null){
            return ResponseEntity.ok(gameManager.getPlayingOrder().get(0));
        }else{
            return ResponseEntity.ok(currentPlayer);
        }
    }

    @PutMapping(value = "/addPlayer")
    public ResponseEntity addPlayer(@RequestBody Device device){
        gameManager.addPlayer(device);
        return ResponseEntity.ok(null);
    }

    @PostMapping(value ="/getPlayingOrder")
    public ResponseEntity getPlayingOrder(){
        return ResponseEntity.ok(gameManager.getPlayingOrder());
    }

}
