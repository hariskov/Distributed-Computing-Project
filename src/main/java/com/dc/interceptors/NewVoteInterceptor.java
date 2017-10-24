package com.dc.interceptors;

import com.dc.pojo.Device;
import com.dc.pojo.DeviceManager;
import com.dc.pojo.Vote;
import com.dc.pojo.VotingManager;
import com.dc.services.VotingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private Logger logger = LoggerFactory.getLogger(NewVoteInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        logger.info("New Vote Interceptor - Pre Handler");
        if(votingManager.getTempVote() == null){
            return true;
        }

        // assume all devices have the temp vote !
        if(deviceManager.getDevices().size() == votingManager.getTempVote().getVotes().size()) {
            Vote storeTempVoteTemporary = votingManager.getTempVote();
            votingManager.applyTempVote();

            // only leader can progress !!!!!!!!!!!! -> starting stage 2
            if(storeTempVoteTemporary.getCreator().equals(deviceManager.getCurrentDevice())){
//                if (votingManager.getTempVote().getVoteStr().equals("LeaderSelect")) {
//                votingManager.getTempVote().getVoteOfDevice(deviceManager.getCurrentDevice()).setAnswer(votingService.generateLeader());
                for (Device device : deviceManager.getDevices()) {
                    votingService.sendVoteResult(deviceManager.getCurrentDevice(), storeTempVoteTemporary);
                }
//                }
            }
//

//            if (votingManager.getTempVote().getVoteOfDevice(deviceManager.getCurrentDevice()) != null) {
//            }
            return false;
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        logger.info("New Vote Interceptor - Post Handler");
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        logger.info("New Vote Interceptor - After Completion");

        for (Device device : deviceManager.getDevices()) {
            if(!device.equals(deviceManager.getCurrentDevice())) {
                if(!votingService.sendNewVoteToDevices(device, votingManager.getTempVote())){
                    // get rid of device ! -> fault tolerance;
                }
            }
        }
    }
}
