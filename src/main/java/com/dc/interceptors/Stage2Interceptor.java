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
import java.util.ArrayList;
import java.util.List;

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
            Vote prevVote = votingManager.getCopyOfVote(votingManager.getLastVote());
            if(prevVote!=null){
                votingManager.setTempVote(prevVote);
            }
            return  true;
        }

        if(votingManager.getTempVote().getVoteStr().equals("LeaderSelect")){
            if(votingManager.getTempVote().getVoteOfDevice(deviceManager.getCurrentDevice()).getAnswer() == ""){
                logger.info("Stage 2 Vote Interceptor - Pre Handler - set answer");
                SingleVote deviceVote = votingManager.getTempVote().getVoteOfDevice(deviceManager.getCurrentDevice());

                deviceVote.setAnswer(votingService.generateLeader());
                deviceVote.setSequence(deviceVote.getSequence() + 1);
                logger.info("Stage 2 Vote Interceptor - Pre Handler - get answer " + votingManager.getTempVote().getVoteOfDevice(deviceManager.getCurrentDevice()).getAnswer());

                return true;
            }
        }

        //check for empty votes
        if(votingManager.getTempVote().getVotes().stream().filter(e->e.getAnswer()=="").count()==0){
            Object answer = votingService.calculateVote(votingManager.getTempVote().getVoteStr());

            if(checkVoteIssues(answer)) {
                return true;
            }else{
                return false;
            }
        }

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        logger.info("Stage 2 Vote Interceptor - Post Handler");


    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        logger.info("Stage 2 Interceptor - After Completion");

        if(votingManager.getTempVote()==null){
            logger.error("fucked up heavily");
        }

        if (deviceManager.containsAllDevices(votingManager.getTempVote().getDevices())) {
            if(votingManager.getTempVote().getVotes().stream().filter(e->e.getAnswer()=="").count()>0){
                for (Device device : deviceManager.getDevices()) {
                    votingService.sendVoteResult(device, votingManager.getTempVote().getVoteOfDevice(deviceManager.getCurrentDevice()));
                }
            }else{
                Object answer = votingService.calculateVote(votingManager.getTempVote().getVoteStr());

                SingleVote v = new SingleVote();
                v.setDevice(deviceManager.getCurrentDevice());
                v.setAnswer(answer);
                v.setSequence(v.getSequence()+1);

                if(checkVoteIssues(answer)){
                    // all answers are the same :) woohoo
                    logger.info(" all answers are the same :) ");

                }

                for (Device device : deviceManager.getDevices()) {
                    votingService.sendVoteResult(device, v);
                }
            }

            if(votingManager.getTempVote().getVoteOfDevice(deviceManager.getCurrentDevice()).getAnswer()==""){
                logger.info(votingManager.getTempVote().getVoteStr() + " parteh");

            }else {
                for (Device device : deviceManager.getDevices()) {
                    votingService.sendVoteResult(device, votingManager.getTempVote().getVoteOfDevice(deviceManager.getCurrentDevice()));
                }
            }
        }
    }

    private boolean checkVoteIssues(Object answer) {

        List<Device> wrongDevices = new ArrayList<Device>();

        for(Device dev : votingManager.getTempVote().getDevices()) {
            Object devAnswer = votingManager.getTempVote().getVoteOfDevice(dev).getAnswer();
            if(answer instanceof Device && devAnswer instanceof Device){
                if(!((Device)answer).equals((Device) devAnswer)){
                    wrongDevices.add(dev);
                }
            }
        }

        if(wrongDevices.size()>0){
            return false;
        }
        return true;
    }
}
