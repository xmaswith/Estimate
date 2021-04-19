package com.pri.estimate.Model;

public class SetNumber {
    private int person;
    private int foc;
    private int guide;

    public SetNumber(int person, int foc, int guide) {
        this.person = person;
        this.foc = foc;
        this.guide = guide;
    }

    public SetNumber() {
    }

    public int getPerson() {
        return person;
    }

    public void setPerson(int person) {
        this.person = person;
    }

    public int getFoc() {
        return foc;
    }

    public void setFoc(int foc) {
        this.foc = foc;
    }

    public int getGuide() {
        return guide;
    }

    public void setGuide(int guide) {
        this.guide = guide;
    }
}
