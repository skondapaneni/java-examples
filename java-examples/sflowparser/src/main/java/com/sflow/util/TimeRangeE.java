package com.sflow.util;

import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum TimeRangeE implements 
	Converter<String, TimeRangeE>{
	LAST_HOUR("last_1h"),
	LAST_MIN_10("last_10m"),
	LAST_MIN_5("last_5m"),
	LAST_HOUR_2("last_2h"),
	LAST_HOUR_5("last_5h");
	
	private String  name;
	
	private TimeRangeE(String n) {
		this.name = n;
	}
	
	public String getName() {
		return name;
	}
	
	@JsonCreator
	public static TimeRangeE fromString(String text) {
		if (text != null) {
			for (TimeRangeE b : TimeRangeE.values()) {
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

	@Override
	public TimeRangeE convert(String arg0) {
		return fromString(arg0);
	}
}

