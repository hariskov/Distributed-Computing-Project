package com.dc.interceptors;

import com.dc.pojo.Device;
import com.dc.pojo.DeviceManager;
import com.dc.pojo.VotingManager;
import com.dc.services.VotingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Random;

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
        if(votingManager.getTempVote() == null){
            return true;
        }

        if(deviceManager.getDevices().size() == votingManager.getTempVote().getVotes().size()){

            if(votingManager.getTempVote().getCreator().equals(deviceManager.getCurrentDevice())){
//                votingService.sendVoteResult();
            }
//            votingManager.applyVote(votingManager.getTempVote());
            if(votingManager.getTempVote().getVoteStr().equals("LeaderSelect")){
                votingManager.getTempVote().getVoteOfDevice(deviceManager.getCurrentDevice()).setAnswer(votingService.generateLeader());
                for (Device device : deviceManager.getDevices()) {
                    votingService.sendVoteResult(device,votingManager.getTempVote());
                }
            }
            return false;
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

        for (Device device : deviceManager.getDevices()) {
//            if(!device.equals(deviceManager.getCurrentDevice())) {
                if(!votingService.sendNewVoteToDevices(device, votingManager.getTempVote())){
                    // get rid of device ! -> fault tolerance;

                }
//            }
        }

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
