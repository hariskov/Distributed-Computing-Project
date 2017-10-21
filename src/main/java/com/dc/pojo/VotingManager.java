package com.dc.pojo;

import com.dc.exceptions.ExistingVoteException;
import com.dc.services.VotingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by xumepa on 10/3/17.
 */

@Component
public class VotingManager {

    @Autowired
    private DeviceManager deviceManager;

    @Autowired
    VotingService votingService;

    private List<Vote> manager;
    private Vote tempVote;

    public VotingManager(){
        manager = new LinkedList<Vote>();
    }

    public Vote createVote(String voteStr) {

        if (manager.contains(voteStr)) {
            throw new ExistingVoteException();
        }else{
            Vote newVote = new Vote(voteStr);
//            tempVote.addVote(deviceManager.getCurrentDevice(),"");
//            deviceManager.getDevices().forEach(d -> tempVote.addVote(d, ""));
            return newVote;
        }
    }

    public void sendVotes(Vote vote) {
        for (Device device : deviceManager.getDevices()) {
            votingService.sendNewVoteToDevices(device, vote);
//                manager.putVote(voteType, device, result);
        }
    }

    public Vote getVoteResults(Vote vote) {
        return getVotes().stream().filter(e->e.getVoteStr().equals(vote.getVoteStr())).findFirst().orElse(null);
    }

    public void generateVoteResult(Vote vote) {
        Object result;
        if(getVoteResults(vote) == null){
            result = null;
        }else {
            Random randomizer = new Random();
            Device random = deviceManager.getDevices().get(randomizer.nextInt(deviceManager.getDevices().size()));
            result = random;
        }
    }

    public void applyVote(Vote vote){
        manager.add(vote);
    }

    public List<Vote> getVotes(){
        return manager;
    }

    public Vote getLastVote(){
        return manager.get(manager.size()-1);
    }

    public Vote getTempVote(){
        return tempVote;
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

    public void setVotes(List<Vote> votes) {
        this.manager = votes;
    }

    public void setCurrentCirculatingVote(Vote currentCirculatingVote) {
        this.tempVote = currentCirculatingVote;
    }

    public void addValueToCurrentTempVote(Vote vote) {
        manager.add(vote);
    }
}
