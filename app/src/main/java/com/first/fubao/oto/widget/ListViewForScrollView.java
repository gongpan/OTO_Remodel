package com.first.fubao.oto.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * @创建者：杨长福
 * @创建时间：2016/1/26
 * @描述：适应ScrollView的效果的ListView
 */
public class ListViewForScrollView extends ListView {
	public ListViewForScrollView(Context context) {
		super(context);
	}

	public ListViewForScrollView(Context context, AttributeSet attrs,
								 int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	public ListViewForScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	/**
	 * 重写该方法，达到使GridView适应ScrollView的效果
	 */
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
				MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expandSpec);
	}
}
