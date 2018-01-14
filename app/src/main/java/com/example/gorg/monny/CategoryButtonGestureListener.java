package com.example.gorg.monny;

import android.support.v7.app.AppCompatActivity;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.Button;

public class CategoryButtonGestureListener implements
        GestureDetector.OnGestureListener, GestureDetector.OnDoubleTapListener {

    protected Button button;

    public CategoryButtonGestureListener(Button b){
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
        return true;
    }

    @Override
    public void onLongPress(MotionEvent event) {
        SecondActivity secActivity =  VarStorage.secondActivity;
        secActivity.setSecret(true);
        sendSetCategory(secActivity);
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
        return true;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent event) {
        return true;
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent event) {
        SecondActivity secActivity =  VarStorage.secondActivity;
        secActivity.setSecret(false);
        sendSetCategory(secActivity);
        return true;
    }

    private void sendSetCategory(SecondActivity activity) {
        activity.setCategory(button.getText().toString());
        activity.updateGoBackButton();
    }

}
