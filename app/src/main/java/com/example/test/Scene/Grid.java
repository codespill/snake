package com.example.test.Scene;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.example.test.Drawable;
import com.example.test.Measurable;

public class Grid implements Drawable, Measurable {
    private int width = 100;
    private int height = 100;
    private int pixelOffset = 50;
    private int[] size = new int[2];
    private Paint paint;

    public Grid(int width, int height, int pixelOffset) {
        this.width = width;
        this.height = height;
        this.pixelOffset = pixelOffset;
        this.paint = new Paint();
        size[0] = width;
        size[1] = height;
    }

    public void draw(Canvas canvas) {
//        paint.setColor(Color.argb(255, 0, 0, 0));
//        paint.setStrokeWidth(5f);
//        for (int i = 0; i <= this.width; i++) {
//            canvas.drawLine(i * pixelOffset, 0, i * pixelOffset, height * pixelOffset, paint);
//        }
//        for (int j = 0; j <= this.height; j++) {
//            canvas.drawLine(0, j * pixelOffset, width * pixelOffset, j * pixelOffset, paint);
//        }
        paint.setDither(true);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(pixelOffset);
        canvas.drawLine(0, -pixelOffset / 2, width * pixelOffset, -pixelOffset / 2, paint);
        canvas.drawLine(0, (height + 0.5f) * pixelOffset, width * pixelOffset, (height + 0.5f) * pixelOffset, paint);
        canvas.drawLine(-pixelOffset / 2, -pixelOffset / 2, -pixelOffset / 2, (height + 0.5f) * pixelOffset, paint);
        canvas.drawLine((width + 0.5f) * pixelOffset, -pixelOffset / 2, (width + 0.5f) * pixelOffset, (height + 0.5f) * pixelOffset, paint);
    }

    @Override
    public int[] getDimensions() {
        return size;
    }
}
