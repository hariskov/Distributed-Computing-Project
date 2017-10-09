package com.dc.controllers;

import com.dc.pojo.Card;
import com.dc.pojo.VotingManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by xumepa on 10/9/17.
 */

@Controller
@RequestMapping(value="card")
public class CardController {

    @Autowired
    VotingManager votingManager;

    @RequestMapping(value="/playCard",method = RequestMethod.POST)
    public void playCard(@RequestBody Card card){
        votingManager.createVote(card.getCardSign());
        System.out.println(card.getCardValue() + " " + card.getCardSign());
    }

}
