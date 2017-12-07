package com.boco.data.reportmanagement.entity;

import java.io.Serializable;


/**
 * 实体类映射表reportmanagement_msg
 * @author Administrator
 *
 */
public class ReportManagementMsg implements Serializable{
	
	private static final long serialVersionUID = 8222199279056409363L;

	private String id;//ID
	
	private String filename;//报表名称
	
	private String circle;//上传周期
	
	private String frequency;//上传的具体时间
	
	private String mobile;//上传人手机号
	
	private java.sql.Timestamp uploadtime;//上传时间

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public String getCircle() {
		return circle;
	}

	public void setCircle(String circle) {
		this.circle = circle;
	}

	public String getFrequency() {
		return frequency;
	}

	public void setFrequency(String frequency) {
		this.frequency = frequency;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	

	public java.sql.Timestamp getUploadtime() {
		return uploadtime;
	}

	public void setUploadtime(java.sql.Timestamp uploadtime) {
		this.uploadtime = uploadtime;
	}

	@Override
	public String toString() {
		return "ReportManagementMsg [id=" + id + ", filename=" + filename + ", circle=" + circle + ", frequency="
				+ frequency + ", mobile=" + mobile + ", uploadtime=" + uploadtime + "]";
	}
	
	
	
	
	
	
	
	

}
