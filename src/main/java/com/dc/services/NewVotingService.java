package com.dc.services;

import com.dc.components.CustomRestTemplate;
import com.dc.pojo.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class NewVotingService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    VotingManager votingManager;

    @Autowired
    DeviceManager deviceManager;

    @Autowired
    CustomRestTemplate restTemplate;

    public SingleVote process(Vote vote) {
        votingManager.setTempVote(vote);
        return votingManager.getTempVote().getVoteOfDevice(deviceManager.getCurrentDevice());
    }

    public Vote creatVote(String newVote) {
        Vote vote = new Vote();
        vote.setVoteStr(newVote);
        vote.setCreator(deviceManager.getCurrentDevice());
        return vote;
    }

    public void sendVote(Vote vote) {
        for (Device device: deviceManager.getDevices()) {
            try {
                logger.info("sending to " + device.getIp() + " value : " + vote.getVoteStr());
                String uri = "http://" + device.getIp() + ":8080/project/voting/receiveNewVote";
                ResponseEntity<SingleVote> result = restTemplate.postForEntity(uri, vote, SingleVote.class);
                votingManager.getTempVote().addVote(result.getBody());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
