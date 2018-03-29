package com.carlt.yema.protocolparser;

import android.text.TextUtils;


import com.carlt.yema.data.BaseResponseInfo;
import com.carlt.yema.model.LoginInfo;
import com.carlt.yema.preference.TokenInfo;
import com.carlt.yema.utils.ILog;
import com.google.gson.JsonObject;


/**
 * @author user 解析登录信息
 */
public class LoginInfoParser extends BaseParser {
	private final static String demoAccount = "13300001111";// 固定的演示账号13300001111(给杭州的包记得改成这个)

	public LoginInfoParser(ResultCallback callback) {
		super(callback);
	}


	@Override
	protected void parser() {
		LoginInfo.Last_Login_Time = System.currentTimeMillis();
		try{
			JsonObject mJSON_data = mJson.getAsJsonObject("data");
			if (mJSON_data != null) {
				JsonObject member = mJSON_data.getAsJsonObject("member");
				LoginInfo.setUseId((member.get("id").getAsString()));
				LoginInfo.setRealname((member.get("realname").getAsString()));
				LoginInfo.setUsername(member.get("username").getAsString());
				LoginInfo.setGender((member.get("gender").getAsString()));
				String mobile = (member.get("mobile").getAsString());
				LoginInfo.setMobile(mobile);

				LoginInfo.setAccess_token((member.get("access_token").getAsString()));
				TokenInfo.setToken(member.get("access_token").getAsString());

				JsonObject membercar = mJSON_data.getAsJsonObject("membercar");
				LoginInfo.setDealerId(membercar.get("dealerid").getAsString());

//				if (!TextUtils.isEmpty(mobile) && mobile.equals(demoAccount)) {
//					LoginInfo.setDemoAccount(true);
//				} else {
//					LoginInfo.setDemoAccount(false);
//				}
////				LoginInfo.setWeixinbind(member.get("weixinbind").getAsString());
////				LoginInfo.setClwbind(member.get("clwbind").getAsString());
////				LoginInfo.setRegip(member.get("regip").getAsString());
////				LoginInfo.setAvatar_id(member.get("avatar_id").getAsString());
////				LoginInfo.setOriginate(member.get("originate").getAsString());
////				LoginInfo.setLastlogin(member.get("lastlogin").getAsString());
////				LoginInfo.setLoginoauth(member.get("loginoauth").getAsString());
////				LoginInfo.setLogintimes(member.get("logintimes").getAsString());
////				LoginInfo.setCreatedate(member.get("createdate").getAsString());
////				LoginInfo.setAvatar_img((member.get("avatar_img").getAsString()));
////				LoginInfo.setLifetime((member.get("lifetime").getAsString()));
////
////				LoginInfo.setMain(getFlagResult(member.get("is_main").getAsString()));
////				LoginInfo.setMainDevicename(member
////						.get("move_device_name").getAsString());
////				LoginInfo.setAuthorize_status(member.get("authorize_switch").getAsString());
////				LoginInfo.setHasAuthorize(getFlagResult(member.get(
////						"has_authorize").getAsString()));
////				LoginInfo.setNeedAuthorize(getFlagResult(member.get(
////						"need_authorize").getAsString()));
////				LoginInfo.setFreezing(getFlagResult(member.get("is_freezing").getAsString()));
//				LoginInfo
//						.setAuthen(getFlagResult(member.get("is_authen").getAsString()));
//
//				// 测试代码
//				// LoginInfo.setAuthen(getFlagResult("0"));
//				// 测试代码结束
//				LoginInfo.setNoneedpsw(getFlagResult(member.get(
//						"lesspwd_switch").getAsString()));
//				LoginInfo.setAuthen_name(member.get("authen_name").getAsString());
//				LoginInfo.setAuthen_card(member.get("authen_card").getAsString());
//
//				LoginInfo.setAccess_token((member.get("access_token").getAsString()));
//				LoginInfo.setExpires_in((member.get("expires_in").getAsString()));
//				LoginInfo.setToken((member.get("access_token").getAsString()));
//				TokenInfo.setToken(member.get("access_token").getAsString());
////				LoginInfo.setExpiresIn((member.get("expires_in").getAsString()));
////				LoginInfo.setSSID(member.get("SSID").getAsString());
////				LoginInfo.setSSIDPWD(member.get("SSIDPWD").getAsString());
//
//				String isSetRemotePwd = member.get("is_set_remotePwd").getAsString();
//				// 测试代码
//				// isSetRemotePwd = "0";
//				// 测试代码结束
//				LoginInfo.setSetRemotePwd(getFlagResult(isSetRemotePwd));
//
//				JsonObject membercar = mJSON_data.getAsJsonObject("membercar");
//
//				int membercarId = membercar.get("id").getAsInt();
//				if (membercarId > 0) {
//					LoginInfo.setBindCar(true);
//				} else {
//					LoginInfo.setBindCar(false);
//				}
//				LoginInfo.setDeviceidstring((membercar.get("deviceidstring").getAsString()));
//				int isDeviceActivate = membercar.get("isDeviceActivate").getAsInt();
//				if (isDeviceActivate == 1) {
//					LoginInfo.setDeviceActivate(true);
//				} else {
//					LoginInfo.setDeviceActivate(false);
//				}
//				String isUpgradeing = membercar.get("upgradeing").getAsString();
//				if (isUpgradeing.equals("1")) {
//					LoginInfo.setUpgradeing(true);
//				} else {
//					LoginInfo.setUpgradeing(false);
//				}
//				String isGpsDevice = membercar.get("gps_device").getAsString();
//				if (isGpsDevice.equals("1")) {
//					LoginInfo.setGpsDevice(true);
//				} else {
//					LoginInfo.setGpsDevice(false);
//				}
//
//				LoginInfo.setBrandid((membercar.get("brandid").getAsString()));
//				LoginInfo.setDevicetype((membercar.get("devicetype").getAsString()));
//				LoginInfo.setOptionid((membercar.get("optionid").getAsString()));
//				LoginInfo.setCarid((membercar.get("carid").getAsString()));
//				String installorder = membercar.get("installorder").getAsInt() + "";
//				// 测试代码
//				// installorder="0";
//				// 测试代码结束
//				LoginInfo.setInstallorder(getFlagResultOther(installorder));
//
//				LoginInfo.setDealerId(membercar.get("dealerid").getAsString());
//				LoginInfo.setModelid(membercar.get("modelid").getAsString());
//
//				LoginInfo.setCarno((membercar.get("carno").getAsString()));
//				LoginInfo.setStandcarno((membercar.get("standcarno").getAsString()));
//				LoginInfo.setCarcity((membercar.get("city").getAsString()));
//				LoginInfo.setEngineno((membercar.get("engineno").getAsString()));
//				LoginInfo.setRegistno((membercar.get("registno").getAsString()));
//				LoginInfo.setCanQueryVio((membercar.get("canQueryVio").getAsString()));
//				LoginInfo.setCity_code(membercar.get("cityCode").getAsString());
//
//				LoginInfo.setCarname((membercar.get("carname").getAsString()));
//				LoginInfo.setCarlogo((membercar.get("carlogo").getAsString()));
//
//				LoginInfo.setBuydate((membercar.get("buydate").getAsString()));
//				String pin = membercar.get("bindpin").getAsString();
//				if (pin != null && !pin.equals("")) {
//					LoginInfo.setPin(member.get("mobile").getAsString(), pin);
//				}
//				String vin = membercar.get("bindvin").getAsString();
//				if (vin != null && !vin.equals("")) {
//					LoginInfo.setVin(member.get("mobile").getAsString(), vin);
//				}
//				LoginInfo
//						.setShortstandcarno(membercar.get("shortstandcarno").getAsString());
//
//				int secretaryid = membercar.get("secretaryid").getAsInt();
//			if (secretaryid == 1) {
//				LoginInfo.setSecretaryImg(R.drawable.secretary_female);
//				LoginInfo.setSecretaryName(YemaApplication.getAppContext()
//						.getResources().getString(
//								R.string.register_secretary_girl));
//
//			} else {
//				LoginInfo.setSecretaryImg(R.drawable.secretary_male);
//				LoginInfo.setSecretaryName(YemaApplication.getAppContext()
//						.getResources().getString(
//								R.string.register_secretary_boy));
//
//			}

//				LoginInfo.setMainten_miles((membercar
//						.get("mainten_miles").getAsString()));
//				LoginInfo
//						.setMainten_time((membercar.get("mainten_date").getAsString()));
				LoginInfo.setMainten_next_miles(membercar
						.get("mainten_next_miles").getAsInt() + "");
				LoginInfo.setMainten_next_day((membercar
						.get("mainten_next_date").getAsInt() + ""));
//
//				String imt = membercar.get("isMainten").getAsString();
//				if (imt.equals("1")) {
//					LoginInfo.setMainten(true);
//				} else {
//					LoginInfo.setMainten(false);
//				}
//
//				int is_tachograph = membercar.get("is_tachograph").getAsInt();
//				LoginInfo.setTachograph(getFlagResult(is_tachograph + ""));

				// dealer
				// JSONObject dealer = mJSON_data.optJSONObject("dealer");
				// LoginInfo.setDealerUsername((dealer.optString("name", "")));
				// LoginInfo.setDealerAddres((dealer.optString("addres", "")));
				// String m = dealer.optString("map", "");
				// String[] map = dealer.optString("map", "").split(",");
				// if (map != null && map.length > 1) {
				// LoginInfo.setDealerLat(Double.parseDouble(map[0]));
				// LoginInfo.setDealerLon(Double.parseDouble(map[1]));
				// }
				// if (map.length > 2) {
				// LoginInfo.setDealerZoom(Integer.parseInt(map[2]));
				// }
				// LoginInfo.setDealerTel(dealer.optString("tel", ""));
				// JSONObject pushset = mJSON_data.optJSONObject("pushset");
				// if (pushset != null && pushset.length() > 0) {
				// LoginInfo.setPush_prizeinfo_flag((pushset.optInt("dealer", 1)));
				// }
			}
		}catch (Exception e){
			ILog.e(TAG, "--e==" + e);
			mBaseResponseInfo.setFlag(BaseResponseInfo.ERRO);
			mBaseResponseInfo.setInfo(MSG_ERRO);

		}


	}

	private boolean getFlagResult(String judge) {
		boolean flag = false;
		if (judge != null) {
			if (judge.equals("0")) {
				flag = false;
			} else if (judge.equals("1")) {
				flag = true;
			}
		}
		return flag;
	}

	private boolean getFlagResultOther(String judge) {
		boolean flag = false;
		if (judge != null) {
			if (judge.equals("2")) {
				flag = false;
			} else if (judge.equals("1")) {
				flag = true;
			}
		}
		return flag;
	}

	// public BaseResponseInfo getBaseResponseInfo(String url, String post) {
	// try {
	// InputStream in = CPApplication.ApplicationContext.getAssets().open(
	// "json_login.txt");
	// mJson = new JSONObject(FileUtil.ToString(in));
	// Log.e("info", "Http响应--" + mJson);
	// mBaseResponseInfo.setFlag(mJson.getString("code"));
	// mBaseResponseInfo.setInfo(mJson.getString("msg"));
	// } catch (Exception e) {
	// Log.e("info", "BaseParser--e==" + e);
	// }
	// if (mBaseResponseInfo.getFlag() == BaseResponseInfo.SUCCESS) {
	// parser();
	// }
	// return mBaseResponseInfo;
	//
	// }
}
