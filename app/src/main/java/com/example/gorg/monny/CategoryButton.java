package com.example.gorg.monny;

import android.content.Context;
import android.support.v4.view.GestureDetectorCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.Button;

public class CategoryButton extends Button {
        protected GestureDetectorCompat mDetector;

        public CategoryButton(Context c) {
            super(c);
            initialize();
        }

        public CategoryButton(Context context, AttributeSet attrs) {
            super(context, attrs);
            initialize();
        }

        public CategoryButton(Context context, AttributeSet attrs, int defStyleAttr) {
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
            mDetector = new GestureDetectorCompat(this.getContext(), new CategoryButtonGestureListener(this));
        }
}
