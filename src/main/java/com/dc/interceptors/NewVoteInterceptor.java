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
            if(votingManager.getTempVote().getVoteStr() == "LeaderSelect"){
                System.out.println("abcde");
            }
            votingManager.setTempVote(null);
            return false;
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

        for (Device device : deviceManager.getDevices()) {
            if(device!=deviceManager.getCurrentDevice()) {
                if(!votingService.sendNewVoteToDevices(device, votingManager.getCurrentSingleVote())){
                    // get rid of device ! -> fault tolerance;

                }
            }
        }

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
