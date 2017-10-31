package com.dc.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xumepa on 10/3/17.
 */

//@JsonSerialize(include=JsonSerialize.Inclusion.ALWAYS)
public class Vote implements Cloneable{
    private List<SingleVote> votes = new ArrayList<SingleVote>();
    private String voteStr;
    private Device creator;
    private Vote passedVote;

    @JsonIgnore
    private final Logger logger = LoggerFactory.getLogger(Vote.class);

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

    public Device getCreator() {
        return creator;
    }

    public void setCreator(Device creator) {
        this.creator = creator;
    }

    public List<SingleVote> getVotes() {
        return votes;
    }


    public void addVote(SingleVote da) {
//        logger.info(da.getDevice().getIp() + " " + containsDevice(da.getDevice()));
        if(votes.stream().filter(e->e.getDevice().equals(da.getDevice())).count()==0) {
            votes.add(da);
        }
    }

    public SingleVote getVoteOfDevice(Device currentDevice) {
        return votes.stream().filter(e->e.getDevice().equals(currentDevice)).findFirst().orElse(null);
    }

    public List<Device> getDevices() {
        List<Device> devs = new ArrayList<Device>();
        votes.stream().forEach(e->devs.add(e.getDevice()));
        return devs;
    }

    public void setPassedVote(Vote passedVote) {
        this.passedVote = passedVote;
    }

    public Vote getPassedVote() {
        return passedVote;
    }

    public boolean hasPassedVote(){
        return passedVote != null;
    }
}