package com.dc.services;

import com.dc.components.CustomRestTemplate;
import com.dc.pojo.Device;
import com.dc.pojo.combos.VoteDevice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

/**
 * Created by xumepa on 10/20/17.
 */
@Component
@Transactional
@Service(value = "deviceService")
public class DeviceServiceImpl implements DeviceService {

    @Autowired
    CustomRestTemplate restTemplate;

    @Override
    public Device discoverDevice(String address) {
        String uri = "http://" + address + ":8080/project/echo/";

        try {
            ResponseEntity<Device> response
                    = restTemplate.postForEntity(uri, null, Device.class);
            System.out.println(response.getBody());
            return response.getBody();
        }catch (Exception e){
            return null;
        }
    }

    @Override
    public List<Device> syncDevices(Device targetDevice, List<Device> deviceList) {

        try {
            String uri = "http://" + targetDevice.getIp() + ":8080/project/echo/getDevices";
            ParameterizedTypeReference<List<Device>> typeRef = new ParameterizedTypeReference<List<Device>>() {};

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<Object> requestEntity = new HttpEntity<Object>(deviceList,headers);

            ResponseEntity<List<Device>> responseEntity = restTemplate.exchange(uri, HttpMethod.POST, requestEntity, typeRef);
            List<Device> deviceResponse = responseEntity.getBody();
            return deviceResponse;
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void sendRemoveDevice(Device device, VoteDevice voteDevice) {
        try {
//            VoteDevice voteDevice = new VoteDevice();
//            voteDevice.setDevice(device);
//            voteDevice.setVoteStr(voteStr);
            String uri = "http://" + device.getIp() + ":8080/project/echo/removeDeviceAndLastVote";
            restTemplate.postForEntity(uri, voteDevice, Device.class);
        }catch(Exception e){

        }
    }
}
