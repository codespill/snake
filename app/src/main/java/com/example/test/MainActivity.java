package com.example.test;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.example.test.Model.Controls;
import com.example.test.Model.FoodGenerator;
import com.example.test.Model.Snake.Snake;
import com.example.test.Scene.Grid;

public class MainActivity extends Activity {
    // gameView will be the view of the game
    // It will also hold the logic of the game
    // and respond to screen touches as well
    GameView gameView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        // Initialize gameView and set it as the view
        gameView = new GameView(this);
        setContentView(gameView);

    }

    // GameView class will go here

    // Here is our implementation of GameView
    // It is an inner class.
    // Note how the final closing curly brace }
    // is inside SimpleGameEngine

    // Notice we implement runnable so we have
    // A thread and can override the run method.
    class GameView extends SurfaceView implements Runnable {

        // This is our thread
        Thread gameThread = null;

        // This is new. We need a SurfaceHolder
        // When we use Paint and Canvas in a thread
        // We will see it in action in the draw method soon.
        SurfaceHolder ourHolder;

        // A boolean which we will set and unset
        // when the game is running- or not.
        volatile boolean playing;
        boolean run = true;

        // A Canvas and a Paint object
        Canvas canvas;
        Paint paint;

        Grid backGrid;
        int backGridXPos;
        int backGridYPos;

        FoodGenerator foodGenerator;
        Controls controls;
        Snake snake;

        int boardWidth = 40;
        int boardHeight = 40;
        int pixelOffset;

        // This variable tracks the game frame rate
        long fps;

        private long score = 0;

        // This is used to help calculate the fps
        private long timeThisFrame;

        Event direction = Event.RIGHT;

        float walkStepPerSecond = pixelOffset;
        float walkSpeedPerSecond = 5.8f;

        float snakeXPosition = 20 * pixelOffset;
        float snakeXGridPosition = snakeXPosition;

        float snakeYPosition = 20 * pixelOffset;
        float snakeYGridPosition = snakeYPosition;

        float controlsXPosition;
        float controlsYPosition;

        int screenWidth;
        int screenHeight;

        // When the we initialize (call new()) on gameView
        // This special constructor method runs
        public GameView(Context context) {
            // The next line of code asks the
            // SurfaceView class to set up our object.
            // How kind.
            super(context);

            Point size = new Point();
            Display display = getWindowManager().getDefaultDisplay();
            display.getSize(size);
            screenWidth = size.x;
            screenHeight = size.y;
            pixelOffset = (int)(0.8f * (screenHeight < screenWidth ? screenHeight / boardHeight : screenWidth / boardWidth));

            walkStepPerSecond = pixelOffset;
            walkSpeedPerSecond = 5.8f;

            snakeXPosition = 20 * pixelOffset;
            snakeXGridPosition = snakeXPosition;

            snakeYPosition = 20 * pixelOffset;
            snakeYGridPosition = snakeYPosition;

            backGrid = new Grid(boardWidth, boardHeight, pixelOffset);
            controls = new Controls(this);
            snake = new Snake(20 * pixelOffset, 20 * pixelOffset, 3 * pixelOffset, pixelOffset);
            foodGenerator = new FoodGenerator(pixelOffset);
            foodGenerator.generateInsideBoundary((boardHeight - 1), (boardWidth - 1));

            // Initialize ourHolder and paint objects
            ourHolder = getHolder();
            paint = new Paint();

            controlsXPosition = screenWidth / 2 - controls.getDimensions()[0] / 2;
            controlsYPosition = screenHeight - controls.getDimensions()[1] - 100;

            backGridXPos = (screenWidth - boardWidth * pixelOffset) / 2;
            backGridYPos = (screenHeight - boardHeight * pixelOffset) / 4;

            // Set our boolean to true - game on!
            playing = true;
        }

        @Override
        public void run() {
            while (playing) {

                // Capture the current time in milliseconds in startFrameTime
                long startFrameTime = System.currentTimeMillis();

                // Update the frame
                update();

                // Draw the frame
                draw();

                // Calculate the fps this frame
                // We can then use the result to
                // time animations and more.
                timeThisFrame = System.currentTimeMillis() - startFrameTime;
                if (timeThisFrame > 0) {
                    fps = 1000 / timeThisFrame;
                }

            }

        }

        // Everything that needs to be updated goes in here
        // In later projects we will have dozens (arrays) of objects.
        // We will also do other things like collision detection.
        public void update() {
            if (fps == 0) {
                return;
            }

            if (snake.isOutOfArea(-1, (boardHeight) * pixelOffset, -1, (boardWidth) * pixelOffset)) {
                run = false;
                return;
            }

            if (snake.isAteItself()) {
                run = false;
                return;
            }

            switch (direction) {
                case UP:
                    snakeYPosition = snakeYPosition - (walkStepPerSecond / fps) * walkSpeedPerSecond;
                    snakeYGridPosition = (int) (snakeYPosition / pixelOffset) * pixelOffset;
                    break;
                case DOWN:
                    snakeYPosition = snakeYPosition + (walkStepPerSecond / fps) * walkSpeedPerSecond;
                    snakeYGridPosition = (int) (snakeYPosition / pixelOffset) * pixelOffset;
                    break;
                case LEFT:
                    snakeXPosition = snakeXPosition - (walkStepPerSecond / fps) * walkSpeedPerSecond;
                    snakeXGridPosition = (int) (snakeXPosition / pixelOffset) * pixelOffset;
                    break;
                case RIGHT:
                    snakeXPosition = snakeXPosition + (walkStepPerSecond / fps) * walkSpeedPerSecond;
                    snakeXGridPosition = (int) (snakeXPosition / pixelOffset) * pixelOffset;
                    break;
            }

            snake.moveTo((int) snakeXGridPosition, (int) snakeYGridPosition);

            Point p = snake.ateFood(foodGenerator.getFood());
            if (p != null) {
                foodGenerator.removeFood(p);
                switch (direction) {
                    case UP:
                        snakeYGridPosition -= pixelOffset;
                        break;
                    case DOWN:
                        snakeYGridPosition += pixelOffset;
                        break;
                    case LEFT:
                        snakeXGridPosition -= pixelOffset;
                        break;
                    case RIGHT:
                        snakeXGridPosition += pixelOffset;
                        break;
                }
                snake.grow(pixelOffset);
                walkSpeedPerSecond += 0.2f;
                foodGenerator.generateInsideBoundary((boardHeight - 1), (boardWidth - 1));
                score++;
            }
        }

        public boolean isAllowed(Enum newKey) {
            if (newKey == Event.UP && direction == Event.DOWN) {
                return false;
            }
            if (newKey == Event.DOWN && direction == Event.UP) {
                return false;
            }
            if (newKey == Event.LEFT && direction == Event.RIGHT) {
                return false;
            }
            if (newKey == Event.RIGHT && direction == Event.LEFT) {
                return false;
            }
            return true;
        }

        // Draw the newly updated scene
        public void draw() {

            // Make sure our drawing surface is valid or we crash
            if (ourHolder.getSurface().isValid()) {
                // Lock the canvas ready to draw
                canvas = ourHolder.lockCanvas();

                // Draw the background color
                canvas.drawColor(Color.argb(255, 0, 128, 0));

                canvas.save();
                canvas.translate(backGridXPos, backGridYPos);
                backGrid.draw(canvas);
                canvas.restore();

                canvas.save();
                canvas.translate(controlsXPosition, controlsYPosition);
                controls.draw(canvas);
                canvas.restore();

                canvas.save();
                canvas.translate(backGridXPos + pixelOffset / 2, backGridYPos + pixelOffset / 2);
                snake.draw(canvas);
                canvas.restore();

                canvas.save();
                canvas.translate(backGridXPos + pixelOffset / 2, backGridYPos + pixelOffset / 2);
                foodGenerator.draw(canvas);
                canvas.restore();

                // Choose the brush color for drawing
                paint.setColor(Color.argb(255, 249, 129, 0));

                // Make the text a bit bigger
                paint.setTextSize(pixelOffset * 2);

                // Display the current fps on the screen
                canvas.drawText("FPS: " + fps, 20, 40, paint);

                // Display points
                canvas.drawText("Score: " + score, screenWidth / 2, 40, paint);

                // Draw everything to the screen
                ourHolder.unlockCanvasAndPost(canvas);
            }

        }

        // If SimpleGameEngine Activity is paused/stopped
        // shutdown our thread.
        public void pause() {
            playing = false;
            try {
                gameThread.join();
            } catch (InterruptedException e) {
                Log.e("Error:", "joining thread");
            }

        }

        // If SimpleGameEngine Activity is started then
        // start our thread.
        public void resume() {
            playing = true;
            gameThread = new Thread(this);
            gameThread.start();
        }

        // The SurfaceView class implements onTouchListener
        // So we can override this method and detect screen touches.
        @Override
        public boolean onTouchEvent(MotionEvent motionEvent) {

            switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {

                // Player has touched the screen
                case MotionEvent.ACTION_DOWN:
                    if (!run) {
                        run = true;
                        score = 0;
                        snake = new Snake(20 * pixelOffset, 20 * pixelOffset, 3 * pixelOffset, pixelOffset);
                        direction = Event.RIGHT;

                        walkStepPerSecond = pixelOffset;
                        walkSpeedPerSecond = 5.8f;

                        snakeXPosition = 20 * pixelOffset;
                        snakeXGridPosition = snakeXPosition;

                        snakeYPosition = 20 * pixelOffset;
                        snakeYGridPosition = snakeYPosition;
                        foodGenerator.empty();
                        foodGenerator.generateInsideBoundary((boardHeight - 1), (boardWidth - 1));
                        return true;
                    }
                    Event key = controls.touched(motionEvent.getX() - controlsXPosition, motionEvent.getY() - controlsYPosition);
                    if (!isAllowed(key)) {
                        break;
                    }
                    switch (key) {
                        case UP:
                            direction = Event.UP;
                            break;
                        case DOWN:
                            direction = Event.DOWN;
                            break;
                        case LEFT:
                            direction = Event.LEFT;
                            break;
                        case RIGHT:
                            direction = Event.RIGHT;
                            break;
                    }
                    break;
            }
            return true;
        }

    }
    // This is the end of our GameView inner class

    // More SimpleGameEngine methods will go here

    // This method executes when the player starts the game
    @Override
    protected void onResume() {
        super.onResume();

        // Tell the gameView resume method to execute
        gameView.resume();
    }

    // This method executes when the player quits the game
    @Override
    protected void onPause() {
        super.onPause();

        // Tell the gameView pause method to execute
        gameView.pause();
    }

}