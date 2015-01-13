package com.symantec.network.message;

import java.io.Serializable;

import com.symantec.network.model.PhysicalInterfaceInfo;

public class NetworkTask extends Task implements Serializable {

	private static final long serialVersionUID = 1L;

	public static final String QUEUE = "NETWORK.INTERFACE.TASK";
	
	private PhysicalInterfaceInfo ifInfo;
	
	public NetworkTask(){
		super();
	};
	
	public NetworkTask(String action, PhysicalInterfaceInfo ifInfo){
		super(action);
		this.ifInfo = ifInfo;
	}
	
	public PhysicalInterfaceInfo getIfInfo() {
		return ifInfo;
	}
	public void setIfInfo(PhysicalInterfaceInfo ifInfo) {
		this.ifInfo = ifInfo;
	}
}
