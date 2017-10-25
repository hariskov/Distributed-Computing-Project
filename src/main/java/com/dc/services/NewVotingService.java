package com.dc.services;

import com.dc.components.CustomRestTemplate;
import com.dc.pojo.Device;
import com.dc.pojo.DeviceManager;
import com.dc.pojo.Vote;
import com.dc.pojo.VotingManager;
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

    public Vote process(Vote vote) {
        votingManager.setTempVote(vote);
        return votingManager.getTempVote();
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
                ResponseEntity result = restTemplate.postForEntity(uri, vote, Object.class);
                result.getBody();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
