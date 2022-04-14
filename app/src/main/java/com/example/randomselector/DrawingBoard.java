package com.example.randomselector;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.View;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class DrawingBoard extends View {

    private Paint backgroundPaint;
    private RandomColors colors;

    private SparseArray<Circle> activeCircles;

    private Handler timingHandler;

    public DrawingBoard(Context context) {
        super(context);
        this.init();
    }

    private void init() {
        setBackgroundPaint();
        activeCircles = new SparseArray<>();
        timingHandler = new Handler();
        colors = new RandomColors();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawPaint(backgroundPaint);

        for(int size = activeCircles.size(), i = 0; i < size; i++) {
            Circle currentCircle = activeCircles.valueAt(i);
            if (currentCircle != null) {
                currentCircle.draw(canvas);
            }
        }

        invalidate();
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(isFingerDown(event)) {
            createNewCircleAroundFinger(event);
            setTimer();
        }
        else if (isFingerUp(event)) {
            deleteCorrespondingCircle(event);
            setTimer();
            if (lastFingerWasLifted(event)) {
                timingHandler.removeCallbacks(selectWinner);
            }
        }
        else if (isFingerMovement(event)) {
            updateCirclePositions(event);
        }

        return true;
    }

    private boolean lastFingerWasLifted(MotionEvent event) {
        return event.getActionMasked() == MotionEvent.ACTION_UP;
    }

    private boolean isFingerUp(MotionEvent event) {
        int actionMasked = event.getActionMasked();
        return actionMasked == MotionEvent.ACTION_CANCEL || actionMasked == MotionEvent.ACTION_UP || actionMasked == MotionEvent.ACTION_POINTER_UP;
    }

    private boolean isFingerDown(MotionEvent event) {
        int actionMasked = event.getActionMasked();
        return actionMasked == MotionEvent.ACTION_DOWN || actionMasked == MotionEvent.ACTION_POINTER_DOWN;
    }

    private boolean isFingerMovement(MotionEvent event) {
        int actionMasked = event.getActionMasked();
        return actionMasked == MotionEvent.ACTION_MOVE;
    }

    private void createNewCircleAroundFinger(MotionEvent event) {
        int pointerIndex = event.getActionIndex();
        int pointerId = event.getPointerId(pointerIndex);

        Circle newCircle = new Circle((int) event.getX(pointerIndex), (int) event.getY(pointerIndex), 200, colors.getColor());
        activeCircles.put(pointerId, newCircle);
    }

    private void deleteCorrespondingCircle(MotionEvent event) {
        int pointerIndex = event.getActionIndex();
        int pointerId = event.getPointerId(pointerIndex);
        activeCircles.remove(pointerId);
    }

    private void updateCirclePositions(MotionEvent event) {
        for (int size = event.getPointerCount(), i = 0; i < size; i++) {
            Circle circle = activeCircles.get(event.getPointerId(i));
            if (circle != null) {
                circle.x = (int)event.getX(i);
                circle.y = (int)event.getY(i);
            }
        }
    }

    private void setTimer() {
        timingHandler.removeCallbacks(selectWinner);
        timingHandler.postDelayed(selectWinner, 3000);
    }

    private void stopTimer() {
        timingHandler.removeCallbacks(selectWinner);
    }

    private Runnable selectWinner = new Runnable() {
        public void run() {
            int rnd = new Random().nextInt(activeCircles.size());

            System.out.println("Size: "+ activeCircles.size() + ", Random: " +rnd);

            Circle winningCircle = activeCircles.get(rnd);
            activeCircles.clear();
            activeCircles.put(rnd, winningCircle);
        }
    };

    private void setBackgroundPaint() {
        backgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        backgroundPaint.setColor(Color.WHITE);
        backgroundPaint.setStyle(Paint.Style.FILL);
    }
}
