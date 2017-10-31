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
 * Created by xumepa on 10/9/17.
 */
public class CardInterceptor implements HandlerInterceptor {

    @Autowired
    DeviceManager deviceManager;

    @Autowired
    NewVotingService newVotingService;

    @Autowired
    GameManager gameManager;

    @Autowired
    GameService gameService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        // now we have a vote
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {


        // calculate ->




        // call next round
        if(deviceManager.getDevices().containsAll((gameManager.getPlayingOrder()))){
            gameManager.setTurn(gameManager.getTurn() + 1);
            Device player = gameManager.getNextPlayer();

            for (Device device : deviceManager.getDevices()) {
                gameService.sendNextPlayer(device, player);
            }
        }else {
            // remove device from order
            List<Device> devicesToRemoveFromOrder = new LinkedList<>();

            gameManager.getPlayingOrder().stream().filter(e->!deviceManager.getDevices().contains(e)).forEachOrdered(devicesToRemoveFromOrder::add);

            gameManager.getPlayingOrder().removeAll(devicesToRemoveFromOrder);
            for (Device device : deviceManager.getDevices()) {
                newVotingService.sendApplyPlayOrder(device,gameManager.getPlayingOrder());
            }
        }
    }
}
