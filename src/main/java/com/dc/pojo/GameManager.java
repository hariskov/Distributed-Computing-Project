package com.dc.pojo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.core.MessageSendingOperations;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by xumepa on 10/23/17.
 */

@Service
public class GameManager {
    private List<Device> playingOrder;
    private Device currentPlayer;
    private int turn = 0;

    @Autowired
    MessageSendingOperations<String> messageSendingOperations;

    @Autowired
    DeviceManager deviceManager;

    public void setPlayingOrder(List<Device> playingOrder) {
        this.playingOrder = playingOrder;
    }

    public List<Device> getPlayingOrder() {
        return playingOrder;
    }

    public Device getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(Device currentPlayer) {
        this.currentPlayer = currentPlayer;
        sendQuotes(deviceManager.getCurrentDevice().equals(getCurrentPlayer()));

    }

    public Device getNextPlayer(){
        if(getPlayingOrder().indexOf(currentPlayer)==getPlayingOrder().size()-1){
            return getPlayingOrder().get(0);
        }
        return getPlayingOrder().get(getPlayingOrder().indexOf(currentPlayer)+1);
    }

    @MessageMapping(value="/hello")
    public void sendQuotes(boolean myTurn){
        String destination = "/broker/turn";
        this.messageSendingOperations.convertAndSend(destination, myTurn);
    }

    public Boolean doesGameExist() {
        return playingOrder != null;
    }

    public int getTurn() {
        return turn;
    }

    public void setTurn(int i){
        this.turn = i;
    }

    public void addPlayer(Device device) {
        this.playingOrder.add(device);
    }
}
