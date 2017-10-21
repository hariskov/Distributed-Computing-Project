package com.dc.interceptors;

import com.dc.pojo.Device;
import com.dc.pojo.DeviceManager;
import com.dc.pojo.Vote;
import com.dc.pojo.VotingManager;
import com.dc.services.VotingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import sun.misc.IOUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;

/**
 * Created by xumepa on 10/4/17.
 */

public class NewVoteInterceptor implements HandlerInterceptor {

    @Autowired
    VotingService votingService;

    @Autowired
    DeviceManager deviceManager;

    @Autowired
    VotingManager votingManager;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if(handler instanceof Vote){
            System.out.println("babadbdafsfdsfsahdfhsadh");
        }
        request.getInputStream();

        request.getHeaderNames();
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
//        calculateVote
        // send Current Temp Vote to all machines to verify !
        // call receiveVote with updated temp vote.

        for (Device device : deviceManager.getDevices()) {
            if(device!=deviceManager.getCurrentDevice()) {
                votingService.sendNewVoteToDevices(device, votingManager.getTempVote());
            }
        }

//
//        if(deviceManager.getDevices().size() != votingManager.getTempVote().getVote().size()) {
////            deviceManager.getDevices().forEach(e -> votingService.sendNewVoteToDevices(e, votingManager.getTempVote()));
//            for (Device device : deviceManager.getDevices()) {
//                if(device != deviceManager.getCurrentDevice()){
//                    votingService.sendNewVoteToDevices(device,votingManager.getTempVote());
//                }
//            }
//        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
