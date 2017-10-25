package com.dc.pojo;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by xumepa on 10/23/17.
 */

@Service
public class GameManager {
    private List<Device> playingOrder;

    public void setPlayingOrder(List<Device> playingOrder) {
        this.playingOrder = playingOrder;
    }
}
