package com.example.gorg.monny;

import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.Button;

public class Button_1000GestureListener implements
        GestureDetector.OnGestureListener, GestureDetector.OnDoubleTapListener {

    protected Button button;

    public Button_1000GestureListener(Button b){
        super();
        button = b;
    }

    @Override
    public boolean onDown(MotionEvent event) {
        return true;
    }

    @Override
    public boolean onFling(MotionEvent event1, MotionEvent event2,
                           float velocityX, float velocityY) {
        String str = button.getText().toString();
        int num = Integer.parseInt(str);
        num *= 10;
        VarStorage.mainActivity.addToCounter(num);
        return true;
    }

    @Override
    public void onLongPress(MotionEvent event) {
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
                            float distanceY) {
        return true;
    }

    @Override
    public void onShowPress(MotionEvent event) {
    }

    @Override
    public boolean onSingleTapUp(MotionEvent event) {
        return true;
    }

    @Override
    public boolean onDoubleTap(MotionEvent event) {
        onSingleTapConfirmed(event);
        return true;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent event) {
        return true;
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent event) {
        String str = button.getText().toString();
        int num = Integer.parseInt(str);
        VarStorage.mainActivity.addToCounter(num);
        return true;
    }
}
