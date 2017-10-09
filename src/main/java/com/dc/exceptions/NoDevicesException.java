package com.dc.exceptions;

/**
 * Created by xumepa on 10/3/17.
 */
public class NoDevicesException extends RuntimeException {

    public NoDevicesException(){
        super("DeviceManager is empty");
    }

}
