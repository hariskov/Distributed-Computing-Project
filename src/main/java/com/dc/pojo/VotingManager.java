package com.dc.pojo;

import com.dc.exceptions.ExistingVoteException;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by xumepa on 10/3/17.
 */

public class VotingManager {

    @Autowired
    private Devices devices;

//    private Map<String,Vote> manager= new HashMap<String,Vote>();

    private List<Vote> manager = new ArrayList<Vote>();

    public void createVote(String voteStr) {
        if(manager.contains(voteStr)){
            throw new ExistingVoteException();
        }
        Vote vote = new Vote(devices);
        vote.createVote(voteStr);
        manager.add(vote);
    }

    public List<Vote> getVotes(){
        return manager;
    }

    public void putVote(String vote, UUID deviceUUID, Boolean body) {
        List<Vote> a = manager.parallelStream().filter(e->e.getVote().equals(vote)).collect(Collectors.toList());
        if(a.size()>0){
            Vote returnedVote = a.get(0);
            returnedVote.setVote(deviceUUID,body);
        }
    }
}

class Vote{
    private final Devices devices;
    HashMap<UUID,Object> vote = new HashMap<>();
    private String voteStr;

    public Vote(Devices devices) {
        this.devices = devices;
    }

    public void createVote(String voteStr) {
        this.voteStr = voteStr;
        devices.getDevices().forEach((k,v)->vote.put(k,null));
    }

    public String getVote(){
        return voteStr;
    }

    public void setVote(UUID deviceUUID, Boolean body) {
        vote.put(deviceUUID,body);
    }

    //HashMap<UUID,Object>


}