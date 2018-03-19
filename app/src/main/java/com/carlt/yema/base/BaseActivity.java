package com.carlt.yema.base;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.carlt.yema.control.ActivityControl;

import java.util.ArrayList;

public class BaseActivity extends AppCompatActivity implements
		BeforeGoToBackground {

	/** 标识是否还有页面显示 */
	protected boolean mIsShowing = false;
	/** 进入后台之前 要干的事的集合 */
	protected static ArrayList<BeforeGoToBackground> mBackDoList = new ArrayList<BeforeGoToBackground>();

	protected Context mContext;

	public boolean IsShowing() {
		return mIsShowing;
	}
	/**
	 * 简化按 ID 查找
	 *
	 * @param id
	 * @param <T>
	 * @return
	 */
	public <T> T $ViewByID(int id) {
		return (T) findViewById(id);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		mContext = this;
		ActivityControl.addActivity(this);
		Log.e("info", this.getClass().getName() + "--onCreate");
	}


	@Override
	protected void onDestroy() {
		ActivityControl.removeActivity(this);
		super.onDestroy();
	}

	@Override
	protected void onResume() {
		Log.e("info", this.getClass().getName() + "--onResume");
		mIsShowing = true;
		Resources res = getResources();
		Configuration config = new Configuration();
		config.setToDefaults();
		res.updateConfiguration(config, res.getDisplayMetrics());

		super.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onStop() {
		mIsShowing = false;
		if (!ActivityControl.anyActivtyShowing()) {
			HandleBeforeGoToBackGround();
		}
		super.onStop();
	}



	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		if (ev.getAction() == MotionEvent.ACTION_DOWN) {
			View v = getCurrentFocus();
			if (isShouldHideInput(v, ev)) {

				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				if (imm != null) {
					imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
				}
			}
			return super.dispatchTouchEvent(ev);
		}
		// 必不可少，否则所有的组件都不会有TouchEvent了
		if (getWindow().superDispatchTouchEvent(ev)) {
			return true;
		}
		return onTouchEvent(ev);
	}

	public boolean isShouldHideInput(View v, MotionEvent event) {
		if (v != null && (v instanceof EditText)) {
			int[] leftTop = { 0, 0 };
			// 获取输入框当前的location位置
			v.getLocationInWindow(leftTop);
			int left = leftTop[0];
			int top = leftTop[1];
			int bottom = top + v.getHeight();
			int right = left + v.getWidth();
			if (event.getX() > left && event.getX() < right
					&& event.getY() > top && event.getY() < bottom) {
				// 点击的是输入框区域，保留点击EditText的事件
				return false;
			} else {
				return true;
			}
		}
		return false;
	}

	protected void registerBeforeGoToBackGround(BeforeGoToBackground listener) {
		if (!mBackDoList.contains(listener)) {
			mBackDoList.add(listener);
		}
	}

	protected void unRegisterBeforeGoToBackGround(BeforeGoToBackground listener) {
		mBackDoList.remove(listener);
	}

	public void doBeforeGoToBackground() {
	}

	private static void HandleBeforeGoToBackGround() {
		Log.e("info",
				"baseactivity____doBeforeGoToBackGround()>>>>>>>>>>>>>>>没有页面显示了");
		for (int i = 0; i < mBackDoList.size(); i++) {
			try {
				mBackDoList.get(i).doBeforeGoToBackground();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	/**
	 *标题头按钮点击事件
	 */
	public interface ClickTitleListener {
		void titleLeftClik();//点击最左侧按钮（一般为返回键）
		void titleRightClik();//点击最右侧按钮
	}

}
