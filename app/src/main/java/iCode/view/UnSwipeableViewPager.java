package iCode.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;


public class UnSwipeableViewPager extends ViewPager {


    public UnSwipeableViewPager(Context context) {
        super(context);
    }

    public UnSwipeableViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return false;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        return false;
    }
}