package com.dc.pojo;

/**
 * Created by xumepa on 10/21/17.
 */
public class SingleVote {
    private Device device;
    private Object answer;
    private String question;
    private int sequence = 1;

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


    @Override
    public boolean equals(Object o){
        if(o instanceof SingleVote){
            if(((SingleVote) o).getDevice().equals(this.getDevice())){
                return true;
            }
            return false;
        }
        return false;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public int getSequence() {
        return sequence;
    }

    public void setSequence(int sequence) {
        this.sequence = sequence;
    }
}
