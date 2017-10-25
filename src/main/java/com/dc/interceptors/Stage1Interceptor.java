package com.dc.interceptors;

import com.dc.pojo.*;
import com.dc.services.VotingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.AsyncHandlerInterceptor;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by xumepa on 10/4/17.
 */

public class Stage1Interceptor implements AsyncHandlerInterceptor {

    @Autowired
    VotingService votingService;

    @Autowired
    DeviceManager deviceManager;

    @Autowired
    VotingManager votingManager;

    private Logger logger = LoggerFactory.getLogger(Stage1Interceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        logger.info("Stage 1 Vote Interceptor - Pre Handler");
        if(votingManager.getTempVote() == null){
            return true;
        }
        if(!deviceManager.containsAllDevices(votingManager.getTempVote().getDevices())){
            return true;
        }
        return false;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        logger.info("Stage 1 Vote Interceptor - Post Handler");

//        if(deviceManager.containsAllDevices(votingManager.getTempVote().getDevices())){
//
//            if (deviceManager.getCurrentDevice().equals(votingManager.getTempVote().getCreator())) {
//                // only leader can progress !!!!!!!!!!!! -> starting stage 2
//
//                Vote storeTempVoteTemporary = votingManager.getTempVote();
//
//                // make sure all devices have consensus
//                votingManager.applyTempVote();
//
//                for (Device device : deviceManager.getDevices()) {
////                    votingService.sendVoteResult(device, storeTempVoteTemporary.getVoteOfDevice(deviceManager.getCurrentDevice()));
//                }
////                }
//            } else {
//                // are all done ? - check
//                votingManager.applyTempVote();
//            }
//        }else {
//            for (Device device : deviceManager.getDevices()) {
//                if (!device.equals(deviceManager.getCurrentDevice())) {
//                    if (deviceManager.getCurrentDevice().equals(votingManager.getTempVote().getCreator())) {
//                        votingService.sendNewVoteToDevices(device, votingManager.getTempVote());
//                    }
//                }
//            }
//        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        logger.info("Stage 1 Vote Interceptor - After Completion");

    }

    @Override
    public void afterConcurrentHandlingStarted(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        logger.info("Stage 1 Vote Interceptor - After Concurrent Handling Started");
    }
}
