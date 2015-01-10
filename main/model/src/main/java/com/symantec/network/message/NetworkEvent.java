package com.symantec.network.message;

import java.io.Serializable;

public class NetworkEvent implements Serializable {
	private static final long serialVersionUID = 1L;
	
	public static final String QUEUE = "NETWORK.INTERFACE.EVENT";
	
	private String id;
	private String result;
	
	public NetworkEvent(String id, String result){
		this.id     = id;
		this.result = result;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	
}
