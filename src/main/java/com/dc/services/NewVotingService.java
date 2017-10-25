package com.dc.services;

import com.dc.components.CustomRestTemplate;
import com.dc.pojo.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class NewVotingService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    VotingManager votingManager;

    @Autowired
    DeviceManager deviceManager;

    @Autowired
    CustomRestTemplate restTemplate;

    public SingleVote setTempVote(Vote vote) {
        votingManager.setTempVote(vote);
        SingleVote v = new SingleVote();
        v.setDevice(deviceManager.getCurrentDevice());
        v.setAnswer(null);
        votingManager.getTempVote().addVote(v);
        return votingManager.getTempVote().getVoteOfDevice(deviceManager.getCurrentDevice());
    }

    public Vote createVote(String newVote) {
        Vote vote = new Vote();
        vote.setVoteStr(newVote);
        vote.setCreator(deviceManager.getCurrentDevice());
        return vote;
    }

//    @Async
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

//    @Async
    public Vote sendApplyVote(Device device, Vote vote) {
        Vote result = null;

        try {
            logger.info("sending to " + device.getIp() + " value : " + vote.getVoteStr());
            String uri = "http://" + device.getIp() + ":8080/project/voting/applyTempVote";
            result = restTemplate.postForEntity(uri, vote, Vote.class).getBody();
//                votingManager.getTempVote().addVote(result.getBody());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    public void sendStartVote(String leaderSelect) {
        try {
            String uri = "http://" + deviceManager.getCurrentDevice().getIp() + ":8080/project/voting/startVote";
            restTemplate.postForEntity(uri, leaderSelect, Object.class);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @Async
    public SingleVote sendValueRequest(Device device, String voteStr){
        SingleVote result = null;
        try {
            String uri = "http://" + device.getIp() + ":8080/project/voting/receiveVoteAnswer";
            result = restTemplate.postForEntity(uri, voteStr, SingleVote.class).getBody();
        }catch(Exception e){
            e.printStackTrace();
        }
        return result;
    }

    public Vote applyTempVote(Vote vote) {
        for(SingleVote singleVote : vote.getVotes()){
            if(!votingManager.getTempVote().containsDevice(singleVote.getDevice())){
                votingManager.getTempVote().addVote(singleVote);
            }
        }
        return votingManager.getTempVote();
    }

    public ResponseEntity<SingleVote> getVoteAnswer(String voteStr) {
        SingleVote currentAnswer = votingManager.getTempVote().getVoteOfDevice(deviceManager.getCurrentDevice());
        if(currentAnswer.getAnswer()!=null){
            if(currentAnswer.getAnswer() instanceof Device){
                currentAnswer.setAnswer(generateLeader());
            }
        }
        return ResponseEntity.ok(currentAnswer);
    }

    public Device generateLeader(){
        Device leader = deviceManager.getDevices().get(new Random().nextInt(deviceManager.getDevices().size()));
        return leader;
    }
}
