package com.dc.interceptors;

import com.dc.pojo.Device;
import com.dc.pojo.DeviceManager;
import com.dc.pojo.VotingManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by xumepa on 10/3/17.
 */
public class DeviceCheckerInterceptor implements HandlerInterceptor {

    @Autowired
    DeviceManager deviceManager;

    @Autowired
    RestTemplate restTemplate;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        // this should send the current state of the deviceManager to all deviceManager.
//        deviceManager.getDevices().forEach(device -> deviceManager.syncDevices());
        if(deviceManager.getDevices().size()>1) {
            deviceManager.getDevices().forEach(e->deviceManager.syncDevices(e));
//            deviceManager.syncDevices(); //votingManager.getVotes()
        }

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        //start leader selection process after devices have discovered and synced.
        //votingManager.createVote("Leader");

            String uri = "http://" + deviceManager.getCurrentDevice().getIp() + ":8080/voting/startVote";

            try {
                ResponseEntity<Object> voteResponse
                        = restTemplate.postForEntity(uri, "Leader", Object.class);
                System.out.println(voteResponse.getBody());
            }catch(Exception e){
                e.printStackTrace();
            }

    }
}
