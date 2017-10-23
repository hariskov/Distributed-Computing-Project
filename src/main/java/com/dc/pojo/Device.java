package com.dc.pojo;

import java.util.UUID;

/**
 * Created by xumepa on 10/6/17.
 */
//@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
public class Device {
    private String ip;
    private UUID uuid;

    public Device(){
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    @Override
    public boolean equals(Object var1) {
        if(var1 instanceof Device){
            Device newDevice = (Device)var1;
            if(newDevice.getUuid().equals(this.getUuid()) || newDevice.getIp().equals(this.getIp())){
                return true;
            }
            return false;
        }else{
            return false;
        }
    }

}