package com.example.test.Model.Snake;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;

import com.example.test.Drawable;
import com.example.test.Moveable;

import java.util.List;

public class Snake implements Drawable, Moveable {
    private int length = 3;
    private UniqueCorners corners = new UniqueCorners();
    private Paint paint;
    private int pixelOffset;

    public Snake(int startXPos, int startYPos, int startLength, int pixelOffset) {
        paint = new Paint();
        length = startLength;
        this.pixelOffset = pixelOffset;

        // tail
        corners.push(new Corner(startXPos - startLength, startYPos));

        // head
        corners.push(new Corner(startXPos, startYPos));
    }

    @Override
    public void draw(Canvas canvas) {
        paint.setStrokeWidth(pixelOffset * 0.8f);
        paint.setDither(true);
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);
        canvas.drawLines(corners.toArray(), paint);
    }

    @Override
    public void moveTo(int xpos, int ypos) {
        Corner head = corners.getHead();
        if (xpos != head.getXPos() && ypos != head.getYPos()) {
            throw new IllegalArgumentException("Illegal position");
        }
        corners.push(new Corner(xpos, ypos));
        corners.update(length);
    }

    public boolean isOutOfArea(int top, int bottom, int left, int right) {
        Corner head = corners.getHead();
        return head.getYPos() <= top || head.getYPos() >= bottom || head.getXPos() <= left ||
                head.getXPos() >= right;
    }

    public void grow(int pixelOffset) {
        length += pixelOffset;
    }

    public boolean isAteItself() {
        Corner headSegment1 = corners.getHead();
        Corner headSegment2 = corners.get(1);

        for (int i = 1, j = 2; j < corners.size(); i++, j++) {
            if (lineIntersect(headSegment1, headSegment2, corners.get(i), corners.get(j))) {
                return true;
            }
        }
        return false;
    }

    public Point ateFood(List<Point> food) {
        Corner head = corners.getHead();
        for (Point p : food) {
            if (head.getXPos() == p.x && head.getYPos() == p.y) {
                return p;
            }
        }
        return null;
    }

    private boolean lineIntersect(Corner A1, Corner A2, Corner B1, Corner B2) {
        int x1 = A1.getXPos();
        int y1 = A1.getYPos();

        int x2 = A2.getXPos();
        int y2 = A2.getYPos();

        int x3 = B1.getXPos();
        int y3 = B1.getYPos();

        int x4 = B2.getXPos();
        int y4 = B2.getYPos();
        double denom = (y4 - y3) * (x2 - x1) - (x4 - x3) * (y2 - y1);
        if (denom == 0.0) { // Lines are parallel.
            return false;
        }
        double ua = ((x4 - x3) * (y1 - y3) - (y4 - y3) * (x1 - x3)) / denom;
        double ub = ((x2 - x1) * (y1 - y3) - (y2 - y1) * (x1 - x3)) / denom;
        if (ua >= 0.0f && ua < 1.0f && ub >= 0.0f && ub < 1.0f) {
            // Get the intersection point.
            return true;
        }
        return false;
    }
}