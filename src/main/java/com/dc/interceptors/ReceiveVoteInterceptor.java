package com.dc.interceptors;

import com.dc.pojo.Device;
import com.dc.pojo.DeviceManager;
import com.dc.pojo.Vote;
import com.dc.pojo.VotingManager;
import com.dc.services.VotingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by xumepa on 10/22/17.
 */
public class ReceiveVoteInterceptor implements HandlerInterceptor {

    @Autowired
    VotingManager votingManager;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        if(votingManager.getTempVote().getVoteStr().equals("LeaderSelect")){
            if(votingManager.getTempVote().getVotes().stream().filter(e->e.getAnswer()=="").count()>0){
                return true;
            }
            Object result = votingManager.getTempVote().calculateVote().getAnswer();
            System.out.println(result);
        }

//            votingManager.setTempVote(null);
        /// check for values in received vote -> follow logic of NewVoteInterceptor
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        Vote v = votingManager.getTempVote();
        // compute shit -> if all correct -> apply localy
        //                  if not all correct -> re-send current computed value to all other machines -> votingService.sendVoteResult()

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
