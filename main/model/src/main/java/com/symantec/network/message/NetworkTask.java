package com.symantec.network.message;

import java.io.Serializable;

import com.symantec.network.model.InterfaceInfo;

public class NetworkTask implements Serializable {

	private static final long serialVersionUID = 1L;

	public static final String QUEUE = "NETWORK.INTERFACE.TASK";
	
	private String action;
	private InterfaceInfo ifInfo;
	
	public NetworkTask(String action, InterfaceInfo ifInfo){
		this.action = action;
		this.ifInfo = ifInfo;
	}
	
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public InterfaceInfo getIfInfo() {
		return ifInfo;
	}
	public void setIfInfo(InterfaceInfo ifInfo) {
		this.ifInfo = ifInfo;
	}
}
