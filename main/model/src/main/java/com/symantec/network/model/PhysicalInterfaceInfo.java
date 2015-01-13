package com.symantec.network.model;

import java.util.LinkedHashMap;

public class PhysicalInterfaceInfo{
	private String name;
	private String ipv4Address;
	private String ipv6Address;
	private String subnetMaskAddress;
	
	public PhysicalInterfaceInfo(){};
	
	public PhysicalInterfaceInfo(String name, String ipv4Address){
		this.name = name;
		this.ipv4Address = ipv4Address;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getIpv4Address() {
		return ipv4Address;
	}
	public void setIpv4Address(String ipv4Address) {
		this.ipv4Address = ipv4Address;
	}
	public String getIpv6Address() {
		return ipv6Address;
	}
	public void setIpv6Address(String ipv6Address) {
		this.ipv6Address = ipv6Address;
	}
	public String getSubnetMaskAddress() {
		return subnetMaskAddress;
	}
	public void setSubnetMaskAddress(String subnetMaskAddress) {
		this.subnetMaskAddress = subnetMaskAddress;
	}
	
	public LinkedHashMap<String,String> generateCommandOptionMap(LinkedHashMap<String,String> optionMap){
		if(null == optionMap){
			return null;
		}
		
		if(null == this.name){
			return null;
		}
		
		if(null != this.ipv4Address){
			optionMap.put(this.name, this.ipv4Address);
		}else if(null != this.ipv6Address){
			optionMap.put(this.name, this.ipv6Address);
		}else{
			return null;
		}
		
		if(null != this.subnetMaskAddress) optionMap.put("netmask", this.subnetMaskAddress);
		
		return optionMap;
	}
	
	@Override
	public String toString(){
		return "PhysicalInterfaceInfo["+
				"name="+ this.name + ", ip4:"+
				this.getIpv4Address() + 
				"]";
	}
}
