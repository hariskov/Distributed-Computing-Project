package com.dc.interceptors;

import com.dc.exceptions.NoDevicesException;
import com.dc.pojo.*;
import com.dc.pojo.combos.VoteDevice;
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
import java.util.Arrays;
import java.util.LinkedList;
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

        Vote lastVote = null;

        while(lastVote == null) {
            lastVote = votingManager.getLastTempVote();
            if(lastVote==null) {
                Thread.sleep(1000);
            }
        }

//        if (!deviceManager.containsAllDevices(lastVote.getDevices())) {
//            while (!deviceManager.containsAllDevices(lastVote.getDevices())) {
//                break;
//            }
//        }

        deviceChecker(lastVote);
        sendBlankTempChecker(lastVote);

        deviceChecker(lastVote);
        applyTempChecker(lastVote);

        // calculate votes
        deviceChecker(lastVote);
        applyVoteChecker(lastVote);
        // lets say it worked

        }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
//        manager.putVote(voteType, device, result);
    }

    private void deviceChecker(Vote lastVote){
        List<Device> devicesToRemove = new LinkedList<>();
        List<Device> allDevices = new ArrayList<>();
        allDevices.addAll(deviceManager.getDevices());

        for(Device device : deviceManager.getDevices()){
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

        for (Device device : deviceManager.getDevices()) {
            if (lastVote.getVoteOfDevice(device) == null) {
                boolean received = false;
                Vote blankVote = new Vote();
                blankVote.setCreator(lastVote.getCreator());
                blankVote.setVoteStr(lastVote.getVoteStr());
                received = newVotingService.sendVote(device, blankVote);
                if (!received) {
                    // the vote has been fucked up ! remove device if cant ping
//                    if (deviceService.discoverDevice(device.getIp()) == null) {
                        devicesToRemove.add(device);
//                    }
                }
            }
//            Thread.sleep(1000);
        }

        if (devicesToRemove.size() > 0) {
            allDevices.removeAll(devicesToRemove);
            constructSendRemoveDevices(allDevices, devicesToRemove, lastVote.getVoteStr());
        }
    }

    private void constructSendRemoveDevices(List<Device> allDevices, List<Device> devicesToRemove, String voteStr) {
        for (Device device : allDevices) {
            if (deviceService.discoverDevice(device.getIp()) == null) {
                logger.error(device.getIp() + " no longer exists");

                VoteDevice voteDevice = new VoteDevice();
                voteDevice.setVoteStr(voteStr);
                voteDevice.addDevices(devicesToRemove);
                deviceService.sendRemoveDevice(device, voteDevice);
            }
        }
    }

    private void applyTempChecker(Vote lastVote) throws InterruptedException {
        List<Device> devicesToRemove = new LinkedList<>();
        List<Device> devicesList = new LinkedList<Device>();
        devicesList.addAll(deviceManager.getDevices());

        if (deviceManager.containsAllDevices(lastVote.getDevices())) {
            //apply

            List<Vote> listTempVotesReceived = new ArrayList<>();
            if (listTempVotesReceived.size() != deviceManager.getDevices().size()) {
                while (listTempVotesReceived.size() != deviceManager.getDevices().size()) {

                    for (Device device : devicesList) {
                        Vote result = newVotingService.sendApplyTempVote(device, lastVote);
                        if (result != null) {
                            listTempVotesReceived.add(result);
                        }else{
                            devicesToRemove.add(device);
//                            deviceManager.removeDevices(Arrays.asList(device));
                            // device broke
                        }
                    }
                    Thread.sleep(1000);
                }

                if(devicesToRemove.size()>0) {
                    constructSendRemoveDevices(devicesList, devicesToRemove, lastVote.getVoteStr());
                }
                System.out.println("still not time for stage 2");
            }

            devicesToRemove = new LinkedList<>();
            devicesList = new LinkedList<>();
            devicesList.addAll(deviceManager.getDevices());
            while (lastVote.getVotes().stream().filter(e -> e.getAnswer() == null).count() > 0) {
                for (Device device : devicesList) {

                    if (lastVote.getVoteOfDevice(device).getAnswer() == null) {
                        SingleVote singleVote = newVotingService.sendValueRequest(device, lastVote.getVoteStr());
                        if(singleVote == null){
                            devicesToRemove.add(singleVote.getDevice());
                        }
                        lastVote.getVoteOfDevice(device).setAnswer(singleVote.getAnswer()); // check to not send null !
                    }
                }

                if(devicesToRemove.size()>0) {
                    constructSendRemoveDevices(devicesList, devicesToRemove, lastVote.getVoteStr());
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
        List<Device> devicesList = new LinkedList<Device>();
        devicesList.addAll(deviceManager.getDevices());

        for (Device device : devicesList) {
            boolean success = newVotingService.sendApplyVote(device, lastVote.getVoteStr(), calculatedVote);
            if(!success) {
                devicesToRemove.add(device);
            }
        }

        if(devicesToRemove.size()>0){
            constructSendRevertTempVote(devicesList, lastVote.getVoteStr());
            constructSendRemoveDevices(devicesList, devicesToRemove, lastVote.getVoteStr());
            applyVoteChecker(lastVote);
        }else{
            if (lastVote.getVoteStr().equals("LeaderSelect")) {
                List<Device> result = newVotingService.calculateOrder(lastVote);
                for (Device device : devicesList) {
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
}
