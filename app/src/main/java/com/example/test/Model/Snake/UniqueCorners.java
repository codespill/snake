package com.example.test.Model.Snake;

import java.util.ArrayList;
import java.util.List;

public class UniqueCorners {
    private List<Corner> corners = new ArrayList<>();

    public void push(Corner corner) {
        corners.add(0, corner);
    }

    public int size() {
        return corners.size();
    }

    public Corner getHead() {
        return corners.get(0);
    }

    public Corner getTail() {
        return corners.get(size() - 1);
    }

    public Corner get(int index) {
        return corners.get(index);
    }

    public float[] toArray() {
        int size = corners.size();
        ArrayList<Float> arr = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            Corner c = corners.get(i);
            if (i > 0 && i < size - 1) {
                arr.add((float) c.getXPos());
                arr.add((float) c.getYPos());
                arr.add((float) c.getXPos());
                arr.add((float) c.getYPos());
            } else {
                arr.add((float) c.getXPos());
                arr.add((float) c.getYPos());
            }
        }

        float[] ret = new float[arr.size()];
        for (int i = 0; i < arr.size(); i++) {
            ret[i] = arr.get(i);
        }
        return ret;
    }

    public void update(int length) {
        int currentLength = 0;
        for (int i = 0, j = 1; j < corners.size(); i++, j++) {
            Corner A = corners.get(i);
            Corner B = corners.get(j);
            int dist = distance(A, B);
            currentLength += dist;
            int diff = length - currentLength;
            if (diff == 0) {
                corners = corners.subList(0, j + 1);
                return;
            } else if (diff < 0) {
                moveCloserToB(B, A, length - currentLength + dist);
                corners = corners.subList(0, j + 1);
                return;
            }
        }
    }

    private int distance(Corner A, Corner B) {
        return (int) Math.sqrt(Math.pow(A.getXPos() - B.getXPos(), 2) + Math.pow(A.getYPos() - B.getYPos(), 2));
    }

    private void moveCloserToB(Corner A, Corner B, int distance) {
        if (A.getXPos() == B.getXPos()) {
            A.setYPos(B.getYPos() + distance * (int) Math.signum(A.getYPos() - B.getYPos()));
        } else if (A.getYPos() == B.getYPos()) {
            A.setXPos(B.getXPos() + distance * (int) Math.signum(A.getXPos() - B.getXPos()));
        }
    }
}
