package com.dc.pojo;

import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by xumepa on 10/3/17.
 */

@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
public class Vote {
    HashMap<Device, Object> vote = new HashMap<>();
    private String voteStr;
    private Device creator;

    public Vote(){
    }

    public Vote(String voteStr) {
        this();
        this.voteStr = voteStr;
    }

    public boolean containsDevice(Device device){
        for (Map.Entry<Device, Object> entry : vote.entrySet()) {
            if(entry.getKey().equals(device)){
                return true;
            }
        }
        return false;
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

    public Device getCreator() {
        return creator;
    }

    public void setCreator(Device creator) {
        this.creator = creator;
    }
}