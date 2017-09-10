package com.sflow.records.domain;

import java.math.BigInteger;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AppResources {
		
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

	@JsonProperty("userTime")
	private long userTime;
	
	@JsonProperty("systemTime")
	private long systemTime;
	
	@JsonProperty("memUsed")
	private BigInteger memUsed;
	
	@JsonProperty("memMax")
	private BigInteger memMax;
	
	@JsonProperty("fdOpen")
	private long fdOpen;

	@JsonProperty("fdMax")
	private long fdMax;

	@JsonProperty("connOpen")
	private long connOpen;

	@JsonProperty("connMax")
	private long connMax;

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

	public Date getEventTime() {
		return eventTime;
	}

	public void setEventTime(Date eventTime) {
		this.eventTime = eventTime;
	}

	public long getSampleSequenceNumber() {
		return sampleSequenceNumber;
	}

	public void setSampleSequenceNumber(long sampleSequenceNumber) {
		this.sampleSequenceNumber = sampleSequenceNumber;
	}

	public long getSysUptime() {
		return sysUptime;
	}

	public void setSysUptime(long sysUptime) {
		this.sysUptime = sysUptime;
	}

	public long getSeqNumber() {
		return seqNumber;
	}

	public void setSeqNumber(long seqNumber) {
		this.seqNumber = seqNumber;
	}

	public long getUserTime() {
		return userTime;
	}

	public void setUserTime(long userTime) {
		this.userTime = userTime;
	}

	public long getSystemTime() {
		return systemTime;
	}

	public void setSystemTime(long systemTime) {
		this.systemTime = systemTime;
	}

	public BigInteger getMemUsed() {
		return memUsed;
	}

	public void setMemUsed(BigInteger memUsed) {
		this.memUsed = memUsed;
	}

	public BigInteger getMemMax() {
		return memMax;
	}

	public void setMemMax(BigInteger memMax) {
		this.memMax = memMax;
	}

	public long getFdOpen() {
		return fdOpen;
	}

	public void setFdOpen(long fdOpen) {
		this.fdOpen = fdOpen;
	}

	public long getFdMax() {
		return fdMax;
	}

	public void setFdMax(long fdMax) {
		this.fdMax = fdMax;
	}

	public long getConnOpen() {
		return connOpen;
	}

	public void setConnOpen(long connOpen) {
		this.connOpen = connOpen;
	}

	public long getConnMax() {
		return connMax;
	}

	public void setConnMax(long connMax) {
		this.connMax = connMax;
	}

	@Override
	public String toString() {
		return "AppResources [ipVersionAgent=" + ipVersionAgent + ", ipAddress="
				+ ipAddress + ", bucket=" + bucket + ", subAgentID="
				+ subAgentID + ", sourceIDType=" + sourceIDType
				+ ", sourceIDIndex=" + sourceIDIndex + ", eventTime="
				+ eventTime + ", sampleSequenceNumber=" + sampleSequenceNumber
				+ ", sysUptime=" + sysUptime + ", seqNumber=" + seqNumber
				+ ", user_time=" + userTime + ", system_time=" + systemTime
				+ ", mem_used=" + memUsed + ", mem_max=" + memMax
				+ ", fd_open=" + fdOpen + ", fd_max=" + fdMax
				+ ", conn_open=" + connOpen + ", conn_max=" + connMax + "]";
	}
}
