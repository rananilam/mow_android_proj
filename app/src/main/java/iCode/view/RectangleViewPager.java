package iCode.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.viewpager.widget.ViewPager;

public class RectangleViewPager extends ViewPager {


    public RectangleViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RectangleViewPager(Context context) {
        super(context);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int width = getMeasuredWidth();
        setMeasuredDimension(width, (int) ((width*50.0)/100));
    }
}
