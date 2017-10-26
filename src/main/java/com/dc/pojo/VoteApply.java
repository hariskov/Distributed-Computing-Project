package com.dc.pojo;

/**
 * Created by xumepa on 10/26/17.
 */


/**
 * useless stupid stuff -> just used to transfer complex objects for PostRequests
 */
public class VoteApply {
    private SingleVote calcVote;
    private String voteStr;

    public VoteApply(){

    }

    public void setVoteStr(String voteStr) {
        this.voteStr = voteStr;
    }

    public void setCalcVote(SingleVote calcVote) {
        this.calcVote = calcVote;
    }

    public SingleVote getCalcVote() {
        return calcVote;
    }

    public String getVoteStr() {
        return voteStr;
    }
}
