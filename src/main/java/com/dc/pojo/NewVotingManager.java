package com.dc.pojo;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by xumepa on 10/25/17.
 */

@Component
public class NewVotingManager {
    private NewVote tempVote;
    private List<NewVote> decidedVote = new ArrayList<>();
    private List<NewVote> manager;

    public NewVotingManager(){
        manager = new LinkedList<NewVote>();
    }
    public void setTempVote(NewVote tempVote) {
        this.tempVote = tempVote;
    }

    public NewVote getTempVote() {
        return tempVote;
    }

    public void addToDecidedVote(NewVote vote) {
        this.decidedVote.add(vote);
    }
}
