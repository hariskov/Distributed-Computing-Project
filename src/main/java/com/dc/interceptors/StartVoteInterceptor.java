package com.dc.interceptors;

import com.dc.exceptions.NoDevicesException;
import com.dc.pojo.*;
import com.dc.services.DeviceService;
import com.dc.services.NewVotingService;
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
 * Created by xumepa on 10/3/17.
 */
public class StartVoteInterceptor implements HandlerInterceptor {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    NewVotingManager votingManager;

    @Autowired
    DeviceManager deviceManager;

    @Autowired
    NewVotingService newVotingService;

    @Autowired
    GameManager gameManager;

    @Autowired
    DeviceService deviceService;

    // to check for previous votes that are not completed.
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        if(deviceManager.getDevices().size()==0) {
            throw new NoDevicesException();
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        // calculate results n shit

        Vote lastVote = votingManager.getLastTempVote();

        if(!deviceManager.containsAllDevices(lastVote.getDevices())) {
            while (!deviceManager.containsAllDevices(lastVote.getDevices())) {
                List<Device> devicesToRemove = new ArrayList<>();
                devicesToRemove.addAll(deviceManager.getDevices());

                for (Device device : deviceManager.getDevices()) {
                    if (lastVote.getVoteOfDevice(device) == null) {
                        boolean received = false;
                        Vote blankVote = new Vote();
                        blankVote.setCreator(lastVote.getCreator());
                        blankVote.setVoteStr(lastVote.getVoteStr());
                        received = newVotingService.sendVote(device, blankVote);
                        if(!received){
                            // the vote has been fucked up ! remove device if cant ping
                            if(deviceService.discoverDevice(device.getIp())!=null){
                                devicesToRemove.remove(device);
                                // remove device and resync
                                //TODO
//                                if you remove - lasTVote should also be removed !!!
                            }else{
                                logger.error(device.getIp() + " no longer exists");
                            }
                        }
                    }
                    Thread.sleep(1000);
                }
                if(devicesToRemove.size()>0){
                    for (Device device : devicesToRemove) {
                        if(!devicesToRemove.contains(device)) {
                            deviceService.sendRemoveDevice(device, lastVote.getVoteStr());
                        }
                    }
                }
            }
        }
        if(deviceManager.containsAllDevices(lastVote.getDevices())){
            //apply

            List<Vote> listTempVotesReceived = new ArrayList<>();
            if(listTempVotesReceived.size()!=deviceManager.getDevices().size()) {
                while (listTempVotesReceived.size() != deviceManager.getDevices().size()) {
                    for (Device device : deviceManager.getDevices()) {
                        Vote result = newVotingService.sendApplyTempVote(device, lastVote);
                        if (result != null) {
                            listTempVotesReceived.add(result);
                        }
                    }
                    Thread.sleep(1000);
                }
                System.out.println("still not time for stage 2");
            }

            while(lastVote.getVotes().stream().filter(e->e.getAnswer()==null).count()>0) {
                for (Device device : deviceManager.getDevices()) {
                    if (lastVote.getVoteOfDevice(device).getAnswer() == null) {
                        SingleVote singleVote = newVotingService.sendValueRequest(device, lastVote.getVoteStr());
                        lastVote.getVoteOfDevice(device).setAnswer(singleVote.getAnswer());
                    }
                }

                Thread.sleep(1000);
            }

            // calculate votes

//                Object calculatedResult = newVotingService.calculateOrder(votingManager.getTempVote());
//                SingleVote calculatedVote = new SingleVote();
//                calculatedVote.setDevice(deviceManager.getCurrentDevice());
//                calculatedVote.setAnswer(calculatedResult);
//
            Object calculatedResult = newVotingService.calculateVote(lastVote);
            SingleVote calculatedVote = new SingleVote();
            calculatedVote.setDevice(deviceManager.getCurrentDevice());
            calculatedVote.setAnswer(calculatedResult);

            for(Device device : deviceManager.getDevices()){

                List<Device> result = newVotingService.calculateOrder(lastVote);
                newVotingService.sendApplyVote(device, lastVote.getVoteStr(), calculatedVote);
                newVotingService.sendApplyPlayOrder(device, result);

            }

            // lets say it worked

        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
//        manager.putVote(voteType, device, result);
    }
}
