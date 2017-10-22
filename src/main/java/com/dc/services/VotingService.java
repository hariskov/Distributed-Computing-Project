package com.dc.services;

import com.dc.components.CustomRestTemplate;
import com.dc.pojo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by xumepa on 10/19/17.
 */

@Service(value="votingService")
public class VotingService {

    @Autowired
    CustomRestTemplate restTemplate;

    @Autowired
    VotingManager votingManager;

    public boolean sendNewVoteToDevices(Device device, Vote vote) {
        try {
            String uri = "http://" + device.getIp() + ":8080/project/voting/receiveNewTempVote";
//        ResponseEntity<Object> response =
            restTemplate.postForEntity(uri, vote, Object.class);
        return true;
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public void sendVoteResult(Device device, Object lastVote) {
        String uri = "http://" + device.getIp() + ":8080/project/voting/receiveVote";
        restTemplate.postForEntity(uri, lastVote, Object.class);
    }

    public void startNewVote(String voteType) {
        Vote vote = votingManager.createVote(voteType);
        if(vote!=null) {
            votingManager.sendVotes(vote);
        }
    }

    public void processVote(Vote vote) {


        if(votingManager.getTempVote() == null){
            votingManager.setTempVote(vote);
            votingManager.setCurrentSingleVote(vote.getVoteStr());
        }else{
            for(SingleVote da : vote.getVotes()) {
                if (!votingManager.getTempVote().containsDevice(da.getDevice())) {
                    votingManager.getTempVote().addVote(da);
                }
            }
        }
    }

}
