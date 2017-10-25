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
        logger.info("Stage 2 Vote Interceptor - Pre Handler");

        if(votingManager.getTempVote() == null){
            if(votingManager.getTempVote()==null){
                Vote prevVote = votingManager.getCopyOfVote(votingManager.getLastVote());
                if(prevVote!=null){
                    votingManager.setTempVote(prevVote);
                }
            }
            return true;
        }

        if(votingManager.getTempVote().getVoteStr().equals("LeaderSelect")){
            if(votingManager.getTempVote().getVoteOfDevice(deviceManager.getCurrentDevice()).getAnswer() == ""){
                logger.info("Stage 2 Vote Interceptor - Pre Handler - set answer");
                SingleVote deviceVote = votingManager.getTempVote().getVoteOfDevice(deviceManager.getCurrentDevice());

                deviceVote.setAnswer(votingService.generateLeader());
                deviceVote.setSequence(deviceVote.getSequence() + 1);
                logger.info("Stage 2 Vote Interceptor - Pre Handler - get answer " + votingManager.getTempVote().getVoteOfDevice(deviceManager.getCurrentDevice()).getAnswer());
            }

            if(votingManager.getTempVote().getVotes().stream().filter(e->e.getAnswer()=="").count()>0){
                return true;
            }

//            Object result = votingManager.getTempVote().calculateVote().getAnswer();

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
        logger.info("Stage 2 Vote Interceptor - Post Handler");

        // compute shit -> if all correct -> apply localy
        //                  if not all correct -> re-send current computed value to all other machines -> votingService.sendVoteResult()

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        logger.info("Stage 2 Interceptor - After Completion");

        if(votingManager.getTempVote()==null){
            logger.error("fucked up heavily");
        }

        if (deviceManager.containsAllDevices(votingManager.getTempVote().getDevices())) {

//            for (Device device : deviceManager.getDevices()) {
//                votingService.sendGetCalculatedResult(device, votingManager.getTempVote().getVoteStr());
//            }
//
            if(votingManager.getTempVote().getVoteOfDevice(deviceManager.getCurrentDevice()).getAnswer()==""){
                logger.info(votingManager.getTempVote().getVoteStr() + " parteh");

                SingleVote v = new SingleVote();
                v.setDevice(deviceManager.getCurrentDevice());
                v.setAnswer(votingService.calculateVote(votingManager.getTempVote().getVoteStr()));
                v.setSequence(v.getSequence()+1);

                for (SingleVote singleVote : votingManager.getTempVote().getVotes()) {
                    if(v.getAnswer() == singleVote.getAnswer()){
                        logger.info("ASFGDFGSFGHFDH SWET$TSHdt y3it9rfksjfvbp n0b6s");
                        // all local ones have the same answer -> continue to stage 3
                    }
                }

                //reset temp vote to blank answers;
//                votingManager.setTempVote(votingManager.getCopyOfVote(votingManager.getTempVote()));

//                votingManager.getCalcVote().getVoteOfDevice(deviceManager.getCurrentDevice()).setAnswer(v.getAnswer());

//                for(Device device : deviceManager.getDevices()){
//                    votingService.sendCalculatedVote(device,votingManager.getCalcVote());
//                }

//                if(votingManager.getTempVote().getCreator().equals(deviceManager.getCurrentDevice())){
//                    for (Device device : deviceManager.getDevices()) {

//                        votingService.sendApplyTemp(device, votingManager.getTempVote().getVoteStr());
//                    }
//                }

                for (Device device : deviceManager.getDevices()) {
                    if(!deviceManager.getCurrentDevice().equals(device)){
                        votingService.sendVoteResult(device, v);
                    }
                }

            }else {
                for (Device device : deviceManager.getDevices()) {
                    if(!deviceManager.getCurrentDevice().equals(device)){
                        votingService.sendVoteResult(device, votingManager.getTempVote().getVoteOfDevice(deviceManager.getCurrentDevice()));
                    }
                }
            }
        }
    }
}
