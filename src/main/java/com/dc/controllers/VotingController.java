package com.dc.controllers;

import com.dc.pojo.Devices;
import com.sun.org.apache.xpath.internal.operations.Bool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.client.RestTemplate;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by xumepa on 9/24/17.
 */


@Controller
@RequestMapping(value = "/voting")
public class VotingController {

    @Autowired
    Devices devices;

    @Autowired
    RestTemplate restTemplate;

    @RequestMapping(value="/startVote",method = RequestMethod.POST)
    public void startVote(){
        devices.getDevices().forEach((k,v)->getVote(v));
    }

    private void getVote(String v) {
        String uri = "http://" + v + ":8080/voting/getVote";
        ResponseEntity<Boolean> response = restTemplate.postForEntity(uri, null, Boolean.class);
        System.err.println("Vote was : " + response.getBody());
    }

    @RequestMapping(value="/getVote",method = RequestMethod.POST)
    public ResponseEntity<Boolean> voting(){

        int randomNum = ThreadLocalRandom.current().nextInt(0, 10);

        if(randomNum>5){
            return ResponseEntity.ok(true);
        }else{
            return ResponseEntity.ok(false);
        }
    }
}
