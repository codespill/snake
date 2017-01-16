package com.example.test.Model.Snake;

import java.util.Arrays;

class Corner {
    private int xpos;
    private int ypos;

    public Corner(int xpos, int ypos) {
        this.xpos = xpos;
        this.ypos = ypos;
    }

    public int getXPos() {
        return xpos;
    }

    public int getYPos() {
        return ypos;
    }

    public void setXPos(int xpos) {
        this.xpos = xpos;
    }

    public void setYPos(int ypos) {
        this.ypos = ypos;
    }

    public Object[] toArray() {
        return null;
    }
}
