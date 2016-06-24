package com.fcr.net.http;

import java.io.File;

/**
 * 上传文件的父类
 * @author chaoranf
 * @createtime 2015-8-29
 */
public class GiveUploadFile extends File {

	private static final long serialVersionUID = -1520406711286243863L;

	private String name;
	private String fileName;

	public GiveUploadFile(String path) {
		super(path);
	}
	
	/**
	 * 必须实现这个方法哦！！！
	 * @param path
	 * @param name
	 * @param fileName
	 */
	public GiveUploadFile(String path, String name, String fileName){
		this(path);
		this.name = name;
		this.fileName = fileName;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	/**
	 * 数据名称
	 */
	public String getName() {
		return name;
	}

	/**
	 * 文件名称
	 * 
	 * @return
	 */
	public String getFileName() {
		return fileName;
	}

	/**
	 * 文件类型，无耻的写死先
	 * 
	 * @return
	 */
	public String getMimeType() {
		return "multipart/form-data";
	}

}
