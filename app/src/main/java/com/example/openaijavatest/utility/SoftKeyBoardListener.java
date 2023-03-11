package com.example.openaijavatest.utility;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;

import java.lang.reflect.Method;

public class SoftKeyBoardListener implements ViewTreeObserver.OnGlobalLayoutListener {
    private View rootView;
    int rootViewVisibleHeight = -1;
    private OnSoftKeyBoardChangeListener onSoftKeyBoardChangeListener;

    public SoftKeyBoardListener(Activity activity) {
        rootView = activity.getWindow().getDecorView();
        rootView.getViewTreeObserver().addOnGlobalLayoutListener(this);
    }

    private void setOnSoftKeyBoardChangeListener(OnSoftKeyBoardChangeListener onSoftKeyBoardChangeListener) {
        this.onSoftKeyBoardChangeListener = onSoftKeyBoardChangeListener;
    }

    @Override
    public void onGlobalLayout() {

        int visibleHeight = getKeyboardHeight(rootView.getContext());

        if (rootViewVisibleHeight == visibleHeight) {
            return;
        }

        if (rootViewVisibleHeight != visibleHeight) {
            rootViewVisibleHeight = visibleHeight;
        }



        if (rootViewVisibleHeight > 0) {
            if (onSoftKeyBoardChangeListener != null) {
                onSoftKeyBoardChangeListener.keyBoardShow(rootViewVisibleHeight);
            }
        } else {
            if (onSoftKeyBoardChangeListener != null) {
                onSoftKeyBoardChangeListener.keyBoardHide(rootViewVisibleHeight);
            }
        }

    }

    public int getKeyboardHeight(Context context){
        try {
            InputMethodManager im = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
            Method method = im.getClass().getDeclaredMethod("getInputMethodWindowVisibleHeight");
            method.setAccessible(true);
            Object height = method.invoke(im);
            return Integer.parseInt(height.toString());
        }catch (Throwable e){
            return -1;
        }
    }

    public interface OnSoftKeyBoardChangeListener {
        void keyBoardShow(int height);

        void keyBoardHide(int height);

    }

    public void setListener(OnSoftKeyBoardChangeListener onSoftKeyBoardChangeListener) {
        setOnSoftKeyBoardChangeListener(onSoftKeyBoardChangeListener);
    }

    public void destroy() {
        try {
            rootView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
