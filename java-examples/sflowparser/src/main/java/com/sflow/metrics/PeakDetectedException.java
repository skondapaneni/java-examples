package com.sflow.metrics;

public class PeakDetectedException extends Exception {
	private double expM;
	private double sd;
	private String tag;
	private double value;
	
	public PeakDetectedException(String tag, double value, double expM, double sd) {
		this.setExpM(expM);
		this.setSd(sd);	
		this.setTag(tag);
		this.setValue(value);
	}

	public double getExpM() {
		return expM;
	}

	public void setExpM(double expM) {
		this.expM = expM;
	}

	public double getSd() {
		return sd;
	}

	public void setSd(double sd) {
		this.sd = sd;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}

}
