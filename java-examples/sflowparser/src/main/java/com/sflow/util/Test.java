package com.sflow.util;

public class Test {

	public static void main(String[] args) {
		
		String istr = "10.1.1.1:eth1";
		long ifIndex = -1;
		long deviceId = -1;
		istr = istr.trim();
		if (istr.matches("\\d+:\\d+")) {
			deviceId = Long.parseLong(istr.substring(0, 
					istr.indexOf(':')) );
			ifIndex = Long.parseLong(istr.substring(
					istr.indexOf(':') + 1, istr.length()));
			
			System.out.println("deviceId " + deviceId);
			System.out.println("ifIndex " + ifIndex);

		} else if (istr.matches(".*:.*")) {
			String deviceIdStr = istr.substring(0, 
					istr.indexOf(':'));
			String ifIndexStr = istr.substring(
					istr.indexOf(':') + 1, istr.length());
			System.out.println("deviceId " + deviceIdStr);
			System.out.println("ifIndex " + ifIndexStr);
		}
		
	}
}
