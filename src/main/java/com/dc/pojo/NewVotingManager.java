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
        Vote returnedVote = manager.stream().filter(e -> e.getVoteStr() == voteStr).findFirst().orElse(null);
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
}
