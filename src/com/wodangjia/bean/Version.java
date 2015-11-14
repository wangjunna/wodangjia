package com.wodangjia.bean;

public class Version {
	int id;
	String version;
	String downloadurl;
	String addtime;
	public Version(int id, String version, String downloadurl, String addtime) {
		super();
		this.id = id;
		this.version = version;
		this.downloadurl = downloadurl;
		this.addtime = addtime;
	}
	public Version(String version, String downloadurl, String addtime) {
		super();
		this.version = version;
		this.downloadurl = downloadurl;
		this.addtime = addtime;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getDownloadurl() {
		return downloadurl;
	}
	public void setDownloadurl(String downloadurl) {
		this.downloadurl = downloadurl;
	}
	public String getAddtime() {
		return addtime;
	}
	public void setAddtime(String addtime) {
		this.addtime = addtime;
	}
	
}
