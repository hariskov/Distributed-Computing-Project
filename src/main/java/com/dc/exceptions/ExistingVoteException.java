package com.dc.exceptions;

/**
 * Created by xumepa on 10/3/17.
 */
public class ExistingVoteException extends RuntimeException {

    public ExistingVoteException(){
        super(" Vote Already Exists, make sure to set unique name for the vote in question");
    }

}
