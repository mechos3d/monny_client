package com.example.gorg.monny;

import android.content.Context;
import android.content.Intent;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.Button;

public class ButtonExtendedGestureListener implements
        GestureDetector.OnGestureListener, GestureDetector.OnDoubleTapListener {

    protected Button button;

    public ButtonExtendedGestureListener(Button b){
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
        Context activity = button.getContext();
        Intent intent = new Intent(activity, ThirdActivity.class);
        activity.startActivity(intent);
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
        VarStorage.current_sum = 0;
        changeCounterOnScreen();
        return true;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent event) {
        return true;
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent event) {
        VarStorage.change_current_sign();
        changeCounterOnScreen();
        return true;
    }

    private void changeCounterOnScreen(){
        int sum = VarStorage.current_sum;
        String sign = VarStorage.current_sign;
        button.setText(sign + " " + sum);
    }
}
