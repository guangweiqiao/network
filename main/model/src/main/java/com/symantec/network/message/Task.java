package com.symantec.network.message;
import java.util.UUID;

public class Task {
	private String id;
	private String action;
	
	public Task(){
		this.id = String.valueOf(UUID.randomUUID());
	}
	
	public Task(String action){
		this.id = String.valueOf(UUID.randomUUID());
		this.action = action;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
}
