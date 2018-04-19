package com.carlt.yema.utils;

/**
 * 作者：秋良
 * 
 * 描述：Log类
 * 
 * 规约：所有Log打印应调用此类，以便所有日志信息有统一出口
 */
public class Log {

	public static boolean flag = false;

	public static void e(String tag, String info) {
		if (flag) {
			com.carlt.yema.utils.Log.e(tag, info);
		}

	}

	public static void v(String tag, String info) {
		if (flag) {
			com.carlt.yema.utils.Log.v(tag, info);
		}

	}
	public static void d(String tag, String info) {
		if (flag) {
			com.carlt.yema.utils.Log.d(tag, info);
		}

	}

	public static void i(String tag, String info) {
		if (flag) {
			com.carlt.yema.utils.Log.i(tag, info);

		}

	}
}
