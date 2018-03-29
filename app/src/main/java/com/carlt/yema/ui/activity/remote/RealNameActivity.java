package com.carlt.yema.ui.activity.remote;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.carlt.yema.R;
import com.carlt.yema.base.BaseActivity;
import com.carlt.yema.control.CPControl;
import com.carlt.yema.data.BaseResponseInfo;
import com.carlt.yema.model.LoginInfo;
import com.carlt.yema.ui.view.PopBoxCreat;
import com.carlt.yema.ui.view.UUToast;
import com.carlt.yema.ui.view.ValidateEditText;


/**
 * 实名认证
 * 
 * @author Administrator
 */
public class RealNameActivity extends BaseActivity implements OnClickListener {
	private ImageView back;// 头部返回键

	private TextView title;// 标题文字

	private ValidateEditText mEdtName;// 姓名

	private ValidateEditText mEdtIDCard;// 身份证号

	private TextView mTxtSubtitle;// 小标题

	private TextView mBtnOption;// 操作按钮（"下一步","提交"）

	private TextView mTxtUserInfo;// 用户信息

	private View mViewAutherTodo;// 未实名认证

	private View mViewAuthered;// 已实名认证

	public final static String TYPE = "type";

	public final static int TYPE_REMOTE = 0;// 从远程跳转过来

	public final static int TYPE_AUTHERED = 1;// 从安全中心跳转过来-已认证

	public final static int TYPE_UNAUTHER = 2;// 从安全中心跳转过来-未认证

	public final static int TYPE_RESETROMOTEPSW = 3;// 从重置远程密码-未认证

	private int type;

	private Dialog mDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_realname);

		try {
			type = getIntent().getIntExtra(TYPE, -1);
		} catch (Exception e) {

		}

		initTitle();
		init();
	}

	private void initTitle() {
		back = (ImageView) findViewById(R.id.head_back_img1);
		title = (TextView) findViewById(R.id.head_back_txt1);

		title.setText("安全认证");

		back.setImageResource(R.drawable.arrow_back);
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

	private void init() {
		mEdtName = (ValidateEditText) findViewById(R.id.realname_edt_name);
		mEdtIDCard = (ValidateEditText) findViewById(R.id.realname_edt_IDCard);

		mEdtName.setEditHint("请输入姓名");
		mEdtName.setNextEditText(mEdtIDCard);
		mEdtIDCard.setmType(ValidateEditText.TYPE_IDN);
		mEdtIDCard.setEditHint("请输入身份证号码");

		mTxtSubtitle = (TextView) findViewById(R.id.realname_txt_title);
		mBtnOption = (TextView) findViewById(R.id.realname_txt_option);
		mTxtUserInfo = (TextView) findViewById(R.id.realname_txt_userinfo);

		mViewAutherTodo = findViewById(R.id.realname_lay_authertodo);
		mViewAuthered = findViewById(R.id.realname_lay_authered);

		switch (type) {
		case TYPE_REMOTE:
			// 远程界面跳转过来-未认证
			mViewAutherTodo.setVisibility(View.VISIBLE);
			mViewAuthered.setVisibility(View.GONE);
			mBtnOption.setText("下一步");
			PopBoxCreat.createDialogNobtn(RealNameActivity.this,
					"温馨提示：您所输入的安全信息将用于找回密码等重要安全功能，请如实填写", "");
			break;

		case TYPE_UNAUTHER:
			// 安全设置界面跳转过来-未认证
			mViewAutherTodo.setVisibility(View.VISIBLE);
			mViewAuthered.setVisibility(View.GONE);
			mBtnOption.setText("提交");
			PopBoxCreat.createDialogNobtn(RealNameActivity.this,
					"温馨提示：您所输入的安全信息将用于找回密码等重要安全功能，请如实填写", "");
			break;

		case TYPE_AUTHERED:
			// 安全设置界面跳转过来-已认证
			mViewAutherTodo.setVisibility(View.GONE);
			mViewAuthered.setVisibility(View.VISIBLE);
			StringBuffer mBuffer = new StringBuffer();
			mBuffer.append(LoginInfo.getAuthen_name());
			mBuffer.append(" ");

			String idCard = LoginInfo.getAuthen_card();
			if (idCard != null && idCard.length() > 0) {
				mBuffer.append(idCard);
			}
			mTxtUserInfo.setText(mBuffer.toString());
			break;
		}

		mTxtSubtitle.setText("为了保证远程操作的安全性，请先进行安全认证");
		mBtnOption.setOnClickListener(this);
	}

	/**
	 * 安全认证
	 */
	private CPControl.GetResultListCallback listener_realname = new CPControl.GetResultListCallback() {

		@Override
		public void onFinished(Object o) {
			Message msg = new Message();
			msg.what = 0;
			msg.obj = o;
			mHandler.sendMessage(msg);
		}

		@Override
		public void onErro(Object o) {
			Message msg = new Message();
			msg.what = 1;
			msg.obj = o;
			mHandler.sendMessage(msg);
		}
	};

	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				// 安全认证成功--跳转至设置远程密码
				if (mDialog != null && mDialog.isShowing()) {
					mDialog.dismiss();
				}
				BaseResponseInfo mInfo = (BaseResponseInfo) msg.obj;
				if (mInfo != null) {
					String info = mInfo.getInfo();
					if (info != null && info.length() > 0) {
						UUToast.showUUToast(RealNameActivity.this, info);
					} else {
						UUToast.showUUToast(RealNameActivity.this, "实名认证成功！");
					}
				} else {
					UUToast.showUUToast(RealNameActivity.this, "实名认证成功！");
				}
				if (type == TYPE_REMOTE) {
					Intent mIntent = new Intent(RealNameActivity.this,
							RemotePswResetActivity3.class);
					// 写成RemotePswResetActivity3.TYPE_REMOTE 因为源头是远程页面
					mIntent.putExtra(RemotePswResetActivity3.TYPE,
							RemotePswResetActivity3.TYPE_REMOTE);
					startActivity(mIntent);
					finish();
				} else {
					finish();
				}

				break;

			case 1:
				// 安全认证失败
				if (mDialog != null && mDialog.isShowing()) {
					mDialog.dismiss();
				}
				BaseResponseInfo mInfo1 = (BaseResponseInfo) msg.obj;
				if (mInfo1 != null) {
					String info = mInfo1.getInfo();
					if (info != null && info.length() > 0) {
						UUToast.showUUToast(RealNameActivity.this, info);
					} else {
						UUToast.showUUToast(RealNameActivity.this, "实名认证失败...");
					}
				} else {
					UUToast.showUUToast(RealNameActivity.this, "实名认证失败...");
				}

				break;
			}
		}

	};

	@Override
	public void onClick(View v) {
		String authen_name = mEdtName.getText().toString();
		String authen_card = mEdtIDCard.getText().toString();
		switch (type) {
		case TYPE_REMOTE:
			// 远程界面跳转过来-未认证
			// 调用认证接口
			if (authen_name == null || authen_name.length() <= 0) {
				UUToast.showUUToast(RealNameActivity.this, "您还没有填写您的真实姓名哦...");
				return;
			} else if (authen_card == null || authen_card.length() <= 0) {
				UUToast.showUUToast(RealNameActivity.this, "您还没有填写您的身份证号码哦...");
				return;
			} else {
				if (mDialog == null) {
					mDialog = PopBoxCreat.createDialogWithProgress(
							RealNameActivity.this, "实名认证中...");
				}
				mDialog.show();
				CPControl.GetRealNameResult(authen_name, authen_card, listener_realname);
			}
			break;

		case TYPE_UNAUTHER:
			// 安全设置界面跳转过来-未认证
			// 调用认证接口
			if (authen_name == null || authen_name.length() <= 0) {
				UUToast.showUUToast(RealNameActivity.this, "您还没有填写您的真实姓名哦...");
				return;
			} else if (authen_card == null || authen_card.length() <= 0) {
				UUToast.showUUToast(RealNameActivity.this, "您还没有填写您的身份证号码哦...");
				return;
			} else {
				if (mDialog == null) {
					mDialog = PopBoxCreat.createDialogWithProgress(
							RealNameActivity.this, "实名认证中...");
				}
				mDialog.show();
				CPControl.GetRealNameResult(authen_name, authen_card,
						listener_realname);
			}
			break;
		}
	}
}
