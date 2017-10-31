package com.dc.interceptors;

import com.dc.pojo.Device;
import com.dc.pojo.DeviceManager;
import com.dc.pojo.GameManager;
import com.dc.services.GameService;
import com.dc.services.NewVotingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by xumepa on 10/3/17.
 */
public class DeviceCheckerInterceptor implements HandlerInterceptor {

    @Autowired
    DeviceManager deviceManager;

    @Autowired
    NewVotingService newVotingService;

    @Autowired
    GameService gameService;

    @Autowired
    GameManager gameManager;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        boolean gameAlreadyExists = false;
        if(deviceManager.getDevices().size()>1) {
            for (Device device : deviceManager.getDevices()) {
                if(newVotingService.sendCheckIfGameExists(device)){
                    gameAlreadyExists=true;
                    break;
                }
            }
        }

        if(!gameAlreadyExists) {
            deviceManager.getDevices().forEach(e -> deviceManager.syncDevices(e));
            newVotingService.sendStartVote("LeaderSelect");
        }else{
            Boolean freeToJoin = false;
            for(Device dev : deviceManager.getDevices()) {
                if(!dev.equals(deviceManager.getCurrentDevice())) {
                    freeToJoin = newVotingService.sendJoinRequest(dev);
                    if(freeToJoin){
                        gameManager.setPlayingOrder(gameService.getPlayingOrder(dev));
                        List<Device> devices = new LinkedList<Device>();
                        devices.addAll(deviceManager.getDevices());
                        devices.forEach(e -> deviceManager.syncDevices(e));
                        if(gameManager.getPlayingOrder()!=null) {
                            for (Device device : deviceManager.getDevices()) {
                                gameService.sendAddPlayer(device, deviceManager.getCurrentDevice());
                            }
                        }
                    }
                    break;
                }
            }
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        //start leader selection process after devices have discovered and synced.
        //votingManager.createVote("Leader");

//        String uri = "http://" + deviceManager.getCurrentDevice().getIp() + ":8080/project/voting/startVote";
//
//        try {
//              restTemplate.postForEntity(uri, "Leader", Object.class);
//        }catch(Exception e){
//            e.printStackTrace();
//        }
    }
}
