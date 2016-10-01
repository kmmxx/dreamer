package com.dreamer.tool.socket;
import java.nio.ByteBuffer;

/**
 * Program  : Response.java
 * Author   : chencheng
 * Create   : 2013-9-27 ����09:52:28
 *
 * Copyright 2008 by iPanel Technologies Ltd.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of iPanel Technologies Ltd.("Confidential Information").  
 * You shall not disclose such Confidential Information and shall 
 * use it only in accordance with the terms of the license agreement 
 * you entered into with iPanel Technologies Ltd.
 *
 */

/**
 * @author kmm
 *
 */
public class ResponseData {

	/** �ļ����� */
	private byte fileType;

	/** ��Ʊ�г� */
	private byte marketId;

	/** �������?*/
	private ByteBuffer data;

	public byte getFileType() {
		return fileType;
	}

	public void setFileType(byte fileType) {
		this.fileType = fileType;
	}

	public byte getMarketId() {
		return marketId;
	}

	public void setMarketId(byte marketId) {
		this.marketId = marketId;
	}

	public ByteBuffer getData() {
		return data;
	}

	public void setData(ByteBuffer data) {
		this.data = data;
	}

}
