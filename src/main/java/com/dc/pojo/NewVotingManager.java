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

    private List<SingleVote> decidedVote = new ArrayList<>();

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

    public List<SingleVote> getDecidedVote() {
        return decidedVote;
    }

    public Vote getLastTempVote(){
        return tempVote.get(tempVote.size()-1);
    }

    public void setDecidedVote(List<SingleVote> decidedVote) {
        this.decidedVote = decidedVote;
    }
}
