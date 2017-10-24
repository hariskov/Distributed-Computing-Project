package com.dc.services;

import com.dc.components.CustomRestTemplate;
import com.dc.pojo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * Created by xumepa on 10/19/17.
 */

@Service(value="votingService")
public class VotingService {

    @Autowired
    CustomRestTemplate restTemplate;

    @Autowired
    VotingManager votingManager;

    @Autowired
    DeviceManager deviceManager;

    @Async
    public void sendNewVoteToDevices(Device device, Vote vote) {
        try {
            String uri = "http://" + device.getIp() + ":8080/project/voting/receiveStage1Vote";
            restTemplate.put(uri, vote, Object.class);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @Async
    public void sendVoteResult(Device device, Object voteResult) {
        String uri = "http://" + device.getIp() + ":8080/project/voting/receiveStage2Vote";
        restTemplate.put(uri, voteResult, Object.class);
    }

    public boolean containsVote(SingleVote vote){
        return votingManager.getTempVote().getVoteOfDevice(vote.getDevice()) == null;
    }

    public void startNewVote(String voteType) {
        Vote vote = votingManager.createVote(voteType);
        if(vote!=null) {
//            votingManager.setTempVote(vote);
//            votingManager.setCurrentSingleVote();
            votingManager.sendVotes(vote);

        }
    }

    public void processTempVote(Vote vote) {
        if(votingManager.containsVote(vote.getVoteStr())!=null){
            return;
        }
        if (votingManager.getTempVote() == null) {
            votingManager.setTempVote(vote);
            votingManager.setCurrentSingleVote();
        }else{
            for (SingleVote da : vote.getVotes()) {
                if (!votingManager.getTempVote().containsDevice(da.getDevice())) {
                    votingManager.getTempVote().addVote(da);
                }
            }
        }
    }

    public void processVote(Vote vote) {
        Vote localVote = votingManager.containsVote(vote.getVoteStr());
        // check for actual vote in progress
        if(localVote!=null){
            // second stage processing start
            // either create new Vote with only one SingleVote for the current machine to be passed , or fill -> check

            SingleVote currentDeviceSingleVote = votingManager.getTempVote().getVoteOfDevice(deviceManager.getCurrentDevice());
            if(currentDeviceSingleVote.getAnswer()==""){
                currentDeviceSingleVote.setAnswer(generateLeader());
            }

            for (SingleVote singleVote : vote.getVotes()) {
                SingleVote localSingleVote = votingManager.getTempVote().getVotes().stream().filter(e->e.getAnswer()=="").findFirst().orElse(null);
                if (localSingleVote != null && localSingleVote.getDevice().equals(singleVote.getDevice())) {
                    localSingleVote.setAnswer(singleVote.getAnswer());
                }
            }

        }


//             else {
//                for (SingleVote da : vote.getVotes()) {
//                    if (!votingManager.getTempVote().containsDevice(da.getDevice())) {
//                        votingManager.getTempVote().addVote(da);
//                    } else {
////                    List<SingleVote> emptyVotes = votingManager.getTempVote().getVotes().stream().filter(e->e.getAnswer()=="").collect(Collectors.toList());
////                    SingleVote singleVote = votingManager.getTempVote().getVotes().stream().filter(e->e.equals(da)).findFirst().orElse(null);
////                    if(singleVote!=null) {
////                        singleVote.setAnswer(da.getAnswer());
////                    }
//                    }
//                }
//            }
//        }
    }

    public Device generateLeader(){
        Device leader = deviceManager.getDevices().get(new Random().nextInt(deviceManager.getDevices().size()));
        return leader;
    }

}
