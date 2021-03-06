package com.example.gorg.monny;

import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.Button;

public class ButtonChangeCatsGestureListener implements
        GestureDetector.OnGestureListener, GestureDetector.OnDoubleTapListener {

    protected Button button;

    public ButtonChangeCatsGestureListener(Button b){
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
        VarStorage.secondActivity.handleTransferCategory();
        return true;
    }

    @Override
    public void onLongPress(MotionEvent event) {
        VarStorage.secondActivity.handleAuthorOverride();
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
        performSingleTapAction();
        return true;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent event) {
        return true;
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent event) {
        performSingleTapAction();
        return true;
    }

    private void performSingleTapAction() {
        VarStorage.secondActivity.switchCategoryButtons();
    }
}
