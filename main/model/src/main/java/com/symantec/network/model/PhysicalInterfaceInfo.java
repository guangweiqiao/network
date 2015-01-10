package com.symantec.network.model;

import java.util.Optional;

public class PhysicalInterfaceInfo extends InterfaceInfo {
	private String name;
	private Optional<String> ipv4Address;
	private Optional<String> ipv6Address;
	private Optional<String> subnetMaskAddress;
	private Optional<String> hwAddress;
	
	private final String DEFAULT_VALUE = null;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getIpv4Address() {
		return ipv4Address.orElse(DEFAULT_VALUE);
	}
	public void setIpv4Address(Optional<String> ipv4Address) {
		this.ipv4Address = ipv4Address;
	}
	public String getIpv6Address() {
		return ipv6Address.orElse(DEFAULT_VALUE);
	}
	public void setIpv6Address(Optional<String> ipv6Address) {
		this.ipv6Address = ipv6Address;
	}
	public String getSubnetMaskAddress() {
		return subnetMaskAddress.orElse(DEFAULT_VALUE);
	}
	public void setSubnetMaskAddress(Optional<String> subnetMaskAddress) {
		this.subnetMaskAddress = subnetMaskAddress;
	}
	public String getHwAddress() {
		return hwAddress.orElse(DEFAULT_VALUE);
	}
	public void setHwAddress(Optional<String> hwAddress) {
		this.hwAddress = hwAddress;
	}
	
	@Override
	public String toString(){
		return "PhysicalInterfaceInfo["+
				"name="+ this.name + ", ip4:"+
				this.getIpv4Address() + ", ipv6:"+
				this.getIpv6Address();
	}
}
