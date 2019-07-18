package com.example.mycareer.model;

public class Settings {
    private int totCredits;
    private int laudeValue;

    public Settings(){
        this.totCredits = -1;
        this.laudeValue = 30;
    }

    public Settings(int totCredits, int laudeValue) {
        this.totCredits = totCredits;
        this.laudeValue = laudeValue;
    }

    public int getTotCredits() {
        return totCredits;
    }

    public void setTotCredits(int totCredits) {
        this.totCredits = totCredits;
    }

    public int getLaudeValue() {
        return laudeValue;
    }

    public void setLaudeValue(int laudeValue) {
        this.laudeValue = laudeValue;
    }
}
