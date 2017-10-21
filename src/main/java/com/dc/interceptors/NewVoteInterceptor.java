package com.dc.interceptors;

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
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
//        calculateVote
        // send Current Temp Vote to all machines to verify !
        // call receiveVote with updated temp vote.

        if(deviceManager.getDevices().size() != votingManager.getTempVote().getVote().size()) {
            deviceManager.getDevices().forEach(e -> votingService.sendNewVoteToDevices(e, votingManager.getTempVote()));
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
