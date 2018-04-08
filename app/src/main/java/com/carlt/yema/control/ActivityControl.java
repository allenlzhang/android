package com.carlt.yema.control;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.InputType;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.ImageView;

import com.carlt.yema.R;
import com.carlt.yema.YemaApplication;
import com.carlt.yema.base.BaseActivity;
import com.carlt.yema.data.UseInfo;
import com.carlt.yema.preference.TokenInfo;
import com.carlt.yema.preference.UseInfoLocal;
import com.carlt.yema.ui.activity.login.UserLoginActivity;
import com.carlt.yema.ui.view.PopBoxCreat;
import com.carlt.yema.ui.view.PopBoxCreat.DialogWithTitleClick;

import java.util.ArrayList;
import java.util.List;

public class ActivityControl {

	private static List<Activity> mActivityList = new ArrayList<Activity>();

	public static void addActivity(Activity activity) {
		if (null != activity) {
			mActivityList.add(activity);
		}
	}

	public static boolean anyActivtyShowing() {
		for (int i = 0; i < mActivityList.size(); i++) {
			BaseActivity ba = (BaseActivity) mActivityList.get(i);
			if (ba != null && !ba.isFinishing() && ba.IsShowing()) {
				return true;
			}
		}
		return false;
	}

	public static void removeActivity(Activity activity) {
		if (null != activity) {
			mActivityList.remove(activity);
		}
	}

	public static Activity getTopActivity() {
		if (mActivityList != null) {
			int size = mActivityList.size();
			return mActivityList.get(size - 1);
		}
		return null;
	}

	/**
	 * 信鸽注册（该方法在登录成功后调用）
	 */
	public static void initXG() {

//		Context mContext = YemaApplication.getInstanse();
//		// 新建自定义样式
//		XGBasicPushNotificationBuilder build = new XGBasicPushNotificationBuilder();
//		// 设置自定义样式属性，该属性对对应的编号生效，指定后不能修改。
//		build.setIcon(R.drawable.ic_launcher);
//		// 设置声音
//		build.setSound(RingtoneManager.getActualDefaultRingtoneUri(mContext,
//				RingtoneManager.TYPE_ALARM));
//		// 振动
//		build.setDefaults(Notification.DEFAULT_VIBRATE);
//		// 是否可清除
//		build.setFlags(Notification.FLAG_AUTO_CANCEL);
//
//		XGPushConfig.enableDebug(mContext, true);
//		Log.e("info", "userId====" + LoginInfo.getUseId());
//		if (YemaApplication.Formal_Version) {
//			XGPushManager.registerPush(mContext, LoginInfo.getUseId());
//			// 设置通知样式，样式编号为2，即build_id为2，可通过后台脚本指定
//			XGPushManager.setPushNotificationBuilder(mContext, 2, build);
//			XGPushManager.setTag(mContext, LoginInfo.getDealerId());
//			if (LoginInfo.getPush_prizeinfo_flag() == 1) {
//				XGPushManager.setTag(mContext, LoginInfo.getDealerId() + "_31");
//			}
//		} else {
//			switch (URLConfig.flag) {
//			case URLConfig.VERSION_FORMAL:
//				// 正式服
//				XGPushManager.registerPush(mContext, LoginInfo.getUseId());
//				XGPushManager.setPushNotificationBuilder(mContext, 2, build);
//				XGPushManager.setTag(mContext, LoginInfo.getDealerId());
//				if (LoginInfo.getPush_prizeinfo_flag() == 1) {
//					XGPushManager.setTag(mContext, LoginInfo.getDealerId()
//							+ "_31");
//				}
//				break;
//			case URLConfig.VERSION_PREPARE:
//				// 预发布服
//				XGPushManager.registerPush(mContext, LoginInfo.getUseId());
//				XGPushManager.setPushNotificationBuilder(mContext, 2, build);
//				XGPushManager.setTag(mContext, LoginInfo.getDealerId());
//				if (LoginInfo.getPush_prizeinfo_flag() == 1) {
//					XGPushManager.setTag(mContext, LoginInfo.getDealerId()
//							+ "_31");
//				}
//				break;
//			case URLConfig.VERSION_TEST:
//				// 测试服
//				XGPushManager.registerPush(mContext,
//						"t_" + LoginInfo.getUseId());
//				// XGPushManager.registerPush(this, "t_" +
//				// LoginInfo.getToken());
//				Log.e("info", "LoginInfo.getToken()==" + LoginInfo.getToken());
//
//				XGPushManager.setPushNotificationBuilder(mContext, 2, build);
//				XGPushManager.setTag(mContext, "t_" + LoginInfo.getDealerId());
//				if (LoginInfo.getPush_prizeinfo_flag() == 1) {
//					XGPushManager.setTag(mContext,
//							"t_" + LoginInfo.getDealerId() + "_31");
//				}
//				break;
//			}
//		}
//		Intent service = new Intent(mContext, XGPushServiceV3.class);
//		mContext.startService(service);
	}

	public static void exit(Context context) {


	}

	public static void logout(final Context context) {
		DialogWithTitleClick click = new DialogWithTitleClick() {

			@Override
			public void onRightClick() {
				// 取消

			}

			@Override
			public void onLeftClick() {
				// 注销
				onLogout(context);
			}
		};
		PopBoxCreat.createDialogWithTitle(context, "提示", "是否要注销?", "", "确定",
				"取消", click);

	}


	// 注销操作
	public static void onLogout(Context context) {
		onExit();
		// XGPushManager.unregisterPush(context);//使用反注册会出现Bug,只要调用后台的注销信鸽token的接口就行了。v1.4.0版本把这一行注释掉
		UseInfo mUseInfo = UseInfoLocal.getUseInfo();
		mUseInfo.setPassword("");
		UseInfoLocal.setUseInfo(mUseInfo);
		TokenInfo.setToken("");
		Intent mIntent = new Intent(context, UserLoginActivity.class);
		mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(mIntent);

	}

	/******** 退出操作 ****************/
	public static void onExit() {
		if (!TextUtils.isEmpty(TokenInfo.getToken())) {
//			CPControl.GetUnRigisterXgTokenResult(YemaApplication.NIMEI,
//					new GetResultListCallback() {
//						@Override
//						public void onFinished(Object o) {
//							LoginInfo.Destroy();
//							TokenInfo.setToken("");
//							Log.e("info", "注销信鸽成功");
//						}
//
//						@Override
//						public void onErro(Object o) {
//							LoginInfo.Destroy();
//							TokenInfo.setToken("");
//							Log.e("info", "注销信鸽失败");
//						}
//					});
		}
		int size = mActivityList.size();
		for (int i = 0; i < size; i++) {
			if (null != mActivityList.get(i)) {
				mActivityList.get(i).finish();
			}
		}
		mActivityList.clear();
	}

	// 授权后逻辑
	public static void bePushAside(Context context) {
		int size = mActivityList.size();
		for (int i = 0; i < size; i++) {
			if (null != mActivityList.get(i)) {
				mActivityList.get(i).finish();
			}
		}
		mActivityList.clear();
		UseInfo mUseInfo = UseInfoLocal.getUseInfo();
		mUseInfo.setPassword("");
		UseInfoLocal.setUseInfo(mUseInfo);
		TokenInfo.setToken("");
		Intent mIntent = new Intent(context, UserLoginActivity.class);
		mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(mIntent);
	}

	public static void onTokenDisable() {
		for (Activity activity : mActivityList) {
			if (activity instanceof UserLoginActivity) {
				BaseActivity base = (BaseActivity) activity;
				if (base.IsShowing()) {
					return;
				}
			}
		}
		Intent mIntent = new Intent(YemaApplication.getInstanse(),
				UserLoginActivity.class);
		// Intent mIntent = new Intent(getApplicationContext(),
		// LoginActivity.class);
		mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		YemaApplication.getInstanse().startActivity(mIntent);
	}
	public static void onStopService(final Context context) {

	}

	public static void passwdToggle(Context context,EditText input, ImageView toggleView,String tag){
		if (!TextUtils.isEmpty(tag)) {
			if (tag.equals("on")) {
				input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
				toggleView.setImageDrawable(context.getResources().getDrawable(R.mipmap.passwd_off, null));
				toggleView.setTag("off");
			} else {
				input.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
				toggleView.setImageDrawable(context.getResources().getDrawable(R.mipmap.passwd_on, null));
				toggleView.setTag("on");
			}
		}
	}
}
