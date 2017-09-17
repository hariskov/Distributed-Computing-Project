package com.dc.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by xumepa on 9/17/17.
 */

@Controller
@RequestMapping(value = "/echo")
public class EchoController {

    @RequestMapping(value="/", method = RequestMethod.POST)
    public ResponseEntity<Boolean> exists(@RequestBody String ip){
        System.err.println("ip : " + ip);
        return ResponseEntity.ok().body(null);
    }

    @RequestMapping(value ="/discovery", method = RequestMethod.GET)
    public List<String> discover() throws IOException {
        List<String> discoveredDevices = new ArrayList<String>();

        InetAddress localhost = InetAddress.getLocalHost();
        byte[] ip = localhost.getAddress();

        for (int i = 1; i <= 254; i++) {
            try {
                ip[3] = (byte) i;
                InetAddress address = InetAddress.getByAddress(ip);

                if (address.isReachable(100)) {
                    String deviceIP = address.toString().substring(1);
                    String uri = "http://" + deviceIP + ":8080/echo/";
                    RestTemplate restTemplate = new RestTemplate();

                    ResponseEntity<ResponseEntity> response
                            = restTemplate.postForEntity(uri,null, ResponseEntity.class);
                    System.out.println("response : " + response.getStatusCode());

                }
            }catch(Exception e){
               //e.printStackTrace();
            }
        }
        return null;
    }

}
