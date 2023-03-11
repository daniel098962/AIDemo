package com.example.openaijavatest.data.model;

import android.view.MotionEvent;
import android.view.View;

import com.example.openaijavatest.view.DrawLineView;

public class DrawLineModel {

    private DrawLineView mDrawLineView;

    private MotionEvent mMotionEvent;

    public DrawLineModel(DrawLineView drawLineView, MotionEvent motionEvent) {
        mDrawLineView = drawLineView;
        mMotionEvent = motionEvent;
    }

    public DrawLineView getDrawLineView() {
        return mDrawLineView;
    }

    public void setDrawLineView(DrawLineView drawLineView) {
        mDrawLineView = drawLineView;
    }

    public MotionEvent getMotionEvent() {
        return mMotionEvent;
    }

    public void setMotionEvent(MotionEvent motionEvent) {
        mMotionEvent = motionEvent;
    }
}
