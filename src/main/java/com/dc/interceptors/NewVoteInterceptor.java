package com.dc.interceptors;

import com.dc.exceptions.NoDevicesException;
import com.dc.misc.CustomInterceptor;
import com.dc.pojo.Device;
import com.dc.pojo.DeviceManager;
import com.dc.pojo.Vote;
import com.dc.pojo.VotingManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by xumepa on 10/3/17.
 */
@CustomInterceptor
public class NewVoteInterceptor implements HandlerInterceptor {

    @Autowired
    VotingManager manager;

    @Autowired
    DeviceManager deviceManager;

    // to check for previous votes that are not completed.
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        if(deviceManager.getDevices().size()==0) {
            throw new NoDevicesException();
        }

        if(manager.hasVotes()) {
            Vote lastVote = manager.getLastVote();
            Map<Device, Object> nullValues = lastVote.getVote().entrySet().stream()
                    .filter(ent -> ent.getValue() == "").collect(Collectors.toMap(p -> p.getKey(), p -> p.getValue()));

            // new vote should come if errors
            if (nullValues.size() > 0) {
                //nullValues.forEach((k, v) -> manager.getNetworkVotes(deviceManager.getDevices().get(k)));
                // request vote where it failed !

                return true;
            }
        }

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        // we have all votes calculated !
//        Vote lastVote = manager.getVotes().get(manager.getVotes().size()-1);
//        if(lastVote.receiveVoteParticipants().size() != deviceManager.getDevices().size()){
//            List<Device> missingAnswers = deviceManager.getDevices().stream().filter(e->!lastVote.receiveVoteParticipants().contains(e)).collect(Collectors.toList());
//            // something went wrong -> a recipient left.
//            // restart
//            missingAnswers.forEach(e->manager.getNetworkVotes(e,lastVote));
//            return;
//        }
//
//        Object resultedObject = manager.getLastVote().calculateVote();
//        if(resultedObject instanceof Device) {
//            Device dev = (Device)resultedObject;
//            System.err.println("winning vote " + dev.getUuid());
//        }

        // send the vote to the rest of clients


    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        for (Device device : deviceManager.getDevices()) {
            manager.sendVote(device,manager.getCurrentLastVote());
        }
    }
}
