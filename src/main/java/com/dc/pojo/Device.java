package com.dc.pojo;

import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.io.Serializable;
import java.util.Random;
import java.util.UUID;

/**
 * Created by xumepa on 10/6/17.
 */
@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
public class Device extends Object implements Serializable{
    private String ip;
    private UUID uuid;
    transient int id;

    public Device(){

    }

    public Device(String ip){
        this(UUID.randomUUID(),ip);
    }

    public Device(UUID uuid, String ip){
        this.ip=ip;
        this.uuid=uuid;
        id = new Random().nextInt(10000);
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
            if(this.id == newDevice.id){
                return true;
            }
            if(newDevice.getUuid().equals(this.getUuid()) && newDevice.getIp().equals(this.getIp())){
                return true;
            }
            return false;
        }else{
            return false;
        }
    }


    @Override public int hashCode() { return id; }

}