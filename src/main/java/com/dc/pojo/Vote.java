package com.dc.pojo;

import org.springframework.security.access.method.P;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by xumepa on 10/3/17.
 */

//@JsonSerialize(include=JsonSerialize.Inclusion.ALWAYS)
public class Vote {
    private List<SingleVote> votes = new ArrayList<SingleVote>();
    private String voteStr;
    private Device creator;

    public Vote(){
    }

    public Vote(String voteStr) {
        this();
        this.voteStr = voteStr;
    }

    public boolean containsDevice(Device device){
        for(SingleVote da : votes) {
            if (da.getDevice().equals(device)) {
                return true;
            }
        }
        return false;
    }

    public String getVoteStr() {
        return voteStr;
    }
    public void setVoteStr(String str){this.voteStr=str;}

    public void addVote(Device device, Object body) {
        SingleVote da = new SingleVote();
        da.setAnswer(body);
        da.setDevice(device);
        votes.add(da);
    }

//    public Object calculateVote() {
////        Vote receivedVote = manager.stream().filter(e->e.getVoteStr().equals(voteMap)).findFirst().get();
//        Map<Object,Long> a = getVoteMap().entrySet().parallelStream().collect(Collectors.groupingBy(w->w.getValue(), Collectors.counting()));
//        return a.entrySet().stream().max(Map.Entry.comparingByValue()).get(); // assumes n/2 + 1
//    }

    public Device getCreator() {
        return creator;
    }

    public void setCreator(Device creator) {
        this.creator = creator;
    }

    public List<SingleVote> getVotes() {
        return votes;
    }

    public void setVotes(List<SingleVote> votes) {
        this.votes = votes;
    }

    public void addVote(SingleVote da) {
        votes.add(da);
    }

    public void setCurrentVote(String voteStr, SingleVote currentVote) {
        Vote vote = new Vote();
        vote.setVoteStr(voteStr);
        vote.addVote(currentVote);
    }

    public SingleVote getVoteOfDevice(Device currentDevice) {
        Optional<SingleVote> result = votes.stream().filter(e->e.getDevice().equals(currentDevice)).findFirst();
        SingleVote vote = result.get();
        return vote;
    }
}