package com.example.gorg.monny;

import android.content.Context;
import android.support.v4.view.GestureDetectorCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.Button;

public class Button_1000 extends Button {

        protected GestureDetectorCompat mDetector;

        public Button_1000(Context context) {
            super(context);
            initialize();
        }

        public Button_1000(Context context, AttributeSet attrs) {
            super(context, attrs);
            initialize();
        }

        public Button_1000(Context context, AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
            initialize();
        }

        protected void initialize() {
            setGestureDetector();
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            mDetector.onTouchEvent(event);
            // Be sure to call the superclass implementation
            return super.onTouchEvent(event);
        }

        protected void setGestureDetector() {
            mDetector = new GestureDetectorCompat(this.getContext(), new Button_1000GestureListener(this));
        }
}
