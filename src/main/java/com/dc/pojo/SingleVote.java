package com.dc.pojo;

/**
 * Created by xumepa on 10/21/17.
 */
public class SingleVote {
    private Device device;
    private Object answer;

    public SingleVote(){

    }

    public Device getDevice() {
        return device;
    }

    public void setDevice(Device device) {
        this.device = device;
    }

    public Object getAnswer() {
        return answer;
    }

    public void setAnswer(Object answer) {
        this.answer = answer;
    }
}
