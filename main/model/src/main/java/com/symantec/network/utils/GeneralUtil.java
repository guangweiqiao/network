package com.symantec.network.utils;

public class GeneralUtil {

	
	public static boolean isStringNullOrEmpty(String str){
		if(null != str && !("".equals(str))){
			return false;
		}
		
		return true;
	}
}
