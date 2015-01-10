package com.symantec.network.model;

public class NetworkURIConstants {
	public static final String GET_IF_INFO    = "/network/{eth}"; //get request
	public static final String PUT_IPV4_ADDRESS = "/network/ipv4";  //put request
	public static final String ADD_IPV6_ADDRESS = "/network/ipv6";  //post request
	
	public static final String GET_GATEWAY_ALL  = "/network/gateway/all";
	public static final String GET_GATEWAY_IPV4 = "/network/gateway/ipv4"; 
	public static final String GET_GATEWAY_IPV6 = "/network/gateway/ipv6";
	public static final String ADD_GATEWAY      = "/network/gateway"; //post request
	public static final String DELETE_GATEWAY   = "/network/gateway/{ip}"; //delete request  
	
	public static final String SERVICE_HOST = "localhost";
	
}
