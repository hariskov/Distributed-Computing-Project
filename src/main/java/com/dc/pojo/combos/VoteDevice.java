package com.dc.pojo.combos;

import com.dc.pojo.Device;

/**
 * Created by xumepa on 10/27/17.
 */
public class VoteDevice {

    private Device device;
    private String voteStr;

    public VoteDevice(){

    }

    public Device getDevice() {
        return device;
    }

    public void setDevice(Device device) {
        this.device = device;
    }

    public String getVoteStr() {
        return voteStr;
    }

    public void setVoteStr(String voteStr) {
        this.voteStr = voteStr;
    }
}
