package com.dc.interceptors;

import com.dc.pojo.Device;
import com.dc.pojo.DeviceManager;
import com.dc.pojo.GameManager;
import com.dc.services.NewVotingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        // now we have a vote
//        deviceManager.getCurrentDevice()
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

        // calculate ->

        // call next round
        for(Device device : deviceManager.getDevices()) {
            newVotingService.sendApplyPlayOrder(device, gameManager.getPlayingOrder());
        }
    }
}
