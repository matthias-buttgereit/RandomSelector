package com.example.randomselector;

import android.view.animation.Animation;
import android.view.animation.Transformation;

public class CircleAnimation extends Animation {
    private DrawingBoard drawingBoard;


    public CircleAnimation(DrawingBoard drawingBoard) {
        this.drawingBoard = drawingBoard;
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation transformation) {

    }
}
