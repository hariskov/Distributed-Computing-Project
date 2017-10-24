package com.dc.pojo;

import com.dc.exceptions.ExistingVoteException;
import com.dc.services.VotingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

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
    private Vote calcVote;

    public VotingManager(){
        manager = new LinkedList<Vote>();
    }

    public Vote createVote(String voteStr) {

        if (manager.contains(voteStr)) {
            throw new ExistingVoteException();
        }else{
            Vote newVote = new Vote(voteStr);
            newVote.setCreator(deviceManager.getCurrentDevice());
//            newVote.addVote(deviceManager.getCurrentDevice(),"");
//            tempVote = newVote;
//            deviceManager.getDevices().forEach(d -> tempVote.addVote(d, ""));
            return newVote;
        }
    }

    public void sendVotes(Vote vote) {
        for (Device device : deviceManager.getDevices()) {
//            if (device != deviceManager.getCurrentDevice()) {
                votingService.sendNewVoteToDevices(device, vote);
//                manager.putVote(voteType, device, result);
            }
//        }
    }

    public void applyTempVote(){
        manager.add(getTempVote());
        setTempVote(null);
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
//
//    public void putVote(String vote, Device device, Object body) {
//        List<Vote> a = manager.parallelStream().filter(e->e.getVoteStr().equals(vote)).collect(Collectors.toList());
//        if(a.size()>0){
//            Vote returnedVote = a.get(0);
//            returnedVote.addVote(device,body);
//        }
//    }

    public void setVotes(List<Vote> votes) {
        this.manager = votes;
    }

    public void setTempVote(Vote currentCirculatingVote) {
        this.tempVote = currentCirculatingVote;
    }

    public void addValueToCurrentTempVote(SingleVote vote) {
        tempVote.addVote(vote);
    }

    public void setCurrentSingleVote(String str){
        SingleVote sv = new SingleVote();
        sv.setDevice(deviceManager.getCurrentDevice());
        sv.setAnswer("");
        sv.setQuestion(str);
//        tempVote.setCurrentVote(voteStr,sv);
        addValueToCurrentTempVote(sv);
    }

    public Vote containsVote(String voteStr) {
        return manager.stream().filter(e->e.getVoteStr().equals(voteStr)).findFirst().orElse(null);
    }

    public Vote getCalcVote() {
        return calcVote;
    }

    public void setCalcVote(Vote calcVote) {
        this.calcVote = calcVote;
    }

    public Vote getCopyOfVote(Vote vote){
        Vote newVote = new Vote();
        newVote.setVoteStr(vote.getVoteStr());
        newVote.setCreator(vote.getCreator());
        for (Device device : vote.getDevices()) {
            SingleVote singleVote = new SingleVote();
            singleVote.setDevice(device);
            singleVote.setAnswer("");
            newVote.addVote(singleVote);
        }
        return newVote;
    }

}
