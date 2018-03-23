package com.carlt.yema.control;



public class CPControl {
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
