package iCode.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.widget.ScrollView;

public class TolerantScrollView extends ScrollView {

    private int mLastX;
    private int mLastY;

    private int distanceX;
    private int distanceY;

    private int mTouchSlop;

    public TolerantScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public TolerantScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TolerantScrollView(Context context) {
        super(context);
        init();
    }

    private void init(){
        /**
         * define touch slop according to the display
         */
        final ViewConfiguration configuration = ViewConfiguration.get(getContext());
        mTouchSlop = configuration.getScaledTouchSlop();
    }
    /**
     * Processes every touch event through the {@link #onTouchEvent(MotionEvent)}
     * and intercepts if only we have restricted vertical scrolling.
     * <p>
     *
     * {@inheritDoc}
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        onTouchEvent(ev);
        int dy = 0;
        int dx = 0;
        switch(ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                distanceX = 0;
                distanceY = 0;
                mLastX = (int) ev.getX();
                mLastY = (int) ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                dx = Math.abs((int) (mLastX - ev.getX()));
                mLastX = (int) ev.getX();
                distanceX += dx;
                dy = Math.abs((int) (mLastY - ev.getY()));
                mLastY = (int) ev.getY();
                distanceY += dy;
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                // define whether we have vertical scrolling
                // without horizontal one and intercept if so
                if (distanceY > mTouchSlop && distanceX < mTouchSlop){
                    Log.d(VIEW_LOG_TAG, "intercepted");
                    return true;
                }
                distanceX = 0;
                distanceY = 0;
                break;
        }
        return false;
    }
}
