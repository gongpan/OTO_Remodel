package com.first.fubao.oto.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * @类名：	NoScrollViewPager
 * @创建者：	杨长福
 * @创建时间：2015-02-01
 * @描述：	不能滑动的ViewPager
 */
public class NoScrollViewPager extends LazyViewPager {

	public NoScrollViewPager(Context context) {
		super(context);
	}

	public NoScrollViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	/**
	 * ViewPager有可能拦截了touch事件，所以复写拦截的方法比较保险。
	 */
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		// 不拦截
		return false;
	}
	
	/**
	 * 复写ViewPager的触摸事件，不让ViewPager消费touch事件，那么ViewPager就不能滑动。
	 */
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		//不消费
		return false;
	}
}
