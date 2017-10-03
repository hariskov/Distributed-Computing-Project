package com.dc.interceptors;

import com.dc.pojo.Devices;
import com.dc.pojo.Vote;
import com.dc.pojo.VotingManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Created by xumepa on 10/3/17.
 */

public class VotingInterceptor implements HandlerInterceptor {

    @Autowired
    VotingManager manager;

    @Autowired
    Devices devices;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if(manager.hasVotes()) {
            Vote lastVote = manager.getVotes().get(manager.getVotes().size() - 1);
            Map<UUID, Object> nullValues = lastVote.getVoteResults().entrySet().stream()
                    .filter(ent -> ent.getValue() == null).collect(Collectors.toMap(p -> p.getKey(), p -> p.getValue()));


            // new vote should come if errors
            if (nullValues.size() > 0) {
                nullValues.forEach((k, v) -> manager.getVote(devices.getDevices().get(k)));
                return false;
            }
        }

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
