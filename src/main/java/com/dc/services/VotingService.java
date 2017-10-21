package com.dc.services;

import com.dc.pojo.Device;
import com.dc.pojo.DeviceManager;
import com.dc.pojo.Vote;
import com.dc.pojo.VotingManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.inject.Inject;
import java.util.Random;

/**
 * Created by xumepa on 10/19/17.
 */

@Service(value="votingService")
public class VotingService {

    @Autowired
    RestTemplate restTemplate;

    public void sendNewVoteToDevices(Device device, Vote vote) {
        try {
            String uri = "http://" + device.getIp() + ":8080/project/voting/receiveNewTempVote";
//        ResponseEntity<Object> response =
            restTemplate.postForEntity(uri, vote, Object.class);
//        return response.getBody();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void sendVoteResult(Device device, Object lastVote) {
        String uri = "http://" + device.getIp() + ":8080/project/voting/receiveVote";
        restTemplate.postForEntity(uri, lastVote, Object.class);
    }
}
