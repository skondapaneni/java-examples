package com.sflow.util;

import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

public class LoggerDefaults {
	public static final Marker logstash = MarkerManager.getMarker("LOGSTASH_MARKER");
	public static final Marker syslog = MarkerManager.getMarker("SYSLOG_MARKER");
	
	public static Marker defaultMarker = syslog;

}