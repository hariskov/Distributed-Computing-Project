package com.dc.pojo;

import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Created by xumepa on 10/23/17.
 */

@Service
public class GameManager {
    private Map<Device, Integer> playingOrder;

    public void setPlayingOrder(Map<Device, Integer> playingOrder) {
        this.playingOrder = playingOrder;
    }
}
