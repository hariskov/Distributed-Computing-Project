package com.dc.interceptors;

import com.dc.pojo.Device;
import com.dc.pojo.DeviceManager;
import com.dc.pojo.GameManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by xumepa on 10/25/17.
 */
public class NewRoundInterceptor implements HandlerInterceptor {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    DeviceManager deviceManager;

    @Autowired
    GameManager gameManager;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        if(gameManager.getCurrentPlayer()!=null) {
            gameManager.setCurrentPlayer(gameManager.getNextPlayer());
        }


        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        logger.info("NewRoundInterceptor - after Completion");
        if (gameManager.getPlayingOrder() != null) {
            gameManager.setCurrentPlayer(gameManager.getPlayingOrder().get(0));
            gameManager.sendQuotes(deviceManager.getCurrentDevice().equals(gameManager.getCurrentPlayer()));
        }
        if(gameManager.getPlayingOrder().get(0).equals(deviceManager.getCurrentDevice())){
            /// your turn to play,
        }
    }
}
