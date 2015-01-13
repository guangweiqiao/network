package com.symantec.network.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

public class NetworkCommandFactory {
	
	public static String[] createCommand(String command, LinkedHashMap<String, String> optionMap){
		
		if(GeneralUtil.isStringNullOrEmpty(command)){
			return null;
		}
		
		List<String> commandList = new ArrayList<String>();
		commandList.add(command);
		
		if(null != optionMap && optionMap.size() !=0){
			Set<Entry<String, String>> entrySet = optionMap.entrySet();
			for(Entry<String,String> entry : entrySet){
				if(null != entry.getValue()){
					commandList.add(entry.getKey());
					commandList.add(entry.getValue());
				}
			}
		}
		
		return commandList.toArray(new String[commandList.size()]);
	}
	
	public static void main(String[] args) {
		LinkedHashMap<String,String> optionMap = new LinkedHashMap<String, String>();
		optionMap.put("eth1", "10.200.100.1");
		optionMap.put("netmask", "255.255.255.0");
		optionMap.put("broadcast", null);
		String[] command = NetworkCommandFactory.createCommand("/sbin/ifconfig", optionMap);
		
		System.out.println(Arrays.toString(command));
	}

}
