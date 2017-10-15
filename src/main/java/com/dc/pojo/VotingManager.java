package com.dc.pojo;

import com.dc.exceptions.ExistingVoteException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by xumepa on 10/3/17.
 */

public class VotingManager {

    @Autowired
    private DeviceManager deviceManager;

    @Autowired
    RestTemplate restTemplate;

    private List<Vote> manager;

    public VotingManager(){
        manager = new LinkedList<Vote>();
    }

    public Vote createVote(String voteStr) {

        if (manager.contains(voteStr)) {
            throw new ExistingVoteException();
        }else{
            Vote vote = new Vote(voteStr);
            manager.add(vote);
            deviceManager.getDevices().forEach(d -> vote.addVote(d, ""));
            return vote;
        }

    }

    public List<Vote> getVotes(){
        return manager;
    }

    public Vote getLastVote(){
        return manager.get(manager.size()-1);
    }

    public boolean hasVotes(){
        return !manager.isEmpty();
    }

    public void putVote(String vote, Device device, Object body) {
        List<Vote> a = manager.parallelStream().filter(e->e.getVoteStr().equals(vote)).collect(Collectors.toList());
        if(a.size()>0){
            Vote returnedVote = a.get(0);
            returnedVote.addVote(device,body);
        }
    }

    public void getNetworkVotes(Device device, Vote vote) {
        String uri = "http://" + device.getIp() + ":8080/voting/getVote";
        ResponseEntity<Object> response =
                restTemplate.postForEntity(uri, vote, Object.class);
        putVote("Vote", device, response.getBody());
        System.err.println("Vote for " + device.getIp() + " was : " + response.getBody());
    }

    public void setVotes(List<Vote> votes) {
        this.manager = votes;
    }
}

    //HashMap<UUID,Object>
