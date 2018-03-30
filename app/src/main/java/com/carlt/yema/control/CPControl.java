package com.carlt.yema.control;


import com.carlt.yema.YemaApplication;
import com.carlt.yema.data.ReportGpsInfo;
import com.carlt.yema.data.home.InformationCategoryInfoList;
import com.carlt.yema.data.home.InformationMessageInfoList;
import com.carlt.yema.data.home.MonthStatisticChartInfo;
import com.carlt.yema.data.home.ReportCalendarMonthInfo;
import com.carlt.yema.data.home.ReportDayInfo;
import com.carlt.yema.data.home.ReportDayLogInfo;
import com.carlt.yema.data.login.UserRegisterParams;
import com.carlt.yema.data.remote.AirMainInfo;
import com.carlt.yema.data.set.ModifyCarInfo;
import com.carlt.yema.protocolparser.BaseParser;
import com.carlt.yema.protocolparser.DefaultParser;
import com.carlt.yema.protocolparser.DefaultStringParser;
import com.carlt.yema.protocolparser.DeviceUpdateInfoParser;
import com.carlt.yema.protocolparser.car.CarModeInfoListV2Parser;
import com.carlt.yema.protocolparser.car.CarModeInfoListV3Parser;
import com.carlt.yema.protocolparser.home.CareerlParser;
import com.carlt.yema.protocolparser.home.InformationCentreInfoListParser;
import com.carlt.yema.protocolparser.home.InformationMessageListParser;
import com.carlt.yema.protocolparser.home.MilesInfoParser;
import com.carlt.yema.protocolparser.home.RemindDefaultParser;
import com.carlt.yema.protocolparser.home.ReportCalendarDayParser;
import com.carlt.yema.protocolparser.home.ReportCalendarMonthParser;
import com.carlt.yema.protocolparser.home.ReportDateParser;
import com.carlt.yema.protocolparser.home.ReportDayLogParser;
import com.carlt.yema.protocolparser.home.ReportDayParser;
import com.carlt.yema.protocolparser.home.ReportGpsParser;
import com.carlt.yema.protocolparser.home.ReportMonthParser;
import com.carlt.yema.protocolparser.home.ReportMonthStatisticParser;
import com.carlt.yema.protocolparser.login.UserRegisterParser;
import com.carlt.yema.systemconfig.URLConfig;
import com.carlt.yema.utils.CreatPostString;
import com.carlt.yema.utils.CreateHashMap;

import java.util.HashMap;
import java.util.List;

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
	/**
	 * 获取信息中心列表
	 * @param callback
	 */
	public static void GetInformationCentreInfoListResult(BaseParser.ResultCallback<InformationCategoryInfoList> callback){
		HashMap mHashMap = CreateHashMap.getNullData();
		InformationCentreInfoListParser parser = new InformationCentreInfoListParser(callback);
		parser.setTest(false);
		parser.executePost(URLConfig.getM_SECRETARY_CATEGORY_URL(),mHashMap);
	}


	/**
	 * 行车报告日期
	 * @param callback
	 */
	public static void GetReportdateResult(BaseParser.ResultCallback callback){
		HashMap mHashMap = CreateHashMap.getNullData();
		ReportDateParser parser = new ReportDateParser(callback);
		parser.setTest(false);
		parser.executePost(URLConfig.getM_REPORTDATE_URL(),mHashMap);
	}

	/**
	 * 指定用户行车日报
	 * @param callback
	 * @param date
	 */
	public static void GetDayReportResult(BaseParser.ResultCallback<ReportDayInfo> callback, String date){
		HashMap mHashMap = CreateHashMap.getDayReportMap(date);
		ReportDayParser parser = new ReportDayParser(callback);
		parser.setTest(false);
		parser.executePost(URLConfig.getM_REPORTDAY_URL(),mHashMap);
	}

	/**
	 * 某一天的行车日志
	 * @param callback
	 * @param date
	 */
	public static void GetDayReportLogResult(BaseParser.ResultCallback<List<ReportDayLogInfo>> callback, String date){
		HashMap mHashMap = CreateHashMap.getDayReportMap(date);
		ReportDayLogParser parser = new ReportDayLogParser(callback);
		parser.setTest(false);
		parser.executePost(URLConfig.getM_REPORTDAYLOG_URL(),mHashMap);
	}

	/**
	 * 指定用户行车月报
	 * @param callback
	 * @param date
	 */
	public static void GetMonthReportResult(BaseParser.ResultCallback callback,String date){
		HashMap mHashMap = CreateHashMap.getDayReportMap(date);
		ReportMonthParser parser = new ReportMonthParser(callback);
		parser.setTest(false);
		parser.executePost(URLConfig.getM_MONTHREPORT_URL(),mHashMap);
	}

	/**
	 * 获取某年月报统计数据
	 * @param callback
	 * @param date
	 */
	public static void GetMonthReportLogResult(BaseParser.ResultCallback<MonthStatisticChartInfo> callback, String date){
		HashMap mHashMap = CreateHashMap.getDayReportMap(date);
		ReportMonthStatisticParser parser = new ReportMonthStatisticParser(callback);
		parser.setTest(false);
		parser.executePost(URLConfig.getM_MONTHREPORTSTATISTIC_URL(),mHashMap);
	}

	/**
	 *车秘书提醒
	 * @param callback
	 */
	public static void GetInformationMessageResult(BaseParser.ResultCallback<InformationMessageInfoList> callback, int class1){
		HashMap mHashMap = CreateHashMap.getMessageMap(class1);
		InformationMessageListParser parser = new InformationMessageListParser(callback);
		parser.setTest(false);
		parser.executePost(URLConfig.getM_SAFETY_MESSAGE_URL(),mHashMap);
	}

	/**
	 * 获取一年中每月平均得分
	 * @param callback
	 * @param date
	 */
	public static void GetUserMonthPointResult(BaseParser.ResultCallback<List<ReportCalendarMonthInfo>> callback, String date){
		HashMap mHashMap = CreateHashMap.getDayReportMap(date);
		ReportCalendarMonthParser parser = new ReportCalendarMonthParser(callback);
		parser.setTest(false);
		parser.executePost(URLConfig.getM_USER_MONTH_POINT_URL(),mHashMap);
	}

	/**
	 * 用户一月中日平均得分
	 * @param callback
	 * @param date
	 */
	public static void GetUserDayPointResult(BaseParser.ResultCallback<List<ReportCalendarMonthInfo>> callback, String date){
		HashMap mHashMap = CreateHashMap.getDayReportMap(date);
		ReportCalendarDayParser parser = new ReportCalendarDayParser(callback);
		parser.setTest(false);
		parser.executePost(URLConfig.getM_USER_DAY_POINT_URL(),mHashMap);
	}

	/**
	 * 获取生涯首页数据
	 * @param callback
	 */
	public static void GetCareerResult(BaseParser.ResultCallback callback){
		HashMap mHashMap = CreateHashMap.getNullData();
		CareerlParser parser = new CareerlParser(callback);
		parser.setTest(false);
		parser.executePost(URLConfig.getM_CAREER_URL(),mHashMap);
	}

	/**
	 * 读取里程
	 * @param callback
	 */
	public static void GetMilesInfoResult(BaseParser.ResultCallback callback){
		HashMap mHashMap = CreateHashMap.getNullData();
		MilesInfoParser parser = new MilesInfoParser(callback);
		parser.setTest(false);
		parser.executePost(URLConfig.getM_MILESINFO_URL(),mHashMap);
	}

	/**
	 * GPS行车轨迹
	 * @param callback
	 * @param gpsStartTime
	 * @param gpsStopTime
	 * @param runSn
	 */
	public static void GetReportGpsResult(BaseParser.ResultCallback<List<ReportGpsInfo>> callback,
										  String gpsStartTime,String gpsStopTime,String runSn){
		HashMap mHashMap = CreateHashMap.getReportGpsMap(gpsStartTime,gpsStopTime,runSn);
		ReportGpsParser parser = new ReportGpsParser(callback);
		parser.setTest(false);
		parser.executePost(URLConfig.getM_GETCOOR_URL(),mHashMap);
	}

	/**
	 * 提醒项 删除
	 * @param callback
	 * @param class1
	 * @param messageid
	 */
	public static void GetRemindDeleteResult(BaseParser.ResultCallback callback,int class1,int messageid){
		HashMap mHashMap = CreateHashMap.getRemindDefaultMap(class1,messageid);
		RemindDefaultParser parser = new RemindDefaultParser(callback);
		parser.setTest(false);
		parser.executePost(URLConfig.getM_SECRETARY_DELETE_URL(),mHashMap);
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
