package com.dc.pojo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    public void addVote(Device device, Object body) {
        SingleVote da = new SingleVote();
        da.setAnswer(body);
        da.setDevice(device);
        votes.add(da);
    }

    public SingleVote calculateVote() {
//        Vote receivedVote = manager.stream().filter(e->e.getVoteStr().equals(voteMap)).findFirst().get();
        Map<Object,Long> a = votes.parallelStream().collect(Collectors.groupingBy(w->w.getAnswer(), Collectors.counting()));

        Map.Entry<Object, Long> maxValue = a.entrySet().stream().max(Map.Entry.comparingByValue()).orElse(null); // assumes n/2 + 1

        List<SingleVote> result = votes.stream()
                .filter(s -> s.getAnswer()==maxValue.getKey())
                .collect(Collectors.toList());

        if(result.size()==1){
            return result.get(0);
        }
        else{
            SingleVote returnSingleVote = null;
            int maxIp = 0;
//            result.stream().max(e->e.getDevice().getIp());
            for (SingleVote singleVote : result) {
                try {
                    int lastDigit = Integer.parseInt(singleVote.getDevice().getIp().split(".")[3]);
                    if (lastDigit > maxIp) {
                        returnSingleVote = singleVote;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return returnSingleVote;
        }
    }

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
}