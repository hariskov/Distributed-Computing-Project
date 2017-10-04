package com.dc.pojo;

import com.dc.exceptions.ExistingVoteException;
import com.dc.exceptions.NoDevicesException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * Created by xumepa on 10/3/17.
 */

public class VotingManager {

    @Autowired
    private Devices devices;

//    private Map<String,Vote> manager= new HashMap<String,Vote>();

    @Autowired
    RestTemplate restTemplate;

    private List<Vote> manager;

    public VotingManager(){
        manager = new LinkedList<Vote>();
    }

    public void createVote(String voteStr) {
        if (manager.contains(voteStr)) {
            throw new ExistingVoteException();
        }else{
            Vote vote = new Vote(devices);
            vote.createVote(voteStr);
            manager.add(vote);
        }
    }

    public List<Vote> getVotes(){
        return manager;
    }

    public boolean hasVotes(){
        return !manager.isEmpty();
    }

    public void putVote(String vote, UUID deviceUUID, Boolean body) {
        List<Vote> a = manager.parallelStream().filter(e->e.getVote().equals(vote)).collect(Collectors.toList());
        if(a.size()>0){
            Vote returnedVote = a.get(0);
            returnedVote.setVote(deviceUUID,body);
        }
    }

    public void getVote(String v) {
        String uri = "http://" + v + ":8080/voting/getVote";
        ResponseEntity<Boolean> response = restTemplate.postForEntity(uri, null, Boolean.class);
        putVote("Vote",devices.getDeviceUUID(v),response.getBody());
        System.err.println("Vote was : " + response.getBody());
    }

    public Object calculateVote(String vote) {
        Vote receivedVote = manager.stream().filter(e->e.getVote().equals(vote)).findFirst().get();
        Map<Object,Long> a = receivedVote.getVoteResults().entrySet().parallelStream().collect(Collectors.groupingBy(w->w.getValue(), Collectors.counting()));
        return a.entrySet().stream().max(Map.Entry.comparingByValue()).get(); // assumes n/2 + 1
    }
}

    //HashMap<UUID,Object>
