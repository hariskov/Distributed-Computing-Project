package com.dc.interceptors;

import com.dc.exceptions.NoDevicesException;
import com.dc.pojo.*;
import com.dc.pojo.combos.VoteDevice;
import com.dc.services.DeviceService;
import com.dc.services.NewVotingService;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by xumepa on 10/3/17.
 */
public class StartVoteInterceptor implements HandlerInterceptor {

    @JsonIgnore
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    NewVotingManager votingManager;

    @Autowired
    DeviceManager deviceManager;

    @Autowired
    NewVotingService newVotingService;

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
        logger.error("Came In : " + this.getClass().getName() + "." + Thread.currentThread().getStackTrace()[1].getMethodName());

        Vote lastVote = null;

        while(lastVote == null) {
            lastVote = votingManager.getLastTempVote();
            if(lastVote==null) {
                Thread.sleep(1000);
            }
        }

        deviceChecker(lastVote);
        sendBlankTempChecker(lastVote);

        deviceChecker(lastVote);
        applyTempChecker(lastVote);

        deviceChecker(lastVote);
        applySetTempToDevicesChecker(lastVote);

        // calculate votes
        deviceChecker(lastVote);
        applyVoteChecker(lastVote);

        }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
//        manager.putVote(voteType, device, result);
    }

    private void deviceChecker(Vote lastVote){
        List<Device> devicesToRemove = new LinkedList<>();
        List<Device> allDevices = new ArrayList<>();
        allDevices.addAll(deviceManager.getDevices());

        for(Device device : allDevices){
            if(deviceService.discoverDevice(device.getIp())==null){
                devicesToRemove.add(device);
            }
        }

        allDevices.removeAll(devicesToRemove);
        if (devicesToRemove.size() > 0) {
            constructSendRemoveDevices(allDevices, devicesToRemove, lastVote.getVoteStr());
        }
    }

    private void sendBlankTempChecker(Vote lastVote) throws InterruptedException {
        List<Device> devicesToRemove = new ArrayList<>();
        List<Device> allDevices = new ArrayList<>();
        allDevices.addAll(deviceManager.getDevices());

        for (Device device : allDevices) {
            if (lastVote.getVoteOfDevice(device).getAnswer() == null) {
                boolean received = false;
                Vote blankVote = new Vote();
                blankVote.setCreator(lastVote.getCreator());
                blankVote.setVoteStr(lastVote.getVoteStr());
                received = newVotingService.sendVote(device, blankVote);
                if (!received) {
                    // the vote has been fucked up ! remove device if cant ping
                        devicesToRemove.add(device);
                }
            }
        }

        if (devicesToRemove.size() > 0) {
            allDevices.removeAll(devicesToRemove);
            constructSendRemoveDevices(allDevices, devicesToRemove, lastVote.getVoteStr());
        }
    }

    private void applySetTempToDevicesChecker(Vote lastVote) {
        List<Device> devicesToRemove = new ArrayList<>();
        List<Device> allDevices = new ArrayList<>();
        allDevices.addAll(deviceManager.getDevices());

        for(Device device : allDevices) {
            boolean success = newVotingService.sendFullTempVote(device, lastVote);
            if (!success) {
                devicesToRemove.add(device);
            }
        }
        if(devicesToRemove.size()>1){
            allDevices.removeAll(devicesToRemove);
            constructSendRemoveDevices(allDevices, devicesToRemove, lastVote.getVoteStr());
        }
    }

    private void applyTempChecker(Vote lastVote) throws InterruptedException {
        List<Device> devicesToRemove = new LinkedList<>();
        List<Device> allDevices = new LinkedList<Device>();
        allDevices.addAll(deviceManager.getDevices());

        if (deviceManager.containsAllDevices(lastVote.getDevices())) {
            //apply

            List<Vote> listTempVotesReceived = new ArrayList<>();
            if (listTempVotesReceived.size() != deviceManager.getDevices().size()) {
                while (listTempVotesReceived.size() != deviceManager.getDevices().size()) {

                    for (Device device : allDevices) {
                        Vote result = newVotingService.sendApplyTempVote(device, lastVote);
                        if (result != null) {
                            listTempVotesReceived.add(result);
                        }else{
                            devicesToRemove.add(device);
//                            deviceManager.removeDevices(Arrays.asList(device));
                            // device broke
                        }
                    }
                    if(devicesToRemove.size()>0) {
                        allDevices.removeAll(devicesToRemove);
                        constructSendRemoveDevices(allDevices, devicesToRemove, lastVote.getVoteStr());
                    }
                    Thread.sleep(1000);
                }


                System.out.println("still not time for stage 2");
            }

            devicesToRemove = new LinkedList<>();
            allDevices = new LinkedList<>();
            allDevices.addAll(deviceManager.getDevices());
            while (lastVote.getVotes().stream().filter(e -> e.getAnswer() == null).count() > 0) {
                for (Device device : allDevices) {

                    if (lastVote.getVoteOfDevice(device).getAnswer() == null) {
                        SingleVote singleVote = newVotingService.sendValueRequest(device, lastVote.getVoteStr());
                        if (singleVote == null) {
                            devicesToRemove.add(device);
                        } else{
                            lastVote.getVoteOfDevice(device).setAnswer(singleVote.getAnswer()); // check to not send null !
                        }
                    }
                }

                if(devicesToRemove.size()>0) {
                    allDevices.removeAll(devicesToRemove);
                    constructSendRemoveDevices(allDevices, devicesToRemove, lastVote.getVoteStr());
                }
                Thread.sleep(1000);
            }
        }
    }

    private void applyVoteChecker(Vote lastVote){

        Object calculatedResult = newVotingService.calculateVote(lastVote);
        SingleVote calculatedVote = new SingleVote();
        calculatedVote.setDevice(deviceManager.getCurrentDevice());
        calculatedVote.setAnswer(calculatedResult);

        List<Device> devicesToRemove = new LinkedList<>();
        List<Device> allDevices = new LinkedList<Device>();
        allDevices.addAll(deviceManager.getDevices());

        for (Device device : allDevices) {
            boolean success = newVotingService.sendApplyVote(device, lastVote.getVoteStr(), calculatedVote);
            if(!success) {
                devicesToRemove.add(device);
            }
        }

        if(devicesToRemove.size()>0){
            allDevices.removeAll(devicesToRemove);
            constructSendRevertTempVote(allDevices, lastVote.getVoteStr());
            constructSendRemoveDevices(allDevices, devicesToRemove, lastVote.getVoteStr());
            applyVoteChecker(lastVote);
        }else{
            if (lastVote.getVoteStr().equals("LeaderSelect")) {
                List<Device> result = newVotingService.calculateOrder(lastVote);
                for (Device device : allDevices) {
                    newVotingService.sendApplyPlayOrder(device, result);
                }
            }
        }
    }

    private void constructSendRevertTempVote(List<Device> devicesList, String voteStr) {
        for (Device device : devicesList) {
            newVotingService.sendRevertVote(device, voteStr);
        }
    }


    private void constructSendRemoveDevices(List<Device> allDevices, List<Device> devicesToCheck, String voteStr) {
        List<Device> devicesToRemove = new LinkedList<>();

        for (Device device : devicesToCheck) {
            if (deviceService.discoverDevice(device.getIp()) == null) {
                devicesToRemove.add(device);
                logger.error(device.getIp() + " no longer exists");
            }
        }

        for (Device device : allDevices) {
            VoteDevice voteDevice = new VoteDevice();
            voteDevice.setVoteStr(voteStr);
            voteDevice.addDevices(devicesToRemove);
            deviceService.sendRemoveDevice(device, voteDevice);
        }
    }

}
