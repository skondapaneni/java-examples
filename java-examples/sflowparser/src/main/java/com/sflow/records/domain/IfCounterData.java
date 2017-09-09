package com.sflow.records.domain;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Calendar;
import java.util.Date;


import com.fasterxml.jackson.annotation.JsonProperty;

public class IfCounterData implements Serializable {
		

	/**
	 * 
	 */
	private static final long serialVersionUID = 3610895374479617121L;

	@JsonProperty("ipVersionAgent")
	private int ipVersionAgent; // 1=v4, 2=v6
	
	@JsonProperty("ipAddress")
	private String ipAddress;	
	
	@JsonProperty("bucket")
	private int bucket;	
	
	@JsonProperty("subAgentId")
	private long subAgentID;
		
	@JsonProperty("sourceIDType")
	private long sourceIDType;    /* sFlowDataSource */
	
	@JsonProperty("sourceIDIndex")
	private long sourceIDIndex;   /* sFlowDataSource */
		
	@JsonProperty("eventTime")    
	private Date eventTime;
	
	@JsonProperty("sampleSequenceNumber")
	private long sampleSequenceNumber;   /* sample sequence number */

	@JsonProperty("sysUptime")
	private long sysUptime; // in milliseconds
	
	@JsonProperty("seqNumber")
	private long seqNumber;
	
	@JsonProperty("ifIndex")
	private long ifIndex;
	
	@JsonProperty("ifType")
	private long ifType;
	
	@JsonProperty("ifSpeed")
	private BigInteger ifSpeed;
	
	@JsonProperty("ifDirection")
	private long ifDirection; // 0 = unknown, 1 = full-duplex, 2 = half-duplex, 3 = in, 4 = out

	@JsonProperty("ifStatus")
	private long ifStatus; // bit 0 => ifAdminStatus 0=down|1=up, bit 1 => ifOperStatus 0=down|1=up
	
	@JsonProperty("ifInOctets")
	private BigInteger ifInOctets;

	@JsonProperty("ifUcastPkts")
	private long ifInUcastPkts;

	@JsonProperty("ifInMulticastPkts")
	private long ifInMulticastPkts;

	@JsonProperty("ifInBroadcastPkts")
	private long ifInBroadcastPkts;

	@JsonProperty("ifInDiscards")
	private long ifInDiscards;
	
	@JsonProperty("ifInErrors")
	private long ifInErrors;

	@JsonProperty("ifInUnknownProtos")
	private long ifInUnknownProtos;
	
	@JsonProperty("ifOutOctets")
	private BigInteger ifOutOctets;

	@JsonProperty("ifOutUcastPkts")
	private long ifOutUcastPkts;

	@JsonProperty("ifOutMulticastPkts")
	private long ifOutMulticastPkts;

	@JsonProperty("ifOutBroadcastPkts")
	private long ifOutBroadcastPkts;

	@JsonProperty("ifOutDiscards")
	private long ifOutDiscards;

	@JsonProperty("ifOutErrors")
	private long ifOutErrors;

	@JsonProperty("ifPromiscuousMode")
	private long ifPromiscuousMode;
	
	@JsonProperty("ifInUtilization")
	private double ifInUtilization;
	
	@JsonProperty("ifOutUtilization")
	private double ifOutUtilization;
	
	@JsonProperty("ifInFrameRate")
	private double ifInFrameRate;
	
	@JsonProperty("ifOutFrameRate")
	private double ifOutFrameRate;
	
	public int getIpVersionAgent() {
		return ipVersionAgent;
	}

	public void setIpVersionAgent(int ipVersionAgent) {
		this.ipVersionAgent = ipVersionAgent;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public long getSourceIDType() {
		return sourceIDType;
	}

	public void setSourceIDType(long sourceIDType) {
		this.sourceIDType = sourceIDType;
	}

	public long getSourceIDIndex() {
		return sourceIDIndex;
	}

	public void setSourceIDIndex(long sourceIDIndex) {
		this.sourceIDIndex = sourceIDIndex;
	}

	public long getSampleSequenceNumber() {
		return sampleSequenceNumber;
	}

	public void setSampleSequenceNumber(long sampleSequenceNumber) {
		this.sampleSequenceNumber = sampleSequenceNumber;
	}

	public int getBucket() {
		return bucket;
	}

	public void setBucket(int bucket) {
		this.bucket = bucket;
	}

	public long getSubAgentID() {
		return subAgentID;
	}

	public void setSubAgentID(long subAgentID) {
		this.subAgentID = subAgentID;
	}

	public long getSeqNumber() {
		return seqNumber;
	}

	public void setSeqNumber(long seqNumber) {
		this.seqNumber = seqNumber;
	}

	public long getSysUptime() {
		return sysUptime;
	}

	public void setSysUptime(long sysUptime) {
		this.sysUptime = sysUptime;
	}

	public long getIfIndex() {
		return ifIndex;
	}

	public void setIfIndex(long ifIndex) {
		this.ifIndex = ifIndex;
	}

	public long getIfType() {
		return ifType;
	}

	public void setIfType(long ifType) {
		this.ifType = ifType;
	}

	public BigInteger getIfSpeed() {
		return ifSpeed;
	}

	public void setIfSpeed(BigInteger ifSpeed) {
		this.ifSpeed = ifSpeed;
	}

	public long getIfDirection() {
		return ifDirection;
	}

	public void setIfDirection(long ifDirection) {
		this.ifDirection = ifDirection;
	}

	public long getIfStatus() {
		return ifStatus;
	}

	public void setIfStatus(long ifStatus) {
		this.ifStatus = ifStatus;
	}

	public BigInteger getIfInOctets() {
		return ifInOctets;
	}

	public void setIfInOctets(BigInteger ifInOctets) {
		this.ifInOctets = ifInOctets;
	}

	public long getIfInUcastPkts() {
		return ifInUcastPkts;
	}

	public void setIfInUcastPkts(long ifInUcastPkts) {
		this.ifInUcastPkts = ifInUcastPkts;
	}

	public long getIfInMulticastPkts() {
		return ifInMulticastPkts;
	}

	public void setIfInMulticastPkts(long ifInMulticastPkts) {
		this.ifInMulticastPkts = ifInMulticastPkts;
	}

	public long getIfInBroadcastPkts() {
		return ifInBroadcastPkts;
	}

	public void setIfInBroadcastPkts(long ifInBroadcastPkts) {
		this.ifInBroadcastPkts = ifInBroadcastPkts;
	}

	public long getIfInDiscards() {
		return ifInDiscards;
	}

	public void setIfInDiscards(long ifInDiscards) {
		this.ifInDiscards = ifInDiscards;
	}

	public long getIfInErrors() {
		return ifInErrors;
	}

	public void setIfInErrors(long ifInErrors) {
		this.ifInErrors = ifInErrors;
	}

	public long getIfInUnknownProtos() {
		return ifInUnknownProtos;
	}

	public void setIfInUnknownProtos(long ifInUnknownProtos) {
		this.ifInUnknownProtos = ifInUnknownProtos;
	}

	public BigInteger getIfOutOctets() {
		return ifOutOctets;
	}

	public void setIfOutOctets(BigInteger ifOutOctets) {
		this.ifOutOctets = ifOutOctets;
	}

	public long getIfOutUcastPkts() {
		return ifOutUcastPkts;
	}

	public void setIfOutUcastPkts(long ifOutUcastPkts) {
		this.ifOutUcastPkts = ifOutUcastPkts;
	}

	public long getIfOutMulticastPkts() {
		return ifOutMulticastPkts;
	}

	public void setIfOutMulticastPkts(long ifOutMulticastPkts) {
		this.ifOutMulticastPkts = ifOutMulticastPkts;
	}

	public long getIfOutBroadcastPkts() {
		return ifOutBroadcastPkts;
	}

	public void setIfOutBroadcastPkts(long ifOutBroadcastPkts) {
		this.ifOutBroadcastPkts = ifOutBroadcastPkts;
	}

	public long getIfOutDiscards() {
		return ifOutDiscards;
	}

	public void setIfOutDiscards(long ifOutDiscards) {
		this.ifOutDiscards = ifOutDiscards;
	}

	public long getIfOutErrors() {
		return ifOutErrors;
	}

	public void setIfOutErrors(long ifOutErrors) {
		this.ifOutErrors = ifOutErrors;
	}

	public long getIfPromiscuousMode() {
		return ifPromiscuousMode;
	}

	public void setIfPromiscuousMode(long ifPromiscuousMode) {
		this.ifPromiscuousMode = ifPromiscuousMode;
	}

	public Date getEventTime() {
		return eventTime;
	}

	public void setEventTime(Date eventTime)
	{
		this.eventTime = eventTime;
		if(eventTime != null)
		{
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(eventTime);
//			setYear(calendar.get(Calendar.YEAR));
//			setMonth(calendar.get(Calendar.MONTH));
//			setDay(calendar.get(Calendar.DAY_OF_MONTH));
//			setHour(calendar.get(Calendar.HOUR));
//			setMinutes(calendar.get(Calendar.MINUTE));
		}
		
	}

	public double getIfInUtilization() {
		return ifInUtilization;
	}

	public void setIfInUtilization(double ifInUtilization) {
		this.ifInUtilization = ifInUtilization;
	}

	public double getIfOutUtilization() {
		return ifOutUtilization;
	}

	public void setIfOutUtilization(double ifOutUtilization) {
		this.ifOutUtilization = ifOutUtilization;
	}

	public double getIfInFrameRate() {
		return ifInFrameRate;
	}

	public void setIfInFrameRate(double ifInFrameRate) {
		this.ifInFrameRate = ifInFrameRate;
	}

	public double getIfOutFrameRate() {
		return ifOutFrameRate;
	}

	public void setIfOutFrameRate(double ifOutFrameRate) {
		this.ifOutFrameRate = ifOutFrameRate;
	}

	@Override
	public String toString() {
		return "IfCounterData [ipVersionAgent=" + ipVersionAgent
				+ ", ipAddress=" + ipAddress + ", bucket=" + bucket
				+ ", subAgentID=" + subAgentID + ", sourceIDType="
				+ sourceIDType + ", sourceIDIndex=" + sourceIDIndex
				+ ", eventTime=" + eventTime + ", sampleSequenceNumber="
				+ sampleSequenceNumber + ", sysUptime=" + sysUptime
				+ ", seqNumber=" + seqNumber + ", ifIndex=" + ifIndex
				+ ", ifType=" + ifType + ", ifSpeed=" + ifSpeed
				+ ", ifDirection=" + ifDirection + ", ifStatus=" + ifStatus
				+ ", ifInOctets=" + ifInOctets + ", ifInUcastPkts="
				+ ifInUcastPkts + ", ifInMulticastPkts=" + ifInMulticastPkts
				+ ", ifInBroadcastPkts=" + ifInBroadcastPkts
				+ ", ifInDiscards=" + ifInDiscards + ", ifInErrors="
				+ ifInErrors + ", ifInUnknownProtos=" + ifInUnknownProtos
				+ ", ifOutOctets=" + ifOutOctets + ", ifOutUcastPkts="
				+ ifOutUcastPkts + ", ifOutMulticastPkts=" + ifOutMulticastPkts
				+ ", ifOutBroadcastPkts=" + ifOutBroadcastPkts
				+ ", ifOutDiscards=" + ifOutDiscards + ", ifOutErrors="
				+ ifOutErrors + ", ifPromiscuousMode=" + ifPromiscuousMode
				+ ", ifInUtilization=" + ifInUtilization
				+ ", ifOutUtilization=" + ifOutUtilization + ", ifInFrameRate="
				+ ifInFrameRate + ", ifOutFrameRate=" + ifOutFrameRate + "]";
	}

	
}



