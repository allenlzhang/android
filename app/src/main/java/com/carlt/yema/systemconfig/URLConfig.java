package com.carlt.yema.systemconfig;


import com.carlt.yema.YemaApplication;

public class URLConfig {

	public final static int VERSION_FORMAL = 1001;// 正式服
	public final static int VERSION_PREPARE = 1002;// 预发布服
	public final static int VERSION_TEST = 1003;// 测试服
	public static int flag = VERSION_TEST;

	// 车乐测试服务器
	private final static String C1 = "0896756ebec5bc62a51b15b9a7541901";

	// 车乐正式服务器
	private final static String C2 = "0896756ebec5bc62a51b15b9a7541901";

	//野马域名 测试服
	private final static String U1_YEMA_TEST = "http://yemaapi.linewin.cc/";

	//野马域名 预发布服
	private final static String U1_YEMA_PRE = "http://yemaapi.linewin.cc/";

	// 众泰野马API域名 正式服
	private final static String U1_Yema = "http://yemaapi.geni4s.com/";

	// 远程下发-正式服务器
	private final static String U_R1 = "https://remote-wildhorse.geni4s.com/";
	// 远程下发-预发布服务器
	private final static String U_R2 = "https://pre-remote-wildhorse.geni4s.com/";
	// 远程下发-测试服务器
	private final static String U_R3 = "http://remote-wildhorse.linewin.cc/";

	public final static String JUHE_APPKEY = "fa80021879c30853fa80137f1110c2a6";// 聚合AppKey

	// 获取yema API URL
	private static String getYemaURL(String s) {
		String version = YemaApplication.Version_API + "/";
		String url = "";
			// 正常版
			if (YemaApplication.Formal_Version) {
				url = U1_Yema + version + s;
			} else {
				switch (flag) {
					case VERSION_FORMAL:
						// 正式服
						url = U1_Yema + version + s;
						break;

					case VERSION_PREPARE:
						// 预发布服
						url = U1_YEMA_TEST + version + s;
						break;
					case VERSION_TEST:
						// 测试服
						url = U1_YEMA_TEST + version + s;
						break;
				}
			}
		return url;
	};

	// 生成和远程下发相关的Url
	private static String getUrlRemote(String s) {
		if (YemaApplication.Formal_Version) {
			return U_R1 + YemaApplication.VERSION_API_REMOTE + "/" + s;
		} else {
			switch (flag) {
			case VERSION_FORMAL:
				// 正式服
				return U_R1 + YemaApplication.VERSION_API_REMOTE + "/" + s;
			case VERSION_PREPARE:
				// 预发布服
				return U_R2 + YemaApplication.VERSION_API_REMOTE + "/" + s;
			case VERSION_TEST:
				// 测试服
				return U_R3 + YemaApplication.VERSION_API_REMOTE + "/" + s;
			default:
				return U_R3 + YemaApplication.VERSION_API_REMOTE + "/" + s;
			}
		}
	}

	public static String getClientID() {
		String clientId = "";
		if (YemaApplication.Formal_Version) {
			clientId = C2;
		} else {
			switch (flag) {
			case VERSION_FORMAL:
				// 正式服
				clientId = C2;
				break;

			case VERSION_PREPARE:
				// 预发布服
				clientId = C2;
				break;
			case VERSION_TEST:
				// 测试服
				clientId = C1;
				break;
			}
		}
		return clientId;
	};

	// 新版登录
	private static String M_LOGIN_NEW_URL = "user/login";

	// 新版注册
	private static String M_REGISTER_NEW_URL = "user/register";

	// 获取设备升级状态
	private static String M_DEVICEUPDATE_URL = "user/checkIsUpgrade";

	// 新版发送验证码
	private static String M_VALIDATE_NEW_URL = "user/setValidate";

	// 车秘书分类
	private static String M_SECRETARY_CATEGORY_URL_NEW = "life/messageCategory";

	// 删除车秘书消息
	private static String M_SECRETARY_DELETE_URL_NEW = "life/deleteMessage";
	//安防信息
	private static String M_SECRETARY_SECURITY_MSG = "life/securityMessage";

	// 获取大迈车系（针对车款列表-二级列表）
	private static String M_OPTIONLIST_URL = "comm/getDomyOptionList";

	// 获取大迈车款（针对车款列表-三级列表）
	private static String M_CARLIST_URL = "comm/getDomyCarList";

	// 获取生涯首页
	private static String M_CAREER_URL = "life/lifeIndex";

	// 月报
	private static String M_MONTHREPORT_URL = "life/monthReport";

	// 行车报告日期
	private static String M_REPORTDATE_URL = "life/reportdate";

	// 某年月报统计数据
	private static String M_MONTHREPORTSTATISTIC_URL = "life/monthReportStatistic";

	// 轨迹回放
	private static String M_GETCOOR_URL = "gps/getCoor";

	// 日历信息--月报
	private static String M_USER_MONTH_POINT_URL = "life/usermonthpoint";

	// 获取TOKEN
	private static String M_USER_ACCESSTOKEN = "user/accesstoken";

	// 日历信息--日报
	private static String M_USER_DAY_POINT_URL = "life/userdaypoint";

	// 获取用户绑定车款配置
	private static String M_CAR_CURCARCONFIG_URL = "car/curCarConfig";

	// 修改车辆信息--切换车型
	private static String M_SWITCHCAR_URL = "remote/switchCar";

	// 座驾首页
	private static String M_CAR_MAIN_URL = "car/carIndex";

	// 日报
	private static String M_REPORTDAY_URL = "life/dayReport";
	// 日志
	private static String M_REPORTDAYLOG_URL = "life/daylogreport";
	//读取里程
	private static String M_MILESINFO = "remote/getMilesInfos";

	private static String M_REMOTE_WARNINGLAMP = "remote/warningLamp";
	//实时车况
	private static String M_REMOTE_STATUS = "remote/status";

	//车辆状态
	private static String M_REMOTE_STATE= "remote/state";
	//胎压监测
	private static String M_REMOTE_DRIECTRRESSURE = "remote/directPressure";

	// 车秘书提醒
	private static String M_SAFETY_MESSAGE_URL = "life/message";

	public static String getM_SAFETY_MESSAGE_URL() {
		return getYemaURL(M_SAFETY_MESSAGE_URL);
	}

	public static String getM_REPORTDAY_URL(){
		return getYemaURL(M_REPORTDAY_URL);
	}

	public static String getM_REPORTDAYLOG_URL(){
		return getYemaURL(M_REPORTDAYLOG_URL);
	}

	public static String getM_MILESINFO_URL(){
		return getUrlRemote(M_MILESINFO);
	}

	public static String getM_LOGIN_URL() {
		return getYemaURL(M_LOGIN_NEW_URL);
	}

	public static String getM_REGISTER_NEW_URL() {
		return getYemaURL(M_REGISTER_NEW_URL);
	}

	public static String getM_OPTIONLIST_URL() {
		return getYemaURL(M_OPTIONLIST_URL);
	}
	public static String getM_CARLIST_URL() {
		return getYemaURL(M_CARLIST_URL);
	}

	public static String getM_CAREER_URL() {
		return getYemaURL(M_CAREER_URL);
	}

	public static String getM_VALIDATE_NEW_URL() {
		return getYemaURL(M_VALIDATE_NEW_URL);
	}

	public static String getM_MONTHREPORT_URL() {
		return getYemaURL(M_MONTHREPORT_URL);
	}

	public static String getM_REPORTDATE_URL() {
		return getYemaURL(M_REPORTDATE_URL);
	}

	public static String getM_MONTHREPORTSTATISTIC_URL() {
		return getYemaURL(M_MONTHREPORTSTATISTIC_URL);
	}

	public static String getM_GETCOOR_URL() {
		return getYemaURL(M_GETCOOR_URL);
	}

	public static String getM_USER_MONTH_POINT_URL() {
		return getYemaURL(M_USER_MONTH_POINT_URL);
	}

	public static String getM_USER_DAY_POINT_URL() {
		return getYemaURL(M_USER_DAY_POINT_URL);
	}

	public static String getM_SECRETARY_CATEGORY_URL() {
		return getYemaURL(M_SECRETARY_CATEGORY_URL_NEW);
	}

	public static String getM_SECRETARY_DELETE_URL() {
		return getYemaURL(M_SECRETARY_DELETE_URL_NEW);
	}

	public static String getM_SECRETARY_MESSAGE_URL() {
		return getYemaURL(M_SECRETARY_SECURITY_MSG);
	}

	public static String getM_CAR_CURCARCONFIG_URL() {
		return getYemaURL(M_CAR_CURCARCONFIG_URL);
	}

	public static String getM_SWITCHCAR_URL() {
		return getUrlRemote(M_SWITCHCAR_URL);
	}

	public static String getM_REMOTE_WARNINGLAMP() {
		return getUrlRemote(M_REMOTE_WARNINGLAMP);
	}

	public static String getM_CAR_MAIN_URL() {
		return getYemaURL(M_CAR_MAIN_URL);
	}

	public static String getM_DEVICEUPDATE_URL() {
		return getYemaURL(M_DEVICEUPDATE_URL);
	}

	public static String getM_USER_ACCESSTOKEN() {
		return getYemaURL(M_USER_ACCESSTOKEN);
	}

	public static String getM_REMOTE_STATUS() {
		return getUrlRemote(M_REMOTE_STATUS);
	}

	public static String getM_REMOTE_STATE() {
		return getUrlRemote(M_REMOTE_STATE);
	}

	public static String getM_REMOTE_DRIECTRRESSURE() {
		return getUrlRemote(M_REMOTE_DRIECTRRESSURE);
	}



}
