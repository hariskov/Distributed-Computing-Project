package com.dc.services;

import com.dc.components.CustomRestTemplate;
import com.dc.pojo.Device;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by xumepa on 10/31/17.
 */

@Service
public class GameService {

    @Autowired
    CustomRestTemplate restTemplate;

    public void sendAddPlayer(Device device, Device currentDevice) {
        String uri = "http://" + device.getIp() + ":8080/project/game/addPlayer";
        restTemplate.put(uri, currentDevice);
    }
}
