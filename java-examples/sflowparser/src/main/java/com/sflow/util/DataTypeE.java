package com.sflow.util;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum DataTypeE {
	UINT8("uint8", 1),
	UINT16("uint16", 2),
	UINT32("uint32", 4),
	UINT64("uint64", 8),
	IPV4("ipv4", 4),
	IPV6("ipv6", 32);

	private int nbytes;
	private String  name;
	
	private DataTypeE(String n, int nbytes) {
		this.name  = n;
		this.nbytes = nbytes;
	}
	
	public String getName() {
		return name;
	}

	@JsonCreator
	public static DataTypeE fromString(String text) {
		if (text != null) {
			for (DataTypeE b : DataTypeE.values()) {
				if (text.equalsIgnoreCase(b.getName())) {					
					return b;
				}
			}
		}
	
		throw new IllegalArgumentException();
	}

	public String toString() {
		return getName();
	}
	
	public int nbytes() {
		return this.nbytes;
	}

	public DataTypeE convert(String arg0) {
		return fromString(arg0);
	}
}