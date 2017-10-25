package com.dc.services;

import com.dc.components.CustomRestTemplate;
import com.dc.pojo.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * Created by xumepa on 10/19/17.
 */

@Service(value="votingService")
public class VotingService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

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
            restTemplate.put(uri, vote);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @Async
    public void sendVoteResult(Device device, SingleVote voteResult) {
        try {
            String uri = "http://" + device.getIp() + ":8080/project/voting/receiveStage2Vote";
            restTemplate.put(uri, voteResult);
        }catch(Exception e){
            e.printStackTrace();
        }
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
            votingManager.setCurrentSingleVote(vote.getVoteStr());
        }else{
            for (SingleVote da : vote.getVotes()) {
                if (!votingManager.getTempVote().containsDevice(da.getDevice())) {
                    votingManager.getTempVote().addVote(da);
                }
            }
        }
    }

    public void processVote(SingleVote vote){


//        SingleVote currentDeviceSingleVote = votingManager.getTempVote().getVoteOfDevice(deviceManager.getCurrentDevice());
//        if(currentDeviceSingleVote.getAnswer()==""){
//            currentDeviceSingleVote.setAnswer(generateLeader());
//        }

        SingleVote singleVote = votingManager.getTempVote().getVoteOfDevice(vote.getDevice());
//        if(singleVote.getAnswer() == ""){

        if(vote.getSequence()>singleVote.getSequence()){
            singleVote.setAnswer(vote.getAnswer());
            singleVote.setSequence(singleVote.getSequence()+1);
        }


    }




//    public void processVote(Vote vote) {
//        Vote localVote = votingManager.containsVote(vote.getVoteStr());
//        // check for actual vote in progress
//        if(localVote!=null){
//            // second stage processing start
//            // either create new Vote with only one SingleVote for the current machine to be passed , or fill -> check
//
//            SingleVote currentDeviceSingleVote = votingManager.getTempVote().getVoteOfDevice(deviceManager.getCurrentDevice());
//            if(currentDeviceSingleVote.getAnswer()==""){
//                currentDeviceSingleVote.setAnswer(generateLeader());
//            }
//
//            for (SingleVote singleVote : vote.getVotes()) {
//                SingleVote localSingleVote = votingManager.getTempVote().getVotes().stream().filter(e->e.getAnswer()=="").findFirst().orElse(null);
//                if (localSingleVote != null && localSingleVote.getDevice().equals(singleVote.getDevice())) {
//                    localSingleVote.setAnswer(singleVote.getAnswer());
//                }
//            }
//
//        }


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
//    }

    public Device generateLeader(){
        Device leader = deviceManager.getDevices().get(new Random().nextInt(deviceManager.getDevices().size()));
        return leader;
    }

    public Object calculateVote(String voteString) {
        List<SingleVote> votes = votingManager.getTempVote().getVotes();

//        Vote receivedVote = manager.stream().filter(e->e.getVoteStr().equals(voteMap)).findFirst().get();
        Map<Object,Long> a = votes.parallelStream().collect(Collectors.groupingBy(w->w.getAnswer(), Collectors.counting()));

        Map.Entry<Object, Long> maxValue = a.entrySet().stream().max(Map.Entry.comparingByValue()).orElse(null); // assumes n/2 + 1

        List<SingleVote> result = votes.stream()
                .filter(s -> s.getAnswer()==maxValue.getKey())
                .collect(Collectors.toList());

        if(result.size()==1){
            logger.info("calculateVote returns with : " + result.get(0).getAnswer());

            return result.get(0).getAnswer();
        }
        else{
            SingleVote returnSingleVote = null;
            int maxIp = 0;
//            result.stream().max(e->e.getDevice().getIp());
            for (SingleVote singleVote : result) {
                try {
                    String ip = singleVote.getDevice().getIp();

                    String numbers = ip.substring(ip.lastIndexOf(".")+1);

                    int lastDigit = Integer.parseInt(numbers);
                    if (lastDigit > maxIp) {
                        returnSingleVote = singleVote;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            logger.info("calculateVote returns with : " + returnSingleVote.getAnswer());

            return returnSingleVote.getAnswer();
        }
    }

    public void calculateTempVote(String voteString) {
        calculateVote(voteString);
    }

}
