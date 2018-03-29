package com.carlt.yema.control;


import com.carlt.yema.YemaApplication;
import com.carlt.yema.data.login.UserRegisterParams;
import com.carlt.yema.data.remote.AirMainInfo;
import com.carlt.yema.data.set.ModifyCarInfo;
import com.carlt.yema.protocolparser.BaseParser;
import com.carlt.yema.protocolparser.DefaultParser;
import com.carlt.yema.protocolparser.DefaultStringParser;
import com.carlt.yema.protocolparser.DeviceUpdateInfoParser;
import com.carlt.yema.protocolparser.car.CarModeInfoListV2Parser;
import com.carlt.yema.protocolparser.car.CarModeInfoListV3Parser;
import com.carlt.yema.protocolparser.login.UserRegisterParser;
import com.carlt.yema.systemconfig.URLConfig;
import com.carlt.yema.utils.CreatPostString;

import java.util.HashMap;

public class CPControl {
    public static void GetRemoteAir(GetResultListCallback mListener, String s, String s1) {
    }

    public static void GetRemoteStart(GetResultListCallback mListener) {
    }

	public static void GetCancelRemoteStart(GetResultListCallback mListener) {
	}

	public static void GetCarLocating(GetResultListCallback mListener) {
	}

	public static void GetRemoteTrunk(GetResultListCallback mListener) {
	}

	public static void GetRemoteLock(String s, GetResultListCallback mListener) {
	}

	public static void GetRemoteClosewin(GetResultListCallback mListener) {
	}

	public static void GetRemoteOpenwin(GetResultListCallback mListener) {
	}

	public static void GetRemoteSkylight(String s, GetResultListCallback mListener) {
	}

	public static void GetRemoteCarTemp(GetResultListCallback mListener_temp, AirMainInfo mAirMainInfo1) {
	}

	public static void GetRemoteCarState(GetResultListCallback mListener_states, String deviceType) {
	}

	public static void GetRemotePswVerify(String password, GetResultListCallback mListener_verify) {
	}

	public static void GetRealNameResult(String authen_name, String authen_card, GetResultListCallback listener_realname) {
	}

	public static void GetSetRemotePwdResult(String pswNew1, GetResultListCallback listener_set) {
	}

	public static void GetForgetRemotePwdResult(String name, String idcard, String mobile, String pswNew1, String validate, GetResultListCallback listener_forget) {
	}


	/**
	 * 注册接口 onFinished表示成功
	 */
	public static void GetRegisteResult(final UserRegisterParams mRegisteParams,
										final BaseParser.ResultCallback listener) {

		if (listener == null)
			return;

		// 链接地址
		// String url = URLConfig.getM_REGISTER_URL();
		String url = URLConfig.getM_REGISTER_NEW_URL();
		// Post参数
		UserRegisterParser mUserInfoParser = new UserRegisterParser(listener);
		HashMap<String, String> mMap = new HashMap<String, String>();

		mMap.put("mobile", mRegisteParams.getMobile());
		mMap.put("password", mRegisteParams.getPassword());
		mMap.put("validate", mRegisteParams.getValidate());
		mMap.put("move_deviceid", YemaApplication.NIMEI);
		mMap.put("move_device_name", YemaApplication.MODEL_NAME);
		mMap.put("originate", mRegisteParams.getOriginate());
		mUserInfoParser
				.executePost(url, mMap);
	}

	public static void GetMessageValidateResult(final String type,
												final String phoneNum, final BaseParser.ResultCallback listener) {
		GetValidateResult(type, phoneNum, "0", listener);
	}

	private static void GetValidateResult(final String type,
										  final String phoneNum, final String voiceVerify,
										  final BaseParser.ResultCallback listener) {

		if (listener == null)
			return;
		// 链接地址
		// String url = URLConfig.getM_VALIDATE_URL();
		String url = URLConfig.getM_VALIDATE_NEW_URL();
		// Post参数
		String post = CreatPostString.getValidate(type, phoneNum,
				voiceVerify);
		HashMap<String, String> params = new HashMap<>();
		params.put("mobile", phoneNum);
		params.put("type", "1");
		params.put("voiceVerify", voiceVerify);
		DefaultStringParser mParser = new DefaultStringParser(listener);
		mParser.executePost(url, params);

	}
	/**
	 * 修改车辆车型信息 调用成功执行onFinished() 无返回值
	 */

	public static void GetUpdateCarTypeResult(
			final ModifyCarInfo modifyCarInfo,
			final BaseParser.ResultCallback listener) {

		if (listener == null)
			return;

		String brandid = modifyCarInfo.getBrandid();
		String optionid = modifyCarInfo.getOptionid();
		String carid = modifyCarInfo.getCarid();
		// 链接地址
		String url = URLConfig.getM_SWITCHCAR_URL();
		// Post参数
		HashMap<String, String> mMap = new HashMap<String, String>();
		mMap.put("brandid", brandid);
		mMap.put("optionid", optionid);
		mMap.put("carid", carid);

		DefaultParser mParser = new DefaultParser(listener,ModifyCarInfo.class);
		mParser.executePost(url, mMap);

	}
	/**
	 * 获取野驴车型列表(针对获取车系级别列表-众泰专用) onFinished返还<CarModeInfo>
	 */
	public static void GetDealerModelListV1Result(final String id,
												  final BaseParser.ResultCallback listener) {

		if (listener == null)
			return;

		// 链接地址
		String url = URLConfig.getM_OPTIONLIST_URL();
		// Post参数
		HashMap<String, String> map = new HashMap<>();
		map.put("brandid","id");
		CarModeInfoListV2Parser mParser = new CarModeInfoListV2Parser(listener);
		mParser.executePost(url, map);
	}
	/**
	 * 获取经销商车型列表(针对获取车款级别列表) onFinished返还<CarModeInfo>
	 *
	 * @param id        车型ID
	 * @param is_before 1：前装 0：后装
	 */
	public static void GetDealerModelListV2Result(final String id,
												  final String is_before, final BaseParser.ResultCallback listener) {

		if (listener == null)
			return;

		// 链接地址
		String url = URLConfig.getM_CARLIST_URL();
		// Post参数
		HashMap<String, String> mMap = new HashMap<String, String>();
		mMap.put("optionid", id);
		mMap.put("is_before", is_before);

		CarModeInfoListV3Parser mParser = new CarModeInfoListV3Parser(listener);
		mParser.executePost(url, mMap);
	}

	/**
	 * 获取设备升级状态 onFinished表示成功
	 */
	public static void GetDeviceUpdateResult(final String deviceid,
											 final BaseParser.ResultCallback listener) {

		if (listener == null)
			return;

		// 链接地址
		String url = URLConfig.getM_DEVICEUPDATE_URL();
		// Post参数
		String post = CreatPostString.getDeviceUpdate(deviceid);

		DeviceUpdateInfoParser mParser = new DeviceUpdateInfoParser(listener);
		HashMap<String, String> mMap = new HashMap<String, String>();
		mMap.put("deviceid", deviceid);
		mParser.executePost(url, mMap);
	}


	public interface GetResultListCallback {
		void onFinished(Object o);

		void onErro(Object o);

	}

	public interface GetResultList2Callback {
		void onFinished(Object o1, Object o2, Object o3);

		void onErro(Object o);

	}

	public interface GetResultList3Callback {
		void onFinished();

		void onSuccess(Object o1);

		void onErro();

	}
}
