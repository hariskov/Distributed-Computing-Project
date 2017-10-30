package com.dc.services;

import com.dc.pojo.Device;
import com.dc.pojo.combos.VoteDevice;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by xumepa on 10/20/17.
 */

@Component
@Service
public interface DeviceService {
    Device discoverDevice(String address);

    List<Device> syncDevices(Device device, List<Device> devices);

    void sendRemoveDevice(Device device, VoteDevice voteDevice);
}
