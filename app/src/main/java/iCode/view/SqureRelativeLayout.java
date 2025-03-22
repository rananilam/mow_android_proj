package iCode.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

public class SqureRelativeLayout extends RelativeLayout {

    public SqureRelativeLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public SqureRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SqureRelativeLayout(Context context) {
        super(context);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int width = getMeasuredWidth();
        setMeasuredDimension(width, (int) ((width*53.9)/100));
    }
}
