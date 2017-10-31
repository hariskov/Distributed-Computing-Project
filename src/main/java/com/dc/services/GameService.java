package com.dc.services;

import com.dc.components.CustomRestTemplate;
import com.dc.pojo.Device;
import com.dc.pojo.DeviceManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by xumepa on 10/31/17.
 */

@Service
public class GameService {

    @Autowired
    DeviceManager deviceManager;

    @Autowired
    CustomRestTemplate restTemplate;

    public void sendAddPlayer(Device device, Device currentDevice) {
        String uri = "http://" + device.getIp() + ":8080/project/game/addPlayer";
        restTemplate.put(uri, currentDevice);
    }

    public List<Device> getPlayingOrder(Device device){
        String uri = "http://" + device.getIp() + ":8080/project/game/getPlayingOrder";

        ParameterizedTypeReference<List<Device>> typeRef = new ParameterizedTypeReference<List<Device>>() {};

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Object> requestEntity = new HttpEntity<Object>(headers);

        ResponseEntity<List<Device>> responseEntity = restTemplate.exchange(uri, HttpMethod.POST, requestEntity, typeRef);
        return responseEntity.getBody();
    }

    public int requestPlayTurn(Device device) {
        String uri = "http://" + device.getIp() + ":8080/project/game/getPlayTurn";
        ResponseEntity response = restTemplate.postForEntity(uri,null,Integer.class);
        return (int) response.getBody();
    }

    @Async
    public void sendNextPlayer(Device device, Device nextPlayer) {
        String uri = "http://" + device.getIp() + ":8080/project/game/setNextPlayer";
        restTemplate.put(uri, nextPlayer);
    }
}
