package iCode.view

import android.content.Context
import android.util.AttributeSet
import androidx.core.view.children
import androidx.viewpager.widget.ViewPager
//import kotlinx.android.synthetic.main.fragment_mow_device_list.view.*

class WrapContentViewPager(context: Context, attrs: AttributeSet): ViewPager(context, attrs) {

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val zeroHeight = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED)

        val maxHeight = children
            .map { it.measure(widthMeasureSpec, zeroHeight); it.measuredHeight }
            .max() ?: 0

        if (maxHeight > 0) {
            //val maxHeightSpec = MeasureSpec.makeMeasureSpec(maxHeight, MeasureSpec.EXACTLY) + tabLayout.height
            val maxHeightSpec = MeasureSpec.makeMeasureSpec(maxHeight, MeasureSpec.EXACTLY)
            super.onMeasure(widthMeasureSpec, maxHeightSpec)
            return
        }

        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }
}