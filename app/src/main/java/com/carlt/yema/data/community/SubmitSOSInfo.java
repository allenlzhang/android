package com.carlt.yema.data.community;


import com.carlt.yema.data.UploadInfo;

import java.util.ArrayList;

public class SubmitSOSInfo {
	private ArrayList<UploadInfo> images = new ArrayList<UploadInfo>();

	// 是否需要救援
	private boolean need_sos;
	// 描述
	private String info;
	// 地址经纬度 98.11,102.26
	private String addr_point;
	// 地址描述
	private String addr_detail;

	public ArrayList<UploadInfo> getImages() {
		return images;
	}

	public void setImages(ArrayList<UploadInfo> images) {
		this.images = images;
	}

	public void AddImages(UploadInfo image) {
		images.add(image);
	}

	public boolean isNeed_sos() {
		return need_sos;
	}

	public void setNeed_sos(boolean need_sos) {
		this.need_sos = need_sos;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public String getAddr_point() {
		return addr_point;
	}

	public void setAddr_point(String addr_point) {
		this.addr_point = addr_point;
	}

	public String getAddr_detail() {
		return addr_detail;
	}

	public void setAddr_detail(String addr_detail) {
		this.addr_detail = addr_detail;
	}

}
