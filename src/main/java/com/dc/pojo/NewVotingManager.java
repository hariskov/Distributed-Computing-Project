package com.dc.pojo;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by xumepa on 10/26/17.
 */

@Service
public class NewVotingManager {

    private List<Vote> manager = new LinkedList<Vote>();
    private List<Vote> tempVote = new LinkedList<Vote>();

    public NewVotingManager(){

    }

    public void removeTempVote(String voteStr) {
        tempVote.remove(getTempVote(voteStr));
    }

    public Vote getTempVote(String voteStr){
        return tempVote.stream().filter(e->e.getVoteStr().equals(voteStr)).findFirst().orElse(null);
    }

    public void setVotes(List<Vote> votes) {
        this.manager = votes;
    }

    public void addTempVote(Vote currentCirculatingVote) {
        this.tempVote.add(currentCirculatingVote);
    }

    public Vote getLastTempVote(){
        return tempVote.get(tempVote.size()-1);
    }

    public SingleVote getDecidedVote(String voteStr) {
        Vote returnedVote = getVote(voteStr);
        if (returnedVote != null) {
            return returnedVote.getVotes().get(0);
        } else{
            return null;
        }
    }

    public void addDecidedVote(String voteStr, SingleVote voteToAdd) {
        Vote newVote = new Vote();
        newVote.addVote(voteToAdd);
        newVote.setVoteStr(voteStr);
        newVote.setCreator(voteToAdd.getDevice());
        manager.add(newVote);
    }

    private void removeVoteForDevice(Device device, String voteString){
        Vote vote = getTempVote(voteString);
        SingleVote voteToRemove = vote.getVotes().stream().filter(e->e.getDevice().equals(device)).findFirst().get();
        vote.getVotes().remove(voteToRemove);
    }

    public void removeVoteForDevices(List<Device> devices, String voteStr) {
        for (Device device : devices) {
            removeVoteForDevice(device, voteStr);
        }
    }

    public void revertVote(String voteString) {
        Vote returnedVote = getVote(voteString);
        Vote voteToReturn = returnedVote.getPassedVote();
        if(returnedVote !=null){
            manager.remove(returnedVote);
            tempVote.add(voteToReturn);
        }else{
            Vote tempVote = getTempVote(voteString);
            tempVote.getVotes().removeAll(tempVote.getVotes());
            for (SingleVote singleVote : tempVote.getPassedVote().getVotes()) {
                tempVote.addVote(singleVote);
            }
        }
    }

    private Vote getVote(String voteString) {
        return manager.stream().filter(e -> e.getVoteStr().equals(voteString)).findFirst().orElse(null);
    }
}
