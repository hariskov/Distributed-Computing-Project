package com.dc.services;

import com.dc.components.CustomRestTemplate;
import com.dc.pojo.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class NewVotingService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    NewVotingManager votingManager;

    @Autowired
    DeviceManager deviceManager;

    @Autowired
    CustomRestTemplate restTemplate;

    public SingleVote setTempVote(Vote vote) {
        votingManager.addTempVote(vote);
        SingleVote v = new SingleVote();
        v.setDevice(deviceManager.getCurrentDevice());
        v.setAnswer(null);
        votingManager.getTempVote(vote.getVoteStr()).addVote(v);
        return votingManager.getTempVote(vote.getVoteStr()).getVoteOfDevice(deviceManager.getCurrentDevice());
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
                votingManager.getTempVote(vote.getVoteStr()).addVote(result.getBody());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

//    @Async
    public Vote sendApplyTempVote(Device device, Vote vote) {
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

    public SingleVote sendValueRequest(Device device, String voteStr){
        SingleVote result = null;
        try {
            String uri = "http://" + device.getIp() + ":8080/project/voting/receiveVoteAnswer";
            System.out.println(voteStr);
            result = restTemplate.postForEntity(uri, voteStr, SingleVote.class).getBody();
        }catch(Exception e){
            e.printStackTrace();
        }
        return result;
    }


    public void sendApplyVote(Device device, String voteStr, SingleVote calculatedVote) {
        try {
            VoteApply voteApply = new VoteApply();
            voteApply.setVoteStr(voteStr);
            voteApply.setCalcVote(calculatedVote);
            String uri = "http://" + device.getIp() + ":8080/project/voting/applyVote";
            restTemplate.put(uri, voteApply);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void sendApplyPlayOrder(Device device, List<Device> order){
        try {
            String uri = "http://" + device.getIp() + ":8080/project/game/applyPlayOrder";
            restTemplate.put(uri, order);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private String sendAskLeader(Device device, String voteStr) {
        String result = null;
        try {
            String uri = "http://" + device.getIp() + ":8080/project/card/askForAnswer";
            result =  restTemplate.postForEntity(uri, voteStr, String.class).getBody();
        }catch(Exception e){
            e.printStackTrace();
        }
        return result;
    }

//    @Async
    public boolean sendCheckIfGameExists(Device device) {
        boolean result = false;
        try {
            String uri = "http://" + device.getIp() + ":8080/project/game/checkGameExists";
            result =  restTemplate.getForEntity(uri, Boolean.class).getBody();
        }catch(Exception e){
            e.printStackTrace();
        }
        return result;
    }


    public boolean sendJoinRequest(Device dev) {
        boolean result = false;
        try {
            String uri = "http://" + dev.getIp() + ":8080/project/echo/joinRequest";
            result =  restTemplate.postForEntity(uri, deviceManager.getCurrentDevice(), Boolean.class).getBody();
        }catch(Exception e){
            e.printStackTrace();
        }
        return result;
    }

    public Vote applyTempVote(Vote vote) {
        for(SingleVote singleVote : vote.getVotes()){
            if(!votingManager.getTempVote(vote.getVoteStr()).containsDevice(singleVote.getDevice())){
                votingManager.getTempVote(vote.getVoteStr()).addVote(singleVote);
            }
        }
        return votingManager.getTempVote(vote.getVoteStr());
    }

    public ResponseEntity<SingleVote> getVoteAnswer(String voteStr) {
        SingleVote currentAnswer = votingManager.getTempVote(voteStr).getVoteOfDevice(deviceManager.getCurrentDevice());
        if(currentAnswer.getAnswer()==null){
            if(voteStr.equals("LeaderSelect")){
                currentAnswer.setAnswer(generateLeader());
            }else{
                String answer = sendAskLeader(votingManager.getTempVote(voteStr).getCreator(),voteStr);
                currentAnswer.setAnswer(answer);
            }
        }
        return ResponseEntity.ok(currentAnswer);
    }

    public Device generateLeader(){
        Device leader = deviceManager.getDevices().get(new Random().nextInt(deviceManager.getDevices().size()));
        return leader;
    }

    public List<Device> calculateOrder(Vote vote){
//        Map<Object,Long> a = vote.getVotes().parallelStream().collect(Collectors.groupingBy(w->w.getAnswer(), Collectors.counting()));
//        Map.Entry<Object, Long> maxValue = a.entrySet().stream().max(Map.Entry.comparingByValue()).orElse(null); // assumes n/2 + 1
//        List<SingleVote> result = vote.getVotes().stream()
//                .filter(s -> s.getAnswer()==maxValue.getKey())
//                .collect(Collectors.toList());
//
//        Map<Device,Integer> resultedOrder = new LinkedHashMap<>();
//        int sequence = 0;
//        while(result.size()>=0){
//            int dev = new Random().nextInt(deviceManager.getDevices().size()+1);
//            Device leader = result.get(dev).getDevice();
//            resultedOrder.put(leader,sequence++);
//            result.remove(leader);
//        }

        List<Device> resultedOrder = new LinkedList<>();

        for (Device device : deviceManager.getDevices()) {
            resultedOrder.add(device);
        }

        return resultedOrder;
    }

    public Object calculateVote(Vote vote) {
        List<SingleVote> votes = vote.getVotes();

        Map<Object,Long> a = votes.parallelStream().collect(Collectors.groupingBy(SingleVote::getAnswer, Collectors.counting()));
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
            for (SingleVote singleVote : result) {
                try {
                    System.out.println("a");

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
            System.out.println("a");

            return returnSingleVote.getAnswer();
        }
    }

    public void applyVote(VoteApply vote) {
        votingManager.getDecidedVote().add(vote.getCalcVote());
        votingManager.removeTempVote(vote.getVoteStr());
    }

}
