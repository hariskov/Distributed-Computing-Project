package com.dc.pojo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by xumepa on 10/3/17.
 */

//@JsonSerialize(include=JsonSerialize.Inclusion.ALWAYS)
public class Vote implements Cloneable{
    private List<SingleVote> votes = new ArrayList<SingleVote>();
    private String voteStr;
    private Device creator;

    private final Logger logger = LoggerFactory.getLogger(Vote.class);
    private List<Vote> devices;

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

    public void setVotes(List<SingleVote> votes) {
        this.votes = votes;
    }

    public void addVote(SingleVote da) {
        logger.info(da.getDevice().getIp() + " " + containsDevice(da.getDevice()));
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

    public Map<Object, Long> getOrderedVotes() {
        Map<Object,Long> a = votes.parallelStream().collect(Collectors.groupingBy(w->w.getAnswer(), Collectors.counting()));

        Map<Object,Long> result = a.entrySet().stream()
                .sorted(Map.Entry.comparingByValue())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue, LinkedHashMap::new));
        return result;
    }

    public List<Device> getDevices() {
        List<Device> devs = new ArrayList<Device>();
        votes.stream().forEach(e->devs.add(e.getDevice()));
        return devs;
    }

}