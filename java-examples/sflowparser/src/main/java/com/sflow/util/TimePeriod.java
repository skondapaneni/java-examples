package com.sflow.util;

import java.util.Date;


public class  TimePeriod {
	
	private Date 	start;
	private Date 	end;
	
	public Date getStart() {
		return start;
	}
	public void setStart(Date start) {
		this.start = start;
	}
	public Date getEnd() {
		return end;
	}
	public void setEnd(Date end) {
		this.end = end;
	}	
	
	public TimePeriod(Date start, Date end) {
		this.start = start;
		this.end = end;
	}
	
	public TimePeriod(TimeRangeE r) {
		switch(r) {
		case LAST_HOUR:
			end = new Date();
			start = new Date(end.getTime() - (3600 * 1000));				
			break;
		case LAST_HOUR_2:
			end = new Date();
			start = new Date(end.getTime() - (2 * 3600 * 1000));				
			break;
		case LAST_HOUR_5:
			end = new Date();
			start = new Date(end.getTime() - (5 * 3600 * 1000));				
			break;
		case LAST_MIN_10:
			end = new Date();
			start = new Date(end.getTime() - (600 * 1000));				
			break;
		case LAST_MIN_5:
			end = new Date();
			start = new Date(end.getTime() - (300 * 1000));				
			break;
		default:
			break;
		
		}
	}
	@Override
	public String toString() {
		return "TimePeriod [start=" + start + ", end=" + end + "]";
	}
	
	
	public static void main(String[] args) {
		
		TimePeriod tp = new TimePeriod(TimeRangeE.LAST_HOUR);
		System.out.println("tp " + tp);
	}
}
