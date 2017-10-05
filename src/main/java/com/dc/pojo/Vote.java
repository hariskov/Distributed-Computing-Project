package com.dc.pojo;

import java.util.HashMap;
import java.util.UUID;

/**
 * Created by xumepa on 10/3/17.
 */


public class Vote {
    private final Devices devices;
    HashMap<UUID, Object> vote = new HashMap<>();
    private String voteStr;

    public Vote(Devices devices) {
        this.devices = devices;
    }

    public void createVote(String voteStr) {
        this.voteStr = voteStr;
        devices.getDevices().forEach((k, v) -> vote.put(k, null));
    }

    public HashMap<UUID,Object> getVoteResults(){
        return vote;
    }
    public String getVote() {
        return voteStr;
    }

    public void setVote(UUID deviceUUID, Boolean body) {
        vote.put(deviceUUID, body);
    }

    public int getVoteParticipants(){
        return vote.size();
    }
}