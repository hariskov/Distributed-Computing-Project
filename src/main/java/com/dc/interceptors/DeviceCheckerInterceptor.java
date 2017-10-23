package com.dc.interceptors;

import com.dc.pojo.DeviceManager;
import com.dc.services.DeviceService;
import com.dc.services.VotingService;
import org.springframework.beans.factory.annotation.Autowired;
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
    VotingService votingService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        if(deviceManager.getDevices().size()>1) {
            deviceManager.getDevices().forEach(e->deviceManager.syncDevices(e));
//            deviceManager.syncDevices(); //votingManager.getVotes()
        }

        deviceManager.startLeaderVote();
        votingService.startNewVote("LeaderSelect");

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        //start leader selection process after devices have discovered and synced.
        //votingManager.createVote("Leader");

//        String uri = "http://" + deviceManager.getCurrentDevice().getIp() + ":8080/project/voting/startVote";
//
//        try {
//              restTemplate.postForEntity(uri, "Leader", Object.class);
//        }catch(Exception e){
//            e.printStackTrace();
//        }
    }
}
