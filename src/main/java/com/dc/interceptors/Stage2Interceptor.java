package com.dc.interceptors;

import com.dc.pojo.*;
import com.dc.services.VotingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by xumepa on 10/22/17.
 */
public class Stage2Interceptor implements HandlerInterceptor {

    @Autowired
    VotingManager votingManager;

    @Autowired
    DeviceManager deviceManager;

    @Autowired
    GameManager gameManager;

    @Autowired
    VotingService votingService;

    private Logger logger = LoggerFactory.getLogger(Stage2Interceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        if(votingManager.getTempVote() == null){
            return true;
        }

        logger.info("Receive Vote Interceptor - Pre Handler");
        if(votingManager.getTempVote().getVoteStr().equals("LeaderSelect")){
            if(votingManager.getTempVote().getVoteOfDevice(deviceManager.getCurrentDevice()).getAnswer()==""){
                votingManager.getTempVote().getVoteOfDevice(deviceManager.getCurrentDevice()).setAnswer(votingService.generateLeader());
            }
            if(votingManager.getTempVote().getVotes().stream().filter(e->e.getAnswer()=="").count()>0){
                return true;
            }
//            Object result = votingManager.getTempVote().calculateVote().getAnswer();

            Object a = votingManager.getTempVote().getOrderedVotes();

            // decide game order !
//            gameManager.setPlayerOrder();
//            System.out.println(result);
        }

//            votingManager.setTempVote(null);
        /// check for values in received vote -> follow logic of Stage1Interceptor
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        logger.info("Receive Vote Interceptor - Post Handler");

        Vote v = votingManager.getTempVote();
        // compute shit -> if all correct -> apply localy
        //                  if not all correct -> re-send current computed value to all other machines -> votingService.sendVoteResult()

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        logger.info("Receive Vote Interceptor - After Completion");

    }
}
