package com.example.randomselector;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class Circle {
    int x, y;
    int radius;
    Paint outerRingColor, innerRingColor;

    public Circle(int x, int y, int r, int color) {
        this.x = x;
        this.y = y;
        this.radius = r;

        outerRingColor = new Paint();
        outerRingColor.setStyle(Paint.Style.STROKE);
        outerRingColor.setStrokeWidth(5);
        outerRingColor.setColor(color);

        innerRingColor = new Paint();
        innerRingColor.setStyle(Paint.Style.FILL_AND_STROKE);
        innerRingColor.setColor(color);
    }

    public void draw(Canvas canvas) {
        canvas.drawCircle(x, y, radius, outerRingColor);
        canvas.drawCircle(x, y, 125, innerRingColor);
    }


}
