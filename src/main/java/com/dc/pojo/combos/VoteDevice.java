package com.dc.pojo.combos;

import com.dc.pojo.Device;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by xumepa on 10/27/17.
 */
public class VoteDevice {

    private List<Device> devices = new LinkedList<Device>();
    private String voteStr;

    public VoteDevice(){

    }

    public List<Device> getDevices() {
        return devices;
    }

    public void addDevice(Device device){
        devices.add(device);
    }

    public void addDevices(List<Device> devices){
        devices.addAll(devices);
    }

    public void setDevice(List<Device> device) {
        this.devices = devices;
    }

    public String getVoteStr() {
        return voteStr;
    }

    public void setVoteStr(String voteStr) {
        this.voteStr = voteStr;
    }
}
