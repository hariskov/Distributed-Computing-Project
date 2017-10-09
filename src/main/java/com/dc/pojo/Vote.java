package com.dc.pojo;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by xumepa on 10/3/17.
 */


public class Vote {
//    private final DeviceManager deviceManager;
    HashMap<Device, Object> vote = new HashMap<>();
    private String voteStr;

    public Vote(String voteStr) {
        this.voteStr = voteStr;
    }

    public HashMap<Device,Object> getVoteResults(){
        return vote;
    }

    public String getVote() {
        return voteStr;
    }

    public void setVote(Device device, Object body) {
        vote.put(device, body);
    }

    public int getVoteParticipants(){
        return vote.size();
    }

    public Object calculateVote() {
//        Vote receivedVote = manager.stream().filter(e->e.getVote().equals(vote)).findFirst().get();
        Map<Object,Long> a = getVoteResults().entrySet().parallelStream().collect(Collectors.groupingBy(w->w.getValue(), Collectors.counting()));
        return a.entrySet().stream().max(Map.Entry.comparingByValue()).get(); // assumes n/2 + 1
    }
}