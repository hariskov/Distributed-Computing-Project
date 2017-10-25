package com.dc.interceptors;

import com.dc.exceptions.NoDevicesException;
import com.dc.pojo.Device;
import com.dc.pojo.DeviceManager;
import com.dc.pojo.Vote;
import com.dc.pojo.VotingManager;
import com.dc.services.NewVotingService;
import com.dc.services.VotingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by xumepa on 10/3/17.
 */
public class StartVoteInterceptor implements HandlerInterceptor {

    @Autowired
    VotingManager votingManager;

    @Autowired
    DeviceManager deviceManager;

    @Autowired
    NewVotingService newVotingService;

    // to check for previous votes that are not completed.
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        if(deviceManager.getDevices().size()==0) {
            throw new NoDevicesException();
        }
//
//        if(manager.hasVotes()) {
//            if (manager.getTempVote()!=null) {
//                return true;
//            }
//        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        // calculate results n shit

        while(!deviceManager.containsAllDevices(votingManager.getTempVote().getDevices())) {
            for (Device device : deviceManager.getDevices()) {
                if (votingManager.getTempVote().getVoteOfDevice(device) == null) {
                    newVotingService.sendVote(votingManager.getTempVote());
                }
                Thread.sleep(1000);
            }
        }

        if(deviceManager.containsAllDevices(votingManager.getTempVote().getDevices())){
            System.out.println("go to stage 2");
        }
//            return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
//        manager.putVote(voteType, device, result);

    }
}
