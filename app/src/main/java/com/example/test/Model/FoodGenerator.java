package com.example.test.Model;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;

import com.example.test.Drawable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class FoodGenerator implements Drawable {
    private List<Point> food = new ArrayList<>();
    private Paint paint;
    private int pixelOffset;

    public FoodGenerator(int pixelOffset) {
        paint = new Paint();
        this.pixelOffset = pixelOffset;
    }

    public void generateInsideBoundary(int bottom, int right) {
        Random rand = new Random();
        int x = rand.nextInt(right) * pixelOffset;
        int y = rand.nextInt(bottom) * pixelOffset;
        Point p = new Point(x, y);
        food.add(p);
    }

    @Override
    public void draw(Canvas canvas) {
        paint.setColor(Color.BLACK);
        for (Point p : food) {
            canvas.drawCircle(p.x, p.y, pixelOffset * 0.5f, paint);
        }
    }

    public List<Point> getFood() {
        return food;
    }

    public void empty() {
        food.clear();
    }

    public void removeFood() {
        food.remove(0);
    }

    public void removeFood(Point p) {
        food.remove(p);
    }
}
