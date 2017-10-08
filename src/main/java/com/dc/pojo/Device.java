package com.dc.pojo;

import java.util.UUID;

/**
 * Created by xumepa on 10/6/17.
 */
public class Device{
    private String ip;
    private UUID uuid;

    public Device(String ip){
        this(UUID.randomUUID(),ip);
    }

    public Device(UUID uuid, String ip){
        this.ip=ip;
        this.uuid=uuid;
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
}