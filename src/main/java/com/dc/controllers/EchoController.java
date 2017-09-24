package com.dc.controllers;

import com.dc.pojo.Devices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.UUID;

/**
 * Created by xumepa on 9/17/17.
 */

@Controller
@RequestMapping(value = "/echo")
public class EchoController {

    @Autowired
    Devices devices;

    @RequestMapping(value="/", method = RequestMethod.POST)
    public ResponseEntity<String> exists(){
        UUID uuid = UUID.randomUUID();
        String randomUUIDString = uuid.toString();
        devices.setCurrentDeviceUUID(uuid);

        return ResponseEntity.ok().body(randomUUIDString);
    }

    @RequestMapping(value ="/discovery", method = RequestMethod.GET)
    public List<String> discover() throws IOException {
//        List<String> discoveredDevices = new ArrayList<String>();

//        InetAddress localhost = InetAddress.getLocalHost();
//        byte[] localhost1 = InetAddress.getLocalHost().getAddress();
//        String localhost2 = InetAddress.getLocalHost().getCanonicalHostName();

        InetAddress localhost = getLocalAddress();

        byte[] ip = localhost.getAddress();
        String address = "192.168.8.31";
        devices.discoverDevice(address);


//        for (int i = 1; i <= 254; i++) {
//            try {
//                ip[3] = (byte) i;
//                InetAddress address = InetAddress.getByAddress(ip);
//                if(!address.equals(localhost)) {
//
//                    if (address.isReachable(100)) {
//                        ping(address.toString().substring(1));
//                    }
//                }
//            }catch(Exception e){
//               //e.printStackTrace();
//            }
//        }

        return null;
    }


    private static InetAddress getLocalAddress(){
        try {
            Enumeration<NetworkInterface> b = NetworkInterface.getNetworkInterfaces();
            while( b.hasMoreElements()){
                for ( InterfaceAddress f : b.nextElement().getInterfaceAddresses())
                    if ( f.getAddress().isSiteLocalAddress())
                        return f.getAddress();
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return null;
    }

}
