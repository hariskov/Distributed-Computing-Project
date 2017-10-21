package com.dc.pojo;

import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by xumepa on 10/3/17.
 */

@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
public class Vote implements Serializable{
    HashMap<Device, Object> vote = new HashMap<>();
    private String voteStr;
    private int id;
    private Device creator;

    public Vote(){
        this.id = new Random().nextInt(10000);
    }

    public Vote(String voteStr) {
        this();
        this.voteStr = voteStr;
    }

    public boolean contains(Device dev){
        boolean contains = false;
        for (Map.Entry<Device, Object> entrySet : vote.entrySet()) {
            if(entrySet.getKey().getUuid() == dev.getUuid()){
                contains = true;
                break;
            }
        }
        return contains;
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

    public int getId(){
        return this.id;
    }

    @Override
    public boolean equals(Object var1) {
        if(var1 instanceof Vote){
            if(this.id == ((Vote) var1).getId()){
                return true;
            }
            return false;
        }else{
            return false;
        }
    }
    @Override public int hashCode() { return id; }

    public Device getCreator() {
        return creator;
    }

    public void setCreator(Device creator) {
        this.creator = creator;
    }
}