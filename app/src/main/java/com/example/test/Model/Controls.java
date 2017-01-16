package com.example.test.Model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

import com.example.test.Drawable;
import com.example.test.Event;
import com.example.test.Measurable;
import com.example.test.R;
import com.example.test.Touchable;

public class Controls implements Drawable, Touchable, Measurable {
    private Bitmap bitmapArrows;
    private Paint paint;
    private int[] size = new int[2];

    private float upArrowULXPos;
    private float upArrowULYPos;
    private float upArrowLRXPos;
    private float upArrowLRYPos;

    private float leftArrowULXPos;
    private float leftArrowULYPos;
    private float leftArrowLRXPos;
    private float leftArrowLRYPos;

    private float rightArrowULXPos;
    private float rightArrowULYPos;
    private float rightArrowLRXPos;
    private float rightArrowLRYPos;

    private float downArrowULXPos;
    private float downArrowULYPos;
    private float downArrowLRXPos;
    private float downArrowLRYPos;

    public Controls(View view) {
        bitmapArrows = BitmapFactory.decodeResource(view.getResources(), R.drawable.arrows);
        paint = new Paint();

        int arrowsWidth = size[0] = bitmapArrows.getWidth();
        int arrowsHeight = size[1] = bitmapArrows.getHeight();

        upArrowULXPos = arrowsWidth / 3;
        upArrowULYPos = 0;
        upArrowLRXPos = 2 * arrowsWidth / 3;
        upArrowLRYPos = arrowsHeight / 2;

        downArrowULXPos = arrowsWidth / 3;
        downArrowULYPos = arrowsHeight / 2;
        downArrowLRXPos = 2 * arrowsWidth / 3;
        downArrowLRYPos = arrowsHeight;

        leftArrowULXPos = 0;
        leftArrowULYPos = arrowsHeight / 2;
        leftArrowLRXPos = arrowsWidth / 3;
        leftArrowLRYPos = arrowsHeight;

        rightArrowULXPos = 2 * arrowsWidth / 3;
        rightArrowULYPos = arrowsHeight / 2;
        rightArrowLRXPos = arrowsWidth;
        rightArrowLRYPos = arrowsHeight;
    }

    @Override
    public void draw(Canvas canvas) {
        paint.setColor(Color.argb(255, 0, 0, 0));
        canvas.drawBitmap(bitmapArrows, 0, 0, paint);
    }

    @Override
    public int[] getDimensions() {
        return size;
    }

    @Override
    public Event touched(float xpos, float ypos) {
        if (xpos >= upArrowULXPos && xpos <= upArrowLRXPos && ypos >= upArrowULYPos && ypos <= upArrowLRYPos) {
            return Event.UP;
        }
        if (xpos >= downArrowULXPos && xpos <= downArrowLRXPos && ypos >= downArrowULYPos && ypos <= downArrowLRYPos) {
            return Event.DOWN;
        }
        if (xpos >= leftArrowULXPos && xpos <= leftArrowLRXPos && ypos >= leftArrowULYPos && ypos <= leftArrowLRYPos) {
            return Event.LEFT;
        }
        if (xpos >= rightArrowULXPos && xpos <= rightArrowLRXPos && ypos >= rightArrowULYPos && ypos <= rightArrowLRYPos) {
            return Event.RIGHT;
        }
        return Event.NONE;
    }
}
