package iCode.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

public class SqureLinearLayout extends LinearLayout {

    public SqureLinearLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public SqureLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SqureLinearLayout(Context context) {
        super(context);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec,widthMeasureSpec);
    }
}
