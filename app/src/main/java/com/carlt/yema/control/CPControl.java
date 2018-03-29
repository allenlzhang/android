package com.carlt.yema.control;


import com.carlt.yema.data.remote.AirMainInfo;

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
