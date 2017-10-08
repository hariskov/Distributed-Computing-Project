package com.dc.pojo;

import java.util.HashMap;
import java.util.UUID;

/**
 * Created by xumepa on 10/3/17.
 */


public class Vote {
    private final Devices devices;
    HashMap<Device, Object> vote = new HashMap<>();
    private String voteStr;

    public Vote(Devices devices) {
        this.devices = devices;
    }

    public void createVote(String voteStr) {
        this.voteStr = voteStr;
        devices.getDevices().forEach(d -> vote.put(d, null));
    }

    public HashMap<Device,Object> getVoteResults(){
        return vote;
    }
    public String getVote() {
        return voteStr;
    }

    public void setVote(Device device, Boolean body) {
        vote.put(device, body);
    }

    public int getVoteParticipants(){
        return vote.size();
    }
}