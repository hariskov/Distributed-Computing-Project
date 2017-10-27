package com.dc.interceptors;

import com.dc.pojo.Device;
import com.dc.pojo.DeviceManager;
import com.dc.pojo.GameManager;
import com.dc.services.NewVotingService;
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

    @Autowired
    NewVotingService newVotingService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        if(gameManager.getCurrentPlayer()!=null) {
            gameManager.setCurrentPlayer(gameManager.getNextPlayer());
        }

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        logger.info("NewRoundInterceptor - Post Handle");

        if (gameManager.getPlayingOrder() != null) {
//            if(gameManager.getCurrentPlayer()!=null) {
//                if (gameManager.getCurrentPlayer().equals(deviceManager.getCurrentDevice())){
//                    newVotingService.sendStartVote("getCurrentPlayer");
//                    Device currentPlayer = (Device) newVotingService.getDecidedVote("getCurrentPlayer").getAnswer();
//                    gameManager.setCurrentPlayer(currentPlayer);
//                }
//            }else if(gameManager.getPlayingOrder().get(0).equals(deviceManager.getCurrentDevice())){
//                newVotingService.sendStartVote("getCurrentPlayer");
//                Device currentPlayer = (Device) newVotingService.getDecidedVote("getCurrentPlayer").getAnswer();
//                gameManager.setCurrentPlayer(currentPlayer);
//            }
            Device currentPlayer = null;

            if(gameManager.getPlayingOrder().get(0).equals(deviceManager.getCurrentDevice())){
                newVotingService.sendStartVote("getCurrentPlayer");
            }

            while (currentPlayer == null) {
                currentPlayer = (Device) newVotingService.getDecidedVote("getCurrentPlayer").getAnswer();

                if(currentPlayer!=null) {
                    gameManager.setCurrentPlayer(currentPlayer);
                }

                Thread.sleep(1000);
            }
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        logger.info("NewRoundInterceptor - after Completion");

    }
}
