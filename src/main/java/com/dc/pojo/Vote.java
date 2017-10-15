package com.dc.pojo;

import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by xumepa on 10/3/17.
 */

@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
public class Vote {
//    private final DeviceManager deviceManager;
    HashMap<Device, Object> vote = new HashMap<>();
    private String voteStr;

    public Vote(){
    }

    public Vote(String voteStr) {
        this.voteStr = voteStr;
    }

    public HashMap<Device,Object> getVote(){
        return vote;
    }
    public void setVote(HashMap<Device, Object> vote){this.vote=vote;}

    public String getVoteStr() {
        return voteStr;
    }
    public void setVoteStr(String str){this.voteStr=str;}

    public void addVote(Device device, Object body) {
        vote.put(device, body);
    }

    public List<Device> receiveVoteParticipants(){
        return Arrays.asList((Device[]) vote.keySet().toArray());
    }

    public Object calculateVote() {
//        Vote receivedVote = manager.stream().filter(e->e.getVoteStr().equals(vote)).findFirst().get();
        Map<Object,Long> a = getVote().entrySet().parallelStream().collect(Collectors.groupingBy(w->w.getValue(), Collectors.counting()));
        return a.entrySet().stream().max(Map.Entry.comparingByValue()).get(); // assumes n/2 + 1
    }
}