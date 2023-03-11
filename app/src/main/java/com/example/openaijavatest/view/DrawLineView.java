package com.example.openaijavatest.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.annotations.NonNull;

public class DrawLineView extends View {

    private final Paint mPaint;
    private float startX;
    private float startY;
    private float endX;
    private float endY;

    private List<Float> mPositionX = new ArrayList<>();
    private List<Float> mPositionY = new ArrayList<>();

    private List<Path> mHistoryPath = new ArrayList<>();

    public DrawLineView(Context context) {
        this(context, null);
    }

    public DrawLineView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(Color.BLACK);
    }

    @Override protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (mPositionX.size() > 0 && mPositionY.size() > 0) {

            float startX = mPositionX.get(0);
            float startY = mPositionY.get(0);

            int limit = Math.min(mPositionX.size(), mPositionY.size());
            Path tempPath = new Path();
            tempPath.rewind();
            tempPath.moveTo(startX, startY);

            for (int i = 0; i < limit - 1; i++) {
                //canvas.drawLine(mPositionX.get(i), mPositionY.get(i), mPositionX.get(i+1), mPositionY.get(i+1), mPaint);
                tempPath.lineTo(mPositionX.get(i), mPositionY.get(i));
                getHistoryPath().add(tempPath);
            }
        }

        if (getHistoryPath().size() > 0) {
            for (Path path : getHistoryPath()) {
                canvas.drawPath(path, mPaint);
            }
        }

        mPositionX.clear();
        mPositionY.clear();
    }

    @Override
    public boolean onTouchEvent(@NonNull MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE: {
                mPositionX.add(event.getX());
                mPositionY.add(event.getY());
                break;
            }
            case MotionEvent.ACTION_UP:
                invalidate();
                break;
        }
        return true;
    }

    public List<Path> getHistoryPath() {
        return mHistoryPath;
    }

    public void clearHistoryPath() {
        mHistoryPath.clear();
    }
}
